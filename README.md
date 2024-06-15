# 인가 프로세스

## SecurityMatcher()
- 특정 패턴에 해당하는 요청에만 보안 규칙을 적용할 수 있도록 설정 할 수 있다. 중복인 경우 마지막 설정으로 대처
```java
//특정 자원 보호가 필요한 경로를 정의한다
1. securityMatcher(String... urlPatterns);
    
//특정 자원 보호가 필요한 경로를 정의한다. AntPathRequestMatcher, MvcRequestMatcher 등의 구현체를 사용할 수 있다
2. securityMatcher(RequestMatcher... requestMatchers);
```

```java
http.securityMatcher("/api/**").authorizeHttpRequests(auth -> auth.requestMatchers(…))
```

![img](/img/securityMatcher.png)

- HttpSecurity 를 /api 로 시작하는 URL 에만 적용하도록 구성한다.
- Spring MVC 가 클래스 경로에 있으면 MvcRequestMatcher 가 사용되고, 그렇지 않으면 AntPathRequestMatcher 가 사용된다.

## securityMatchers(Customizer<RequestMatcherConfigurer>)
- 다중 패턴 설정
  - securityMatchers 메소드는 특정 패턴에 해당하는 요청을 단일이 아닌 다중 설정으로 구성해서 보안 규칙을 적용할 수 있으며    
  현재의 규칙은 이전의 규칙을 대체하지 않는다
```java
// 패턴 1
http. securityMatchers((matchers) -> matchers.requestMatchers("/api/**", "/oauth/**"));
// 패턴 2
http. securityMatchers((matchers) -> matchers.requestMatchers("/api/**").requestMatchers("/oauth/**"));
// 패턴 3
http.securityMatchers((matchers) -> matchers.requestMatchers("/api/**")
    .securityMatchers((matchers) -> matchers.requestMatchers("/oauth/**"));
```
