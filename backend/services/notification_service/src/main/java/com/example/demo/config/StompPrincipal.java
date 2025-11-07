package com.example.demo.config;

import java.security.Principal;

/** Simple Principal implementation used for WebSocket sessions. */
public class StompPrincipal implements Principal {

    private final String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}