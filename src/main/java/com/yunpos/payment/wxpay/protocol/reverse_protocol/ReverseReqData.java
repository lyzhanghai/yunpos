package com.yunpos.payment.wxpay.protocol.reverse_protocol;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.yunpos.model.SysWechatConfigWithBLOBs;
import com.yunpos.payment.wxpay.common.RandomStringGenerator;
import com.yunpos.payment.wxpay.common.Signature;
import com.yunpos.payment.wxpay.config.WechatPayConfig;

/**
 * User: rizenguo
 * Date: 2014/10/25
 * Time: 16:42
 */
public class ReverseReqData {
    //每个字段具体的意思请查看API文档
    private String appid = "";
    private String mch_id = "";
    private String transaction_id = "";
    private String out_trade_no = "";
    private String nonce_str = "";
    private String sign = "";
    private String sdk_version = "";
    private String sub_appid = "";//子商户公众账号ID
    private String sub_mch_id=""; //子商户号

    /**
     * 请求撤销服务
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
     * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @return API返回的XML数据
     * @throws Exception
     */
    //普通商户
    public ReverseReqData(String transactionID,String outTradeNo,SysWechatConfigWithBLOBs sysWechatConfig){

        //--------------------------------------------------------------------
        //以下是测试数据，请商户按照自己的实际情况填写具体的值进去
        //--------------------------------------------------------------------

        setSdk_version(WechatPayConfig.sdkVersion);

        //微信分配的公众号ID（开通公众号之后可以获取到）
        setAppid(sysWechatConfig.getAppId());

        //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
        setMch_id(sysWechatConfig.getMchId());

        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        //setTransaction_id(transactionID);

        //商户系统自己生成的唯一的订单号
        setOut_trade_no(outTradeNo);

        //随机字符串，不长于32 位
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));

        //根据API给的签名规则进行签名
        String sign = Signature.getSign(toMap(),sysWechatConfig.getApiSecret());
        setSign(sign);//把签名数据设置到Sign这个属性中

    }
    //服务商
	public ReverseReqData(String transactionID, String outTradeNo, SysWechatConfigWithBLOBs sysWechatConfigPar,
			SysWechatConfigWithBLOBs sysWechatConfigSub) {
    	
    	//--------------------------------------------------------------------
    	//以下是测试数据，请商户按照自己的实际情况填写具体的值进去
    	//--------------------------------------------------------------------
    	
    	setSdk_version(WechatPayConfig.sdkVersion);
    	
    	//微信分配的公众号ID（开通公众号之后可以获取到）
		setAppid(sysWechatConfigPar.getAppId());
		//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		setMch_id(sysWechatConfigPar.getMchId());
		//微信分配的公众号ID（开通公众号之后可以获取到）
		setSub_appid(sysWechatConfigSub.getAppId());
		//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		setSub_mch_id(sysWechatConfigSub.getMchId());
    	
    	//transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
    	//setTransaction_id(transactionID);
    	
    	//商户系统自己生成的唯一的订单号
    	setOut_trade_no(outTradeNo);
    	
    	//随机字符串，不长于32 位
    	setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
    	
    	//根据API给的签名规则进行签名
    	String sign = Signature.getSign(toMap(),sysWechatConfigPar.getApiSecret());
    	setSign(sign);//把签名数据设置到Sign这个属性中
    	
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSdk_version(){
        return sdk_version;
    }

    public void setSdk_version(String sdk_version) {
        this.sdk_version = sdk_version;
    }

    public String getSub_appid() {
		return sub_appid;
	}

	public void setSub_appid(String sub_appid) {
		this.sub_appid = sub_appid;
	}

	public String getSub_mch_id() {
		return sub_mch_id;
	}

	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}

	public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
