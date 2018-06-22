package io.github.teonistor.dms;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity public class AppData {
	
	@Id long id;
	
	public int u;

	public AppData() {
		id=1;
		
		u=0;
	}

}
