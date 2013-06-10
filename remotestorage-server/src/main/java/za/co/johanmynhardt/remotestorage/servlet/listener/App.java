package za.co.johanmynhardt.remotestorage.servlet.listener;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class App implements ServletContextListener {

	private final Logger LOG = Logger.getLogger(App.class.getName());

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		LOG.info("Initialized.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		LOG.info("Destroyed.");
	}
}
