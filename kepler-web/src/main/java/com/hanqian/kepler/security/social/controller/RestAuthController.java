package com.hanqian.kepler.security.social.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.web.controller.BaseController;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/17 。
 * ============================================================================
 */
@Controller
@RequestMapping("/security/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestAuthController extends BaseController {
	private static final long serialVersionUID = 698156953556237388L;

	private final AuthRequestFactory authRequestFactory;

	@GetMapping("/login/{oauthType}")
	public void renderAuth(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
		if(StrUtil.isBlank(oauthType)){
			throw new RuntimeException("不支持的类型");
		}
		AuthRequest authRequest = authRequestFactory.get(oauthType);
		response.sendRedirect(authRequest.authorize(oauthType + "::" + AuthStateUtils.createState()));
	}

	@RequestMapping("/{type}/callback")
	public AuthResponse login(@PathVariable String type, AuthCallback callback) {
		AuthRequest authRequest = authRequestFactory.get(type);
		AuthResponse response = authRequest.login(callback);
		System.out.println(("【response"+JSONUtil.toJsonStr(response)));
		return response;
	}

}
