package io.github.teonistor.dms;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.googlecode.objectify.ObjectifyService;

@WebListener public class Bootstrapper implements ServletContextListener {
	@Override public void contextInitialized(ServletContextEvent event) {
		ObjectifyService.init();
		ObjectifyService.register(AppData.class);
		ObjectifyService.register(Person.class);
	}

	@Override public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
}
