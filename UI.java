import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Part of Project 3 for CS 241 Winter 2018
 */

/**
 * This class represents the user interface that is to be used in the program.
 * Objects of this class are instantiated with a prompt message, which is the main
 * message to be used regularly to prompt the user. The main functionality of this
 * class is to add options into the user interface in order to set up what this class
 * is looking for when receiving input from the user.
 * @author Joel Tengco
 *
 */
class UI {
	private String promptMessage;
	private ArrayList<Option> options;
	private Scanner input;
	private StringTokenizer tokenizer;
	
	/**
	 * Constructs a new <code>UI</code> object with the given prompt message.
	 * @param promptMessage
	 */
	public UI(String promptMessage) {
		this.promptMessage = promptMessage;
		input = new Scanner(System.in);
		options = new ArrayList<Option>();
	}
	
	/**
	 * Adds a new option to this user interface. Take caution that duplicate
	 * options are allowed and may cause the interface to malfunction. Duplicate
	 * options are defined as two options that have the same option letter.
	 * @param optLetter The character to represent this option.
	 * @param description A string that describes what this option pertains to.
	 */
	public void addOption(char optLetter, String description) {
		for(Option option : options)
			if(Character.compare(option.optLetter, Character.toUpperCase(optLetter)) == 0) {
				System.err.println("Option letter " + optLetter + " already exists as an option. Duplicates are not allowed.");
				return;
			}
		
		options.add(new Option(optLetter, description));
	}
	
	/**
	 * Prints out the prompt message that this user interface was first instantiated with.
	 */
	public void promptUser() {
		System.out.print(promptMessage + " ");
	}
	
	/**
	 * Prints out all the options available to the user along with their descriptions.
	 */
	public void printHelp() {
		for(Option opt : options)
			System.out.println("  " + opt.toString());
	}
	
	/**
	 * Prints out the given prompt to the user.
	 * @param output The prompt to output to the user.
	 */
	public void printPrompt(String output) {
        System.out.print(output + " ");
	}
	
	/**
	 * Prints the given string to the screen.
	 * @param output The string to print out to the screen.
	 */
	public void printToScreen(String output) {
		System.out.println(output);
	}
	
	/**
	 * Prints the error message to the screen.
	 * @param message An error to print out to the screen.
	 */
	public void printError(String message) {
		System.err.println(message);
	}
	
	/**
	 * Gets a command from the user. This method will appropriately print out
	 * error messages for invalid inputs from the user until a valid one is given.
	 * @return One of characters that represent the options in this user interface, in lower case.
	 */
	public char getUserCommand() {
		String rawInput;
		String command;
		
		do {
			try {
				rawInput = input.nextLine();
				tokenizer = new StringTokenizer(rawInput);
				command = tokenizer.nextToken();
				
				if(command.length() > 1)
					throw new IllegalArgumentException("Invalid command given.");
				
				command = command.toLowerCase();
				
				for(Option opt : options)
					if(command.compareToIgnoreCase(opt.getOptLetter()) == 0)
						return command.charAt(0);
				
				throw new IllegalArgumentException("Command given does not match any of the options.");
			} catch(NoSuchElementException nsee) {
				System.err.println("Please enter a command.");
			} catch(IllegalArgumentException iae) {
				System.err.println(iae.getMessage());
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
		} while(true);
	}
	
	/**
	 * Gets inputs from the user, according to the given number of inputs needed. For example, if the number
	 * of inputs needed is 1, then this interface will keep prompting the user for a valid input until one
	 * single token is given. An empty string is returned when the number of inputs needed is specified as zero.
	 * @param inputsNeeded Number of inputs (tokens) needed from the user.
	 * @return An empty string for zero inputs needed, otherwise the user's raw input.
	 */
	public String getUserInput(int inputsNeeded) {
		String rawInput;
		
		if(inputsNeeded == 0)
			return "";
		
		do {
			try {
				rawInput = input.nextLine();
				tokenizer = new StringTokenizer(rawInput);
				if(tokenizer.countTokens() == 0)
					throw new IllegalArgumentException("Please enter an input.");
				if(tokenizer.countTokens() != inputsNeeded)
					throw new IllegalArgumentException("Invalid number of inputs.");
				return rawInput;
			} catch(IllegalArgumentException iae) {
				System.err.println(iae.getMessage());
			}
		} while(true);
	}
	
	/**
	 * This class represents an option in this user interface. It simply has an option letter
	 * to represent it, along with a description to describe its purpose.
	 * @author Joel Tengco
	 *
	 */
	private class Option {
		private char optLetter;
		private String description;
		
		public Option(char optLetter, String description) {
			this.optLetter = Character.toUpperCase(optLetter);
			this.description = description;
		}
		
		public String getOptLetter() {
			return Character.toString(optLetter);
		}
		
		@Override
		public String toString() {
			return Character.toString(optLetter) + " " + description;
		}
	}
}
