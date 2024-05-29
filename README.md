# 인증 아키텍쳐

## UserDetailsService
- UserDetailsService 의 중요 기능은 사용자와 관련된 상세 데이터를 로드하는 것이며 사용자의 신원 권한 자격증명 등과 같은 정보를 포함할 수 있다.
- 주로 AuthenticationProvider 클래스를 사용하며 사요아자가 시스템에 존자하는지 여부와 사용자 데이터를 검색하고 인증 과정을 수행한다
  (RDBMS, NOSQL, MEMORY...)
- UserDetails 객체로 반환 해야 합니다.

## UserDetails
- 사용자의 기본 정보를 저장하는 인터페이스, Spring Security 에서 사용 하는 사용자 타입이다.
- 저장된 사용자 정보는 추후에 인증 절차에서 사용되기위해 Authentication 객체에 포함되며 구현체로는 User 클래스가 제공된다.