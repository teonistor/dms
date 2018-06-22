/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.teonistor.dms;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START enqueue]
// The Enqueue servlet should be mapped to the "/enqueue" URL.
// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(
		name = "TaskEnque",
		description = "taskqueue: Enqueue a job with a key",
		urlPatterns = "/taskqueues/enqueue"
		)
public class Enqueue extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			
			
		String key = request.getParameter("key");
		System.out.println(key);
		
		
		AppData ad = ofy().load().type(AppData.class).first().now();
		if (ad == null) {
			System.out.println("Create fresh data");
			ad = new AppData();
		}
		
//			javax.mail.Message msg = new Message(
//					"nistorteodor6a@gmail.com",
//					"teo.g.nistor@gmail.com",
//					"Test",
//					"Testing...");
			Message msg = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
			msg.setFrom(new InternetAddress("nistorteodor6a@gmail.com"));
			msg.addRecipient(RecipientType.TO, new InternetAddress("nistorteodor6a+rcv@gmail.com"));
			msg.setSubject("Test");
			msg.setText("Testing...");
			
			Transport.send(msg);
		

		// Add the task to the default queue.
		// [START addQueue]
//		Queue queue = QueueFactory.getDefaultQueue();
//		queue.add(TaskOptions.Builder.withUrl("/worker").param("key", key));
//		// [END addQueue]
//
//		response.sendRedirect("/");
		response.getWriter().write("ad = " + ad.u);
		ad.u++;
		ofy().save().entity(ad).now();
		
		} catch (Exception e) {
			StringWriter st = new StringWriter();
			e.printStackTrace(new PrintWriter(st));
			Worker.log.log(Level.SEVERE, st.toString());
			response.getWriter().write(st.toString());
		}
	}
}
// [END enqueue]
