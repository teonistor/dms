package io.github.teonistor.dms;

import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity public class Person {

	@Id long id;
	String pswd, email;
	long delay;
	boolean waiting;

	/**
	 * Create a dummy Person
	 */
	public Person() {
		id = 2;
		pswd = email = "NIL";
		delay = Long.MAX_VALUE;
		waiting = true;
	}

	/**
	 * Send an email to this person if time since last flip exceeds their defined delay
	 * @param lastFlip Last flip of the switch
	 * @param now Present moment
	 * @return true if and only if an email was sent
	 * @throws MessagingException if an email was supposed to be sent but failed
	 */
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
