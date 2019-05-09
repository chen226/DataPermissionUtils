package com.chj.common.mybatis.aspect;

import com.chj.common.mybatis.DPHelper;
import com.chj.common.mybatis.DataPermission;
import com.chj.common.mybatis.annotation.DataPermissionTable;
import com.chj.common.mybatis.annotation.UnDataPermissionTable;
import com.chj.common.sys.SessionUser;
import com.chj.common.sys.TableInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
@Order(2)
@Component
public class DataPermissionFilterAspect {

    @Around("@annotation(com.chj.common.mybatis.annotation.DataPermissionTable)")
    public Object dataPermissionFilter(ProceedingJoinPoint point) throws Throwable {
        Class<?> className = point.getTarget().getClass();
        String[] tableNames = {};
        if (className.isAnnotationPresent(DataPermissionTable.class)) {
            DataPermissionTable ds = className.getAnnotation(DataPermissionTable.class);
            tableNames = ds.tables();
        }else {
            // 得到访问的方法对象
            MethodSignature signature = (MethodSignature) point.getSignature();
            String methodName = signature.getName();
            Class[] argClass = signature.getParameterTypes();
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DataPermissionAop注解
            if (method.isAnnotationPresent(DataPermissionTable.class)) {
                DataPermissionTable annotation = method.getAnnotation(DataPermissionTable.class);
                // 取出注解中所需要做数据权限的表
                tableNames = annotation.tables();
            }
        }
        SessionUser sessionUser=null;
        final String[] tableNamex=tableNames;
        if(sessionUser!=null){
            DataPermission dataPermission1 = new DataPermission();
            List<TableInfo> tableInfos=new ArrayList<>();

            DataPermission dataPermission=sessionUser.getDataPermission();
            DPHelper.setLocalDataPermissions(dataPermission);
            dataPermission.getTables().forEach(dataPermission2->{
                if(Arrays.asList(tableNamex).contains(dataPermission2.getTableName())){
                    tableInfos.add(dataPermission2);
                }
            });
            dataPermission1.setTables(tableInfos);
            DPHelper.start(tmp->dataPermission1,tableNames);
        }
        return point.proceed();
    }
}