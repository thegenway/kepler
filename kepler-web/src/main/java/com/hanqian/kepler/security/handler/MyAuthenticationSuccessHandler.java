package com.hanqian.kepler.security.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanqian.kepler.common.Constants;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.utils.IpUtils;
import com.hanqian.kepler.common.utils.ServletUtils;
import com.hanqian.kepler.flow.entity.User;
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
import java.util.Date;

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
			User user = userService.get(userId);
			request.getSession().setAttribute(Constants.CURRENT_USER, user);

			if(user!=null && StrUtil.isNotBlank(user.getId())){
				String agent= request.getHeader("user-agent");
				user.setLoginTime(new Date());
				user.setLoginIp(IpUtils.getIpAddr(request));
				user.setLoginBrowser(UserAgentUtil.parse(agent).getBrowser().toString());
				userService.save(user);
			}

		}

		if (ServletUtils.isAjaxRequest(request)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(AjaxResult.success("登录成功",authentication)));
		}else{
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

}
