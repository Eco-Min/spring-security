
## 스프링 시큐리티 필터 설정
- 스프링 시큐리티는 HttpSecurity 설정을 통해 어플리케이션의 보안 요구사항에 맞게 필터 체인을 추가 할 수 있도록 제공한다
- 필터 추가는 addFilterBefore, addFilterAfter, addFilter, addFilterAt 메소드를 사용하여 필터의 순서와 위치를 제어할 수 있다.

### 필터 설정 추가
- addFilterBefore
  - 지정된 필터를 필터 체인의 특정 필터 이전에 추가하며 주로 특정 처리가 다른 필터보다 먼저 실행되어야 할 때 사용된다
  ```java
  http.addFilterBefore(new CustomFilter(), UsernamePasswordAuthenticationFilter.class);
  ```
- addFilterAfter
  - 지정된 필터를 필터 체인의 특정 필터 이후에 추가하며 특정 작업이 다른 필터의 처리를 따라야 할 때 유용하다
  ```java
    http.addFilterAfter(new CustomFilter(), UsernamePasswordAuthenticationFilter.class);
    ```
- addFilter
  - 시큐리티 필터 체인에 새로운 필터를 추가하며 필터의 위치를 지정하지 않고 필터의 유형에 따라 자동으로 적절한 위치에 필터를 추가한다
  - 추가하는 필터가 스프링 시큐리티의 필터를 상속받을 경우에 해당하며 그렇지 않을 경우 예외가 발생한다
  ```java 
  http.addFilter(new CustomFilter());
  ```
- addFilterAt
  - 지정된 필터를 필터 체인의 특정 필터 위치에 추가하며 특정 필터를 대체하지는 않는다
  ```java
  http.addFilterAfter(new CustomFilter(), UsernamePasswordAuthenticationFilter.class)
  ```
  
## SecurityContextRepository 설정
- 인증 필터에서 인증에 성공한 이후 인증 객체를 세션에 저장할 수 있도록 HttpSessionSecurityContextRepository 를 지정한다.
- AbstractAuthenticationProcessingFilter 상속하면 RequestAttributeSessionRepository 가 기본으로 설정 된다.
- 즉, 세션의 영속성을 가지기 위해선 SecurityContextRepository 를 설정해야 한다.
- 아래는 세션 기준으로 는 경우 이다.
- [SecurityConfig.java / RestAuthenticationFilter](./src/main/java/com/spring/security/secure/config/SecurityConfig.java)
- [RestAuthenticationFilter.java](./src/main/java/com/spring/security/filters/RestAuthenticationFilter.java)
- 예시를 위해 세션에 저장할뿐 실제로는 jwt 를 사용하는 방법이 좋다. 
  - 세션을 사용하면 서버의 부하가 증가하고 서버의 확장성이 떨어지기 때문이다.
  - 동시성 처리를 위해 세션 클러스터링을 사용해야 하며, 이는 서버의 부하를 더욱 증가시킨다.