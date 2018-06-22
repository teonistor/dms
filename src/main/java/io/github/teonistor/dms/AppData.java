package io.github.teonistor.dms;

import java.util.LinkedList;
import java.util.List;
import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity public class AppData {
	static final int MAX_HIST = 20;
	
	@Id long id;
	
	List<Long> crons, flips;
	String masterPswd;

	public AppData() {
		id=1;
		crons = new LinkedList<>();
		flips = new LinkedList<>();
		masterPswd = "1234"; // TODO
	}

	public static AppData retrieve() {
		AppData ad = ofy().load().type(AppData.class).first().now();
		if (ad == null) {
			System.out.println("Create fresh data");
			ad = new AppData();
		}
		return ad;
	}
	
	public boolean doCron() {
		crons.add(System.currentTimeMillis());
		if (crons.size() > MAX_HIST)
			crons.remove(0);
		save();
		
		return false; // TODO
	}
	
	public boolean doFlip(String pswd) {
		if (pswd.equals(masterPswd)) {
			flips.add(System.currentTimeMillis());
			if (flips.size() > MAX_HIST)
				flips.remove(0);
			save();
			return true;
		}
		
		return false;
	}
	
	private void save() {
		ofy().save().entity(this).now();
	}
}
