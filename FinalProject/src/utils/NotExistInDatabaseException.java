package utils;

/**
 *
 * Eigene Exception-Class
 *
 */
public class NotExistInDatabaseException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -8361206910365083185L;

	public NotExistInDatabaseException( String reason ) {
		super(reason);
	}

	public String getMessage() {
		return super.getMessage();
	}

	public void printStackTrace() {
		super.printStackTrace();
	}
}
