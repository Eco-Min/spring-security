# 인증 아키텍쳐

## 기본인증 - httpBasic()
- RFC 7235 표준이며 인증 프로토콜은 HTTP 인증 헤더에 기술되어 있다. -> header 에 인코딩된 값이 포한되어 날라간다.
- base-64 인코딩된 값은 디코딩이 가능하기 때문에 인증정보가 노출된다.
- HTTP basic 인증은 반드시 HTTPS 와 같이 TLS 기술과 함께 사용해야 한다.

### httpBasic() API
- HttpBasicConfigurer 설정 클래슬르 통해 여러 API 들을 설정 할 수 있다.
- 내부적으로 BasicAuthenticationFilter 가 생성되어 기본 인증 방식의 인증 처리를 담당하게 된다.

## 기본인증 필터 - BasicAuthenticationFilter
- 기본인증 서비스를 제공하는 데 사용한다. -> httpBasic() 과 연관
- BasicAuthenticationConverter 를 사용해서 욫어 헤더에 기술된 인증정보의 유효성을 체크하며 Base64 인코딩된 username 과 password 를 추출한다.
- 인증 이후 세션을 사용하는 경우와 사용하지 않응 경웨 따라 처리되는 흐름에 차이가 있다.   
세션을 사용하는 경우 매 요청마다 인증 과정을 거치치 않으나 세션을 사용하지 않는 경우 매 요청마다 인증과정을 거쳐야 한다.

![img.png](img/img.png)
