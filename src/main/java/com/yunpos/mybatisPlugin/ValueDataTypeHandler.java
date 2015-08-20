/*
 * *
 *  * 功能描述：
 *  * <p>
 *  * 版权所有：小牛信息科技有限公司
 *  * <p>
 *  * 未经本公司许可，不得以任何方式复制或使用本程序任何部分
 *  *
 *  * @author Bino Zhong 新增日期：${date}
 *  * @author Bino Zhong 修改日期：${date}
 *  *
 *
 */

/*
 * *
 *  * 功能描述：
 *  * <p>
 *  * 版权所有：小牛信息科技有限公司
 *  * <p>
 *  * 未经本公司许可，不得以任何方式复制或使用本程序任何部分
 *  *
 *  * @author Bino Zhong 新增日期：${date}
 *  * @author Bino Zhong 修改日期：${date}
 *  *
 *
 */

package com.yunpos.mybatisPlugin;

import com.yunpos.rewriter.filter.Filter;
import com.yunpos.rewriter.value.Value;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 功能描述：
 * <p>
 * 版权所有：小牛信息科技有限公司
 * <p>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分
 *
 * @author bino 新增日期：2015/8/14
 * @author bino 修改日期：2015/8/14
 */
public class ValueDataTypeHandler extends BaseTypeHandler<Value.DataType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Value.DataType dataType, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, dataType.getCode());
    }

    @Override
    public Value.DataType getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        Integer code = resultSet.getInt(columnName);
        return code == null?null: Value.DataType.fromCode(code);
    }

    @Override
    public Value.DataType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Integer code = rs.getInt(columnIndex);
        return code == null?null:Value.DataType.fromCode(code);
    }

    @Override
    public Value.DataType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Integer code = cs.getInt(columnIndex);
        return code == null?null: Value.DataType.fromCode(code);
    }
}
