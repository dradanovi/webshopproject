package hr.java.web.radanovic.webShop.exception;

public class NotEnoughProducts extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4558735173224300134L;

	public NotEnoughProducts() {
		super();
	}

	public NotEnoughProducts(String message) {
		super(message);
	}

	public NotEnoughProducts(Throwable cause) {
		super(cause);
	}

	public NotEnoughProducts(String message, Throwable cause) {
		super(message, cause);
	}
}
