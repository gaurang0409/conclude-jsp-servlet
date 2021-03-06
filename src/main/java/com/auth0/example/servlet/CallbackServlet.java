package com.auth0.example.servlet;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;
import com.auth0.example.controller.AuthenticationControllerProvider;
import com.auth0.exception.IdTokenValidationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * The Servlet endpoint used as the callback handler in the OAuth 2.0
 * authorization code grant flow. It will be called with the authorization code
 * after a successful login.
 */
@WebServlet(urlPatterns = { "/callback" })
public class CallbackServlet extends HttpServlet {

	private String redirectOnSuccess;
	private String redirectOnFail;
	private AuthenticationController authenticationController;

	/**
	 * Initialize this servlet with required configuration.
	 * <p>
	 * Parameters needed on the Local Servlet scope:
	 * <ul>
	 * <li>'com.auth0.redirect_on_success': where to redirect after a successful
	 * authentication.</li>
	 * <li>'com.auth0.redirect_on_error': where to redirect after a failed
	 * authentication.</li>
	 * </ul>
	 * Parameters needed on the Local/Global Servlet scope:
	 * <ul>
	 * <li>'com.auth0.domain': the Auth0 domain.</li>
	 * <li>'com.auth0.client_id': the Auth0 Client id.</li>
	 * <li>'com.auth0.client_secret': the Auth0 Client secret.</li>
	 * </ul>
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		redirectOnSuccess = "/portal/home";
		redirectOnFail = "/login";

		try {
			authenticationController = AuthenticationControllerProvider.getInstance(config);
		} catch (UnsupportedEncodingException e) {
			throw new ServletException(
					"Couldn't create the AuthenticationController instance. Check the configuration.", e);
		}
	}

	/**
	 * Process a call to the redirect_uri with a GET HTTP method.
	 *
	 * @param req the received request with the tokens (implicit grant) or the
	 *            authorization code (code grant) in the parameters.
	 * @param res the response to send back to the server.
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		handle(req, res);
	}

	/**
	 * Process a call to the redirect_uri with a POST HTTP method. This occurs if
	 * the authorize_url included the 'response_mode=form_post' value. This is
	 * disabled by default. On the Local Servlet scope you can specify the
	 * 'com.auth0.allow_post' parameter to enable this behaviour.
	 *
	 * @param req the received request with the tokens (implicit grant) or the
	 *            authorization code (code grant) in the parameters.
	 * @param res the response to send back to the server.
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		handle(req, res);
	}

	private void handle(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {

			if (req.getSession().getAttribute("accessToken") == null) {
				Tokens tokens = authenticationController.handle(req, res);
				req.getSession().setAttribute("accessToken", tokens.getAccessToken());
				req.getSession().setAttribute("refreshToken", tokens.getRefreshToken());
				req.getSession().setAttribute("idToken", tokens.getIdToken());
			}
			res.sendRedirect(redirectOnSuccess);
		
		} catch (IdentityVerificationException e) {
			e.printStackTrace();
			req.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_BAD_REQUEST);
			req.setAttribute("javax.servlet.error.exception", e);
			RequestDispatcher dd = req.getRequestDispatcher("/error");
			dd.forward(req, res);

			return;

		}

	}

}