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