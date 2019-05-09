package com.chj.common.utils;

import com.chj.common.sys.ColumnInfo;

import java.util.List;

/**
 * @author chenhj
 * @description sql拼接工具类
 * @create 2018-05-07 下午2:53
 */
public class SqlSpliceUtils {
    public static String spliceColumnInfos(String tableName, List<ColumnInfo> columnInfoList) {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ");

        for (ColumnInfo columnInfo:columnInfoList) {
            stringBuilder.append(columnInfo.getColumnName()).append("=");
            if(DataTypeUtils.checkType(columnInfo.getColumnType())){
                stringBuilder.append("'");
            }
            stringBuilder.append(columnInfo.getColumnValue());
            if(DataTypeUtils.checkType(columnInfo.getColumnType())){
                stringBuilder.append("'");
            }
            stringBuilder.append(" and ");
        }
        stringBuilder.delete(stringBuilder.lastIndexOf("and"),stringBuilder.lastIndexOf("and")+3);
        return stringBuilder.toString();
    }
}
