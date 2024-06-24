# 인가 프로세스

## 계층적 권한
- 기본적으로 스프링 시큐리티에서 권한과 역할은 계층적이거나 상하 관계로 구분하지 않는다. 그래서 인증 주체가 다양한 역할과 권한을 부여 받아야 한다
-  RoleHirerachy 는 역할 간의 계층 구조를 정의하고 관리하는 데 사용되며 보다 간편하게 역할 간의 계층 구조를 설정하고 이를 기반으로    
사용자에 대한 액세스 규칙을 정의할 수 있다
```html
<property6name="hierarchy">
<value>
ROLE_A > ROLE_B
ROLE_B > ROLE_C
ROLE_C > ROLE_D
</value>
</property>
```
- ROLE_A 를 가진 모든 사용자는 ROLE_B, ROLE_C 및 ROLE_D 도 가지게 된다
- ROLE_B 를 가진 모든 사용자는 ROLE_C 및 ROLE_D도 가지게 된다
- ROLE_C 를 가진 모든 사용자는 ROLE_D도 가지게 된다
- 계층적 역할을 사용하면 액세스 규칙이 크게 줄어들 뿐만 아니라 더 간결하고 우아한 형태로 규칙을 표현할 수 있다

```java
@Bean
static RoleHierarchy roleHierarchy(){
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy( //java 21 부터 text block 사용 가능
            "ROLE_A > ROLE_B\n" +
            "ROLE_B > ROLE_C\n" +
            "ROLE_C > ROLE_D");
    return roleHierarchy;
}
```
### setHirerachy
- 역할 계층을 설정하고 각 역할에 대해 해당 역할의 하위 계층에 속하는 모든 역할 집합을 미리 정해 놓는다
- 역할 계층 : ROLE_A > ROLE_B > ROLE_C

### getReachableGrantedAuthorities
- 모든 도달 가능한 권한의 배열을 반환한다
- 도달 가능한 권한은 직접 할당된 권한에 더해 역할 계층에서 이들로부터 도달 가능한 모든 권한을 의미한다
  - 직접 할당된 권한 : ROLE_B
  - 도달 가능한 권한 : ROLE_B, ROLE_C
