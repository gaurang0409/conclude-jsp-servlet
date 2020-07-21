package com.auth0.example.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.AuthenticationController;
import com.auth0.example.controller.AuthenticationControllerProvider;
import com.auth0.example.validate.Auth0VerifyToken;
import com.auth0.jwk.JwkException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@WebServlet(urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AuthenticationController authenticationController;
	private String domain;
	private String scope;
	private String audience;
	private ServletConfig servletConfig;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		domain = config.getServletContext().getInitParameter("com.auth0.domain");
		scope = config.getServletContext().getInitParameter("com.auth0.scope");
		audience = config.getServletContext().getInitParameter("com.auth0.audience");
		try {
			authenticationController = AuthenticationControllerProvider.getInstance(config);

		} catch (UnsupportedEncodingException e) {
			throw new ServletException(
					"Couldn't create the AuthenticationController instance. Check the configuration.", e);
		}
		servletConfig = config;
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse res)
			throws ServletException, IOException {
		String redirectUri = req.getScheme() + "://" + req.getServerName();
		if ((req.getScheme().equals("http") && req.getServerPort() != 80)
				|| (req.getScheme().equals("https") && req.getServerPort() != 443)) {
			redirectUri += ":" + req.getServerPort();
		}

		redirectUri += "/callback";

		String accessToken = (String) req.getSession().getAttribute("accessToken");
		String refreshToken = (String) req.getSession().getAttribute("refreshToken");

		boolean validateToken = false;

		/*if (accessToken != null && !("").equals(accessToken)) {

			try {
				validateToken = Auth0VerifyToken.validateToken(accessToken, refreshToken, req, res, 10, servletConfig);

			} catch (Exception e) {
				req.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
				req.setAttribute("javax.servlet.error.exception", e);
				throw new ServletException(e.getMessage());
				
				 * RequestDispatcher dd = req.getRequestDispatcher("/error"); dd.forward(req,
				 * res); return;
				 

			}

		}*/
		if (!validateToken) {
			String authorizeUrl = authenticationController.buildAuthorizeUrl(req, res, redirectUri).withScope(scope)
					.withAudience(audience).build();
			res.sendRedirect(authorizeUrl);
		} else {
			res.sendRedirect("/callback");
		}
	}

}
