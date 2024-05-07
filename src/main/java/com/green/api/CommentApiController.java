package com.green.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.CommentDto;
import com.green.service.CommentService;

//댓글 조회, 댓글 추가, 댓글 삭제
@RestController
public class CommentApiController {
	@Autowired
	private CommentService commentService;
	
	//1. 댓글 조회(GET)
	//@PathVariable
	//: 경로 변수를 표시하기 위해 메서드에 매개변수에 사용, URL 경로에서 변수 값을 추철하여 매개변수에 할당
	@GetMapping("/api/articles/{articleId}/comments")
	public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId) {
		//정보 조회를 서비스에게 위임
		List<CommentDto> dtos = commentService.comments(articleId);
		
		//ResponseEntity : status.ok + dtos(arraylist -> json으로 출력(이유 : @RestController라서)를 리턴
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}
	
	//2. 댓글 생성(POST)
	// POST http://localhost:9090/api/articles/{articleId}/comments
	/*	
	입력 data : {"article_id":4, "nickname":"Hoon", "body":"이프 온리"}
	결과 {"id": null, "article_id": 4, "nickname": "Hoon","body": "이프 온리"	}
	에러 입력 : {id: 4, "article_id": 4, "nickname": "Hoon","body": "이프 온리"	}
	결과  400(Bad Request) 에러 -  {"id": 4,  입력데이터 키 json type "" 안에 저장 
	에러 입력 : {"id": 4, "article_id": 4, "nickname": "Hoon","body": "이프 온리"	}
	결과  500  message : "댓글 생성실패! 댓글의 id가 없어야합니다",
	*/
	@PostMapping("/api/articles/{articleId}/comments")	//{articleId}(게시글 번호)      입력된 자료들(input, select 태그, 게시글 내용)
	public ResponseEntity<CommentDto> create(@PathVariable Long articleId, @RequestBody CommentDto dto) {	
		//입력된 자료들 input, select : js fetch body : {}
		commentService.create(articleId, dto);
		
		//결과 응답
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
	
	//3. 댓글 수정(Patch)
	// PATCH http://localhost:9090/api/comments/7
	//수정 전 data {"article_id":6, "id":7, "body":"조깅", "nickname":"Park"}
	//입력 data {"article_id":6, "id":7, "body":"수영", "nickname":"Park2"}
	//입력 data {"id":7, 
	@PatchMapping("/api/comments/{id}") //									    수정할 data
	public ResponseEntity<CommentDto> update(@PathVariable Long id, @RequestBody CommentDto dto) {
		CommentDto updateDto = commentService.update(id, dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(updateDto);
	}
	
	//4. 댓글 삭제(Delete)
	// DELETE http://localhost:9090/api/comments/7
	@DeleteMapping("/api/comments/{id}")
	public ResponseEntity<CommentDto> delete(@PathVariable Long id){
		CommentDto deletedDto = commentService.delete(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
	}
}
