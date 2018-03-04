/**
 * Part of Project 3 for CS 241 Winter 2018
 */

/**
 * This class will represent the properties of a city, including its city number, code, name,
 * population and elevation.
 * @author Joel Tengco
 *
 */
class City implements Comparable<City> {
	private int cityNumber;
	private String cityCode;
	private String cityName;
	private int population;
	private int elevation;
	
	/**
	 * Constructs a <code>City</code> object with a zero value for this city's number, population and elevation,
	 * with the code that is given, and a name of "unnamed_city".
	 * @param code A string to represent this city's code.
	 */
	public City(String code) {
		cityNumber = 0;
		cityCode = code;
		cityName = "unnamed_city";
		population = 0;
		elevation = 0;
	}
	
	/**
	 * Constructs a <code>City</code> object with the given values.
	 * @param number An integer to represent this city's number.
	 * @param code A string to represent this city's code.
	 * @param name A string representing this city's full name.
	 * @param pop The population of this city.
	 * @param elev The elevation of this city.
	 */
	public City(int number, String code, String name, int pop, int elev) {
		cityNumber = number;
		cityCode = code;
		cityName = name;
		population = pop;
		elevation = elev;
	}
	
	/**
	 * Gets this city's number.
	 * @return This city's number as an integer value.
	 */
	public int getCityNumber() {
		return cityNumber;
	}
	
	/**
	 * Gets this city's code value.
	 * @return This city's code as a string.
	 */
	public String getCityCode() {
		return cityCode;
	}
	
	/**
	 * Gets this city's full name.
	 * @return This city's full name as a string.
	 */
	public String getCityName() {
		return cityName;
	}
	
	/**
	 * Gets this city's population.
	 * @return This city's population as an integer value.
	 */
	public int getCityPopulation() {
		return population;
	}
	
	/**
	 * Gets this city's elevation.
	 * @return This city's elevation as an integer value.
	 */
	public int getCityElevation() {
		return elevation;
	}
	
	/**
	 * Compares this city with the given <code>City</code> object parameter.
	 * Two <code>City</code> objects are compared using their respective city codes.
	 * Note that these two city codes are compared while ignoring case.
	 */
	@Override
	public int compareTo(City o) {
		return cityCode.compareToIgnoreCase(o.cityCode);
	}
	
	/**
	 * Returns a string formatted as: "<i>number code name population elevation</i>". 
	 */
	@Override
	public String toString() {
		return String.format("%d %s %s %d %d", cityNumber, cityCode, cityName, population, elevation);
	}
}
