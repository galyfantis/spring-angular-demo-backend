package org.gal.fullstack.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RewriteFilter implements Filter {
	
	private static final Pattern PATTERN = Pattern.compile(
			"/((.*\\.(js|ts|css|json|png|jpg|jpeg|gif|svg|woff|woff2|ttf|eot|ico|pdf))|(index\\.html)|(home\\.html)|(api/.*))?$", 
			Pattern.CASE_INSENSITIVE);

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String path = req.getRequestURI().substring(req.getContextPath().length());
		Matcher matcher = PATTERN.matcher(path);
		if (!matcher.matches()) {
			RequestDispatcher dispatcher = req.getRequestDispatcher("/index.html");
			dispatcher.forward(request, response);
			return;
		}
		
		chain.doFilter(req, resp);
	}

}
