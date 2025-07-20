package org.dromara.demo.controller;

import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface UserApiService {

    public record Nyew(String name, int age) {}

    @GetExchange("/users")
    List<Nyew> getUsers();

}
