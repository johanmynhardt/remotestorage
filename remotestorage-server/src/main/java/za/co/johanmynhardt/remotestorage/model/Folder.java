package za.co.johanmynhardt.remotestorage.model;

import java.util.List;

/**
 * @author Johan Mynhardt
 */
public interface Folder {
	List<Folder> getFolders();

	List<Document> getDocuments();

	/*
	* item name
    * item type (folder or document)
    * current version
	 */

	public String getName();
	public String getType();
	public String getCurrentVersion();
}
