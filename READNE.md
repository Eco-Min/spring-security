> [스프링 시큐리티 완전 정복 [6.x 개정판] - 정수원](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%EC%99%84%EC%A0%84%EC%A0%95%EB%B3%B5/dashboard) 

# SecurityBuilder / SecurityConfigurer
- SecurityBuilder 는 빌더 클래스로 웹 보안을 구성하는 빈 객체와 설정 클래스 들을 생성하는 역활은한다.   
  WebSecurity, HttpSecurity
- SecurityConfigurer 는 Http 요청과 관련된 보안처리를 담당하는 필터들을 생성하고 여러 초기화 설정에 관여 한다.
- SecurityBuilder 는 SecurityConfigurer 를 참조하고 있으며 인증 및 인가 초기화 작업은 SecurityConfigurer 에 의해 진행 된다.

![img_1.png](img/SecurityBuilder_Configurer.png)
![img_1.png](img/SecurityBuilder_Configurer2.png)

HttpSecurity.java, SpringBootWebSecurityConfiguration.java, AbstractSecurityBuilder.java 에서 
해당 break point 찍어 확인하면 된다.

# WebSecurity / HttpSecurity
- HttpSecurityConfiguration 에서 HttpSecurity 를 생성하고 초기화를 진행한다
- HttpSecurity 는 보안에 필요한 각 설정클래스와 필터들을 생성하고 최종적으로 SecurityFilterChain 빈생성

![img.png](img/HttpSecurity.png)
![img.png](img/filterChain.png)

## WebSecurity
- WebSecurityConfiguration 에서 WebSecurity 를 생성하고 초기화를 진행한다
- WebSecurity 는 HttpSecurity 에서 생성한 SecurityFilter Chain 빈을 SecurityBuilder 에 저장한다
- WebSecurity 가 build() 를 실행하면 SecurityBuilder 에서 SecurityFilterChain 을 꺼내어   
  FilterChainProxy 생성자에게 전달한다