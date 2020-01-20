package com.hanqian.kepler.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanqian.kepler.common.Constants;
import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.common.utils.ServletUtils;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.security.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * security登录成功后处理
 * ============================================================================
 * author : dzw
 * createDate:  2019/8/15 。
 * ============================================================================
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		//将当前登录人放到session中，为@CurrentUser注解
		if(authentication.getPrincipal() instanceof UserPrincipal){
			String userId = ((UserPrincipal)authentication.getPrincipal()).getId();
			request.getSession().setAttribute(Constants.CURRENT_USER, userService.getOne(userId));
		}

		if (ServletUtils.isAjaxRequest(request)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(AjaxResult.success("登录成功",authentication)));
		}else{
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

}
