package com.chj.common.mybatis.interceptor;

import com.chj.common.mybatis.DPHelper;
import com.chj.common.mybatis.DataPermission;
import com.chj.common.mybatis.visitor.SelectVisitorImpl;
import com.chj.common.utils.ReflectUtil;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author chenhj
 * @description 数据权限拦截器（在PageInterceptor之后）
 * @create 2018-12-24 下午3:12
 */
@Intercepts(
        {
                @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class })
        }
)
@Order(1)
@Component
public class DataPermissionInterceptor implements Interceptor {
    private final static Logger logger = LoggerFactory.getLogger(DataPermissionInterceptor.class);
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
        //从当前线程获取需要进行数据权限控制的业务
        DataPermission dataPermission = DPHelper.getLocalDataPermissions();
        //判断有没有进行数据权限控制，是不是最高权限的管理员（这里指的是数据权限的白名单用户）
        if (dataPermission != null && dataPermission.getAdmin() == false && !dataPermission.getTables().isEmpty()) {
            BoundSql boundSql = delegate.getBoundSql();
            String sql = boundSql.getSql();
            if(CCJSqlParserUtil.parse(sql) instanceof Select){
                //获得方法类型
                Select select = (Select) CCJSqlParserUtil.parse(sql);
                select.getSelectBody().accept(new SelectVisitorImpl());
                //修改sql
                ReflectUtil.setFieldValue(boundSql, "sql", select.toString());
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
