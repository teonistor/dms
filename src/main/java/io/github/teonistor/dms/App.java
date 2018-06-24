package io.github.teonistor.dms;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Home", urlPatterns = "/")
public class App extends HttpServlet {
	private static final long serialVersionUID = 7474714634611156487L;

	@Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		switch (request.getRequestURI()) {
		case "/reset": response.getWriter().write(getReset()); break;
		case "/catastrophe": response.getWriter().write(getCatastrophe()); break;
		case "/cron": response.getWriter().write(cron()); break;
		default: response.getWriter().write(status());
		}
	}

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		switch (request.getRequestURI()) {
		case "/reset": response.getWriter().write(postReset(request.getParameter("pswd"))); break;
		case "/catastrophe": postCatastrophe(request.getParameter("pswd"), response); break;
		default: response.sendRedirect("/");
		}



		//		response.getWriter().write("Home page under construction");
		//		request.getRequestDispatcher("/home.html").include(request, response);
	}


	private String status () {
		AppData ad = AppData.retrieve();

		StringBuilder html = new StringBuilder("<html><head><title>DMS Status</title></head><body>");
		long waiting = ad.people.stream().filter(p -> p.waiting).count();
		html.append(Long.toString(ad.people.size() - waiting))
			.append(" emails sent, ")
			.append(Long.toString(waiting))
			.append(" waiting.");

		html.append("<h2>Cron worked</h2>");
		for (Long date : ad.crons) {
			html.append(new Date(date))
				.append("<br>");
		}

		html.append("<h2>Switches flipped</h2>");
		for (Long date : ad.flips) {
			html.append(new Date(date))
				.append("<br>");
		}

		html.append("<br><a href='/reset'>Reset</a></body></html");
		return html.toString();
	}

	private String cron() {
		AppData ad = AppData.retrieve();
		long lastFlip = ad.flips.get(ad.flips.size() - 1);
		long now = System.currentTimeMillis();
		int sentCount=0, errCount=0;
		StringBuilder errMsg = new StringBuilder();
		
		for (Reminder r : ad.reminders) {
			try {
				if (r.checkSend(lastFlip, now))
					sentCount++;
			} catch (MessagingException e) {
				errCount++;
				errMsg.append("<br>");
				errMsg.append(e);
				e.printStackTrace();
			}
		}

		for (Person p : ad.people) {
			try {
				if (p.checkSend(lastFlip, now))
					sentCount++;
			} catch (MessagingException e) {
				errCount++;
				errMsg.append("<br>");
				errMsg.append(e);
				e.printStackTrace();
			}
		}

		ad.doCron(now);
		String result = String.format("Cron performed, %d emails sent, %d errors.%s", sentCount, errCount, errMsg);
		System.out.println(result);
		return result;
	}

	private String getReset () {
		return "<html><head><title>Reset DMS</title><head><body><p>Enter password:</p><form action=\"/reset\" method=\"post\"><input type=\"password\" name=\"pswd\"><input type=\"submit\"></form></body></html>";
	}

	private String getCatastrophe () {
		return "<html><head><title>Reset DMS</title><head><body><p>Enter password:</p><form action=\"/catastrophe\" method=\"post\"><input type=\"password\" name=\"pswd\"><input type=\"submit\"></form></body></html>";
	}

	private String postReset (String pswd) {
		AppData ad = AppData.retrieve();

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			pswd += ad.salt;
			String sc = printHexBinary(md.digest(pswd.getBytes()));
			System.out.println(sc);
			System.out.println(sc.length());

			if(ad.doFlip(sc)) {
				return "<html><head><title>Reset DMS Successful</title><head><body>Reset DMS Successful</body></html>";
			}
			return "<html><head><title>Reset DMS Failed</title><head><body>Reset DMS failed</body></html>";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	private void postCatastrophe (String pswd, HttpServletResponse response) throws IOException {
		AppData ad = AppData.retrieve();

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			pswd += ad.salt;
			String sc = printHexBinary(md.digest(pswd.getBytes()));
			System.out.println(sc);
			System.out.println(sc.length());
			
			String path = ad.people.stream()
					     .filter(p -> sc.equals(p.pswd))
					     .findFirst()
					     .map(p -> p.path)
					     .orElse(null);
			if (path == null)
				response.getWriter().write("<html><head><title>Error</title><head><body>Wrong password</body></html>");
			else
			response.sendRedirect("/docs/" + path + ".pdf");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			response.getWriter().write(e.toString());
		}
	}
}
