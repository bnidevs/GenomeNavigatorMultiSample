package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ChrIntervalsFileReader {
	
	public LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap = new LinkedHashMap<String, ArrayList<Interval>>();
	
	public void readFile(String file)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String line = reader.readLine();
			processLine(line);
			while (line != null) {
				line = reader.readLine();
				processLine(line);
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
	
	private void processLine(String line) {
		try {
			String[] splitLine = line.split("\t");
			String chr = splitLine[0];
			Interval interval = new Interval(Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]));
			if (chrIntervalsMap.containsKey(chr)) {
				ArrayList<Interval> intervalList = chrIntervalsMap.get(chr);
				intervalList.add(interval);
				chrIntervalsMap.put(chr, intervalList);
			} else {
				ArrayList<Interval> intervalList = new ArrayList<Interval>();
				intervalList.add(interval);
				chrIntervalsMap.put(chr, intervalList);
			}
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		File file = new File("etc/bear/bear_intervals.txt");
		ChrIntervalsFileReader reader = new ChrIntervalsFileReader();
		reader.readFile(file.getAbsolutePath());
		System.out.println(reader.chrIntervalsMap);
	}

}




