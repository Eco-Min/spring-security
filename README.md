
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