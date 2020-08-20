package com.mmgss.demo.controller;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.security.provider.SHA;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alexouyang
 * @Date 2020-08-20
 */
@RestController
@RequestMapping("/safecharge/")
public class SafeChargeController {

    String secretKey = "9REnHvMzHKFYa8cDn8Pr2QoG3LEOqJBtgiaQBR4caFh81ONhgBZqpbxHO38WhPit";

    @RequestMapping(method = RequestMethod.POST, value = "getSession")
    public void getSessionToken() throws Exception{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://ppp-test.safecharge.com/ppp/api/v1/getSessionToken.do");
        List<NameValuePair> list = new ArrayList<>();
        String merchantId = "2953591139398726027";
        // param1
        list.add(new BasicNameValuePair("merchantId",merchantId));
        String merchantSiteId = "205686";
        // param2
        list.add(new BasicNameValuePair("merchantSiteId",merchantSiteId)); //Applause9
        String clientRequestId = "1484759782197";
        // param3
        list.add(new BasicNameValuePair("clientRequestId",clientRequestId));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStamp = dtf.format(LocalDateTime.now());
        // param4
        list.add(new BasicNameValuePair("timeStamp",timeStamp));
        // concatenation of the following fields, in the following order
        // merchantId, merchantSiteId,clientRequestId,timeStamp, {your secret key}
        // (no spaces, no separators between the fields).
        String checksum = SHA256(merchantId+merchantSiteId+clientRequestId+timeStamp+secretKey);
        // param5
        list.add(new BasicNameValuePair("checksum",checksum));
        StringEntity entity = new UrlEncodedFormEntity(list,"utf-8");
        post.setEntity(entity);
        CloseableHttpResponse response = client.execute(post);
        int code = response.getStatusLine().getStatusCode();
        HttpEntity contentEntity = response.getEntity();
        String content = EntityUtils.toString(contentEntity);
        System.out.print(code);
        System.out.print(content);
        response.close();
        client.close();
    }

    public void testDataTimeFormatter(){
        LocalDateTime dt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYYMMDDHHmmss");
        System.out.println(dtf.format(dt));
    }

    public String SHA256(final String strText)
    {
        return SHA(strText, "SHA-256");
    }


    /**
     * 字符串 SHA 加密
     *
     * @param strText
     * @param strType
     * @return
     */
    private String SHA(final String strText, final String strType)
    {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0)
        {
            try
            {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++)
                {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1)
                    {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
        }

        return strResult;
    }


}
