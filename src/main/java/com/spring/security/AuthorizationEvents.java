package com.spring.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationEvent;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationEvents {

    @EventListener // AuthorizationEvent의 하위 클래스 이벤트 발행시 항상 호출
    public void onAuthorization(AuthorizationEvent event){
        System.out.println("event = " + event.getAuthentication().get().getAuthorities());
    }

    @EventListener // AuthorizationFilter > authorizationEventPublisher.publishAuthorizationEvent(new AuthorizationDeniedEvent())
    public void onAuthorization(AuthorizationDeniedEvent failure){
        System.out.println("event = " + failure.getAuthentication().get().getAuthorities());
    }

    @EventListener
    public void onAuthorization(AuthorizationGrantedEvent success){
        System.out.println("event = " + success.getAuthentication().get().getAuthorities());
    }
}