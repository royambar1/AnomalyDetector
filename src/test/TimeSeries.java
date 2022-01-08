package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TimeSeries {
	HashMap<String, ArrayList<Float>> csvTable = new HashMap<>();
	String[] headers;


	public TimeSeries(String csvFileName) {

		Scanner scanFile;
		//String lineToScan = null;
		String[] buffer = null;

		try {
			scanFile = new Scanner(new File(csvFileName));
			//Headers
			headers = scanFile.next().split(",");
			for (int i = 0; i < headers.length; i++) {
				ArrayList<Float> v = new ArrayList<Float>();
				csvTable.put(headers[i],v);
			}
			//Data (numbers)
			while(scanFile.hasNext())
			{
				buffer = scanFile.next().split(",");
				for (int i = 0; i < headers.length; i++) {
					csvTable.get(headers[i]).add(Float.parseFloat(buffer[i]));
				}

			}
			scanFile.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

