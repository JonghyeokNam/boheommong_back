package com.sesac.boheommong.domain.bookmark.controller;

import com.sesac.boheommong.domain.bookmark.dto.request.BookmarkRequestDto;
import com.sesac.boheommong.domain.bookmark.dto.response.BookmarkResponseDto;
import com.sesac.boheommong.domain.bookmark.service.BookmarkService;
import com.sesac.boheommong.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/bookmarks/state")
    public Response<Boolean> getBookmarkState(
            @RequestParam String loginEmail,
            @RequestParam Long productId
    ) {
        Boolean state = bookmarkService.getState(loginEmail, productId);
        return Response.success(state);
    }

    @GetMapping("/bookmarks/user/{userId}")
    public Response<List<BookmarkResponseDto>> getAllBookmarksByUser(@PathVariable Long userId) {
        List<BookmarkResponseDto> results = bookmarkService.getAllBookmarksByUser(userId);
        return Response.success(results);
    }

    @PostMapping("/bookmarks")
    public Response<BookmarkResponseDto> createBookmark(@RequestBody BookmarkRequestDto requestDto) {
        BookmarkResponseDto created = bookmarkService.createBookmark(requestDto);
        return Response.success(created);
    }

    @GetMapping("/bookmarks/{bookmarkId}")
    public Response<BookmarkResponseDto> getBookmark(@PathVariable Long bookmarkId) {
        BookmarkResponseDto result = bookmarkService.getBookmark(bookmarkId);
        return Response.success(result);
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public Response<Void> deleteBookmark(@PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return Response.success();
    }
}
