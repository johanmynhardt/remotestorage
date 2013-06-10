package za.co.johanmynhardt.remotestorage.model;

/**
 * @author Johan Mynhardt
 */
public interface Document {
	/*
	 *     For a document, the server stores, and should be able to produce:

	       * content type
	       * content
	       * current version
	*/
	public String getName();
	public String getContentType();
	public String getContent();
	public String getCurrentVersion();
}
