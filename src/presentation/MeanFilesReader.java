package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class MeanFilesReader {
	
	public LinkedHashMap<String, Interval> sampleIntervalMap = new LinkedHashMap<String, Interval>();
	
	public void readFile(String file, String sample)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String line = reader.readLine();
			processLine(line, sample);
			while (line != null) {
				line = reader.readLine();
				processLine(line, sample);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void processLine(String line, String sample) {
		try {
			String[] splitLine = line.split(" ");
			Interval interval = new Interval(Integer.parseInt(splitLine[2]), Integer.parseInt(splitLine[3]));
			sampleIntervalMap.put(sample, interval);
		} catch (Exception e) {

		}
	}
	
	public void processFiles(String directory, String samplePrefix, String sampleSuffix, int firstID, int lastID) {
		for (int i = firstID; i <= lastID; i++) {
			File f = new File(directory + samplePrefix + i + sampleSuffix);
			//System.out.println("mean file " + f.getAbsolutePath());
			if (f.exists()) {
				readFile(f.getAbsolutePath(), samplePrefix + i);
			}
		}
	}
	
	public static void main(String args[]) {
		String directory = "etc/bear/mean_files/";
		String samplePrefix = "SAMN0";
		String sampleSuffix = "_sorted.bam.mean";
		// polar
		int firstSampleIDPolar = 2261802;
		int lastSampleIDPolar = 2261880;
		// brown
		int firstSampleIDBrown = 2256321;
		int lastSampleIDBrown = 2256322;
		
		MeanFilesReader reader = new MeanFilesReader();
		reader.processFiles(directory, samplePrefix, sampleSuffix, firstSampleIDPolar, lastSampleIDPolar);
		reader.processFiles(directory, samplePrefix, sampleSuffix, firstSampleIDBrown, lastSampleIDBrown);
//		MeanFilesReader reader = new MeanFilesReader();
//		for (int i = firstSampleIDPolar; i <= lastSampleIDPolar; i++) {
//			File f = new File(directory + samplePrefix + i + sampleSuffix);
//			if (f.exists()) {
//				reader.readFile(f.getAbsolutePath(), samplePrefix + i);
//			}
//		}
//		for (int i = firstSampleIDBrown; i <= lastSampleIDBrown; i++) {
//			File f = new File(directory + samplePrefix + i + sampleSuffix);
//			if (f.exists()) {
//				reader.readFile(f.getAbsolutePath(), samplePrefix + i);
//			}
//		}
		
		System.out.println(reader.sampleIntervalMap);
	}

}





