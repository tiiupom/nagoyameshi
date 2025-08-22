package com.example.nagoyameshi.security;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println(http);
		http
			.authorizeHttpRequests((requests) -> requests
					.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**").permitAll()
					.requestMatchers("/stores/{storeId}/reviews/**", "/reservations/**", "/restaurants/{restaurantId}/reservations/**", "/favorites/**", "/restaurants/{restaurantId}/favorites/**").hasAnyRole("GENERAL","SUBSCRIBER") // 無料・有料会員のみ
					.requestMatchers("/stores/**", "/company", "/terms").hasAnyRole("ANONYMOUS", "GENERAL", "SUBSCRIBER") // 未ログインのユーザー、無料・有料会員のみ
					.requestMatchers("/subscription/register", "/subscription/create").hasRole("GENERAL")	// 無料会員のみ
					.requestMatchers("/subscription/edit", "/subscription/update", "/subscription/cancel", "/subscription/delete").hasRole("SUBSCRIBER")  // 有料会員のみ
					.requestMatchers("/admin/**").hasRole("ADMIN")	// 管理者のみアクセス
					.anyRequest().authenticated()	// 上記以外はログイン必須
			)
			.formLogin((form) -> form
					.loginPage("/login")				// ログインページのURL
					.loginProcessingUrl("/login")		// ログインフォームの送信先URL
					.defaultSuccessUrl("/?loggedIn")	// ログイン成功時のリダイレクト先URL
					.failureUrl("/login?error")			// ログイン失敗時のリダイレクト先URL
					.permitAll()
			)
			.logout((logout) -> logout
					.logoutSuccessUrl("/?loggedOut")	// ログアウト時のリダイレクト先URL
					.permitAll()
			);
		System.out.println(http);
		return http.build();
	}
	
	// ハッシュアルゴリズムを設定（パスワード用のハッシュ値を生成）
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
