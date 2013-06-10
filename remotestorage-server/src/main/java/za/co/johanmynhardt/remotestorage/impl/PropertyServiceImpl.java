package za.co.johanmynhardt.remotestorage.impl;

import java.io.IOException;
import java.util.Properties;

import za.co.johanmynhardt.remotestorage.service.PropertyService;

/**
 * @author Johan Mynhardt
 */
public class PropertyServiceImpl implements PropertyService {

	private static Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(PropertyServiceImpl.class.getResourceAsStream("/remotestorage.properties"));
			System.out.println("Properties loaded: " + properties);
		} catch (IOException e) {
			System.err.println("Could not load properties:");
			e.printStackTrace();
		}
		System.err.println("PropertyServiceImpl constructed");
	}

	@Override
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
}
