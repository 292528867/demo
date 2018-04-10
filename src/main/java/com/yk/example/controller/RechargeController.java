package com.yk.example.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.yk.example.config.AliConfig;
import com.yk.example.config.Wxconfig;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.RechargeRecord;
import com.yk.example.enums.PayStatus;
import com.yk.example.enums.PayType;
import com.yk.example.service.RechargeService;
import com.yk.example.utils.WXPayUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by yk on 2018/3/28.
 */
@ApiIgnore
@RestController
@RequestMapping("recharge")
public class RechargeController {

    private static final Logger logger = LoggerFactory.getLogger(RechargeRecord.class);

    @Autowired
    private AliConfig aliConfig;

    @Autowired
    private Wxconfig wxconfig;

    @Autowired
    private RechargeService rechargeService;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * 支付宝充值
     *
     * @param rechargeRecord
     * @return
     */
    @RequestMapping(value = "/aliPay")
    public ControllerResult aliPay(@RequestBody RechargeRecord rechargeRecord) {
        //生成充值订单记录
        rechargeRecord.setPayType(PayType.zfb);
        RechargeRecord newRecharge = rechargeService.save(rechargeRecord);
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", aliConfig.getAPP_ID(), aliConfig.getAPP_PRIVATE_KEY(),
                "json", aliConfig.getCHARSET(), aliConfig.getALIPAY_PUBLIC_KEY(), aliConfig.getSignType());
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("支付宝充值");
        model.setSubject("支付宝充值");
        model.setOutTradeNo(newRecharge.getId());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(newRecharge.getMoney() + "");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(aliConfig.getNotifyUrl());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            logger.info("生成支付宝充值订单返回前端的签名sign：" + response.getBody());
            return new ControllerResult().setRet_code(0).setRet_values(Collections.singletonMap("sign", response.getBody()));
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 支付宝回调接口
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "aliPayNotify")
    public String aliPayNotify(HttpServletRequest request, HttpServletResponse response) {
        logger.info("--------支付宝支付通知notify-----");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        logger.info("支付宝回调返回的参数：" + params);
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, aliConfig.getALIPAY_PUBLIC_KEY(), aliConfig.getCHARSET(), aliConfig.getSignType());
            logger.info("验证签名是否通过：" + flag);
            if (flag) {
                // 支付成功修改订单号状态
                rechargeService.updatePayStatus(params.get("out_trade_no"), PayStatus.success );
                return "success";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 微信支付
     *
     * @param rechargeRecord
     * @return
     */
    @RequestMapping(value = "wxPay")
    public ControllerResult wxPay(@RequestBody RechargeRecord rechargeRecord, HttpServletRequest request) {
        //生成充值订单记录
        rechargeRecord.setPayType(PayType.wx);
        RechargeRecord newRecharge = rechargeService.save(rechargeRecord);
        // 微信支付
        Map<String, String> wxRequestParamMap = new HashMap<>();
        // 微信支付分配的公众账号ID（企业号corpid即为此appId）
        wxRequestParamMap.put("appid", wxconfig.getAppID());
        // 微信支付分配的商户号
        wxRequestParamMap.put("mch_id", wxconfig.getMch_id());
        //随机字符串，长度要求在32位以内
        wxRequestParamMap.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
        //商品简单描述
        wxRequestParamMap.put("body", "微信充值");
        wxRequestParamMap.put("out_trade_no", newRecharge.getId());
        wxRequestParamMap.put("total_fee", 1 + "");
        // APP和网页支付提交用户端ip
        wxRequestParamMap.put("spbill_create_ip", request.getRemoteHost());
        // 异步接收微信支付结果通知的回调地址
        wxRequestParamMap.put("notify_url", wxconfig.getNotify_url());
        // JSAPI 公众号支付NATIVE 扫码支付 APP APP支付
        wxRequestParamMap.put("trade_type", "APP");
        try {
            // 生成签名sign
            String sign = WXPayUtils.generateSignature(wxRequestParamMap, wxconfig.getAppSecret());
            wxRequestParamMap.put("sign", sign);
            String wxRequestParamXML = WXPayUtils.mapToXml(wxRequestParamMap);
            logger.info("微信统一订单提交前参数:" + wxRequestParamXML);
            // 请求统一订单接口
            String wxResposeXml = restTemplate.postForObject(wxconfig.getUnifiedorder_url(), wxRequestParamXML, String.class);
            Map<String, String> resultMap = WXPayUtils.xmlToMap(wxResposeXml);
            return new ControllerResult().setRet_code(0).setRet_values(resultMap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ControllerResult().setRet_code(1).setMessage("微信支付异常");
    }

    /**
     * 微信回调通知
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "wxPayNotify")
    public String wxPayNotify(HttpServletRequest request) throws Exception {
        logger.info("--------微信支付通知notify开始-----");
        ServletInputStream inputStream = request.getInputStream();
        Map<String, String> resultMap = new HashMap<>();
        if (inputStream != null) {
            String notifyResultXml = IOUtils.toString(inputStream, "UTF-8");
            logger.info("--------微信支付通知返回结果-----" + notifyResultXml);
            // 校验签名
            boolean signatureValid = WXPayUtils.isSignatureValid(notifyResultXml, wxconfig.getAppSecret());
            if (signatureValid) {
                Map<String, String> notifyResultMap = WXPayUtils.xmlToMap(notifyResultXml);
                if (notifyResultMap.get("return_code").equals("SUCCESS")) {
                    // 支付成功修改订单号状态
                    rechargeService.updatePayStatus(notifyResultMap.get("out_trade_no"), PayStatus.success);
                    resultMap.put("return_code", "SUCCESS");
                    resultMap.put("return_msg", "OK");
                }
            } else {
                resultMap.put("return_code", "FAIL");
                resultMap.put("return_msg", "签名错误");
                return WXPayUtils.mapToXml(resultMap);
            }

        }
        logger.info("--------微信支付通知notify结束-----");
        return WXPayUtils.mapToXml(resultMap);
    }

}
