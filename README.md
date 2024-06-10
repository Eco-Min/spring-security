# 악용 보호

## 사용
- CSRF 공격을 방지하기 위한 토큰 패턴을 사용하러면 실제 CSRF 토큰을 HTTP 요청에 포함해야 한다.
- 그래서 브라우저에 의해 HTTP 요청에 자동으로 포함되 않는 부분(form 매개변수, HTTP 헤더 또는 기타) 중 하나에 포함 시켜야 한다.

## HTML Forms
``` html
<form action="/memberJoin" method="post">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
<input type="hidden" name="_csrf" value="4bfd1575-3ad1-4d21-96c7-4ef2d9f86721"/>
```
- 폼에 실제 CSRF 토큰을 자동으로 포함하는 뷰는 다음과 같다
  - Thymeleaf
  - Spring 의 폼 태그 라이브러리 - <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

## JavaScript
### Single Page Application
- CookieCsrfTokenRepository.withHttpOnlyFalse 를 사용해서 클라이언트가 서버가 발행한 쿠키로 부터 CSRF 토큰을 읽을 수 있도록 한다
- 사용자 정의 CsrfTokenRequestHandler 을 만들어 클라이언트가 요청 헤더나 요청 파라미터로 CSRF 토큰을 제출할 경우 이를 검증하도록 구현한다
- 클라이언트의 요청에 대해 CSRF 토큰을 쿠키에 렌더링해서 응답할 수 있도록 필터를 구현한다

![img](/img/single.png)

### Multi Page Application
- JavaScript 가 각 페이지에서 로드되는 멀티 페이지 애플리케이션의 경우 CSRF 토큰을 쿠키에 노출시키는 대신 HTML 메타 태그 내에 CSRF 토큰을 포함시킬 수 있다
```html
!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<meta name="_csrf" th:content="${_csrf.token}"/>
<meta name="_csrf_header" th:content="${_csrf.headerName}"/>
</html>
```
```javascript
function login() {
const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
const csrfToken = $('meta[name="_csrf"]').attr('content');
fetch('/api/login', {
    method: 'POST',
    headers: {[csrfHeader]: csrfToken }
});
```
