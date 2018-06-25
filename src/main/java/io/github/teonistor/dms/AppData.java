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

	/**
	 * Create a quasi-empty AppData instance (instantiate lists)
	 */
	public AppData() {
		id=1;
		crons = new LinkedList<>();
		flips = new LinkedList<>();
		people = new ArrayList<>();
		reminders = new ArrayList<>();
	}

	/**
	 * Retrieve the Application Data instance from Datastore (first instance, but there should only be one)
	 * @return the instance
	 */
	public static AppData retrieve() {
		AppData ad = ofy().load().type(AppData.class).first().now();
		if (ad == null) {
			System.out.println("Create fresh data");
			ad = new AppData();
		}
		return ad;
	}
	
	/**
	 * Add the present moment to the list of times the cron job was run, capping the total count to MAX_HIST, then save application data to Datastore
	 * @param now Unixtime (in milliseconds) to be considered the present moment
	 */
	public void doCron(long now) {
		crons.add(now);
		if (crons.size() > MAX_HIST)
			crons.remove(0);
		save();
	}
	
	/**
	 * Add the present moment to the list of times the cron job was run, capping the total count to MAX_HIST, then save application data to Datastore
	 */
	public void doCron() {
		doCron(System.currentTimeMillis());
	}
	
	/**
	 * Flip the switch if password is correct
	 * @param pswd Given password, hashed
	 * @param now Unixtime (in milliseconds) to be considered the present moment
	 * @return true if and only if the flip was performed
	 */
	public boolean doFlip(String pswd, long now) {
		if (pswd.equals(masterPswd)) {
			flips.add(now);
			if (flips.size() > MAX_HIST)
				flips.remove(0);
			for (Reminder r : reminders)
				r.waiting = true;
			save();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Flip the switch if password is correct
	 * @param pswd Given password, hashed
	 * @return true if and only if the flip was performed
	 */
	public boolean doFlip(String pswd) {
		return doFlip(pswd, System.currentTimeMillis());
	}
	
	/**
	 * Save this instance to Datastore
	 */
	void save() {
		ofy().save().entity(this).now();
	}
}
