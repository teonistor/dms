package io.github.teonistor.dms;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity public class AppData {
	static final int MAX_HIST = 20;
	
	@Id long id;
	
	List<Long> crons, flips;
	List<Person> people;
	List<Reminder> reminders;
	String masterPswd, masterEmailFrom, masterEmailTo, salt, reminderMsg, peopleMsg;

	public AppData() {
		id=1;
		crons = new LinkedList<>();
		flips = new LinkedList<>();
		people = new ArrayList<>();
		reminders = new ArrayList<>();
	}

	public static AppData retrieve() {
		AppData ad = ofy().load().type(AppData.class).first().now();
		if (ad == null) {
			System.out.println("Create fresh data");
			ad = new AppData();
		}
		return ad;
	}
	
	public void doCron(long now) {
		crons.add(now);
		if (crons.size() > MAX_HIST)
			crons.remove(0);
		save();
	}
	
	public void doCron() {
		doCron(System.currentTimeMillis());
	}
	
	public boolean doFlip(String pswd) {
		if (pswd.equals(masterPswd)) {
			flips.add(System.currentTimeMillis());
			if (flips.size() > MAX_HIST)
				flips.remove(0);
			for (Reminder r : reminders)
				r.waiting = true;
			save();
			return true;
		}
		
		return false;
	}
	
	void save() {
		ofy().save().entity(this).now();
	}
}
