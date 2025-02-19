package com.sesac.boheommong.domain.bookmark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long bookmarkId;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}
