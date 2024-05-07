package com.green.dto;

import com.green.entity.Comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	private Long id; //댓글 id
	private Long articleId; //article의 부모글 id
	private String nickname; //작성자 별명
	private String body; //댓글 내용
	
	//CommentDto(dto) <- Comments(entity, DB에서 조회한 데이터)
	public static CommentDto createCommentDto(Comments comments) {
		return new CommentDto(
					comments.getId(),
					comments.getArticle().getId(),
					comments.getNickname(),
					comments.getBody()
				);
	}
}
