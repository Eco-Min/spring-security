# 인가 아키텍쳐

## 포인트컷 메서드 보안 구현하기
- 메서드 보안은 AOP 를 기반으로 구축되었기 때문에 어노테이션이 아닌 패턴 형태로 권한 규칙을 선언할 수 있으며 이는 요청 수준의 인가와 유사한 방식이다
- 자체 어드바이저(Advisor)를 발행하거나 포인트컷(PointCut)을 사용하여 AOP 표현식을 애플리케이션의 인가 규칙에 맞게 매칭할 수 있으며 이를 통해    
어노테이션을 사용하지 않고도 메소드 수준에서 보안 정책을 구현할 수 있다

아래 코드를 PointCut 으로 구현 할 예정.
```java
@Service
public class Myservice{
    public void user1() { //MethodInterceptor 에서 호출
        System.out.println("user");
    }
    public void display() { // MethodInterceptor 에서 호출
        System.out.println("display");
    }   
}
```

## 단일 포인트 컷 (AspectJExpressionPointcut)
```java
@Bean
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public Advisor protectServicePointcut() {
    AspectJExpressionPointcut pattern = new AspectJExpressionPointcut();
    pattern.setExpression("execution(* io.security.MyService.user(..))");
    manager = AuthorityAuthorizationManager.hasRole("USER");
    return new AuthorizationManagerBeforeMethodInterceptor(pattern, manager);
}
```

## 다중 포인트 컷 (ComposablePointcut)
```java
@Bean
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public Advisor protectServicePointcut() {
    return new AuthorizationManagerBeforeMethodInterceptor(createCompositePointcut(), hasRole("USER"));
}
public Pointcut createCompositePointcut() {
    AspectJExpressionPointcut pointcut1 = new AspectJExpressionPointcut();
    pointcut1.setExpression("execution(* com.spring.security.MyService.user(..))");
    AspectJExpressionPointcut pointcut2 = new AspectJExpressionPointcut();
    pointcut2.setExpression("execution(* com.spring.security.MyService.display(..))");
    // 두 포인트컷을 조합
    ComposablePointcut compositePointcut = new ComposablePointcut(pointcut1);
    compositePointcut.union(pointcut2);
    return compositePointcut;
}
```