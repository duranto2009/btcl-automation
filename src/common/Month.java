package common;

public enum Month {
	JANUARY(0,"January"), FEBRUARY(1, "February"), MARCH(2, "March"), APRIL(3, "April"), 
	MAY(4, "May"), JUNE(5, "June"), JULY(6, "July"), AUGUST(7, "August"), SEPTEMBER(8, "September"), 
	OCTOBER(9, "October"), NOVEMBER(10, "November"), DECEMBER(11, "December");
	
	Month(int id, String monthName) {
		this.monthName = monthName;
		this.id = id;
	}
	private String monthName;
	private int id;
	public String getMonthName() {
		return monthName;
	}
	public int getId() {
		return id;
	}
	public static String getMonthNameById( int id) {
		String monthName = "";
		for(Month month : Month.values()) {
			if(month.getId() == id) {
				monthName = month.getMonthName();
				break;
			}
		}
		return monthName;
	}
	
}
	