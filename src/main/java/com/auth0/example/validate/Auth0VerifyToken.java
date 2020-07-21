package com.auth0.example.validate;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.auth0.example.model.TokenResponse;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Auth0VerifyToken {

	public static boolean validateToken(String accessToken, String refreshToken, HttpServletRequest req,
			HttpServletResponse res, long interval, ServletConfig config)
			throws IOException, JwkException, JsonParseException, JsonMappingException {

		RestTemplate restTemplate = null;
		HttpHeaders headers = null;
		TokenResponse output = null;
		String domainUrl = config.getServletContext().getInitParameter("com.auth0.domainUrl");
		String refreshUrl = config.getServletContext().getInitParameter("com.auth0.refreshUrl");
		String clientid = config.getServletContext().getInitParameter("com.auth0.clientId");
		String clientSecret = config.getServletContext().getInitParameter("com.auth0.clientSecret");

		DecodedJWT jwt = JWT.decode(accessToken);
		JwkProvider provider = new UrlJwkProvider(domainUrl);

		Jwk jwk = provider.get(jwt.getKeyId());

		Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

		algorithm.verify(jwt);

		if (jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
			return false;
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expirationTime = jwt.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		System.out.println(now.until(expirationTime, ChronoUnit.MINUTES));

		long minutes = now.until(expirationTime, ChronoUnit.MINUTES);
		if (minutes < interval) {
			String url = refreshUrl;
			restTemplate = new RestTemplate();
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "refresh_token");
			map.add("client_id", clientid);
			map.add("client_secret", clientSecret);
			map.add("refresh_token", refreshToken);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

			ObjectMapper objectMapper = new ObjectMapper();

			output = objectMapper.readValue(response.getBody(), TokenResponse.class);
			req.getSession().setAttribute("accessToken", output.getAccessToken());
			req.getSession().setAttribute("refreshToken", output.getRefreshToken());

		}

		return true;

	}

}
