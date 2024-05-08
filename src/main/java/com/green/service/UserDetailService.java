package com.green.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.green.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //not null인 필수입력 항목 생성자
@Service
//반드시 UserDetailsService interface를 implement 해야함
public class UserDetailService implements UserDetailsService {

	private UserRepository userRepository;
	
	//implement할 메소드(클래스 이름 UserDetailService 클릭하고 만들어진 함수)
	//로그인에 필요한 Username 로그인 아이디(이메일)를 통해서 UserDetail 정보를 가져옴
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDetails userDetails = userRepository.findByEmail(email).orElseThrow(() -> 
			new IllegalArgumentException(email)
		);

		return userDetails;
	}
}
