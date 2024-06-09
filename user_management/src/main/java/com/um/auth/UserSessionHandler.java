package com.um.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.common.auth.SessionIdGenerator;
import com.common.session.SessionCache;
import com.um.entity.UserEntity;
import com.um.exception.ValidationException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service("userSessionHandler")
public class UserSessionHandler {

	private SessionCache sessionCache;
	
	public UserSessionHandler(SessionCache memCache) {
		sessionCache = memCache;
	}
	
	private String createSessionId(UserEntity user) {
		return SessionIdGenerator.createSessionId(user.getLoginId());
	}
	
	private boolean isValidSessionId(String sessionId) {
		return SessionIdGenerator.isValidSessionId(sessionId);
	}
	
	public void addSession(UserEntity user, HttpServletResponse response) {
		String sessionId = createSessionId(user);
		sessionCache.store(sessionId, user, 3600);
		Cookie cookie = new Cookie("sessionToken", sessionId);
		cookie.setDomain("local.finfree.com");
		cookie.setMaxAge(3600);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public UserEntity getSession(HttpServletRequest request) throws ValidationException {
		Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
		Optional<Cookie> cookieOptional = List.of(cookies).stream()
				.filter((c) -> "sessionToken".equals(c.getName()))
				.findFirst();
		if(cookieOptional.isPresent() && cookieOptional.get() != null) {
			Cookie sessionCookie = cookieOptional.get();
			String userSessionId = sessionCookie.getValue();
			if(isValidSessionId(userSessionId)) {
				Object userObj = sessionCache.get(userSessionId);
				if(userObj != null) {
					return (UserEntity) userObj;
				}
			}	else {
				throw new ValidationException("User session expired");
			}
		}	else {
			throw new ValidationException("User not logged in!");
		}
		return null;
	}
	
	public void removeSession(HttpServletRequest request) throws ValidationException {
		Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
		Optional<Cookie> cookieOptional = List.of(cookies).stream()
				.filter((c) -> "sessionToken".equals(c.getName()))
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
		}	else {
			throw new ValidationException("User not logged in!");
		}
	}
	
	
}
