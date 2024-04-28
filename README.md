# 인증 아키텍쳐

## RememberMe 인증
- 사용자가 웹 사이트나 어플리케이션에 로그인 할때 자동으로 인증 정보를 기억하는 기능
- UsernamePasswordAuthenticationFilter 와 함께 사용되며, AbstractAuthenticationProcessingFilter 슈퍼클래스에서 훅을 통해 구현
  - 인증 성공 : RememberMeService.loginSuccess() 를 통해 RememberMe 토큰 생성하고 쿠기로 전달
  - 인증 실패 : RememberMeServices.loginFail() 를 통해 쿠키 삭제
  - LogoutFilter 와 연계하여 쿠키 삭제

## 토큰 생성
- 기본적으로 암호화된 토큰을 생성 브루우저에 쿠키를 보내고, 향후 세션에서 이쿠키를 감지하여 자동 로그인 이 이루어진다.
- base64

## RememberMeServices 구현체
- TokenBasedRememberMeServices - 쿠키 기반 토큰의 보안을 위해 해싱 사용
- PersistentTokenBasedRememberMeServices - 생성된 토큰을 저정하기위해 데이터베이스 또는 다른 영구 저장매체 사용
- 두 구현 모두 사용자의 정보 검색을 위한 UserDetailsService 가 필요

## RememberMeAuthenticationFilter
- SecurityContextHolder 에 Authentication 이 포함되지 않은 경우 실행되는 필터
- 세션이 만료되었거나 어플리케이션 종룡로 인해 인증 상태가 소멸된 경우 토큰 기반 인증을 사용해 유효성을 검사하고 토큰이 검증되면 자동 로그인 처리 수행

![img.png](img/img.png)