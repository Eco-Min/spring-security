# 개념
- SecurityBuilder 는 빌더 클래스로 웹 보안을 구성하는 빈 객체와 설정 클래스 들을 생성하는 역활은한다.   
  WebSecurity, HttpSecurity
- SecurityConfigurer 는 Http 요청과 관련된 보안처리를 담당하는 필터들을 생성하고 여러 초기화 설정에 관여 한다.
- SecurityBuilder 는 SecurityConfigurer 를 참조하고 있으며 인증 및 인가 초기화 작업은 SecurityConfigurer 에 의해 진행 된다.

![img.png](img/img.png)

HttpSecurity.java, SpringBootWebSecurityConfiguration.java, AbstractSecurityBuilder.java 에서 
해당 break point 찍어 확인하면 된다.