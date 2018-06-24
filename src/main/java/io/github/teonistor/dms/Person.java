package io.github.teonistor.dms;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Properties;

import javax.mail.Message;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Message.RecipientType.CC;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity public class Person {

	@Id long id;
	String pswd, path, email;
	long delay;
	boolean waiting;

	public Person() {
		id = 2;
		pswd = path = email = "NIL";
		delay = Long.MAX_VALUE;
		waiting = true;
	}

	public boolean checkSend(long lastFlip, long now) throws MessagingException {
		if (waiting && now - lastFlip > delay) {
			AppData ad = AppData.retrieve();
			//            javax.mail.Message msg = new Message(
			//                    "nistorteodor6a@gmail.com",
			//                    "teo.g.nistor@gmail.com",
			//                    "Test",
			//                    "Testing...");
			Message msg = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
			msg.setFrom(new InternetAddress(ad.masterEmailFrom));
			msg.addRecipient(TO, new InternetAddress(email));
			msg.addRecipient(CC, new InternetAddress(ad.masterEmailTo));
			msg.setSubject("Dead Man's Switch");
			msg.setText(ad.peopleMsg);
			Transport.send(msg);
			
			waiting = false;
			return true;
		}
		return false;
	}
}