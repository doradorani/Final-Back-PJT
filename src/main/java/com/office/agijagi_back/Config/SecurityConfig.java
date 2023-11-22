package com.office.agijagi_back.Config;

import com.office.agijagi_back.Util.Jwt.CustomAccessDeniedHandler;
import com.office.agijagi_back.Util.Jwt.CustomAuthenticationEntryPoint;
import com.office.agijagi_back.Util.Jwt.JwtAuthenticationFilter;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //클래스의 인스턴스를 주입받는 필드 => 사용자 정의 인증 진입 지점
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    //클래스의 인스턴스를 주입받는 필드 => 사용자 정의 접근 거부 핸들러
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    //클래스의 인스턴스를 주입받는 필드 => JWT 관련 작업을 수행
    private final JwtProvider jwtProvider;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, JwtProvider jwtProvider) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()                       //CSRF(Cross-Site Request Forgery) 보안을 비활성화
//                .httpBasic().disable()              //HTTP Basic 인증을 비활성화

                //하위 부분부터는 설정에 정의한 내용을 순서대로 실행
                // 접근 권한 설정부

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // CORS Preflight 방지
                .antMatchers("/user/newToken", "/user/logOut", "/user/dupNickname/**").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/newToken", "/admin/signUp", "/admin/signIn", "/admin/logOut", "/admin/signOut").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/coBuy/admin/**").hasRole("ADMIN")
                .antMatchers("/coBuy/fundingProduct/**", "/coBuy/userDetailProduct/**", "/coBuy/coBuyHit/**", "/coBuy/myFundingProduct/**", "/coBuy/myHitProduct/**").hasRole("USER")
                .antMatchers("/coBuy/**").permitAll()
                .antMatchers("/kakao/**").permitAll()
                .antMatchers("/swagger-ui.html", "/api/v2/**", "/health", "/swagger/**", "/swagger-resources/**", "/webjars/**", "/v2/api-docs").permitAll()
                .antMatchers("/notice/**").permitAll()
                .antMatchers("/community/getMyPosts/**").hasRole("USER")
                .antMatchers("/community/**").permitAll()
                .antMatchers("/healthCheck").permitAll()
                // 특정 URL 경로에 대한 요청을 모두 허용
                .anyRequest().authenticated() // 나머지 모든 요청은 인증이 필요
                .and()
                .formLogin().disable()              //Form 로그인을 비활성화
                .headers()                          //
                .frameOptions()                     //
                .sameOrigin()                       //HTTP 헤더에서 프레임 옵션을 설정하여 동일한 출처에서만 프레임을 로드할 수 있도록 함
                .and()
                .cors()                             // CORS 에러 방지용
                                                    // 원래 출처(https://www.agijagi.site)에서의 요청을 허용하도록 설정

                // 세션을 사용하지 않을거라 세션 설정을 Stateless 로 설정
                //JSESSIONID 쿠키 생성 막음
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)



                // JWT 토큰 예외처리부
                .and()
                .exceptionHandling()                                            //예외 처리 설정 부분
                .authenticationEntryPoint(customAuthenticationEntryPoint)       //커스텀 인증 진입 지점
                .accessDeniedHandler(customAccessDeniedHandler)                 //커스텀 접근 거부 핸들러를 설정
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
                //JWT 인증 필터

        return http.build();    //Spring Security에서 사용되는 HttpSecurity 객체를 구성한 후, 해당 설정을 반환
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true); // 쿠키 허용
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 특정 도메인 패턴을 지정합니다.
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("https://www.agijagi.site"));

        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }


}
