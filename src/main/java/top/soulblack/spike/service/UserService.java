package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.soulblack.spike.entity.User;
import top.soulblack.spike.mapper.UserMapper;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/24 13:26
 * @Version 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getById(int id) {
        return userMapper.getById(id);
    }

    @Transactional
    public boolean tx() {
        User u1 = new User();
        u1.setId(2);
        u1.setName("admin");
        userMapper.insert(u1);
        User u2 = new User();
        u2.setId(1);
        u2.setName("admin");
        userMapper.insert(u2);
        return true;
    }
}
