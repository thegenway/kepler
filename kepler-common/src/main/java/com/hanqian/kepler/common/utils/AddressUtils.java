package com.hanqian.kepler.common.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 * 
 * @author ruoyi
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    public static String getRealAddressByIP(String ip)
    {
        String address = "XX XX";

        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "内网IP";
        }

        String rspStr = HttpUtil.post(IP_URL, "ip=" + ip);
        if (StringUtils.isEmpty(rspStr))
        {
            log.error("获取地理位置异常 {}", ip);
            return address;
        }
        JSONObject obj;
        try
        {
            obj = JSONUtil.parseObj(rspStr);
            JSONObject data = obj.getJSONObject("data");
            String region = data.getStr("region");
            String city = data.getStr("city");
            address = region + " " + city;
        }
        catch (Exception e)
        {
            log.error("获取地理位置异常 {}", ip);
        }

        return address;
    }
}
