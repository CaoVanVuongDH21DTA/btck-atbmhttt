package com.java.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java.model.User;

/**
 * Servlet Filter implementation class SiteServlet
 */
@WebFilter()
public class SiteFilter implements Filter {

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		HttpServletResponse res = (HttpServletResponse)response;
		
		HttpSession session = req.getSession();
		
		User user = (User) session.getAttribute("user");
		
		if(user == null) {
			res.sendRedirect("/btck-atbmhttt/LoginServlet");
		}else {
			chain.doFilter(request, response);
		}
		
	}


}
