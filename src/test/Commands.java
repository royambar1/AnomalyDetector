package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Commands {
	
	// Default IO interface
	public interface DefaultIO{
		public String readText();
		public void write(String text);
		public float readVal();
		public void write(float val);
		public void readNewFile(String fileName);
		// you may add default methods here
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;


	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here
	
	
	
	// the shared state of all commands
	private class SharedState{
		TimeSeries train, test;
		SimpleAnomalyDetector anomalyDetector = new SimpleAnomalyDetector();
		float threshold = (float) 0.90;
		List<AnomalyReport> anomalyList;
	}
	
	private  SharedState sharedState=new SharedState();


	// Command abstract class
	public abstract class Command{
		protected String description;
		
		public Command(String description) {
			this.description=description;
		}
		
		public abstract void execute();
	}


	// Command class's

	public class UploadFile extends Command{

		public UploadFile() {
			super("This command loads a file uploaded by a client");
		}
		@Override
		public void execute() {
			dio.write("Please upload your local train CSV file.\n");
			String trainFile = "anomalyTrain.csv";
			String testFile = "anomalyTest.csv";
			dio.readNewFile(trainFile);
			dio.write("upload complete.\nPlease upload your local test CSV file.\n");
			dio.readNewFile(testFile);
			dio.write("upload complete.\n");

			sharedState.train = new TimeSeries(trainFile);
			sharedState.test = new TimeSeries(testFile);
		}
	}

	public class AlgorithmSettings extends Command{
		public AlgorithmSettings() {
			super("This allows the client to change threshold");
		}
		@Override
		public void execute() {
			float threshold = sharedState.threshold;
			dio.write("The current correlation threshold is "+threshold+"\nType a new threshold\n");
			String userInput = dio.readText();
			float tempThreshold = Float.parseFloat(userInput);
			while(tempThreshold < 0 || tempThreshold > 1){
				dio.write("please choose a value between 0 and 1.");
				tempThreshold = dio.readVal();
			}
			sharedState.threshold = (float) tempThreshold;
		}
	}

	public class DetectAnomalies extends Command{

		public DetectAnomalies() {
			super("this is a command to detect anomalies");
		}

		@Override
		public void execute() {
			sharedState.anomalyDetector.learnNormal(sharedState.train);
			sharedState.anomalyList = sharedState.anomalyDetector.detect(sharedState.test);
			dio.write("anomaly detection complete.\n");

		}
	}

	public class DisplayResults extends Command{

		public DisplayResults() {
			super("this is a command that displays the results");
		}

		@Override
		public void execute() {
			for (AnomalyReport a: sharedState.anomalyList) {
				dio.write(a.timeStep+"\t"+a.description+"\n");
			}
		}
	}

	public class UploadAnomaliesAndAnalyzeResults extends Command{

		public UploadAnomaliesAndAnalyzeResults() {
			super("this is an example of command");
		}

		@Override
		public void execute() {
			dio.write(description);
		}
	}

	public class Exit extends Command{

		public Exit() {
			super("close");
		}

		@Override
		public void execute() {
			dio.write(description);
		}
	}
}

