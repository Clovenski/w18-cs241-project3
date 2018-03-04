import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Project3 {
	private Digraph<City> graph;
	private UI ui;
	
	public Project3() {}
	
	private void initializeData() throws FileNotFoundException, IOException {
		// file input variables
		BufferedReader br;
		FileReader fr;
		File file;
		// data input variables
		String inputString;
		StringTokenizer tokenizer;
		String temp = null;
		// graph construction variables
		ArrayList<City> initialCities;
		int cityNumber;
		String cityCode;
		String cityName;
		int cityPop;
		int cityElev;
		
		// read data from file "city.dat"
		file = new File("city.dat");
		if(!file.exists())
			throw new FileNotFoundException("Error: city.dat file not found");
		
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		initialCities = new ArrayList<City>();
		
		inputString = br.readLine();
		while(inputString != null && !inputString.equals("")) {
            tokenizer = new StringTokenizer(inputString);
            
            cityNumber = Integer.parseInt(tokenizer.nextToken());
            cityCode = tokenizer.nextToken();
            cityName = tokenizer.nextToken();
            
            /*
             * keep attempting to parse the next token;
             * 	if an exception is thrown, then the next token
             * 	must have been part of the city name
             */
            do {
            	try {
            		temp = tokenizer.nextToken();
            		cityPop = Integer.parseInt(temp);
            		break;
            	} catch(NumberFormatException nfe) {
            		cityName = cityName + " " + temp;
            	}
            } while(true);
            
            cityElev = Integer.parseInt(tokenizer.nextToken());
            initialCities.add(new City(cityNumber, cityCode, cityName, cityPop, cityElev));
            inputString = br.readLine();
		}
		
		// construct graph containing City objects as its vertices
		graph = new Digraph<City>(initialCities.toArray(new City[initialCities.size()]));
		
		br.close();
		// end read data from file "city.dat"
		
		
		// read data from file "road.dat"
		file = new File("road.dat");
		if(!file.exists())
            throw new FileNotFoundException("Error: road.dat file not found");
		
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		
		inputString = br.readLine();
		while(inputString != null && !inputString.equals("")) {
            tokenizer = new StringTokenizer(inputString);
            graph.addEdge(Integer.parseInt(tokenizer.nextToken()) - 1,		// source vertex index
                            Integer.parseInt(tokenizer.nextToken()) - 1,	// target vertex index
                            Integer.parseInt(tokenizer.nextToken()));		// weight for that edge
            
            inputString = br.readLine();
		}
		
		br.close();
		// end read data from file "road.dat"
	}
	
	private void initializeUI() {
		// build the user interface with the following prompt message and options
		ui = new UI("Command?");
		ui.addOption('Q', "Query the city information by entering the city code.");
		ui.addOption('D', "Find the minimum distance between two cities.");
		ui.addOption('I', "Insert a road by entering two city codes and distance.");
		ui.addOption('R', "Remove an existing road by entering two city codes.");
		ui.addOption('H', "Display this message.");
		ui.addOption('E', "Exit.");
	}
	
	private void execQCommand() {
		String userInput;
		StringTokenizer tokenizer;
		String targetCode;
		int vertexIndex;
		
		ui.printPrompt("City Code:");
		userInput = ui.getUserInput(1);
		tokenizer = new StringTokenizer(userInput);
		targetCode = tokenizer.nextToken();
		vertexIndex = graph.getVertexIndex(new City(targetCode));
		
		if(vertexIndex == -1)
			ui.printError("City code " + targetCode.toUpperCase() + " does not exist in this graph.");
		else
			ui.printToScreen(graph.getVertex(vertexIndex).toString());
	}
	
	private void execDCommand() {
		// user input variables
		String userInput;
		StringTokenizer tokenizer;
		String cityCode1;
		String cityCode2;
		int sourceIndex;
		int targetIndex;
		// graph info variables
		int[] pathInfo;
		String[] pathCityCodes;
		String result;
		
		ui.printPrompt("City codes:");
		userInput = ui.getUserInput(2);
		tokenizer = new StringTokenizer(userInput);
		cityCode1 = tokenizer.nextToken();
		cityCode2 = tokenizer.nextToken();
		
		sourceIndex = graph.getVertexIndex(new City(cityCode1));
		targetIndex = graph.getVertexIndex(new City(cityCode2));
		
		// true if city code does not exist in this graph
		if(sourceIndex == -1) {
			ui.printError("City code " + cityCode1.toUpperCase() + " does not exist in this graph.");
			return;
		}
		if(targetIndex == -1) {
			ui.printError("City code " + cityCode2.toUpperCase() + " does not exist in this graph.");
			return;
		}
		
		// get an integer array containing the shortest path length along with the path in vertex indices
		pathInfo = graph.getShortestPath(sourceIndex, targetIndex);
		pathCityCodes = new String[pathInfo.length - 1];
		for(int i = 1; i < pathInfo.length; i++)
			pathCityCodes[i - 1] = graph.getVertex(pathInfo[i]).getCityCode();
		
		result = String.format("The minimum distance between %s and %s is %d through the route: ",
								graph.getVertex(sourceIndex).getCityName(),
								graph.getVertex(targetIndex).getCityName(), pathInfo[0]);
		
		// concatenate the path sequence to the result string
		result = result.concat(pathCityCodes[0]);
		for(int i = 1; i < pathCityCodes.length; i++)
			result = result.concat(", " + pathCityCodes[i]);
		result = result.concat(".");
		
		// print the final resulting string to screen
		ui.printToScreen(result);
	}
	
	private void execICommand() {
		String userInput;
		StringTokenizer tokenizer;
		String cityCode1;
		String cityCode2;
		City sourceCity;
		City targetCity;
		int sourceIndex;
		int targetIndex;
		int distance = 0;
		
		ui.printPrompt("City codes and distance:");
		userInput = ui.getUserInput(3);
		tokenizer = new StringTokenizer(userInput);
		
		cityCode1 = tokenizer.nextToken();
		cityCode2 = tokenizer.nextToken();
		// ensure that the distance given is an integer and is positive, otherwise print the error to screen
		try {
			distance = Integer.parseInt(tokenizer.nextToken());
			if(distance < 0)
				throw new IllegalArgumentException("This graph does not support negative weights.");
			if(distance == 0)
				throw new IllegalArgumentException("Insertion of a road with zero distance is not allowed.");
		} catch(NumberFormatException nfe) {
			ui.printError("Please enter an integer for the distance.");
			return;
		} catch(Exception e) {
			ui.printError(e.getMessage());
			return;
		}
		sourceIndex = graph.getVertexIndex(new City(cityCode1));
		targetIndex = graph.getVertexIndex(new City(cityCode2));
		
		// check if the city codes do not exist in this graph
		if(sourceIndex == -1) {
			ui.printError("City code " + cityCode1.toUpperCase() + " does not exist in this graph.");
			return;
		}
		if(targetIndex == -1) {
			ui.printError("City code " + cityCode2.toUpperCase() + " does not exist in this graph.");
			return;
		}
		
		sourceCity = graph.getVertex(sourceIndex);
		targetCity = graph.getVertex(targetIndex);
		
		if(graph.getEdgeWeight(sourceIndex, targetIndex) != 0)
			ui.printError("There exists a road from " + sourceCity.getCityName() + " to " + targetCity.getCityName() + " already.");
		else {
			graph.addEdge(sourceIndex, targetIndex, distance);
			ui.printToScreen(String.format("You have inserted a road from %s to %s with a distance of %d.", sourceCity.getCityName(), targetCity.getCityName(), distance));
		}
	}
	
	private void execRCommand() {
		String userInput;
		StringTokenizer tokenizer;
		String cityCode1;
		String cityCode2;
		City sourceCity;
		City targetCity;
		int sourceIndex;
		int targetIndex;
		
		ui.printPrompt("City codes:");
		userInput = ui.getUserInput(2);
		tokenizer = new StringTokenizer(userInput);
		
		cityCode1 = tokenizer.nextToken();
		cityCode2 = tokenizer.nextToken();
		
		sourceIndex = graph.getVertexIndex(new City(cityCode1));
		targetIndex = graph.getVertexIndex(new City(cityCode2));
		
		// check if city codes do not exist in this graph
		if(sourceIndex == -1) {
			ui.printError("City code " + cityCode1.toUpperCase() + " does not exist in this graph.");
			return;
		}
		if(targetIndex == -1) {
			ui.printError("City code " + cityCode2.toUpperCase() + " does not exist in this graph.");
			return;
		}
		
		sourceCity = graph.getVertex(sourceIndex);
		targetCity = graph.getVertex(targetIndex);
		
		if(graph.getEdgeWeight(sourceIndex, targetIndex) == 0)
			ui.printError(String.format("The road from %s to %s does not exist.", sourceCity.getCityName(), targetCity.getCityName()));
		else {
			graph.removeEdge(sourceIndex, targetIndex);
			ui.printToScreen(String.format("You have removed a road from %s to %s.", sourceCity.getCityName(), targetCity.getCityName()));
		}
	}
	
	public void start() {
		try {
			initializeData();	// read input files and store its data
		} catch(FileNotFoundException fnfe) {
			System.err.println(fnfe.getMessage());
			System.exit(1);
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
		initializeUI();			// build the user interface
		
		// main loop, will only return once the user opts to exit with 'e' or 'E'
		do {
			ui.promptUser();
			switch(ui.getUserCommand()) {
			// Query city info
			case 'q':
				execQCommand();
				break;
			// find shortest path
			case 'd':
				execDCommand();
				break;
			// insert a road into the graph
			case 'i':
				execICommand();
				break;
			// remove a road from the graph
			case 'r':
				execRCommand();
				break;
			// display the help message
			case 'h':
				ui.printHelp();
				break;
			// exit the program
			case 'e':
				return;
			}
		} while(true);
	}

	public static void main(String[] args) {
		Project3 program = new Project3();
		program.start();
	}
}
