# 인가 아키텍쳐

## 메서드 기반 인가 관리자 PreAuthorizeAuthorizationManager
- 스프링 시큐리티는 메서드 기반의 인증된 사용자 및 특정 권한울 가진 사용자의 자원 접근 허요 여부를 결정하는 인가 관리자 클래스를 제공
- PreauthorizeAuthorizationManager, PostAuthorizeAuthorizationManager, Jsr250AuthorizationManager, SecuredAuthorizationManager 등이 있다.
- 메서드 기반 권한 부여는 내부적으로 AOP 방식에 의해 초기화 설정이 으로어 지며 메서드 호출을 MethodInterceptor 가 가로채어 처리하고 있다.
- Aop 기반으로 ```AbstractAutoProxyCreator.createProxyClass``` 를 통해 프록시 객체를 생성하고 ```AbstractAutoProxyCreator.postProcessAfterInitialization``` 를 통해 프록시 객체를 생성한다.
```java
@PreAuthorize("hasAuthority('ROLE_USER')")
public List<User> users() {
System.out.println("users: " + UserRepositiry.findAll());
}
```
users() 메서드 호출[Proxy MethodInterceptor]은 인증객체[Authentication] 에서 ROLE_USER권한[GrantedAuthority] 을 필요로 한다.

### 메서드 권한 부여 초기화 과정
1. 스프링은 초기화 시 생성되는 전체 빈을 검사하면서 빈이 가진 메소드 중에서 보안이 설정된 메소드가 있는지 탐색한다
2. 보안이 설정된 메소드가 있다면 스프링은 그 빈의 프록시 객체를 자동으로 생성한다 (기본적으로 Cglib 방식으로 생성한다)
3. 보안이 설정된 메소드에는 인가처리 기능을 하는 Advice 를 등록한다
4.  스프링은 빈 참조시 실제 빈이 아닌 프록시 빈 객체를 참조하도록 처리한다
5. 초기화 과정이 종료된다
6. 사용자는 프록시 객체를 통해 메소드를 호출하게 되고 프록시 객체는 Advice 가 등록된 메서드가 있다면 호출하여 작동 시킨다
7. Advice 는 메소드 진입 전 인가 처리를 하게 되고 인가처리가 승인되면 실제 객체의 메소드를 호출하게 되고 인가처리가 거부되면 예외가 발생하고 메소드 진입이 실패한다

### 메서드 인터셉터 구조(MethodInterceptor)
- AuthorizationManagerBeforeMethodInterceptor   
지정된 AuthorizationManager 를 사용하여 Authentication 이 보안 메서드를 호출 할 수 있는지 여부를 결정하는 MethodInterceptor 구현체이다
- AuthorizationManagerAfterMethodInterceptor    
지정된 AuthorizationManager 를 사용하여 Authentication 이 보안 메서드의 반환 결과에 접근 할 수 있는지 여부를 결정할 수 있 는 구현체이다
- PreFilterAuthorizationMethodInterceptor   
@PreFilter 어노테이션에서 표현식을 평가하여 메소드 인자를 필터링 하는 구현체이다
- PostFilterAuthorizationMethodInterceptor   
@PostFilter 어노테이션에서 표현식을 평가하여 보안 메서드에서 반환된 객체를 필터링 하는 구현체이다

## 메서드 기반 CustomAuthorizationManager
- 사용자 정의 AuthorizationManager 를 생성함으로 메서드 보안을 구현할 수 있다.
- ```AuthorizationInterceptorsOrder``` 를 사용하여 인터셉터 순서를 지정할 수 있다.
- ```@EnableMethodSecurity(prePostEnabled = false)``` 시큐리티가 제공하는 클래스들을 비활성화 한다. 그렇지 않으면 중복해서 검사하게 된다
```java
public enum AuthorizationInterceptorsOrder {
    FIRST (Integer.MIN_VALUE),
    PRE_FILTER, // 100
    PRE_AUTHORIZE, // 200
    SECURED, // 300
    JSR250, // 400
    POST_AUTHORIZE, // 500
    POST_FILTER, // 600
    LAST (Integer.MAX_VALUE);
}
```
### 인터셉터 순서 지정
- 메서드 보안 어노테이션에 대응하는 AOP 메소드 인터셉터들은 AOP 어드바이저 체인에서 특정 위치를 차지한다
- 구체적으로 @PreFilter 메소드 인터셉터의 순서는 100, @PreAuthorize의 순서는 200 등으로 설정되어 있다
- 이것이 중요한 이유는 @EnableTransactionManagement와 같은 다른 AOP 기반 어노테이션들이 Integer.MAX_VALUE 로 순서가 설정되어 있는데    
기본적으로 이들은 어드바이저 체인의 끝에 위치하고 있다
- 만약 스프링 시큐리티보다 먼저 다른 어드바이스가 실행 되어야 할 경우, 예를 들어 @Transactional 과 @PostAuthorize 가 함께    
어노테이션 된 메소드가 있을 때 @PostAuthorize가 실행될 때 트랜잭션이 여전히 열려있어서 AccessDeniedException 이 발생하면    
롤백이 일어나게 하고 싶을 수 있다
- 그래서 메소드 인가 어드바이스가 실행되기 전에 트랜잭션을 열기 위해서는 @EnableTransactionManagement 의 순서를 설정해야 한다
- ```@EnableTransactionManagement(order = 0)```
- 위의 order = 0 설정은 트랜잭션 관리가 @PreFilter 이전에 실행되도록 하며 @Transactional 어노테이션이 적용된 메소드가 스프링 시큐리티의    
@PostAuthorize 와 같은 보안 어노테이션보다 먼저 실행되어 트랜잭션이 열린 상태에서 보안검사가 이루어지도록 할 수 있다.    
이러한 설정은 트랜잭션 관리와 보안 검사의 순서에 따른 의도하지 않은 사이드 이펙트를 방지할 수 있다
- AuthorizationInterceptorsOrder 를 사용하여 인터셉터 간 순서를 지정할 수 있다
