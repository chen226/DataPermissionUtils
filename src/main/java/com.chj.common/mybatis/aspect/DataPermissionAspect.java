package com.chj.common.mybatis.aspect;

import com.chj.common.Interface.DataFilterInterface;
import com.chj.common.mybatis.DPHelper;
import com.chj.common.mybatis.DataPermission;
import com.chj.common.mybatis.annotation.DataPermissionAop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/****************************************************************************************
 实现AOP的切面主要有以下几个要素：

 使用@Aspect注解将一个java类定义为切面类
 使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
 根据需要在切入点不同位置的切入内容
 使用@Before在切入点开始处切入内容
 使用@After在切入点结尾处切入内容
 使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
 使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
 使用@AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑
 使用@Order(i)注解来标识切面的优先级。i的值越小，优先级越高
 *
 * @author chenhj
 *
 * @version 0.0.1
 *
 * @date 2018/10/19
 *
 */
@Aspect
@Order(1)
@Component
public class DataPermissionAspect {

    @Autowired
    private DataFilterInterface dataFilterInterface;
/**
 * 方法的注解覆盖class注解
 * */
    @Around("@annotation(com.chj.common.mybatis.annotation.DataPermissionAop)")
    public Object dataPermission(ProceedingJoinPoint point) throws Throwable {

        Class<?> className = point.getTarget().getClass();
        boolean enabled = false;
        if (className.isAnnotationPresent(DataPermissionAop.class)) {
            DataPermissionAop ds = className.getAnnotation(DataPermissionAop.class);
            enabled = ds.enabled();
        }
        // 得到访问的方法对象
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getName();
        Class[] argClass = signature.getParameterTypes();
        Method method = className.getMethod(methodName, argClass);
        // 判断是否存在@DataPermissionAop注解
        if (method.isAnnotationPresent(DataPermissionAop.class)) {
            DataPermissionAop annotation = method.getAnnotation(DataPermissionAop.class);
            // 取出注解中所需要做数据权限的表
            enabled = annotation.enabled();
        }
        if(enabled){
            DataPermission dataPermission=dataFilterInterface.queryDataPermission();
            DPHelper.setLocalDataPermissions(dataPermission);
        }
        return point.proceed();
    }
}