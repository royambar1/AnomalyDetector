package test;

import java.util.ArrayList;

import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;
	
	public CLI(DefaultIO dio) {
		this.dio=dio;
		c=new Commands(dio); 
		commands=new ArrayList<>();
		commands.add(c.new UploadFile());
		commands.add(c.new AlgorithmSettings());
		commands.add(c.new DetectAnomalies());
		commands.add(c.new DisplayResults());
		commands.add(c.new UploadAnomaliesAndAnalyzeResults());
		commands.add(c.new Exit());
		// example: commands.add(c.new ExampleCommand());
		// implement
	}
	
	public void start() {

		while(true) {
			dio.write("Welcome to the Anomaly Detection Server.\n");
			dio.write("Please choose an option:\n" +
					"1. upload a time series csv file\n" +
					"2. algorithm settings\n" +
					"3. detect anomalies\n" +
					"4. display results\n" +
					"5. upload anomalies and analyze results\n" +
					"6. exit\n");
			String userInput = dio.readText();
			int commandNum = Integer.parseInt(userInput);
			if(commands.get(commandNum -1).description.equals("close"))
				break;
			if(commandNum > 0 || commandNum < 7)
				commands.get(commandNum -1).execute();
		}
	}
}
