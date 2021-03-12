package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class FASTASubsequenceFileProcessor {
	
	String seq = "";
	
	public void readFile(String file, int startIndex, int endIndex, int lineLength)
	{ 
		seq = "";
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String line = reader.readLine();
			if (ProjectConstants.REF_FROM_JAVA) {				
				processLine(line, startIndex, endIndex, lineLength);
			}			
			while (line != null) {
				line = reader.readLine();
				processLine(line, startIndex, endIndex, lineLength);
			}
			fis.close();
			//System.out.println(seq);
		} catch (FileNotFoundException e) {
			System.out.println("Fasta subsequence " + file + " file not found");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File error");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void processLine(String line, int startIndex, int endIndex, int lineLength) {
		if (line != null) {
			seq += line.toUpperCase();
		}
	}
	
	public String subsequence(int startIndex, int endIndex, int lineLength) {
		int start = startIndex%lineLength;
		int end = endIndex - startIndex*lineLength;
		return seq.substring(start, end);
		
	}
	
	public static void main(String args[]) {
//		String path = "E:/bear/";
//		String chrName = "NW_007907148.1";
//		
//		int startIndex = 11435089;
//		int endIndex = 11435889;
//		
		int lineLength = 60;
		int endIndex = 2279010;
		
		//File file = new File("NW_007907018.1_1062248-1063468.fa");
		File file = new File("NW_007907020.1_2278115-2279010.fa");
		
		FASTASubsequenceFileProcessor reader = new FASTASubsequenceFileProcessor();
		reader.readFile(file.getAbsolutePath(), 0, endIndex, lineLength);
//		File file = new File(path + chrName + "_" + startIndex + "-" + endIndex + ".fa");
//		reader.readFile(file.getAbsolutePath(), startIndex, endIndex, lineLength);
		//System.out.println(reader.seq);
		System.out.println(reader.seq.substring(1));

	}

}
