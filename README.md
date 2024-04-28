# 인증 아케텍쳐

## 폼 인증 필터 - UsernamePasswordAuthenticationFilter
- 스프링 시큐리티는 AbstractAuthenticationProcessingFilter 클래스를 사용자의 자격 증명을 인증하는 기폰 빌터로 사용
- UsernamePasswordAuthenticationFilter 는 AbstractAuthenticationProcessingFilter 를 확장한 클래스로   
HttpServletRequest 에서 제출된 사용자 이름과 비밀번호로부터 인증을 수행한다.
- 인증 프로세스가 초기화 될때 DefaultLoginPAgeGenerationFilter 및 DefaultLogoutPageGenerationFilter 가 초기화 된다.

![img.png](img/img.png)
