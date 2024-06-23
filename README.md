# 인가 프로세스
- 메소드 수준 권한 부여를 활상화 하기위해 @EnableMethodSecurity 어노테이션을 사용한다.

## @EnableMethodSecurity
- jsr250Enabled(): JSR-250 관련 어노테이션들(@RolesAllowed, @PermitAll, @DenyAll) 을 활상화 한다.
- prePostEnabled(): @PreAUTHorize, @PostAuthorize, @PreFilter, @PostFilter 어노테이션을 활성화 한다. (기본 true)
- securedEnabeld(): @Secured 어노테이션을 활성화 한다. (기본 false)

## @PreAuthorize
- 메소드가 실행되기 전에 특정한 보안 조건이 충족 되는지 확인하느데 사용되며 보통 서비스 또는 컨트롤러 레이어의 메소드에 적용 되어
해당 메소드가 호출되기 전에 사용자의 인증 정보와 권한을 검사한다.
```java
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public void adminOnlyMethod() {
/* 관리자 역할을 가진 사용자만 실행할 수 있는 메소드 */
}
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
public void adminOrUserMethod() {
/* 관리자 또는 일반 사용자 역할을 가진 사용자가 실행할 수 있는 메소드 */
}
@PreAuthorize("isAuthenticated()")
public void authenticatedUserOnlyMethod() {
/* 인증된 사용자만 실행할 수 있는 메소드 */
}
@PreAuthorize("#id == authentication.name")
public void userSpecificMethod(String id) {
/* 인증된 사용자가 자신의 ID에 해당하는 작업을 수행할 수 있는 메소드 */
}
```

## @PostAuthorize
- 메소드가 실행된 후에 보안 검사를 수행하는 데 사용된다.
- @PreAuthorize와 달리 메소드가 실행 후 결과에 대한 보안 조건을 검사하여 특정 조건을 만족하는 경우에만 사용자가 결과를 받을 수 있도록 한다.
```java
@PostAuthorize("returnObject.owner == authentication.name")
public BankAccount getAccount(Long id) {
// 계정를 반환하지만 계정의 소유자만 결과를 볼 수 있음
    return new BankAccount();
}
@PostAuthorize("hasAuthority('ROLE_ADMIN') and returnObject.isSecure")
public BankAccount getSecureAndAdminAccount(Long id) {
// 계정을 반환하지만 계정이 기밀이고 사용자가 관리자일 경우에만 결과를 볼 수 있음
    return new BankAccount();
}
@PostAuthorize("returnObject != null and (returnObject.status == 'APPROVED' or hasAuthority('ROLE_ADMIN'))")
public BankAccount updateRequestStatus() {
    return new BankAccount();
}
```

## @PreFilter
- 메소드가 실행되기전에 메소드에 전달된 컬렉션 타입의 파라미터에 대한 필터링을 수행하느데 사용된다.
- 객체들을 특정 기준에 따라 필터링 하고 그중 보안 조건에 만족하는 객체들에 대해서만 메소드가 처리하도록 사용된다.

```java
@PreFilter("filterObject.owner == authentication.name")
public Collection<BankAccount> updateAccounts(BankAccount[] data){
    return data;
}
@PreFilter("filterObject.owner == authentication.name")
public Collection<BankAccount> updateAccounts(Collection<BankAccount> data){
    return data;
}
@PreFilter("filterObject.value.owner == authentication.name")
public Collection<BankAccount> updateAccounts(Map<String, BankAccount> data){
    return data;
}
@PreFilter("filterObject.owner == authentication.name")
public Collection<BankAccount> updateAccounts(Stream<BankAccount> data) {
    return data;
}
```

## @PostFilter
- 컬렉션 타입의 결과에 대한 필터링을 수행하는데 사용된다.
- 켈력션을 반환할 때 반환되는 각 객체가 특정 보안 조건을 충족하는지 확인하고 저건을 만족하지 않으면 객체들은 결과에서 제거한다.

```java
@PostFilter("filterObject.owner == authentication.name")
public List<BankAccount> readAccounts1(){
    return dataService.readList();
} 
@PostFilter("filterObject.value.owner == authentication.name")
public Map<String, BankAccount> readAccounts2(){
    return dataService.readMap();
}
```

