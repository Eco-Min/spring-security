# 악용 보호

## CSRF(Cross Site Request Forgery, 사이트 간 요청 위조)
- 웹 어플리케이션의 보약 취약점으로 공격자가 사용자로 하여금 이미 인증된 다른 사이트에 원치 않는 작업을 수행하게 만드는 기법
- 사용자의 브라우저가 자동으로 보낼 수 있는 인증 정보르, 예를들어 쿠키나 기본 인증 세션을 이용하여 사용자가 의도하지 않은 요청을 서버로 전송하게 만든다.
- 이는 사용자가 로그인한 상태에서 악의적인 웹 사이트를 방문하거나 이메일 등을 통해 악의적인 링크를 클릭할 때 발생할 수 있다.

## CSRF 진행 순서
![img](/img/csrf.png)

- 토큰은 서버에 의해 생성되어 클라이언트의 세션에 저장되고 폼을 통해 서버로 전송되는 모든 변경 요청에 포함되어야 하며   
서버는 이 토큰을 검증하여 요청의 유효성을 확인한다
- 기본 설정은 'GET', 'HEAD', 'TRACE', 'OPTIONS’ 와 같은 안전한 메서드를 무시하고 'POST', 'PUT', 'DELETE’ 와 같은    
변경 요청 메서드에 대해서만 CSRF 토큰 검사를 수행한다
- 중요한 점은 실제 CSRF 토큰이 브라우저에 의해 자동으로 포함되지 않는 요청 부분에 위치해야 한다는 것으로서    
HTTP 매개변수나 헤더에 실제 CSRF 토큰을 요구하는 것이 CSRF 공격을 방지하는데 효과적이라 할 수 있다
- 반면에 쿠키에 토큰을 요구하는 것은 브라우저가 쿠키를 자동으로 요청에 포함시키기 때문에 효과적이지 않다고 볼 수 있다

## CSRF 기능 비활성화
CSRF 는 쿠키, 세션 등을 활용하여 공격 하는 방법으로 이것들을 사용하지 않는 방식의 공격에서는 굳이 CSRF 기능이 필요 없을 수 있다.
```java
http.csrf(csrf -> csrf.disabled()); // -> 전체 비활성화
http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/*")); // -> 특정 엔드 포인트만 비활성화 
```

## CSRF 토큰 유지 
- CsrfToken 은 CsrfTokenRepository 를 사용하여 영속화 하며 HttpSessionCsrfTokenRepository 와 CookieCsrfTokenRepository 를 지원한다.
- 두군데 중 원하는 위치에 토큰을 저장할 수 있다.

### 1. 세션에 토큰 저장 HttpSessionCsrfTokenRepository()
```java
HttpSessionCsrfRepository repository = new HttpSessionCsrfRepository();
http.csrf(csrf -> csrf.csrfTokenRepository(repository));
```
   - 기본적으로 토큰은 HttpSessionCsrfTokenRepository 사용한다
   - Http 요청 헤던인 X-CSRF-TOKEN 또는 요청 매개변수인 _csrf 에서 토큰을 읽느다.
   

### 2. 쿠키에 토큰 저장 CookieCsrfTokenRepository
```java
CookieSessionCsrfRepository repository = new CookieSessionCsrfRepository();
http.csrf(csrf -> csrf.csrfTokenRepository(repository)); // 1 번 방식 -> response -> 브라우저에 저장 -> http 통신에만 사용 가능
http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTkoenRepository.withHttpOnlyFalse())); // 2 번 방식
// -> javaScript 에서도 쿠키를 읽는게 가능
```

  - CookieCsrfTokenRepository 는 기본적으로 XSRF-TOKEN 명을 가진 쿠키에 작성하고 HTTP 요청 헤더인 X-XSRF-TOKEN 또는 요청 매개변수인 _csrf 에서 읽는다

## CSRF 토큰 처리 - CsrfTokenRequestHandler
```java
XorCsrfTokenRequestAttributeHandler csrfTokenHandler = new XorCsrfTokenRequestAttributeHandler();
http.csrf(csrf -> csrf.csrfTokenRequestHandler(csrfTokenHandler));
```
- CsrfToken 은 CsrfTokenRequestHandler 를 사용하여 토큰을 생성 및 응답하고 HTTP 헤더 또는 요청 매개변수로부터 토큰의 유효성을 검증하도록 한다
- XorCsrfTokenRequestAttributeHandler 와 CsrfTokenRequestAttributeHandler 를 제공하며 사용자 정의 핸들러를 구현할 수 있다
- “_csrf ” 및 CsrfToken.class.getName() 명으로 HttpServletRequest 속성에 CsrfToken 을 저장하며 HttpServletRequest 으로부터    
CsrfToken 을 꺼내어 참조할 수 있다
- 클라이언트의 매 요청마다 CSRF 토큰 값(UUID) 에 난수를 인코딩하여 변경한 CsrfToken 이 반환 되도록 보장한다. 세션에 저장된 원본 토큰 값은 그대로 유지한다
- 헤더 값 또는 요청 매개변수로 전달된 인코딩 된 토큰은 원본 토큰을 얻기 위해 디코딩되며, 그런 다음 세션 혹은 쿠키에 저장된 영구적인 CsrfToken과 비교된다

## CSRF 토큰 지연 로딩
```java
XorCsrfTokenRequestAttributeHandler handler = new XorCsrfTokenRequestAttributeHandler();
handler.setCsrfRequestAttributeName(null); //지연된 토큰을 사용하지 않고 CsrfToken 을 모든 요청마다 로드한다

http.csrf(csrf -> csrf
.csrfTokenRequestHandler(handler));
```

- Spring Security 는 CsrfToken 을 필요할 때까지 로딩을 지연시키는 전략을 사용한다. 즉, HttpSession 에 저장되어 있기 때문에   
매 요청마다 세션으로부터 csrfToken 을 로드할 필요가 없어져 성능을 향상시킬 수 있다.
- CsrfToken 은 POST 와 같은 안전하지 않은 HTTP 메소드를 사용하여 요청이 발생할때와    
CSRF 토큰을 응답에 렌더링 하는 모든 요청에서 필요하기 때문에 그 외 요청에는 지연 로딩 하는것을 권장한다