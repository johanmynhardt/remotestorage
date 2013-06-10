package za.co.johanmynhardt.remotestorage.exception;

/**
 * @author Johan Mynhardt
 */
public class RemoteStorageException extends Exception {
	public RemoteStorageException() {
		super();
	}

	public RemoteStorageException(String message) {
		super(message);
	}

	public RemoteStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemoteStorageException(Throwable cause) {
		super(cause);
	}
}
