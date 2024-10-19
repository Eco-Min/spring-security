# 고급 설정

## Custom DSLs
- Spring Security 는 사용자정의 DSL을 구현할 수 있도록 지원 한다.
- DSL 을 구성하면 필터, 핸들로, 메소드, 속성등을 한곳에 정의하여 처리할 수 있는 편리함을 제공한다.
- [DSL 이런 뭔가](https://devbksheen.tistory.com/entry/%EB%AA%A8%EB%8D%98-%EC%9E%90%EB%B0%94-DSL%EB%8F%84%EB%A9%94%EC%9D%B8-%EC%A0%84%EC%9A%A9-%EC%96%B8%EC%96%B4%EC%9D%B4%EB%9E%80)

## AbstractHttpConfigurer<AbstractHttpConfigurer, HttpSecurityBuilder>
- AbstractHttpConfigurer 는 DSL 자기 자신
- HttpSecurityBuilder 는 HttpSecurity
- 사용자의 DSL 을 구현하기 위해서 상속받는 추상 클래스로서 구현 클래스는 두개의 메소드를 오버라이딩 한다.
  - init(HttpSecurity http)(B builder) : HttpSecurity 의 구성요소를 성정 및 공유하는 작업
  - configure(HttpSecurity http)(B builder) : 공동 클래스를 구성 하거나 사용자 정의 필터를 생성하는 작업

## API
-  HttpSecurity.with(C configurer, Customizer<C> customizer)
    - configurer 는 AbstractHttpConfigurer 을 상속하고 DSL 을 구현한 클래스가 들어간다
    - customizer 는 DSL 구현 클래스에서 정의한 여러 API 를 커스트 마이징한다 
    - 동일한 클래스를 여러 번 설정하더라도 한번 만 적용 된다