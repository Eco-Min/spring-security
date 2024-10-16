# 인가 아키텍처

## 요청 기반 인가 처리
- 스프링 시큐리티는 요청 기반의 인증된 사용자 및 특정 권한을 가진 사용자의 자원 접근 허용여부를 결정하는 인가 관리자 클래스를 제공
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
## 요청기반 Custom_authorizationManager

- 스프링 시큐리티 인가 설정 시 선언적 방식이 아닌 프로그래밍 방식으로 구현할 수 있으며 access(AuthorizationManager) API 를 사용한다
- access() 에는 AuthorizationManager<RequestAuthorizationContext> 타입의 객체를 전달할 수 있으며 사용자의 요청에 대한 권한 검사를    
access()에 지정한 AuthorizationManager 가 처리하게 된다
- access() 에 지정한 AuthorizationManager 객체는 RequestMatcherDelegatingAuthorizationManager 의 매핑 속성에 저장된다

```java
/*
http.authorizeHttpRequests(auth ->
    auth.requestMatcher().access(AuthorizationManager));
*/
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/user", "/myPage").hasAuthority("USER")
    .requestMatchers("/admin").hasRole("ADMIN")
    .requestMatchers("/api").access(new CustomAuthorizationManager())
```
- 특정한 엔드포인트에 대한 권한 검사를 수행하기 위해 AuthorizationManager를 구현하여 설정한다.
- /user, /myPage, /admin 요청 패턴의 권한 검사는 AuthorityAuthorizationManager 가 처리
- /api 요청 패턴의 권한 검사는 CustomAuthorizationManager 가 처리하게 된다   

[CustomAuthorizationManager.java](/src/main/java/com/spring/security/CustomAuthorizationManager.java)

## RequestMatcherDelegatingAuthorizationManager
- RequestMatcherDelegatingAuthorizationManager 는 RequestMatcher 와 AuthorizationManager 를 매핑하여 저장하고,   
  사용자의 요청 정보와 매핑된 AuthorizationManager 를 사용하여 권한 검사를 수행한다
- RequestMatcherDelegatingAuthorizationManager 의 mappings 속성에 직접 RequestMatcherEntry 객체를 생성하고 추가한다

```java
RequestMatcherEntry<T>
    .getEtnry() // 요청 패턴에 맾이된 AuthorizationManager 객체를 반환
    .getReqeustMatcher() //요청 패턴을 저장한 RequestMatcher 객체를 반환
```

### 적용
- RequestMatcherDelegatingAuthorizationManager 를 감싸는 CustomRequestMatcherDelegatingAuthorizationManager 를 구현한다   

CustomRequestMatcherDelegatingAuthorizationManager   
RequestMatcherDelegatingAuthorizationManager(mappings)   

```java

http.authorizeHttpRequests(auth -> auth
        .anyRequest().access(new CustomRequestMatcherDelegatingAuthorizationManager());
```
- 적용 후 구조
```java
RequestMatcherDelegatingAuthorizationManager > CustomRequestMatcherDelegatingAuthorizationManager > CustomAuthorizationManager   
access() 설정시 기본 생성되는 대리자 객체 ////////// -> 직접 구현한 커스텀 객체
```
- 요청에 대한 권한 검사를 RequestMatcherDelegatingAuthorizationManager 가 처리하게 되고,   
  CustomRequestMatcherDelegatingAuthorizationManager 가 모든 요청에 대한 권한 검사를 처리하게 된다
- 위의 적용후 구조는 개선이 필요하다.
