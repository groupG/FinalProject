/**
 * 
 * Eigene Exception-Class
 *
 */

package utils;

public class NotExistInDatabaseException extends Exception {
	
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
