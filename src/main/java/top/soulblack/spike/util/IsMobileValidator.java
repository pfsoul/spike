package top.soulblack.spike.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/25 15:43
 * @Version 1.0
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 判断是否合法
        if (required) {
            return ValidatorUtil.isMobile(value);
        } else {
            if (value.isEmpty()) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
