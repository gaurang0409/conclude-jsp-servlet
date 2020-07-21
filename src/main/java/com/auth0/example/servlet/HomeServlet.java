package com.auth0.example.servlet;

import java.io.IOException;
import java.util.Arrays;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.example.validate.Auth0VerifyToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(urlPatterns = { "/portal/home" })
public class HomeServlet extends HttpServlet {

	private ServletConfig servletConfig;

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		this.servletConfig = config;
		//com.sun.org.apache.xml.internal.security.Init.init();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		final String accessToken = (String) req.getSession().getAttribute("accessToken");
		final String refreshToken = (String) req.getSession().getAttribute("refreshToken");
		final String idToken = (String) req.getSession().getAttribute("idToken");
		boolean validateToken = false;

		System.out.println("Access token "+accessToken);
		System.out.println("Refresh token "+refreshToken);
		if (accessToken != null) {
			try {
				validateToken = Auth0VerifyToken.validateToken(accessToken, refreshToken, req, res, 10, servletConfig);

			} catch (Exception e) {

				req.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
				req.setAttribute("javax.servlet.error.exception", e);
				throw new ServletException(e.getMessage());
				/*
				 * RequestDispatcher dd = req.getRequestDispatcher("/error"); dd.forward(req,
				 * res); return;
				 */
			}
		}

		if (validateToken == true) {
			DecodedJWT accessTokenjwt = JWT.decode(accessToken);
			String accessTokenInfo = new String(Base64.decodeBase64(accessTokenjwt.getPayload().getBytes()));
			JSONObject jsonObjectaccessToken = new JSONObject(accessTokenInfo);
			String algorithm = accessTokenjwt.getAlgorithm();
			String issuer = accessTokenjwt.getIssuer();
			String subject = accessTokenjwt.getSubject();
			JSONArray audience = (JSONArray) jsonObjectaccessToken.get("aud");

			String scope = jsonObjectaccessToken.getString("scope");

			DecodedJWT idjwt = JWT.decode(idToken);
			String idTokenInfo = new String(Base64.decodeBase64(idjwt.getPayload().getBytes()));

			JSONObject jsonObject = new JSONObject(idTokenInfo);
			String nickName = jsonObject.getString("nickname");
			String name = jsonObject.getString("name");
			String picture = jsonObject.getString("picture");
			String iss = jsonObject.getString("iss");
			String sub = jsonObject.getString("sub");

			req.setAttribute("accessToken", accessToken);
			req.setAttribute("refreshToken", refreshToken);
			req.setAttribute("idToken", idToken);

			req.setAttribute("algorithm", algorithm);
			req.setAttribute("issuer", issuer);
			req.setAttribute("userid", subject);
			req.setAttribute("audience", audience);
			req.setAttribute("scope", scope);

			req.setAttribute("nickName", nickName);
			req.setAttribute("name", name);
			req.setAttribute("picture", picture);
			req.setAttribute("iss", iss);
			req.setAttribute("sub", sub);
			req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, res);
		} else {
			res.sendRedirect("/login");
		}
	}
}
