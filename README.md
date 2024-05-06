# 인증 프로세스

## logout 1
- 스프링 시큐리티는 기본적으로 DefaultLogoutPageGenerationFilter 를 통해 로그아웃 페이지를 제공한다. 
- 로그아웃 실행은 기본적으로 POST 이지만 CSRF 기능을 비활성화 할 경우 혹은 RequestMatcher 를 사용할 경우 GET,PUT,DELETE 모두 가능
- 로그아웃 필터를 거치지 않고 스프링 MVC 에서 커스텀 하게 구현 가능 -> 로그인 페이즈 커스텀 하게 생성할 경우 로그아웃 기능도 구현해야한다.