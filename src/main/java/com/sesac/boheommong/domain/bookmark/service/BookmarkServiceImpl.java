package com.sesac.boheommong.domain.bookmark.service;

import com.sesac.boheommong.domain.bookmark.dto.request.BookmarkRequestDto;
import com.sesac.boheommong.domain.bookmark.dto.response.BookmarkResponseDto;
import com.sesac.boheommong.domain.bookmark.entity.Bookmark;
import com.sesac.boheommong.domain.bookmark.repository.BookmarkRepository;
import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final InsuranceProductRepository insuranceProductRepository;

    @Override
    public Boolean getState(String loginEmail, Long productId) {
        User user = userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        InsuranceProduct product = insuranceProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        return bookmarkRepository.findByUserAndProduct(user, product).isPresent();
    }

    @Override
    @Transactional
    public List<BookmarkResponseDto> getAllBookmarksByUser(Long userId) {
        // 1) 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + userId));

        // 2) 해당 유저의 모든 북마크 조회
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserOrderByCreatedAtDesc(user);

        // 3) Bookmark 엔티티 -> BookmarkResponseDto 변환
        List<BookmarkResponseDto> result = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            result.add(new BookmarkResponseDto(
                    bookmark.getBookmarkId(),
                    bookmark.getUser().getUserId(),
                    bookmark.getProduct().getProductId(),
                    bookmark.getCreatedAt()
            ));
        }

        return result;
    }

    @Override
    public BookmarkResponseDto createBookmark(BookmarkRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + requestDto.getUserId()));

        InsuranceProduct product = insuranceProductRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 보험 상품이 존재하지 않습니다. productId=" + requestDto.getProductId()));

        Bookmark bookmark = Bookmark.create(user, product);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return new BookmarkResponseDto(
                savedBookmark.getBookmarkId(),
                savedBookmark.getUser().getUserId(),
                savedBookmark.getProduct().getProductId(),
                savedBookmark.getCreatedAt() // BaseEntity 사용 가정
        );
    }

    @Transactional
    @Override
    public BookmarkResponseDto getBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 북마크입니다. bookmarkId=" + bookmarkId));

        return new BookmarkResponseDto(
                bookmark.getBookmarkId(),
                bookmark.getUser().getUserId(),
                bookmark.getProduct().getProductId(),
                bookmark.getCreatedAt()
        );
    }

    @Override
    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 북마크입니다. bookmarkId=" + bookmarkId));

        bookmark.softDelete();
    }
}
