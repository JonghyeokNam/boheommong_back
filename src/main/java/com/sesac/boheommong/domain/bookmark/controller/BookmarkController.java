package com.sesac.boheommong.domain.bookmark.controller;

import com.sesac.boheommong.domain.bookmark.dto.response.BookmarkResponseDto;
import com.sesac.boheommong.domain.bookmark.service.BookmarkService;
import com.sesac.boheommong.domain.bookmark.swagger.*;
import com.sesac.boheommong.global.jwt.service.TokenProvider;
import com.sesac.boheommong.global.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final TokenProvider tokenProvider;

    /**
     * 특정 상품이 북마크 되었는지 여부 조회
     */
    @GetBookmarkState
    @GetMapping("/state")
    public Response<Boolean> getBookmarkState(
            HttpServletRequest request,
            @RequestParam Long productId
    ) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        Boolean state = bookmarkService.getState(loginEmail, productId);
        return Response.success(state);
    }

    /**
     * 로그인 유저의 모든 북마크 조회
     */
    @GetAllBookmarks
    @GetMapping
    public Response<List<BookmarkResponseDto>> getAllBookmarks(HttpServletRequest request) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        List<BookmarkResponseDto> results = bookmarkService.getAllBookmarksByLoginEmail(loginEmail);
        return Response.success(results);
    }

    /**
     * 북마크 추가/취소(Toggle)
     */
    @PostToggleBookmark
    @PostMapping("/products/{productId}/toggle")
    public Response<Void> addOrCancelBookmark(
            HttpServletRequest request,
            @PathVariable("productId") Long productId
    ) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        bookmarkService.addOrCancelBookmark(loginEmail, productId);
        return Response.success();
    }

    /**
     * 북마크 생성
     */
    @PostCreateBookmark
    @PostMapping
    public Response<BookmarkResponseDto> createBookmark(
            HttpServletRequest request,
            @RequestParam Long productId
    ) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        BookmarkResponseDto created = bookmarkService.createBookmark(loginEmail, productId);
        return Response.success(created);
    }

    /**
     * 특정 북마크 단건 조회
     */
    @GetBookmarkById
    @GetMapping("/{bookmarkId}")
    public Response<BookmarkResponseDto> getBookmark(
            HttpServletRequest request,
            @PathVariable Long bookmarkId
    ) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        BookmarkResponseDto result = bookmarkService.getBookmark(loginEmail, bookmarkId);
        return Response.success(result);
    }

    /**
     * 북마크 삭제(소프트 삭제)
     */
    @DeleteBookmark
    @DeleteMapping("/{bookmarkId}")
    public Response<Void> deleteBookmark(
            HttpServletRequest request,
            @PathVariable Long bookmarkId
    ) {
        String loginEmail = tokenProvider.getUserLoginEmail(request);
        bookmarkService.deleteBookmark(loginEmail, bookmarkId);
        return Response.success();
    }
}
