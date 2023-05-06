package com.test.controller;

import com.basic.domain.ResultVo;
import com.easy.annotations.RestMapping;
import com.test.entity.User;
import com.test.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * TODO
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
@RestMapping("test")
public class TestController {

    private final UserService service;

    public TestController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResultVo<List<User>> test() {
        return ResultVo.ok(service.test());
    }

    @GetMapping("get")
    public ResultVo<String> get() {
        return ResultVo.ok("123", "000000");
    }
}
