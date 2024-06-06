# 악용 보호

## CORS (Cross Origin Resource Sharing, 교차 출처 리소스 공유)

- 웹에서는 보안을 위해 기본적으로 한 웹 페이지(출처 A)에서 다른 웹 페이지(출처 B)의 데이터를 직접 불러오는 것을 제한하는데 이를    
'동일 출처 정책(Same-Origin Policy)' 이라고 한다 
- 만약 다른 출처의 리소스를 안전하게 사용하고자 할 경우 CORS 가 등장하며 CORS는 특별한 HTTP 헤더를 통해 한 웹 페이지가 다른 출처의 리소스에    
접근할 수 있도록 '허가'를 구하는 방법이라 할 수 있다.    
즉, 웹 애플리케이션이 다른 출처의 데이터를 사용하고자 할 때, 브라우저가 그 요청을 대신해서 해당 데이터를 사용해도 되는지 다른 출처에게 물어보는 것이라 할 수 있다
-  출처를 비교하는 로직은 서버에 구현된 스펙이 아닌 브라우저에 구현된 스펙 기준으로 처리되며 브라우저는 클라이언트의 요청 헤더와 서버의 응답헤더를 비교해서    
최종 응답을 결정한다
-  두개의 출처를 비교하는 방법은 URL의 구성요소 중 Protocol, Host, Port 이 세가`지가 동일한지 확인하면 되고 나머지는 틀려도 상관없다
- XMLHttpRequest와 Fetch API는 동일 출처 정책을 따르기 때문에 이 API를 사용하는 웹 애플리케이션은 자신의 출처와 동일한 리소스만 불러올 수 있으며,    
다른 출처의 리소스를 불러오려면 그 출처에서 올바른 CORS 헤더를 포함한 응답을 반환해야 한다.

## CORS 의 종류
### 1. Simple Request
- 예비 요청(preFlight) 과정 없이 자동으로 CORS 가 작동하여 서버에 본 요청을 한 후, 서버가 응답의 해더에 Access-Control-Allow-Origin 과 같은   
값을 전송하면 브라우저가 서로 비교후 CORS 정책 위반여부를 검사하는 방식
- 제약 사항
  - GET, POST, HEAD 중의 한가지 Method를 사용해야 한다
  - 헤더는 Accept, Accept-Language, Content-Language, Content-Type, DPR, Downlink, Save-Data, Viewport-Width Width 만 가능   
   Custom Header 는 허용되지 않는다
  - Content-type 은 application/x-www-form-urlencoded, multipart/form-data, text/plain 만 가능하다

### 2. PreFlight Request
- 브라우저는 요청을 한번에 보내지 안혹, 예비 요청과 본 요청을 나누어 서버에 전달하느데 브라우저가 예비요청을 보내는 것을 PreFlight 라고하며   
예비 요청의 메소드에는 OPTIONS 가 사용된다.
- 예비요청 역활은 본 요청을 보내기 전에 브라우저 스스로 안전한 요청인지 확인하는 것으로 요청 사양이 SimpleRequest 에 해당하지 않을 경우   
브라우저가 PreFlight Request 를 실행한다


## CORS 해결 - 서버에서 Access-Control-Allow-* 세팅
- Access-Control-Allow-Origin - 헤더에 작성된 출처만 브라우저가 리소스를 접근할 수 있도록 허용한다
  - *, https://security.io
- Access-Control-Allow-Methods - preflight request 에 대한 응답으로 실제 요청 중에 사용할 수 있는 메서드를 나타낸다
  - 기본값은 GET,POST,HEAD,OPTIONS, *
- Access-Control-Allow-Headers - preflight request 에 대한 응답으로 실제 요청 중에 사용할 수 있는 헤더 필드 이름을 나타낸다
  - 기본값은 Origin,Accept,X-Requested-With,Content-Type, Access-Control-Request-Method,Access-Control-Request-Headers, Custom Header, *
- Access-Control-Allow-Credentials - 실제 요청에 쿠기나 인증 등의 사용자 자격 증명이 포함될 수 있음을 나타낸다. Client의 credentials:include 옵션일 경우 true 는 필수
- Access-Control-Max-Age - preflight 요청 결과를 캐시 할 수 있는 시간을 나타내는 것으로 해당 시간동안은 preflight 요청을 다시 하지 않게 된다

## cors() & CorsFilter
- CORS 의 사전 요청(pre-flight request)에는 쿠키 (JSESSIONID)가 포함되어 있지 않기 때문에 Spring Security 이전에 처리되어야 한다
- 사전 요청에 쿠키가 없고 Spring Security 가 가장 먼저 처리되면 요청은 사용자가 인증되지 않았다고 판단하고 거부할 수 있다
- CORS 가 먼저 처리되도록 하기 위해서 CorsFilter 를 사용할 수 있으며 CorsFilter 에 CorsConfigurationSource 를 제공함으로써 Spring Security 와 통합 할 수 있다