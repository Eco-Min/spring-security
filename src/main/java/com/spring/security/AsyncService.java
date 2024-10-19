package com.spring.security;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    // Was 객체에(SecurityApplication) @EnableAsync 어노테이션을 추가하면 비동기로 실행됨 -> 안하면 비동기로 실행 되지 않고 같은 쓰레드를 사용 하게 됩니다.
    // @EnableAsync 사용하면 비동기로 실행이 되어 exception 을 띄우게 됩니다. -> SecurityContextHolder.getContextHolderStrategy().getContext(); 를 사용하면 exception 이 발생합니다.
    // SecurityConfig 에서 SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL); 를 추가하면 비동기로 실행이 되어도 exception 이 발생하지 않습니다.
    @Async
    public void asyncMethod(){
        SecurityContext securityContext = SecurityContextHolder.getContextHolderStrategy().getContext();
        System.out.println("securityContextAsync = " + securityContext);
        System.out.println("Child Thread: " + Thread.currentThread().getName());
        Authentication authentication = securityContext.getAuthentication();
        authentication.setAuthenticated(false);
        System.out.println("securityContextAsync2 = " + securityContext);
    }
}