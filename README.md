# 세션 관리

## 동시 세션 제어
- 동시 세션제어는 사용자가 동시에 여러 세션을 생헝하는 것을 관리하는 전략이다.
- 이 전략은 사용자의 인증 후에 활상화된 세션의 수가 설정된 maximumSessions 값과 비교하여 제어 여부를 결정한다.

## 동시세션 제어 2가지 유형
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
