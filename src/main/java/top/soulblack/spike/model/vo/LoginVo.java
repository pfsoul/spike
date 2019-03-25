package top.soulblack.spike.model.vo;

import org.hibernate.validator.constraints.Length;
import top.soulblack.spike.util.IsMobile;

import javax.validation.constraints.NotNull;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/25 11:44
 * @Version 1.0
 */
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
