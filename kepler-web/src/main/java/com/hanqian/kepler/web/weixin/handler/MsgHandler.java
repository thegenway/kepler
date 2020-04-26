package com.hanqian.kepler.web.weixin.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.web.weixin.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服") && weixinService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        //TODO 组装回复消息
        //一个例子，对对联，输入上联返回下联
	    String baseUrl = "https://v1.alapi.cn/api/couplet?keyword=";
        String content = wxMessage.getContent();
        String returnContent = "";
        try{
	        String re = HttpUtil.get(baseUrl+content);
	        JSONObject jsonObject = JSONUtil.parseObj(re);
	        if(!StrUtil.equals("200", jsonObject.getStr("code"))){
		        returnContent = "您输入的是【"+content+"】，但接口返回错误";
	        }else{
	        	JSONObject data = jsonObject.getJSONObject("data");
		        returnContent = "上联：【"+data.getStr("keyword")+"】\n下联：【"+data.getStr("text")+"】";
	        }
        }catch (Exception e){
	        returnContent = "您输入的是【"+content+"】，但是接口已挂";
        }

        return new TextBuilder().build(returnContent, wxMessage, weixinService);
    }

}
