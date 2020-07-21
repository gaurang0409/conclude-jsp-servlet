package com.auth0.example.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/error")
public class ErrorHandler extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);

	}

	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Integer statusCode = (Integer) request.getAttribute("status_code");
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		String message = throwable.getMessage();
		message = "Please checks logs on server for error occured";
		request.setAttribute("statusCode", statusCode);
		request.setAttribute("message", message);
		request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);

	}
}
