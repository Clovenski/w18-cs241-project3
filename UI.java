import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

class UI {
	private String promptMessage;
	private ArrayList<Option> options;
	private Scanner input;
	private StringTokenizer tokenizer;
	
	public UI(String promptMessage) {
		this.promptMessage = promptMessage;
		input = new Scanner(System.in);
		options = new ArrayList<Option>();
	}
	
	public void addOption(char optLetter, String description) {
		options.add(new Option(optLetter, description));
	}
	
	public void promptUser() {
		System.out.print(promptMessage + " ");
	}
	
	public void printHelp() {
		for(Option opt : options)
			System.out.println("  " + opt.toString());
	}
	
	public void printPrompt(String output) {
        System.out.print(output + " ");
	}
	
	public void printToScreen(String output) {
		System.out.println(output);
	}
	
	public void printError(String message) {
		System.err.println(message);
	}
	
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
	
	public String getUserInput(int inputsNeeded) {
		String rawInput;
		
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
