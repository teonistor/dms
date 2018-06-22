package io.github.teonistor.dms;
import java.io.IOException;
import java.util.Date;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Home", urlPatterns = "/")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 7474714634611156487L;

	@Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		switch (request.getRequestURI()) {
		case "/reset": response.getWriter().write(getReset()); break;
		case "/catastrophe": response.getWriter().write(getCatastrophe()); break;
		default: response.getWriter().write(status());
		}
		
		
		
//		response.getWriter().write("Home page under construction");
//		request.getRequestDispatcher("/home.html").include(request, response);
	}
	
	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		switch (request.getRequestURI()) {
		case "/reset": response.getWriter().write(postReset(request.getParameter("pswd"))); break;
		case "/catastrophe": response.getWriter().write(postCatastrophe(request.getParameter("pswd"))); break;
		default: response.sendRedirect("/");
		}
		
		
		
//		response.getWriter().write("Home page under construction");
//		request.getRequestDispatcher("/home.html").include(request, response);
	}
	
	
	private String status () {
		AppData ad = AppData.retrieve();
		boolean send = ad.doCron();
		if (send); // TODO
		
		StringBuilder html = new StringBuilder("<html><head><title>DMS Status</title></head><body><h2>Cron call log</h2>");
		for (Long date : ad.crons) {
			html.append(new Date(date));
			html.append("<br>");
		}
		
		html.append("<h2>Switches flipped</h2>");
		for (Long date : ad.flips) {
			html.append(new Date(date));
			html.append("<br>");
		}
		
		return html.toString();
	}
	
	private String getReset () {
		return "<html><head><title>Reset DMS</title><head><body><p>Enter password:</p><form action=\"/reset\" method=\"post\"><input type=\"password\" name=\"pswd\"><input type=\"submit\"></form></body></html>";
	}
	
	private String getCatastrophe () {
		
	}
	
	private String postReset (String pswd) {
		AppData ad = AppData.retrieve();
		if(ad.doFlip(pswd)) {
			return "<html><head><title>Reset DMS Successful</title><head><body>Reset DMS Successful</body></html>";
		}
		return "<html><head><title>Reset DMS Failed</title><head><body>Reset DMS failed</body></html>";
	}
	
	private String postCatastrophe (String pswd) {
		
	}
}
