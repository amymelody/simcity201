package simcity;

public enum Day {
	Sun, Mon, Tue, Wed, Thu, Fri, Sat;
	public Day next() {
		return this.ordinal() < Day.values().length - 1
				? Day.values()[this.ordinal() + 1]
				: Day.Sun;
	}
}
