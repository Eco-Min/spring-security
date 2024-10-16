# 이벤트 처리

## Authentication Events Publisher
```java
@Bean
@Bean
public AuthenticationEventPublisher customAuthenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    Map<Class<? extends AuthenticationException>, Class<? extends AbstractAuthenticationFailureEvent>> mapping = 
            Collections.singletonMap(CustomException.class, CustomAuthenticationFailureEvent.class);

    DefaultAuthenticationEventPublisher authenticationEventPublisher = new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    authenticationEventPublisher.setAdditionalExceptionMappings(mapping); 
    // CustomException 을 던지면 CustomAuthenticationFailureEvent 를 발행하도록 추가 함

    return authenticationEventPublisher;
}
```
커스텀한 예외를 추가 할 수있다.
```java
uthenticationEventPublisher.publishAuthenticationFailure(new CustomException("CustomException"), authentication);
```
이걸 EventListener 가 받는다고 하면
```java
@EventListener
public void onFailure(CustomAuthenticationFailureEvent failures) { // 커스텀 예외에 대한 이벤트를 수신할 수 있다.
    System.out.println(" failures = " + failures.getException().getMessage());
}
```

## 기본 이벤트 설정
AuthenticationException 이 발생시 해당 예외에 매핑된 이벤트가 발행이 안되어 있을경우 기본 AuthenticationFailureEvent 를 발행 및 수신 가능하다.
```java
@Bean
public AuthenticationEventPublisher defaulAuthenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    DefaultAuthenticationEventPublisher authenticationEventPublisher = 
            new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    authenticationEventPublisher.setDefaultAuthenticationFailureEvent(CustomDefaultAuthenticationFailureEvent.class);
    return authenticationEventPublisher;
}
```
```java
eventPublisher.publishAuthenticationFailure(new CustomAuthenticationException("CustomAuthenticationException"), authentication);
```
```java
@EventListener
public void onSuccess(CustomAuthenticationFailureEvent failures) { // 매핑이 안된 모든 예외에 대해 받는다.
System.out.println("failures = " + failures.getException().getMessage());
}
```