package com.chj.common.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TableInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tableName;
    List<ColumnInfo> columnInfoList;
}
