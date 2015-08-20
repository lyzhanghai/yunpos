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

package com.yunpos.rewriter.filter;

import com.yunpos.rewriter.Binding;
import com.yunpos.rewriter.value.Value;
import com.yunpos.rewriter.value.ValueList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public abstract class Filter implements Binding{
    private static Logger logger = LoggerFactory.getLogger(Filter.class);
    public static enum Op{
        EQ(0,"="),GE(1,">="),GT(2,">"),LE(3,"<="),LT(4,"<"),LIKE(5,"like"),IN(6,"in");
        private int code;
        private String symbol;
        Op(int code,String symbol){
            this.code=code;
            this.symbol=symbol;
        }
        public int getCode(){
            return code;
        }
        public String getSymbol(){
            return symbol;
        }
        public static Op fromCode(int code){
            switch(code){
                case 0:return EQ;
                case 1:return GE;
                case 2:return LT;
                case 3:return LE;
                case 4:return LT;
                case 5:return LIKE;
                default:throw new java.lang.IllegalArgumentException("unsported code "+code+" for enum Filter.Op");
            }
        }

        public static List<Op> disjoint(int code){
            List<Filter.Op> opList=new ArrayList<>();
            for(Filter.Op op:Filter.Op.values()){
                if(isOp(code,op.getCode())){
                    opList.add(op);
                }
            }
            return opList;
        }
        public static int overlap(List<Filter.Op> opList){
            int code=0;
            for(Filter.Op op:opList){
                code=setOp(code,code);
            }
            return code;
        }
        public static int setOp(int code,int opCode) {
            code |= 1 << opCode;
            return code;
        }

        public static int unsetOp(int code,int opCode) {
            code &= ~(1 << opCode);
            return code;
        }

        public static boolean isOp(int code,int opCode) {
            return (code >> opCode & 1) == 1;
        }
    }
    private int id;
    private String name;
    private Op op;
    protected Value filterValue;
    public Filter(){

    }

    public Filter(int dataTypeCode,String json) throws IOException, ParseException {
        this(Value.DataType.fromCode(dataTypeCode),json);
    }

    public Filter(Value.DataType dataType,String json) throws IOException, ParseException {
        filterValue =Value.fromJson(dataType, json);
    }

    public String toSql(){
        String key= getFilterKey();
        String value= getFilterValue().toString();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(key);
        joiner.add(op.getSymbol());
        switch (op){
            case IN:
                joiner.add("("+value+")");
                break;
            default:
                joiner.add(value);
        }
        return joiner.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Op getOp() {
        return op;
    }

    public void setOp(Op op) {
        this.op = op;
        if(filterValue instanceof ValueList && op!=Op.IN){
            throw new java.lang.IllegalArgumentException(this.getClass()+" filter value is a list "+filterValue+
                    " when op is "+op.name());
        }
    }

    public Value getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(Value filterValue) {
        this.filterValue = filterValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void bind(Map<String,Object> params){
        try {
            bindKey(params);
            if (filterValue instanceof Binding){
                ((Binding) filterValue).bind(params);
            }
        }catch (MissBindingParamExecption e){
            logger.error("filter error when binding",e);
        }

    };
    protected abstract void bindKey (Map<String,Object> params) throws MissBindingParamExecption;
    protected  abstract String getFilterKey();

    @Override
    public String toString() {
        return "Filter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", op=" + op +
                ", filterValue=" + filterValue +
                '}';
    }
}