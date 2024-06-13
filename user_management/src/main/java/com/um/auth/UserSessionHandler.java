package com.um.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.common.auth.SessionIdGenerator;
import com.common.auth.UserThreadLocal;
import com.common.session.SessionCache;
import com.um.exception.ValidationException;
import com.um.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service("userSessionHandler")
public class UserSessionHandler {

	private SessionCache sessionCache;
	private String sessionTokenName;
	private String sessionDomain;
	private Environment env;
	
	public UserSessionHandler(SessionCache memCache, Environment env) {
		sessionCache = memCache;
		this.env = env;
		sessionTokenName = env.getProperty("session.auth-token");
		sessionDomain = env.getProperty("session.cookie.domain");
	}
	
	private String createSessionId(User user) {
		return SessionIdGenerator.createSessionId(user.getUserEntity().getLoginId());
	}
	
	private boolean isValidSessionId(String sessionId) {
		return SessionIdGenerator.isValidSessionId(sessionId);
	}

	
	public void createSessionOnLogin(User user, HttpServletRequest request, HttpServletResponse response) throws ValidationException {
		String sessionId = createSessionId(user);
		removeSessionFromCache(request);
		removeSessionCookie(request, response);
		sessionCache.store(sessionId, user, Integer.parseInt(env.getProperty("session.expiry.seconds")));
		Cookie cookie = new Cookie(sessionTokenName, sessionId);
		cookie.setDomain(sessionDomain);
		cookie.setMaxAge(Integer.parseInt(env.getProperty("session.cookie.max-age")));
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public User getSession(HttpServletRequest request, boolean extendSession) throws ValidationException {
		UserThreadLocal<User> threadLocal = new UserThreadLocal<User>();
		if(threadLocal.get() != null) {
			return threadLocal.get();
		} else {
			Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
			Optional<Cookie> cookieOptional = List.of(cookies).stream()
					.filter((c) -> sessionTokenName.equals(c.getName()))
					.findFirst();
			if(cookieOptional.isPresent() && cookieOptional.get() != null) {
				Cookie sessionCookie = cookieOptional.get();
				String userSessionId = sessionCookie.getValue();
				if(isValidSessionId(userSessionId)) {
					Object userObj = sessionCache.get(userSessionId);
					if(userObj != null) {
						User user = (User) userObj;
						if(extendSession) {
							sessionCache.store(userSessionId, user, Integer.parseInt(env.getProperty("session.expiry.seconds")));
						}
						return user;
					}
				}	else {
					throw new ValidationException("User session expired");
				}
			}	else {
				throw new ValidationException("User not logged in!");
			}
		}
		return null;
	}
	
	public void removeSessionOnLogout(HttpServletRequest request, HttpServletResponse response) throws ValidationException {
		removeSessionFromCache(request);
		removeSessionCookie(request, response);
	}
	
	public void removeSessionFromCache(HttpServletRequest request) throws ValidationException {
		Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
		Optional<Cookie> cookieOptional = List.of(cookies).stream()
				.filter((c) -> sessionTokenName.equals(c.getName()))
				.findFirst();
		if(cookieOptional.isPresent() && cookieOptional.get() != null) {
			Cookie sessionCookie = cookieOptional.get();
			String userSessionId = sessionCookie.getValue();
			if(isValidSessionId(userSessionId)) {
				Object userObj = sessionCache.get(userSessionId);
				if(userObj != null) {
					sessionCache.delete(userSessionId);
				}
			}	else {
				throw new ValidationException("User session expired");
			}
		}
	}
	
	private void removeSessionCookie(HttpServletRequest request, HttpServletResponse response) {
		Optional.ofNullable(request.getCookies())
			.ifPresent((cookies) -> {
				List.of(cookies).forEach(cookie -> {
					if(sessionTokenName.equals(cookie.getName())) {
						cookie.setMaxAge(0);
						cookie.setValue(null);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				});
			});
	}
	
}
