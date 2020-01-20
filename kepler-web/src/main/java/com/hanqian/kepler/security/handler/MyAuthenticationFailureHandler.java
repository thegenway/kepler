package com.hanqian.kepler.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.common.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * security登录失败后处理
 * ============================================================================
 * author : dzw
 * createDate:  2019/8/15 。
 * ============================================================================
 */
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		if (ServletUtils.isAjaxRequest(request)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(AjaxResult.error(e.getMessage())));
		}else{
			super.onAuthenticationFailure(request, response, e);
		}

	}
}
