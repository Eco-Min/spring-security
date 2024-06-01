# 세션 관리

## 동시 세션 제어
- 동시 세션제어는 사용자가 동시에 여러 세션을 생헝하는 것을 관리하는 전략이다.
- 이 전략은 사용자의 인증 후에 활상화된 세션의 수가 설정된 maximumSessions 값과 비교하여 제어 여부를 결정한다.

### 동시세션 제어 2가지 유형
1. 사용자 세션 강제 만료
    - 최대허용 개수만큼 동시 인증이 가능하고 그 외 이전 사용자의 세션을 만료 시킨다.
    - 최대 세션 허용 개수 초과 -> <span style="color:red"> 세션 강제 만료 </span>
2. 사용자 인증 시도 차단
   - 최대 허용 개수만큼 동시 인증이 가능하고 그 외 사용자의 인증 시도를 차단 
   - 최대 세션 허용 개수 초과 -> <span style="color:red"> 인증 예외 발생 </span>


```java
http.sessionManagement(session -> session
    // 이미 만료된 세션으로 요청을 하는 사용자를 특정 엔드포인트로 리다이렉션 할 Url 을 지정한다.maximumSessions(1)
    // 사용자당 최대 세션 수를 제어한다. 기본값은 무제한 세션을 허용한다
    .invalidSessionUrl(“/invalidSessionUrl”)
    // true 이면 최대 세션 수(maximumSessions(int))에 도달했을 때 사용자의 인증을 방지한다
    // false(기본 설정)이면 인증하는 사용자에게 접근을 허용하고 기존 사용자의 세션은 만료된다
    .maxSessionsPreventsLogin(true) 
    // 세션을 만료하고 나서 리다이렉션 할 URL 을 지정한다
    .expiredUrl("/expired"));
```
여기서 invalidSessionUrl, expiredUrl 의 같은경우 ```사용자 세션 강제 만료 maxSessionsPreventsLogin(false)``` 일때만 가능하다   

| invalidSessionUrl() | expiredUrl() |result|
|---------------------|--------------|---|
| X                   | X            |This session has been expired|
| O                   | X            |This session has been expired|
| O                   | O            |invalidSessionUrl()에 설정된 URL로 리다이렉션|
| X                   | O            |expiredUrl()에 설정된 URL로 리다이렉션|

## 세션 고정 보호
- 세션 고정 공격은 악의적인 공격자가 사이트에 접근항 ㅕ세션을 생성한 다음 다른 사용자 같은 세션으로 로그인 하도록 유도하는 위험을 말한다.
- 스프링 시큐리티는 사용자가 로그인 할 때 새로운 세션을 생성하거나 세션 ID를 변경함으로써 이러한 공격에 자동 대응 한다.

### sessionFixation()   
- .changeSessionId()   
기존 세션을 유지하면서 세션 ID만 변경하여 인증 과정에서 세션 고정 공격을 방지하는 방식이다. 기본 값으로 설정되어 있다   
- .newSession()   
새로운 세션을 생성하고 기존 세션 데이터를 복사하지 않는 방식이다(SPRING_SECURITY_ 로 시작하는 속성은 복사한다)   
- .migrateSession()   
새로 운 세션을 생성하고 모든 기존 세션 속성을 새 세션으로 복사한다   
- .none()   
기존 세션을 그대로 사용한다

## 세션 생성 정책
- 스프링 시큐리티에서는 인증된 사용자에 대한 세션 생성 정책을 성정하여 어떻게 세션을 관리할지 결정할 수 있다.   

   
- SessionCreationPolicy.ALWAYS
  - 인증 여부에 상관없이 항상 세션을 생성한다
  - ForceEagerSessionCreationFilter 클래스를 추가 구성하고 세션을 강제로 생성시킨다
- SessionCreationPolicy.NEVER
  - 스프링 시큐리티가 세션을 생성하지 않지만 애플리케이션이 이미 생성한 세션은 사용할 수 있다
- SessionCreationPolicy.IF_REQUIRED
  - 필요한 경우에만 세션을 생성한다. 예를 들어 인증이 필요한 자원에 접근할 때 세션을 생성한다
- SessionCreationPolicy.STATELESS
  - 세션을 전혀 생성하거나 사용하지 않는다
  - 인증 필터는 인증 완료 후 SecurityContext 를 세션에 저장하지 않으며 JWT 와 같이 세션을 사용하지 않는 방식으로 인증을 관리할 때 유용할 수 있다
  - SecurityContextHolderFilter 는 세션 단위가 아닌 요청 단위로 항상 새로운 SecurityContext 객체를 생성하므로 컨텍스트 영속성이 유지되지 않는다
> STATELESS 설정에도 세션이 생성 될수 있다.   
스프링 시큐리티에서 CSRF 기능이 활성화 되어 있고 CSRF 기능이 수행 될 경우 사용자의 세션을 생성해서 CSRF 토큰을 저장하게 된다   
세션은 생성되지만 CSRF 기능을 위해서 사용될 뿐 인증 프로세스의 SecurityContext 영속성에 영향을 미치지는 않는다.   
-> 인증과 관련되자 않는곳에는 세션에 영향을 받기 때문 (NullSecurityContextRepository 디버깅)
 
