package com.yk.example.utils;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SmsUtils {

    private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);

    /**
     *  发送短信
     * @param phone    手机号
     * @param code     验证码
     * @param templateId  模板编号
     * @param time        验证码时间
     * @return
     */
    public static boolean sendSms(String phone, String code, String templateId, int time) {
        Map<String, Object> result = null;

        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        // 初始化服务器地址和端口 沙盒环境（用于应用开发调试）
        restAPI.init("sandboxapp.cloopen.com", "8883");
        //  初始化服务器地址和端口 生产环境（用户应用上线使用）
//        restAPI.init("app.cloopen.com", "8883");
        // 初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN
        restAPI.setAccount("8aaf0708624670f20162758586d714a2", "1a2f70696e4046dc9d7ee338a471e63d");
        // 初始化应用ID
        restAPI.setAppId("8aaf0708624670f201627585873314a9");
        result = restAPI.sendTemplateSMS(phone, templateId, new String[]{code, time+""});
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
   /*         for (String key : keySet) {
                Object object = data.get(key);
//                System.out.println(key + " = " + object);
            }*/
            return true;
        }
        return false;
    }
}