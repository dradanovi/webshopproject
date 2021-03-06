package hr.java.web.radanovic.webShop.enums;

/**
 * defines the months and their integer values
 * 
 * @author demoo
 *
 */
public enum Months {
	January(1), February(2), March(3), April(4), May(5), June(6), July(7), August(8), September(9), October(10),
	November(11), December(12);

	private final int value;

	private Months(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
