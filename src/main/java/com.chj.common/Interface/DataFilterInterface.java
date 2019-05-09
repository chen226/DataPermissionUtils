package com.chj.common.Interface;

import com.chj.common.mybatis.DataPermission;

/**
 * @author chenhj
 * 数据权限业务系统实现接口
**/
public interface DataFilterInterface {

    /**
     *
     * @return 返回DataPermission 如果没有数据权限过滤要求，返回null
     *
     */
    DataPermission queryDataPermission();
}
