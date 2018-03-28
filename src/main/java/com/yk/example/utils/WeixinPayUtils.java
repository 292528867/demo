package com.yk.example.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

/**
 * 微信支付参数 xml map x相互转换
 * Created by yk on 2018/3/28.
 */
public class WeixinPayUtils {

    private static final Logger logger = LoggerFactory.getLogger(WeixinPayUtils.class);

    /**
     * 微信参数转xml
     *
     * @param params
     * @return
     */
    public static String mapToXml(Map<String, String> params) {
        String xml = "<xml>";
        Iterator<String> itr = params.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            xml += "<" + key + ">";
            xml += params.get(key);
            xml += "</" + key + ">";
        }
        xml += "</xml>";
        return xml;
    }

    /**
     * xml结果转换成Map
     *
     * @param xml
     * @return
     */
    public static Map<String, String> xmlToMap(String xml) {
        Map<String, String> resultMap = new HashMap<String, String>();
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> elementList = root.elements();
            for (Element element : elementList) {
                resultMap.put(element.getName(), element.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * md5对微信参数加密
     * @param buffer
     * @return
     */
    public static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }


}
