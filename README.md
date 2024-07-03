# 인가 아키텍처

## 요청 기반 인가 처리
- 스프링 시큐리티는 요청 기반의 인증된 사용자 및 특정 권한ㅇ르 가진 사용자의 자원 접근 허용여부를 결정하는 인가 관리자 클래스를 제공
- 대표적으로 AuthorityAuthorizationManager, AuthenticatedAuthorizationManager 와 대리자인    
RequestMatcherDelegatingAuthorizationManager 가 있다.

### 스프링 시큐리티 인가 처리
```java
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/user").hasRole("USER")
```
/user 엔드포인트 접근```Request``` 은 인증객체```Authentication``` 에서 ROLE_USER ```GrantedAuthority``` 권한을 필요
![img](/img/springSecurity.png)

### AuthenticationAuthorizationManager 구조
- FullyAuthenticatedAuthorizationStrategy: 익명 인증 및 기억하기 인증이 아닌지 검사
- AuthenticatedAuthorizationManager: 인증된 사용자인지 확인
- RememberMeAuthorizationStrategy: 기억하기 인증이 아닌지 확인
- AnonymousAuthorizationStrategy: 익명 사용자가 아닌지 확인   
```java
@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/user").authenticated()
        .requestMatchers("/myPage").fullyAuthenticated()
        .requestMatchers("/guest").anonymous()
        .requestMatchers("/history").rememberMe());
    return http.build();
}
```
AuthenticatedAuthorizationManager 는 내부적으로 네 개의 AbstractAuthorizationStrategy 구현을 통해 인증 여부 확인 전략을 세운다

### AuthorityAuthorizationManager 구조
- 요청 패턴과 매핑할 권한 정보를 설정한다
- AuthorityAuthorizationManager 는 내부적으로 AuthoritiesAuthorizationManager 를 사용하여 권한 여부 결정을 위임한다
- AuthorityAuthorizationManager 는 매핑된 요청 패턴과 권한 정보를 사용하여   
  사용자의 요청 정보와 Authentication 권한을 비교해서 서로 일치하는지 검사한다
```java
@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http){
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/user", "/myPage").hasAuthority("USER")
        .requestMatchers("/admin").hasRole("ADMIN")
        .requestMatchers("/payment").hasAnyRole("ADMIN","MANAGER")
        .requestMatchers("/api").hasAnyAuthority("USER",“GUEST")
    return http.build();
}
```