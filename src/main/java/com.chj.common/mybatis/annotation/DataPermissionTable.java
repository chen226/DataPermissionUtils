package com.chj.common.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 加在其他方法上可以使用AOP做切面处理
 * 只针对某些表进行数据权限处理
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataPermissionTable {
    String[] tables() default {};
}
