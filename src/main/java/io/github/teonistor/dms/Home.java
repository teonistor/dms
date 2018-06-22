package io.github.teonistor.dms;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Home", urlPatterns = "/")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 7474714634611156487L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().write("Home page under construction");
	}
}
