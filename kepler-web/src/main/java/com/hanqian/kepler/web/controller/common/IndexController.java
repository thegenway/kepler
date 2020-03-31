package com.hanqian.kepler.web.controller.common;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.utils.RedisUtil;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页服务
 * ============================================================================
 * author : dzw
 * createDate:  2020/3/31 。
 * ============================================================================
 */
@Controller
@RequestMapping(value = "/common/index")
public class IndexController extends BaseController {
	private static final long serialVersionUID = 1160849292953660582L;

	@Autowired
	private RedisUtil redisUtil;

	private final static String baseWeatherUrl = "http://t.weather.sojson.com/api/weather/city/";

	/**
	 * 获取天气数据
	 */
	@GetMapping("weather/{cityId}")
	@ResponseBody
	public Object weather(@PathVariable String cityId){
		Object obj = redisUtil.get("weather_"+cityId);
		if(ObjectUtil.isNotNull(obj)){
			return JSONUtil.parseObj(obj);
		}else{
			String re = HttpUtil.get(baseWeatherUrl + cityId);
			if(JSONUtil.isJsonObj(re)){
				JSONObject jsonObject = JSONUtil.parseObj(re);
				if(StrUtil.equals("200", Convert.toStr(jsonObject.get("status")))){
					redisUtil.set("weather_"+cityId, re, 60*60*2); //放进redis存2小时
				}
			}
			return JSONUtil.parseObj(re);
		}
	}

}
