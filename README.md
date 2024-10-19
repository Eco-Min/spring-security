# 통합 하기

## @AuthenticationPrincipal
- Spring Security는 Spring MVC 인수에 대한 현재 Authentication.getPrincipal()을 자동으로 해결 할 수 있는 AuthenticationPrincipalArgumentResolver 를 제공한다
- Spring MVC 에서 @AuthenticationPrincipal 을 메서드 인수에 선언하게 되면 Spring Security 와 독립적으로 사용할 수 있다
```java
public void findUser() {
    Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
    CustomUser custom = (CustomUser) authentication == null ? null : authentication.getPrincipal();
}

// 아래 코드로 대체 가능하다.
public Customer findUser(@AuthenticationPrincipal CustomUser customUser) {...} 
```

## @AuthenticationPrincipal(expression="표현식")
- Principal 객체 내부에서 특정 필드나 메서드에 접근하고자 할 때 사용할 수 있으며 사용자 세부 정보가 Principal 내부의 중첩된 객체에 있는 경우 유용하다
- [indexController.java](./src/main/java/com/spring/security/IndexController.java)

## @AuthenticationPrincipal 메타 주석
- @AuthenticationPrincipal 을 자체 주석으로 메타 주석화 하여 Spring Security 에 대한 종속성을 제거할 수도 있다
- [CurrentUser.java](./src/main/java/com/spring/security/CurrentUser.java)
- [CurrentUserName.java](./src/main/java/com/spring/security/CurrentUserName.java)