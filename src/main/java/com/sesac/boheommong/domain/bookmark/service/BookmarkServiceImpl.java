package com.sesac.boheommong.domain.bookmark.service;

import com.sesac.boheommong.domain.bookmark.dto.response.BookmarkResponseDto;
import com.sesac.boheommong.domain.bookmark.entity.Bookmark;
import com.sesac.boheommong.domain.bookmark.repository.BookmarkRepository;
import com.sesac.boheommong.domain.bookmark.swagger.*;
import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.global.exception.BaseException;
import com.sesac.boheommong.global.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final InsuranceProductRepository insuranceProductRepository;

    /**
     * 특정 상품이 북마크되었는지 여부 조회
     */
    @Override
    public Boolean getState(String loginEmail, Long productId) {
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        InsuranceProduct product = insuranceProductRepository.findById(productId)
                .orElseThrow(() -> BaseException.from(ErrorCode.INSURANCE_PRODUCT_NOT_FOUND));

        return bookmarkRepository.findByUserAndProduct(user, product).isPresent();
    }

    /**
     * 로그인 이메일로 유저 조회 후, 유저의 모든 북마크 조회
     */
    @Override
    public List<BookmarkResponseDto> getAllBookmarksByLoginEmail(String loginEmail) {
        // 1) 유저 존재 여부 확인
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // 2) 해당 유저의 모든 북마크 조회 (최신 순)
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserOrderByCreatedAtDesc(user);

        // 3) Bookmark -> BookmarkResponseDto 변환
        return bookmarks.stream()
                .map(bookmark -> new BookmarkResponseDto(
                        bookmark.getBookmarkId(),
                        bookmark.getUser().getUserId(),
                        bookmark.getProduct().getProductId(),
                        bookmark.getCreatedAt()
                ))
                .toList();
    }

    /**
     * 북마크 토글 메서드
     * - 이미 존재하면 소프트 삭제
     * - 존재하지 않으면 새로 생성
     */
    @Transactional
    @Override
    public void addOrCancelBookmark(String loginEmail, Long productId) {
        // 1) 유저 조회
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // 2) 상품 조회
        InsuranceProduct product = insuranceProductRepository.findById(productId)
                .orElseThrow(() -> BaseException.from(ErrorCode.INSURANCE_PRODUCT_NOT_FOUND));

        // 3) 기존 북마크가 있는지 확인 (물리 삭제 방식이므로 소프트 삭제 고려 X)
        Optional<Bookmark> optionalBookmark = bookmarkRepository.findByUserAndProduct(user, product);

        if (optionalBookmark.isPresent()) {
            // (A) 이미 북마크가 존재하면 -> 물리 삭제
            bookmarkRepository.delete(optionalBookmark.get());
            // 실제 DB row가 삭제되므로 중복키 충돌 안 남.
        } else {
            // (B) 없으면 -> 새 북마크 생성
            Bookmark bookmark = Bookmark.create(user, product);
            bookmarkRepository.save(bookmark);
        }
    }


    /**
     * 북마크 생성
     */
    @Override
    public BookmarkResponseDto createBookmark(String loginEmail, Long productId) {
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        InsuranceProduct product = insuranceProductRepository.findById(productId)
                .orElseThrow(() -> BaseException.from(ErrorCode.INSURANCE_PRODUCT_NOT_FOUND));

        Bookmark bookmark = Bookmark.create(user, product);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return new BookmarkResponseDto(
                savedBookmark.getBookmarkId(),
                savedBookmark.getUser().getUserId(),
                savedBookmark.getProduct().getProductId(),
                savedBookmark.getCreatedAt()
        );
    }

    /**
     * 북마크 단건 조회
     */
    @Override
    public BookmarkResponseDto getBookmark(String loginEmail, Long bookmarkId) {
        // 1) 유저 조회
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // 2) 북마크 조회
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> BaseException.from(ErrorCode.BOOKMARK_NOT_FOUND));

        // 3) 본인 북마크인지 확인 (옵션)
        if (!bookmark.getUser().equals(user)) {
            throw BaseException.from(ErrorCode.INVALID_PERMISSION);
        }

        // 4) 엔티티 -> Dto 변환
        return new BookmarkResponseDto(
                bookmark.getBookmarkId(),
                bookmark.getUser().getUserId(),
                bookmark.getProduct().getProductId(),
                bookmark.getCreatedAt()
        );
    }

    /**
     * 북마크 삭제(소프트 삭제)
     */
    @Override
    public void deleteBookmark(String loginEmail, Long bookmarkId) {
        // 1) 유저 조회
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // 2) 북마크 조회
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> BaseException.from(ErrorCode.BOOKMARK_NOT_FOUND));

        // 3) 본인 북마크인지 확인 (옵션)
        if (!bookmark.getUser().equals(user)) {
            throw BaseException.from(ErrorCode.INVALID_PERMISSION);
        }

        // 4) Soft Delete 처리
        bookmark.softDelete();
        // BaseEntity나 별도의 @SQLDelete를 통한 업데이트가 적용되도록 구성
    }
}
