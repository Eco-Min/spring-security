# 인가 프로세스

## 표현식 및 커스텀 권한 구현
- 스프링 시큐리티는 표현식을 사용해서 권한 규칙을 설정 하도록 WebExpressionAuthorizationManager 를 제공한다.
- 표현식은 시큐리티가 제공하는 권한 규칙을 사용하거나 사용자가 표현식을 커스텀 하게 구현해서 설정 가능하다.

### 표현식 권한 구현
```java
requestMatchers().access(new WebExpressionAuthorizationManager("expression"));
// 요청으로부터 값을 추출할 수 있다
requestMatchers("/resource/{name}").access(new WebExpressionAuthorizationManager("#name == authentication.name")
// 여러개의 권한 규칙을 조합할 수 있다
requestMatchers("/admin/db").access(new WebExpressionAuthorizationManager("hasAuthority('DB') or hasRole('ADMIN')"))

위으 두 내용을 합친 것
requestMatchers("/admin/db").access(anyOf(hasAuthority("db"), hasRole("ADMIN")))
```

### 커스텀 권한 표현식 구현
```java
DefaultHttpSecurityExpressionHandler expressionHandler = new DefaultHttpSecurityExpressionHandler();
expressionHandler.setApplicationContext(context);

WebExpressionAuthorizationManager expressManager = new WebExpressionAuthorizationManager(
        "@customWebSecurity.check(authentication,request)"); // 빈 이름을 참조하여 접근 제어

expressManager.setExpressionHandler(expressionHandler);

http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/resource/**").access(expressManager));
```
```java
@Component("customWebSecurity")
public class CustomWebSecurity {
    public boolean check(Authentication authentication, HttpServletRequest request) {
        return authentication.isAuthenticated(); //,사용자가,인증되었는지를,검사
    }
}
```
- 사용자 정의 빈을 생성하고 새로운 표현식으로 사용할 메서드를 정의하고 권한 검사 로직을 구현한다.

## 커스텀 RequestMatcher
- RequestMatcher 의 macher 및 matches 메서드를 사용하여 클라이언트의 요청객체로부터 값을 검증하도록   
  커스텀한 RequestMatcher 를 구현하고 requestMatchers() 메서드에 설정한다
- endPoint 의 요청에 대해서 권한 검사 요청이 가능
```java
public class CustomRequestMatcher implements RequestMatcher {
    private final String urlPattern;
    public CustomRequestMatcher(String urlPattern) {
        this.urlPattern = urlPattern;
    }
    
    @Override
    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith(urlPattern);
    }
}
```
```java
http.authorizeHttpRequests((authorize) -> authorize
        .requestMatchers(new CustomRequestMatcher("/api/**"))
        .hasAuthority("USER")
        .anyRequest().authenticated());
```