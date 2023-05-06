package com.test.service.impl;

import com.test.entity.User;
import com.test.mapper.UserMapper;
import com.test.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;

    public UserServiceImpl(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<User> test() {
        return mapper.list();
    }
}
