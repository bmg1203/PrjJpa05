package com.green.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.green.entity.Comments;

public interface CommentRepository extends JpaRepository<Comments, Long> {
	//@Query 에노테이션으로 findByArticleId()를 실행한다
	//@Query 에노테이션은 JPA 기능을 사용하지 않고 @Query 안의 SQL을 사용한다. 
	//findByArticleId() 함수는 Comments 테이블 칼럼을 사용해서 만든다
	/*
	Native Query Method - oracle 전용 문법으로 작성된 쿼리를 입력하여 조회
	nativeQuery = true : 오라클 전용함수
	nativeQuery = false : JPA 함수
	:articleId(파라미터)로 조회 //파라미터는 : 뒤에 작성
	//JPQL - JPA용 sql : DB의 종류와 상관없이 사용가능한 sql
	//QueryDsl - JPA에서 oracle 전용 sql 문을 실행하는 기술
	 */
	@Query(value = "SELECT * FROM comments WHERE article_id=:articleId", nativeQuery = true)
	List<Comments> findByArticleId(Long articleId);
	
	//native query xml : 
	//src/main/resources/META-INF/orm.xml //폴더와 파일 생성해야함
	//orm.xml에 sql을 저장해서 findByNickname() 함수 호출
	List<Comments> findByNickname(String nickname);
}
