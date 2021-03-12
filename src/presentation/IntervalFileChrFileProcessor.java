package presentation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author James Kelley
 * Genome Navigator and Genome Navigator Data Processor code
 * - likely used by both
 */
public class IntervalFileChrFileProcessor {
	
	public LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap = new LinkedHashMap<String, ArrayList<Interval>>();
	public LinkedHashMap<String, ArrayList<Integer>> chrIntervalLinesMap = new LinkedHashMap<String, ArrayList<Integer>>();
	public ArrayList<String> largestIntervals = new ArrayList<String>();
	
	int count = 0;
	
	public void readFile(String file, int startCol, int endCol)
	{ 
		count = 0;
		chrIntervalsMap.clear();
		chrIntervalLinesMap.clear();
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, startCol, endCol);
			while (line != null) {
				line = reader.readLine();
				processLine(line, startCol, endCol);
			}
			fis.close();
//			System.out.println("largest intervals " + largestIntervals);
//			System.out.println("largest intervals size " + largestIntervals.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void processLine(String line, int startCol, int endCol) {
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			//System.out.println(line);
			if (line != null) {
				String[] splitLine = line.split("\t");
				String chr = splitLine[0];
				int start = Integer.parseInt(splitLine[startCol]);
				int end = Integer.parseInt(splitLine[endCol]); 
				Interval interval = new Interval(start, end);
				//System.out.println(start + "," + end + "," + i.size());
				//if (interval.size() <= Constants.MAX_INTERVAL_WIDTH) {
					if (chrIntervalsMap.get(chr) == null) {
						ArrayList<Interval> intervalList = new ArrayList<Interval>();
						intervalList.add(interval);
						chrIntervalsMap.put(chr, intervalList);
						// line numbers
						ArrayList<Integer> intervalLineList = new ArrayList<Integer>();
						intervalLineList.add(count);
						chrIntervalLinesMap.put(chr, intervalLineList);
					} else {
						ArrayList<Interval> intervalList = chrIntervalsMap.get(chr);
						intervalList.add(interval);
						chrIntervalsMap.put(chr, intervalList);
						// line numbers
						ArrayList<Integer> intervalLineList = chrIntervalLinesMap.get(chr);
						intervalLineList.add(count);
						chrIntervalLinesMap.put(chr, intervalLineList);
					}
//				} else {
//					String entry = chr + "_" + interval.toString() + " size = " + interval.size() + "\n";
//					System.out.println(entry);
//					largestIntervals.add(entry);
//				}
			}
			count += 1;
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		
	}

}




