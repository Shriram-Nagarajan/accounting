package com.um.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.common.auth.UserThreadLocal;
import com.um.UserManagement;
import com.um.auth.UserSessionHandler;
import com.um.exception.ValidationException;
import com.um.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserManagementFilter extends OncePerRequestFilter {
	
	private UserSessionHandler userSessionHandler;
	private List<String> postLoginUrls;
	
	public UserManagementFilter(UserSessionHandler userSessionHandler, Environment env) {
		this.userSessionHandler = userSessionHandler;
		Optional.ofNullable(env.getProperty("session.post-login.routes"))
			.ifPresent((routesStr) -> {
				if(!routesStr.isBlank()) {
					postLoginUrls = List.of(routesStr.split(","));
				}
			});
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		UserThreadLocal<User> userThreadLocal = new UserThreadLocal<User>();
		try {
			User user = userSessionHandler.getSession(request, true);
			if(user != null) {
				userThreadLocal.put(user);
				filterChain.doFilter(request, response);
			}	else {
				if(isSessionRequired(request.getPathInfo())) {
					response.setStatus(403);
					response.getWriter().write("User not logged in!");
					response.getWriter().flush();
				}
			}
		} catch (ValidationException e) {
			log.error("ValidationException occurred : ", e);
			if(isSessionRequired(request.getPathInfo())) {
				response.setStatus(403);
				response.getWriter().write(e.getMessage());
				response.getWriter().flush();
			}	else {
				filterChain.doFilter(request, response);
			}
		} catch(Exception e) {
			log.error("Unexpected exception occurred : ", e);
			response.setStatus(500);
			response.getWriter().write(e.getMessage());
			response.getWriter().flush();
		} finally {
			userThreadLocal.remove();
		}
		
	}
	
	private boolean isSessionRequired(String path) {
		return Optional.ofNullable(postLoginUrls).isPresent() ?
				postLoginUrls.contains(path) : false;
	}

	private static final Logger log = LogManager.getLogger(UserManagement.class);
	
}
