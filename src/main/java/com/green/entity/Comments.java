package com.green.entity;

import com.green.dto.CommentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //클래스를 테이블로 생성한다
@Data //@Getter, @Setter, @ToString, hashCode, equals
@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모든 인자 생성자
@SequenceGenerator(name = "COMMNETS_SEQ_GENERATOR", sequenceName = "COMMENTS_SEQ", initialValue = 1, allocationSize = 1)
//이름							   시퀀스 이름					 초기값			   증가치
public class Comments {
	@Id //기본키(jakarta.perisit)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMNETS_SEQ_GENERATOR")
	private Long id;
	@ManyToOne //다대일 설정 (Comments <-> Article)
	@JoinColumn(name = "article_id") //왜래키(부모키 Article id column)
	private Article article; //연결될 entity 객체의 이름
	//@Column(name = "", nullable = false, length = 50)
	@Column
	private String nickname;
	@Column
	private String body;
	
	public static Comments createComment(CommentDto dto, Article article) {
		if(dto.getId() != null) { //입력된 댓글 중 이미 id가 존재하면 예외 처리 
			throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 이미 존재합니다.");
		}
		
		//dto.getArticleId() : 입력받은 게시글의 id
		//article.getId() : 조회한 게시글의 id
		if(dto.getArticleId() != article.getId()) { 
			//댓글의 부모 게시글 아이디와 조회한 게시글의 아이디가 다르면 예외 처리 
			throw new IllegalArgumentException("댓글 생성 실패! 게시글 id가 잘못되었습니다.");
		}
		
		//입력받은 댓글 아이디, 조회한 부모 게시글 정보, 입력 받은 댓글 닉네임, 댓글 내용
		return new Comments(dto.getId(), article, dto.getNickname(), dto.getBody());
	}

	public void patch(CommentDto dto) {
		if(this.id != dto.getId()) {
			throw new IllegalArgumentException("댓글 수정 실패! 잘못된 아이디가 입력되었습니다.");
		}
		
		if(dto.getNickname() != null) { //입력받은 데이터에 수정할 닉네임이 존재하면 
			this.nickname = dto.getNickname();			
		}
		
		if(dto.getBody() != null) { //입력받은 데이터에 수정할 댓글 내용이 존재하면 
			this.body = dto.getBody();
		}
	}
}
