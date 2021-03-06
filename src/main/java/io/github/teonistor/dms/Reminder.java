package io.github.teonistor.dms;

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

@Entity public class Reminder {

	@Id long id;
	long delay;
	boolean waiting;

	/**
	 * Create a dummy Reminder
	 */
	public Reminder() {
		id = 31;
		delay = 86427531;
		waiting = true;
	}

	/**
	 * Send an email for this reminder if time since last flip exceeds its defined delay
	 * @param lastFlip Last flip of the switch
	 * @param now Present moment
	 * @return true if and only if an email was sent
	 * @throws MessagingException if an email was supposed to be sent but failed
	 */
	public boolean checkSend(long lastFlip, long now) throws MessagingException {
		if (waiting && now - lastFlip > delay) {
			AppData ad = AppData.retrieve();
			
			Message msg = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
			msg.setFrom(new InternetAddress(ad.masterEmailFrom));
			msg.addRecipient(TO, new InternetAddress(ad.masterEmailTo));
			msg.setSubject("Dead Man's Switch");
			msg.setText(ad.reminderMsg);
			Transport.send(msg);

			waiting = false;
			return true;
		}
		return false;
	}
}
