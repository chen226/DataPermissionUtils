package com.chj.common.mybatis;

import com.chj.common.sys.TableInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenhj
 * @description 数据权限bean
 * @create 2018-05-02 下午7:18
 */
@Data
public class DataPermission implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * key为需要进行权限控制的表名，value表示用户可以查询的记录和对应的字段
     */
    private List<TableInfo> tables;
    /**
     * 是否是管理员，如果是管理员，不进行数据过滤(数据权限的白名单用户)
     */
    private Boolean admin = false;

}
