package com.sesac.boheommong.domain.bookmark.service;

import com.sesac.boheommong.domain.bookmark.dto.request.BookmarkRequestDto;
import com.sesac.boheommong.domain.bookmark.dto.response.BookmarkResponseDto;

import java.util.List;

public interface BookmarkService {

    BookmarkResponseDto createBookmark(BookmarkRequestDto requestDto);
    BookmarkResponseDto getBookmark(Long bookmarkId);
    void deleteBookmark(Long bookmarkId);
    Boolean getState(String Email, Long boardId);
    List<BookmarkResponseDto> getAllBookmarksByUser(Long userId);
}
