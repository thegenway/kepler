package com.hanqian.kepler.security.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/19 。
 * ============================================================================
 */
public class CurrUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	public CurrUserMethodArgumentResolver() {
	}

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		if(methodParameter.hasParameterAnnotation(CurrentUser.class)){
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		CurrentUser currentUserAnnotation = methodParameter.getParameterAnnotation(CurrentUser.class);
		return nativeWebRequest.getAttribute(currentUserAnnotation.value(), NativeWebRequest.SCOPE_SESSION);
	}

}
