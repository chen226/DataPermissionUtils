package com.chj.common.registrar;

import com.chj.common.mybatis.DataPermission;
import com.chj.common.mybatis.annotation.DataPermissionAop;
import org.springframework.context.annotation.Bean;

/**
 * @author chenhj
 */
public class DataPermissionRegistrar {


    @SuppressWarnings("rawtypes")
    @Bean
    public DataPermission cacheManager() {
        DataPermission dataPermission = new DataPermission();
        return dataPermission;
    }
}