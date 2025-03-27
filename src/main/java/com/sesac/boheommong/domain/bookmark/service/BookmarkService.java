package com.sesac.boheommong.domain.bookmark.service;

import com.sesac.boheommong.domain.bookmark.dto.response.BookmarkResponseDto;

import java.util.List;

public interface BookmarkService {

    /**
     * 특정 상품이 북마크 되었는지 여부를 반환
     * @param loginEmail 로그인 이메일
     * @param productId 상품 ID
     * @return 북마크 되어 있다면 true, 아니면 false
     */
    Boolean getState(String loginEmail, Long productId);

    /**
     * 로그인 이메일로 유저를 조회한 뒤 해당 유저의 모든 북마크 목록을 반환
     * @param loginEmail 로그인 이메일
     * @return BookmarkResponseDto 리스트
     */
    List<BookmarkResponseDto> getAllBookmarksByLoginEmail(String loginEmail);

    /**
     * 북마크 생성
     * @param loginEmail 로그인 이메일
     * @param productId 북마크할 상품 ID
     * @return 생성된 BookmarkResponseDto
     */
    BookmarkResponseDto createBookmark(String loginEmail, Long productId);

    /**
     * 단일 북마크 조회
     * @param loginEmail 로그인 이메일
     * @param bookmarkId 북마크 ID
     * @return 조회된 BookmarkResponseDto
     */
    BookmarkResponseDto getBookmark(String loginEmail, Long bookmarkId);

    /**
     * 북마크 삭제 (소프트 삭제)
     * @param loginEmail 로그인 이메일
     * @param bookmarkId 북마크 ID
     */
    void deleteBookmark(String loginEmail, Long bookmarkId);

    void addOrCancelBookmark(String loginEmail, Long productId);
}
