package test;

import java.io.*;
import java.util.Scanner;

import test.Commands.DefaultIO;

public class FileIO implements DefaultIO{

	Scanner in;
	PrintWriter out;
	public FileIO(String inputFileName,String outputFileName) {
		try {
			in=new Scanner(new FileReader(inputFileName));
			out=new PrintWriter(new FileWriter(outputFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String readText() {
		return in.nextLine();
	}

	@Override
	public void write(String text) {
		out.flush();
		out.print(text);
	}

	@Override
	public float readVal() {
		return in.nextFloat();
	}

	@Override
	public void write(float val) {
		out.print(val);
	}

	@Override
	public void readNewFile(String fileName) {
		out.flush();
		File file = new File(fileName);
		try {
			// create FileWriter object with file as parameter
			PrintWriter out=new PrintWriter(file);
			while(in.hasNext()) {
				String line = in.nextLine();
				if (line.contains("done"))
					break;
				out.println(line);
			}
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void close() {
		in.close();
		out.close();
	}
}
