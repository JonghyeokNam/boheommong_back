package com.sesac.boheommong.domain.bookmark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor  // 직렬화/역직렬화를 위해 기본 생성자
@AllArgsConstructor // 모든 필드를 받는 생성자
public class BookmarkRequestDto {
    private Long userId;             // 실제로는 jwt에서 userId를 꺼낼 수도 있지만, 여기서는 직접 전달받는다고 가정
    private Long productId; // 북마크할 상품 ID
}