## 그외 기능들

### @Secured
- 지정된 권한(역할)을 가진 사용자만 해당 메소드를 호출할 수 있으며 더 풍부한 형식을 지원하는 @PreAuthorize 사용을 권장한다
- 스프링 시큐리티 설정에서 @EnableMethodSecurity(securedEnabled = true) 설정을 활성화해야 한다
```java
@Secured("ROLE_USER")
public void performUserOperation() {
// ROLE_USER 권한을 가진 사용자만 이 메소드를 실행할 수 있습니다.
}
```

### JSR-250
- @RolesAllowed, @PermitAll 및 @DenyAll 어노테이션 보안 기능이 활성화 된다
- 스프링 시큐리티 설정에서 @EnableMethodSecurity(jsr250Enabled = true) 설정을 활성화해야 한다
```java
@RolesAllowed("USER")
public void editDocument() {
// 'ROLE_USER' 권한을 가진 사용자만 문서를 편집할 수 있습니다.
}
@PermitAll
public void viewDocument() {
// 모든 사용자가 문서를 볼 수 있습니다.
}
@DenyAll
public void hiddenMethod() {
// 어떠한 사용자에게도 접근이 허용되지 않습니다.
}
``` 

### 메타 주석 사용
- 특정 사용을 위해 편리성과 가독성을 높일 수 있는 매타 주석을 지원한다.
```java
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN')")
public @interface IsAdmin {}

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("returnObject.owner == authentication.name")
public @interface RequireOwnership {}
```
- @PreAuthorize("hasRole('ADMIN')")를 다음과 같이 @IsAdmin
```java
@IsAdmin
public BankAccount readAccount(Long id) {
// ADMIN 권한을 가진 사용자에게 메소드 호출이 승인 될 수 있다
}
@RequireOwnership
public Account readAccount(Long id) {
// 'Account'가 로그인한 사용자에게 속할 경우에만 반환된다
}
```
### 특정 주석 활성화
- 특정 주석을 활성화하려면 @EnableGlobalMethodSecurity 어노테이션을 사용한다.
- Method Security 의 사전 구성을 비활성화한 다음 @PostAuthorize 를 활성화한다
```java
@EnableMethodSecurity(prePostEnabled = false)
class MethodSecurityConfig {
    @Bean 
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    Advisor postAuthorize() {
        return AuthorizationManagerAfterMethodInterceptor.postAuthorize();
    }
}
```

### 커스텀 빈을 사용하여 표현식 구현하기

- 사용자 정의 빈을 생성하고 새로운 표현식으로 사용할 메서드를 정의하고 권한 검사 로직을 구현한다.

```java
@GetMapping("/delete")
@PreAuthorize("@authorizer.isUser(#root)") //,빈,이름을,참조하고,접근,제어,로직을,수행한다
public void delete() {
    // 사용자가 삭제 권한이 있는지 확인
}

@Component("authorizer")
class MyAuthorizer{
    public boolean isUser(MethodSecurityExpressionOperations root) {
        boolean decision = root.hasAuthority("ROLE_USER"); // 인증된 사용자가 ROLE_USER 권한을 가지고 있는지를 검사
        return decision;
    }
}
```

### 클래스 레벨 권한 부여
- 모든 메소드는 클래스 수준의 권한 처리 동작을 상속한다
- 메서드에 어노테이션을 선언한 메소드는 클래스 수준의 어노테이션을 덮어쓰게 된다
- 인터페이스에도 동일한 규칙이 적용되지만 클래스가 두 개의 다른 인터페이스로부터 동일한 메서드의 어노테이션을 상속받는 경우에는    
  시작할 때 실패한다. 그래서 구체적인 메소드에 어노테이션을 추가함으로써 모호성을 해결할 수 있다
```java
@Controller
@PreAuthorize("hasAuthority('ROLE_USER')")
public class MyController {
    @GetMapping("/endpoint")
    public String endpoint() { ...}
}
@Controller
@PreAuthorize("hasAuthority('ROLE_USER')")
public class MyController {
    @GetMapping("/endpoint")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 이 설정이 우선적으로 동작한다
    public String endpoint() { ...}
}
```
