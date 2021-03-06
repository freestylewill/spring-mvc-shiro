package com.jincou.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * @Description: 自定义FormAuthenticationFilter，认证之前实现 验证码校验
 *
 * @author xub
 * @date 2018/7/18 上午11:49
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

	/**
	 * 原FormAuthenticationFilter的认证方法
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,ServletResponse response) throws Exception {
		//在这里进行验证码的校验

		//从session获取正确验证码
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session =httpServletRequest.getSession();
		//取出session的验证码（正确的验证码）
		String validateCode = (String) session.getAttribute("validateCode");

		//取出页面的验证码
		//输入的验证和session中的验证进行对比
		String randomcode = httpServletRequest.getParameter("randomcode");
		if(randomcode!=null && validateCode!=null && !randomcode.equals(validateCode)){
			//如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
			httpServletRequest.setAttribute("shiroLoginFailure", "randomCodeError");
			//拒绝访问，不再校验账号和密码
			return true;
		}
		//验证码通过，才会到用户认证否则直接到controller层
		return super.onAccessDenied(request, response);
	}


}
