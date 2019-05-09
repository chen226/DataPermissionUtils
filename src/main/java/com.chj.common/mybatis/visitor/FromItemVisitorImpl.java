package com.chj.common.mybatis.visitor;

import com.chj.common.mybatis.DPHelper;
import com.chj.common.sys.ColumnInfo;
import com.chj.common.sys.TableInfo;
import com.chj.common.utils.SqlSpliceUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * @author： chenhj
 * @date： 18-5-7 下午2:49
 * @desciptions:
 */
public class FromItemVisitorImpl implements FromItemVisitor {
    private Logger logger = LoggerFactory.getLogger(FromItemVisitorImpl.class);

    private SubSelect subSelect;

    @Override
    public void visit(Table table) {
        String tableName = table.getName();
        //关键点：解析到需要进行数据权限控制的表时进行拼装，可以从当前线程获取表数据
        //需要进行的数据权限控制的表数据
        List<TableInfo> tables = DPHelper.getLocalDataPermissions().getTables();
        tables.forEach(tableInfo -> {
            if (tableInfo.getTableName().equalsIgnoreCase(tableName)) {
                List<ColumnInfo> columnInfoList = tableInfo.getColumnInfoList();

                SubSelect subSelect = new SubSelect();
                String subSql = SqlSpliceUtils.spliceColumnInfos(tableName, columnInfoList);
                try {
                    subSelect.setSelectBody(((Select) (CCJSqlParserUtil.parse(subSql))).getSelectBody());
                } catch (JSQLParserException e) {
                    logger.error("数据权限sql解析异常");
                }
                //TODO:采用随机别名不能避免重名
                subSelect.setAlias(table.getAlias() != null ? table.getAlias() : new Alias("DP" + UUID.randomUUID()
                        .toString().replace("-", "")));
                this.subSelect = subSelect;
            }
        });
    }

    public SubSelect getSubSelect() {
        return this.subSelect;
    }

    // FROM 子查询
    @Override
    public void visit(SubSelect subSelect) {
        // 如果是子查询的话返回到select接口实现类
        subSelect.getSelectBody().accept(new SelectVisitorImpl());
    }

    // FROM subjoin
    @Override
    public void visit(SubJoin subjoin) {
        subjoin.getLeft().accept(new FromItemVisitorImpl());
        subjoin.getJoin().getRightItem().accept(new FromItemVisitorImpl());
    }

    // FROM 横向子查询
    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
        lateralSubSelect.getSubSelect().getSelectBody()
                .accept(new SelectVisitorImpl());
    }

    // FROM value列表
    @Override
    public void visit(ValuesList valuesList) {
    }

    // FROM tableFunction
    @Override
    public void visit(TableFunction tableFunction) {
    }
}