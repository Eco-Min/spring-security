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

### 단일 포인트 컷 (AspectJExpressionPointcut)
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

### 다중 포인트 컷 (ComposablePointcut)
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

## AOP 메서드 보안 구현하기
- MethodInterceptor, Pointcut, Advisor, AuthorizationManager 등을 커스텀하게 생성하여 AOP 메서드 보안을 구현 할 수 있다

### Aop 요소
1. Advisor
   - Aop Advice 와 Advice 적용 가능성을 결정하는 포이트컷을 가진 기본 인터페이스.
2. MethodInterceptor(Advice)
   - 대상 각체를 호출하기 전과 후에 추가작업을 수행하기 위한 인터페이스로 수행 이후 실제 대상 객체의 조인 포이트를 호출(메소드 호출)    
   을 위해 Joinpoint.proceed() 메소드를 호출
3. PointCut
   - Aop 에서 Advice 가 적용될 메소드나 클래스를 정의하는 것으로서 어드바이스가 실행되어야 하는 ```적용지점``` ```조건```을 지정
   - ClassFilter와 MethodMatcher를 사용해서 어떤 클래새 및 어떤 매서드에 Advice를 저용할 것인지 결정한다.

![AOP](/img/Aop.png)

### Aop 초기화
1. AnnotationAwareAspectAutoProxyCreator 현재 어플리 케이션 컨텍스트 내의 모든 AspectJ 어노테이션과 스프링 어드바이저들을 처리한다.
   - SpringSecurityAdvisor (MethodInterceptor/Pointcut)
   - CustomAdvisor (MethodInterceptor/Pointcut)

### Aop 적용 순서
1. CustomMethodInterceptor 를 생성하고 메소드 보안 검사를 수행할 AuthorizationManager 를 CustomMethodInterceptor 에 전달한다
2. CustomPointcut 을 생성하고 프록시 대상 클래스와 대상 메서드를 결정할 수 있도록 포인트컷 표현식을 정의한다
3. DefaultPointcutAdvisor 을 생성하고 CustomMethodInterceptor 와 CustomPointcut 을 DefaultPointcutAdvisor 에 전달한다
4. 서비스를 호출하면 Pointcut 으로부터 대상 클래스와 대상 메서드에 등록된 MethodInterceptor 를 탐색하고 결정되면 이를 호출하여 AOP 를 수행한다

### 예제.
[CustomMethodInterceptor]()
[MethodSecurityAopConfig]()