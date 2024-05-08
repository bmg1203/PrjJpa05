package com.green.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usersjpa")
//entity는 table 명과 같지만 oracle은 user table명이 불가하다
//table 명을 변경해야 한다(usersjpa)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@NoArgsConstructor
//UserDetails 인터페이스를 구현하여 User 엔티티를 생성
public class User implements UserDetails {
	
	//칼럼부분을 표시
	@Id //기본키
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", updatable = false)
	private Long id;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	//생성자
	/*
	생성자 대신 아래 문법으로 사용 가능, 파라미터 순서와 무관
	빌더에선 이렇게 사용가능, 순서도 안지켜도 됨
	@Builder
	User user = User.builder()
					.password('aaa')
					.email('aaa@aaa.com')
					.build();
	*/
	@Builder
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	//implement할 메소드들(클래스 이름 User 클릭하고 만들어진 함수들)
	
	//권한 반환
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//사용자가 가지고 있는 권한의 목록을 반환, 현재는 User 권한만 반환 
		return List.of(new SimpleGrantedAuthority("user"));
	}

	//사용자의 비밀번호 반환
	@Override
	public String getPassword() {
		return password;
	}

	//사용자 아이디(이메일) 반환
	@Override
	public String getUsername() { //<input name="username">
		return email;
	}

	//계정 만료 여부 반환
	@Override
	public boolean isAccountNonExpired() {
		//계정이 만료되었는지를 확인하는 로직(DB나 칼럼 등등으로 구현 가능)
		return true; //true -> 만료되지 않음, false 시 로그인 불가
	}

	//계정 잠금 여부 반환
	@Override
	public boolean isAccountNonLocked() {
		//계정 잠금 여부를 확인하는 로직
		return true; //true -> 계정이 잠기지 않음, false 시 계정이 잠긴 것으로 로그인 불가
	}

	//패스워드 만료 여부 반환
	@Override
	public boolean isCredentialsNonExpired() {
		//패스워드가 만료되었는지 확인하는 로직
		return true; //true -> 패스워드가 만료되지 않음
	}

	//계정 사용 가능 여부 반환
	@Override
	public boolean isEnabled() {
		//계정이 사용 가능한지 확인하는 로직
		return true; //true -> 사용가능
	}	
}
