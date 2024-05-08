package com.green.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.green.service.UserDetailService;

import lombok.RequiredArgsConstructor;

//spring security 6.1.0 기준 코딩
//버전이 낮으면 함수가 다 deprecated 됨
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

	private final UserDetailService userService;

	// 1. 스프링 시큐리티 기능 비활성화 (인증 제외 설정)
	// 로그인 로직에서 제외할 폴더 지정
	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring()
				// .requestMatchers(toH2Console()) // /h2-console
				.requestMatchers("/static/**"); // /static/** : .html, .js, .css
	}

	// 2.특정 HTTP 요청에 대한 웹 기반 보안 구성 // spring security 6.1.0
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrfConfig) -> csrfConfig.disable()) // csrf 비활성 ) -> 실무는 활성화 필요
														// .authorizeHttpRequests() : 새로운 함수
				.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
						// .requestMatchers(PathRequest.toH2Console()).permitAll()
						// "/login", "/signup", "user" 는 요청인가 없이 접근허용
						// 로그인 회원가입 회원등록기능
						.requestMatchers("/login", "/signup", "user").permitAll().anyRequest().authenticated() // 나머지
																												// 요청은
																												// 인증 필요
				) // "/login", "/signup", "user" 는 요청인가 없이 접근허용
				.formLogin((formLogin) -> formLogin.loginPage("/login") // 로그인할 페이지 경로
						.defaultSuccessUrl("/") // 로그인 성공시 경로 : index.html
				) // 로그인처리
				.logout((logout) -> logout.logoutSuccessUrl("/login") // 로그아웃성공시 경로
						.invalidateHttpSession(true) // 로그아웃 이후에 세션 전제 삭제 여부설정
				); // 로그아웃

		return http.build();
	}

	// 7) 인증관리자 관련 설정 : 사용자 정보를 가져올 서비스 재정의하거나
	// 인증방법(LDAP, JDBC 기반) 설정
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
			UserDetailService userDetailService) throws Exception {
		// userDetailService : 사용자 정보를 가져올 서비스 클래스 설정
		// (반드시 UserDetailsService class 를 상속받은 클래스)
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userService);

		authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 비밀번호 암호화하기 위한 인코더설정
		return new ProviderManager(authProvider);
	}

	// 8) 패스워드 인코더로 사용할 빈 등록
	// Crypt : 암호화 - 비밀번호 암호화
	// @Bean : 스프링 부품으로 등록
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}