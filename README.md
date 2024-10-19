# 인가 이벤트

## Authorization Events
- Spring Security 는 권한 부여 이벤트 처리를 지원하며 권한이 부여되거나 거부된 경우에 발생하는 이벤트를 수신할 수 있다
- 이벤트를 수신하려면 ApplicationEventPublisher 를 사용하거나 시큐리티에서 제공하는 AuthorizationEventPublisher 를 사용해서 발행해야 한다
- AuthorizationEventPublisher 의 구현체로 SpringAuthorizationEventPublisher 가 제공 된다
### 이벤트 발행 방법
- ApplicationEventPublisher.publishEvent(ApplicationEvent)
- AuthorizationEventPublisher.publishAuthorizationEvent(Supplier<Authentication>, T, AuthorizationDecision)
### 이벤트 수신 방법
[AuthorizationEvents](./java/com/spring/security/AuthorizationEvents.java)

## 인가 이벤트 발행 & 수신
- 인가 이벤트를 발행하기 위해서는 SpringAuthorizationEventPublisher 를 사용해야 한다   
[SecurityConfig_authorizationEventPublisher()](./java/com/spring/security/SecurityConfig.java)
- 인가가 실패 했을 경우만 발생 되며 성공은 발생하지 않는다

## 커스텀 AuthorizationEventPublisher 구현
- AuthorizationEventPublisher 를 구현한 클래스를 만들어서 사용할 수 있다
[MyAuthorizationEventPublisher](./java/com/example/spring/MyAuthorizationEventPublisher.java)
```java
public class MyAuthorizationEventPublisher implements AuthorizationEventPublisher {
    ...
    @Override
    public <T> void publishAuthorizationEvent(Supplier<Authentication> authentication,
                                              T object, AuthorizationDecision decision) {
        ...
    }
```
