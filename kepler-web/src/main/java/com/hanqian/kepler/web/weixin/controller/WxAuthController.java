package com.hanqian.kepler.web.weixin.controller;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.web.controller.BaseController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.web.bind.annotation.*;

/**
 * 微信公众号认证
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/15 。
 * ============================================================================
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/wx/portal")
public class WxAuthController extends BaseController {
	private static final long serialVersionUID = 5998956994559832709L;

	private final WxMpService wxService;
	private final WxMpMessageRouter messageRouter;

	@GetMapping(value = "auth", produces = "text/plain;charset=utf-8")
	public String auth(
	                   @RequestParam(name = "signature", required = false) String signature,
	                   @RequestParam(name = "timestamp", required = false) String timestamp,
	                   @RequestParam(name = "nonce", required = false) String nonce,
	                   @RequestParam(name = "echostr", required = false) String echostr){
		log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
		if (StrUtil.hasBlank(signature, timestamp, nonce, echostr)) {
			throw new IllegalArgumentException("请求参数非法，请核实!");
		}

		if (wxService.checkSignature(timestamp, nonce, signature)) {
			return echostr;
		}

		return "非法请求";
	}

	@PostMapping(value = "auth", produces = "text/plain;charset=utf-8")
	public String post(
			@RequestBody String requestBody,
			@RequestParam("signature") String signature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestParam("openid") String openid,
			@RequestParam(name = "encrypt_type", required = false) String encType,
			@RequestParam(name = "msg_signature", required = false) String msgSignature){
		log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
				openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

		if (!wxService.checkSignature(timestamp, nonce, signature)) {
			throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
		}

		String out = null;
		if (encType == null) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
			WxMpXmlOutMessage outMessage = this.route(inMessage);
			if (outMessage == null) {
				return "";
			}

			out = outMessage.toXml();
		} else if ("aes".equalsIgnoreCase(encType)) {
			// aes加密的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
			log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
			WxMpXmlOutMessage outMessage = this.route(inMessage);
			if (outMessage == null) {
				return "";
			}

			out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
		}

		log.debug("\n组装回复信息：{}", out);
		return out;

	}

	private WxMpXmlOutMessage route(WxMpXmlMessage message) {
		try {
			return this.messageRouter.route(message);
		} catch (Exception e) {
			log.error("路由消息时出现异常！", e);
		}
		return null;
	}
}
