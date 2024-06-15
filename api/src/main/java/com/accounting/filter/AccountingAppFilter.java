package com.accounting.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.accounting.auth.AccountingAppSessionHandler;
import com.accounting.handler.UserAccountsHandler;
import com.accounting.model.AccountingUser;
import com.common.auth.UserThreadLocal;
import com.common.exception.ValidationException;
import com.common.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccountingAppFilter extends OncePerRequestFilter {
	
	private AccountingAppSessionHandler accountingAppSessionHandler;
	private UserAccountsHandler userAccountsHandler;
	private List<String> postLoginUrls;
	
	public AccountingAppFilter(AccountingAppSessionHandler accountingAppSessionHandler, UserAccountsHandler userAccountsHandler, Environment env) {
		this.accountingAppSessionHandler = accountingAppSessionHandler;
		this.userAccountsHandler = userAccountsHandler;
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

		UserThreadLocal<AccountingUser> userThreadLocal = new UserThreadLocal<AccountingUser>();
		try {
			User user = accountingAppSessionHandler.getSession(request, true);
			if(user != null) {
				AccountingUser accountingUser = new AccountingUser(user);
				List<Long> userAccountIds = userAccountsHandler
						.findAccountsForUser(user.getUserDetails().getUserId())
						.stream()
						.map(account -> account.getUserAccountId().getAccountId()).toList();
				accountingUser.setUserAccounts(userAccountIds);
				userThreadLocal.put(accountingUser);
				filterChain.doFilter(request, response);
			}	else {
				if(isSessionRequired(getRequestPath(request))) {
					response.setStatus(403);
					response.getWriter().write("User not logged in!");
					response.getWriter().flush();
				}
			}
		} catch (ValidationException e) {
			log.error("ValidationException occurred : ", e);
			if(isSessionRequired(getRequestPath(request))) {
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
	
	private String getRequestPath(HttpServletRequest request) {
		return request.getPathInfo() != null && !request.getPathInfo().isBlank()
				? request.getPathInfo() : (request.getServletPath() != null ? request.getServletPath() : "");
	}

	private static final Logger log = LogManager.getLogger(AccountingAppFilter.class);
	
}
