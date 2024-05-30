# 인증 상태 영속성

## SecurityContextRepository
- 스프링 시큐리티에서 사용자가 인증을 한 이후 요청에 대해 계속 사용자의 인증을 유지하기 위해 사용되는 클래스이다.
- 인정 상태의 영속 매커니즘은 사용자가 인증을 하게 되면 해당 사용자의 인증 정보와 권한이 SecurityContext 에 저장되고   
HttpSession 을 통해 요청 간 영속이 이루어 지는 방식이다.

### 인증 요청
Client (Get /login) -> AuthenticationFilter -> SecurityContext -> SecurityContextRepository (SecurityContext 를 명시적으로 저장) -> HttpSession

### 인증 후 요청
Client (Get /user) -> SecurityContextHolderFilter -> SecurityContextRepository -> HttpSession -> SecurityContext

- HttpSessionSecurityContextRepository:    
요청 간에 HttpSession 에 보안 컨텍스트를 저장한다. 후속 요청 시 컨텍스트 영속성을 유지한다   


- RequestAttributeSecurityContextRepository:  
ServletRequest 에 보안 컨텍스트를 저장한다. 후속 요청 시 컨텍스트 영속성을 유지할 수 없다


- NullSecurityContextRepository:   
세션을 사용하지 않는 인증(JWT, OAuth2) 일 경우 사용하며 컨텍스트 관련 아무런 처리를 하지 않는다


- DelegatingSecurityContextRepository:   
RequestAttributeSecurityContextRepository 와 HttpSessionSecurityContextRepository 를    
동시에 사용할 수 있도록 위임된 클래스로서 초기화 시 기본으로 설정된다

## SecurityContextHolderFilter
- SecurityContextRepository 를 사용하여 SecurityContext 를 얻고 이를 SecurityContextHolder 에 설정하는 필터 클래스이다.
- 이 필터클래스는 사용자가 명시적으로 호출되어야 SecurityContext 를 저장할 수 있다 -> SecurityContextPersistenceFilter (deprecated) 와 가장 큰차이점
- 인증이 지속되어야 하는지를 각 인증 메커니즘이 독립적으로 선택할 수 있게하여 더 나은 유연성을 제공, HttpSession 에 필요할 때만 저장함으로 성능을 향상 시킨다.

1. 익명 사용자
   - SecurityContextRepository 를 사용하여 새로운 SecurityContext 객체를 생성하여 SecurityContextHolder 에 저장 후 다음 필터로 전달
   - AnonymousAuthenticationFilter 에서 AnonymousAuthenticationToken 객체를 SecurityContext 에 저장
2. 인증 요청
   - SecurityContextRepository 를 사용하여 새로운 SecurityContext 객체를 생성하여 SecurityContextHolder 에 저장 후 다음 필터로 전달
   - UsernamePasswordAuthenticationFilter 에서 인증 성공 후 SecurityContext 에 UsernamePasswordAuthentication 객체를 SecurityContext 에 저장
   - SecurityContextRepository 를 사용하여 HttpSession 에 SecurityContext 를 저장
3. 인증 후 요청
   - SecurityContextRepository 를 사용하여 HttpSession 에서 SecurityContext 꺼내어 SecurityContextHolder 에서 저장 후 다음 필터로 전달
   - SecurityContext 안에 Authentication 객체가 존재하면 계속 인증을 유지한다
4. 클라이언트 응답 시 공통
   - SecurityContextHolder.clearContext() 로 컨텍스트를 삭제 한다 <span style="color:red">**(스레드 풀의 스레드일 경우 반드시 필요)**</span>