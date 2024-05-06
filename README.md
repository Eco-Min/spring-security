# 인증 프로세스

## RequestCache
- 인증 절차 문제로 리다이렉트 된 후에 이전에 모든 정보를 담고 있는 ```SavedRequest``` 객체를 쿠키    
혹은 세션에 저장하고 필요시 다시 가져와 실행하는 캐시 매커니즘

## SavedRequest
- 로그인과 같은 인증 절차 후 사용자를 인증 이전의 페이지로 안내하며 이전 요청과 관련된 여러 정보를 저장한다.

## RequestCacheAwareFilter
- 이전에 저장했떤 웹 요청(SavedRequest) 을 다시 불러오는 역활을 한다.
- SavedRequest 가 현재 Request 와 일치 하면 이요청을 필터체인의 doFilter 에 전당하고 SavedRequest 가 없으면 기존 Request 를 진행한다.