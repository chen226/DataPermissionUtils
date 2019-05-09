package com.chj.common.mybatis.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 开启数据权限
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Documented
public @interface DataPermissionAop {
    boolean enabled() default true;
}
