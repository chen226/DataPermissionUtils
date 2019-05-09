package com.chj.common.sys;

import lombok.Data;

/**
 * @author： chenhj
 * @date： 18-5-7 下午2:31
 * @desciptions: 有权限的id和字段，类似与二元组
 */
@Data
public class ColumnInfo {
    private String columnName;

    private String columnValue;
    /**
     * 字段类型
     **/
    private String columnType;
    /**
     * 规则（like in =）
     **/
    private String columnRule;
}
