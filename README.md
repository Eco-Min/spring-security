# 인가 프로세스

## 요청 기반 권한 부여

- Spring Security 는 요청 기반 권한 과 메소드 기반 관한 을 통해 자원에 대한 방어를 ㅔㅈ공한다.
- 요쳥 기반 권한 부여는 클라이언트 요청 즉, HttpServletRequest 에 대한 권한 부여 모델링 하는것이다.

## HttpSecurity.authorizeHttpRequests()

```java

@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated()); // 애플리케이션의 모든 엔드포인트가 최소한 인증된 보안 컨텍스트가 있어야 한다고 알림!!

    return http.build();
}
```

- authorizeHttpRequests() 는 사용자의 자원 접근을 위한 요청 엔드포인트와 접근에 필요한 권한을 매핑 시키기 위한 규칙을
  설정 하는것으로 서블릿 기반 엔드포인트에 접근 하려면 authorizeHttpRequests() 에 규칙들을 포함해야한다.
- authorizeHttpRequests() 를 통해 권한 규칙이 설정 되면 내부적으로 AuthorizationFilter 가 요청에 대한 권한 검사를 실시한다.

## authorizeHttpRequests() API
### requestMatchers()
- requestMatchers 메소드는 HTTP 요청의 URL 패턴, HTTP 메소드, 요청 파라미터 등을 기반으로 어떤 요청에 대해서는 특정 보안 설정을 적용하고    
다른 요청에 대해서는 적용하지 않도록 세밀하게 제어할 수 있게 해 준다
- 예를 들어 특정 API 경로에만 CSRF 보호를 적용하거나, 특정 경로에 대해 인증을 요구하지 않도록 설정할 수 있다. 이를 통해 애플리케이션의    
보안 요구 사항에 맞춰서 유연한 보안 정책을 구성할 수 있다

> 1. requestMatchers(String... urlPatterns)   
   • 보호가 필요한 자원 경로를 한 개 이상 정의한다
> 2. requestMatchers(RequestMatcher... requestMatchers)   
   • 보호가 필요한 자원 경로를 한 개 이상 정의한다. AntPathRequestMatcher, MvcRequestMatcher 등의 구현체를 사용할 수 있다
> 3. requestMatchers(HttpMethod method, String... utlPatterns)   
   • Http Method 와 보호가 필요한 자원 경로를 한 개 이상 정의한다

### 주의 사항
- 스프링 시큐리티는 클라이언트의 요청에 대하여 위에서 부터 아래로 나열된 순서대로 처리하며 요청에 대하여 첫 번째 일치만 적용되고 다음 순서로 넘어가지 않는다
- /admin/** 가 /admin/db 요청을 포함하므로 의도한 대로 권한 규칙이 올바르게 적용 되지 않을 수 있다. 그렇기 때문에 엔드 포인트 설정 시    
좁은 범위의 경로를 먼저 정의하고 그것 보다 큰 범위의 경로를 다음 설정으로 정의 해야 한다

### 권한 규칙 종류

| api                | 정의                                                                 |
|--------------------|--------------------------------------------------------------------|
| authenticated      | 인증된 사용자의 접근을 허용한다                                                  |
| fullyAuthenticated | 아이디와 패스워드로 인증된 사용자의 접근을 허용, rememberMe 인증 제외한다                     |
| anonymous          | 익명사용자의 접근을 허용한다                                                    |
| rememberMe         | 기억하기를 통해 인증된 사용자의 접근을 허용한다                                         |
| permitAll          | 요청에 승인이 필요하지 않는 공개 엔드포인트이며 세션에서 Authentication 을 검색하지 않는다          |
| denyAll            | 요청은 어떠한 경우에도 허용되지 않으며 세션에서 Authentication 을 검색하지 않는다               |
| access             | 요청이 사용자 정의 AuthorizationManager 를 사용하여 액세스를 결정한다(표현식 문법 사용)        |
| hasAuthority       | 사용자의 Authentication 에는 지정된 권한과 일치하는 GrantedAuthority 가 있어야 한다      |
| hasRole            | hasAuthority 의 단축키로 ROLE_ 또는 기본접두사로 구성된다. ROLE_ 을 제외해야 한다          |
| hasAnyAuthority    | 사용자의 Authentication 에는 지정된 권한 중 하나와 일치하는 GrantedAuthority 가 있어야 한다 |
| hasAnyRole         | hasAnyAuthority의 단축키로 ROLE_ 또는 기본 접두사로 구성된다. ROLE_ 을 제외해야 한다       |
