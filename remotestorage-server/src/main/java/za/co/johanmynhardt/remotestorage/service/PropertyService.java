package za.co.johanmynhardt.remotestorage.service;

/**
 * @author Johan Mynhardt
 */
public interface PropertyService {
	public String getProperty(String key);

	public String getProperty(String key, String defaultValue);
}
