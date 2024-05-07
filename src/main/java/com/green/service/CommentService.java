package com.green.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.green.dto.CommentDto;
import com.green.entity.Article;
import com.green.entity.Comments;
import com.green.repository.ArticleRepository;
import com.green.repository.CommentRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	//1. 댓글 조회
	public List<CommentDto> comments(Long articleId) {
		/*
		//1) 댓글을 DB에서 articleId로 조회 Entity에 담는다
		List<Comments> commentList = commentRepository.findByArticleId(articleId);
		
		//2) 엔티티 -> Dto 변환
		List<CommentDto> dtos = new ArrayList<CommentDto>();
		for(int i = 0; i < commentList.size(); i++) {
			Comments c = commentList.get(i);
			CommentDto dto = CommentDto.createCommentDto(c);
			dtos.add(dto);
		}
				
		//3) 결과를 리턴
		return dtos;
		*/
		/*
		ArrayList.stream() 스트림 전용함수를 사용할 수 있게 됨
		.map(comment -> { //람다식
			CommentDto.createCommentDto(comment)
		})
		 - 집합 .map()은 스트림 전용 함수
		 - 집합(Collection)에 대해 element를 반복적으로 조작
		 - .map() vs. forEach()
		   공통점 : 둘 다 배열을 조작하는 기능
		   차이점 : .map()은 배열 내부의 element의 값이나 사이즈를 변경할 때 사용(ex 모두 대문자로 변경)
		    	  : forEach()는 내부의 값이나 내용을 변경하지 않을 때 사용(일반적인 for문) : 
		*/
		return commentRepository.findByArticleId(articleId)
								.stream()//stream으로 전환하여 반환
								.map(comment -> CommentDto.createCommentDto(comment))
								.collect(Collectors.toList());
	}

	//2. 댓글 생성
	//insert
	@Transactional //오류 발생시 DB 롤백을 위해(throw 사용 이유)
	public CommentDto create(Long articleId, CommentDto dto) {
		//1) 게시글 조회 및 조회 실패시 예외 처리
		//게시글에 존재하지 않는 articleid가 넘어오면 조회 결과 없음 예외처리
		Article article = articleRepository.findById(articleId).orElseThrow(() -> 
			new IllegalArgumentException("댓글 생성 실패! 대상 게시물이 없습니다. ")
		);
		
		//2) 댓글 엔티티 생성 -> 저장할 데이터(Comments)를 만듦
		Comments comments = Comments.createComment(dto, article);
		
		//3) 댓글 엔티티를 DB에 저장
		Comments created = commentRepository.save(comments);
		
		//4) 저장된 Comments type의 created를 dto로 변환 후 Controller에 return
		//변환 이유가 Controller에서 Entity Type을 사용하지 않기 위해
		return CommentDto.createCommentDto(created);
	}
	
	//3. 댓글 수정
	@Transactional
	public CommentDto update(Long id, CommentDto dto) {
		//1) 댓글 조회 및 예외 발생
		Comments target = commentRepository.findById(id).orElseThrow(() -> 
			new IllegalArgumentException("댓글 수정 실패! 수정할 댓글이 없습니다.")
		);
		
		//2) 댓글 수정 : 조회한 데이터의 내용을 수정(class 안의 내용 변경)
		//target : 수정할 원본 데이터
		//dto : 수정할 입력 데이터를 가지고 있음
		target.patch(dto); //target <- dto(nickname, body)
		
		//3) DB에 저장
		Comments updated = commentRepository.save(target);
		
		//4) 결과 updated -> commentDto로 변경하여 return
		return CommentDto.createCommentDto(updated);
	}

	//댓글 삭제
	@Transactional
	public CommentDto delete(Long id) {
		//1) 삭제할 댓글 조회 및 예외 발생
		Comments target = commentRepository.findById(id).orElseThrow(() -> 
			new IllegalArgumentException("댓글 삭제 실패! 삭제할 대상이 없습니다.")
		);
		
		//2) 실제 DB에서 삭제
		commentRepository.delete(target);
		
		//3) 삭제 댓글들을 dto로 반환 후 리턴		
		return CommentDto.createCommentDto(target);
	}
}
