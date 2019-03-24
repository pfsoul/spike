package top.soulblack.spike.entity;

import org.springframework.stereotype.Component;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/24 13:14
 * @Version 1.0
 */

@Component
public class User {
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
