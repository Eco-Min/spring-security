# 세션관리

## SessionManagementFilter
- 요청이 시작된 이후 사용자가 인증되었는지 감지, 인증된 경우 세션 고호 보호 메커니즘을 활성화 하거나 다웆로그인을 확인하는 등 세션 관련 활동을   
수행하기위해 설정된 세션전략을 호출하는 필터 클래스이다.
- 스프링 시큐리티 6 이상에는 SessionManagementFilter 가 기본적으로 설정되지 않는다.   

![img](/img/sessionManagementFilter.png)

## ConcurrentSessionFilter
- 각 요청에 대해 SessionRegistry 에서 SessionInformation 을 검생하고 세션이 만료로 표시되었는지 확인하고, 만료인 경우 로그아웃 처리 수행
- 각 요청에 대해 SessionRegistry.refreshLastRequest(String) 호출하여 등록된 세션들이 항상 '마지막 업데이트' 날짜 / 시간을 가지도록 한다.

![img](/img/concurrent.png)

## 시퀀스 다이어 그램
- user1, user2 가 동일한 계정으로 들어온다고 가정

![img](/img/sequence.png)

