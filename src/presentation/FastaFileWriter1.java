package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Genome Navigator Data Processor code? Is this used?
 * Writes FASTA File for single chr or scaffold from file containing multiple items
 * @author jkelley
 *
 */
public class FastaFileWriter1 {
	
	HashMap<String, String> chrIntervalSeqMap = new HashMap<String, String>();
	
	int count = 1;
	boolean read = false;
	String content = "";
	
	public void readFile(String inFilePath, String outFilePath, LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap, 
			HashMap<String, Integer> scaffoldIndexMap, int lineLength, int correction)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(inFilePath);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String line = reader.readLine();
			processLine(line, chrIntervalsMap, outFilePath, scaffoldIndexMap, lineLength, correction);
			while (line != null) {
				line = reader.readLine();
				processLine(line, chrIntervalsMap, outFilePath, scaffoldIndexMap, lineLength, correction);
			}
			fis.close();
			System.out.println("done");
			//File out = new File(outFilePath);
			//if (!out.exists()) {
//				TextFileWriter writer = new TextFileWriter();
//				//String content = "";
//				//System.out.println(outFilePath);
//				writer.writeFile(outFilePath, content);
			//}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void processLine(String line, LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap, String outFilePath, 
			HashMap<String, Integer> scaffoldIndexMap, int lineLength, int correction) {
		if (line != null) {
			for (String chr : chrIntervalsMap.keySet()) {
				int chrStartIndex = scaffoldIndexMap.get(chr);
				ArrayList<Interval> intervals = chrIntervalsMap.get(chr);
				for (int i = 0; i < intervals.size(); i++) {
					String key = chr + "_" + intervals.get(i).keyString();
					int endIndex = intervals.get(i).end;
					if (ProjectConstants.ADJUST_READS_RIGHT_END) {
						endIndex = intervals.get(i).end + (int)(ProjectConstants.READS_RIGHT_END_CORRECTION*intervals.get(i).size());
					}
					if (count > chrStartIndex + (intervals.get(i).start + correction)/lineLength && count <= chrStartIndex + endIndex/lineLength + 1) {
						if (chrIntervalSeqMap.containsKey(key)) {
							String seq = chrIntervalSeqMap.get(key);
							seq += line + "\n";
							chrIntervalSeqMap.put(key, seq);
						} else {
							chrIntervalSeqMap.put(key, line + "\n");
						}
					}
					if (count > chrStartIndex + intervals.get(i).end/lineLength + 1) {
						TextFileWriter writer = new TextFileWriter();
						if (chrIntervalSeqMap.containsKey(key)) {
							String seq = chrIntervalSeqMap.get(key);
							String oldOutFilePath = outFilePath;
							outFilePath = outFilePath + key + ".fa";
							System.out.println("path " + outFilePath);
							writer.writeFile(outFilePath, seq);
							chrIntervalSeqMap.remove(key);
							// if more than one interval is present when this condition is true, file
							// names would be combined without this reset of base path
							outFilePath = oldOutFilePath;
						}
					}
				}
			}
			//System.out.println("count " + count);
			count += 1;
		}
	}
	
	public static void main(String args[]) {
		//String path = "E:/bear/";
		String path = "E:/work_Documents/Java Workspace/GenomeNavigator1/etc/bear/ref_seq/";
		
		File faiFile = new File(path + "GCF_000687225.1_UrsMar_1.0_genomic.fna.fai.indexes.txt");
		
		FAIChrIndexReader faiReader = new FAIChrIndexReader();
		faiReader.readFile(faiFile.getAbsolutePath());
		
		System.out.println("scaffold index " + faiReader.scaffoldIndexMap);
		
		IntervalFileChrFileProcessor iProc = new IntervalFileChrFileProcessor();
		String intervalsFilename = "E:/test_intervals-polar040319_plus1.txt";
		
		File iFile = new File(intervalsFilename);
		
		iProc.readFile(iFile.getAbsolutePath(), 1, 2);
		
		int lineLength = 80;
		
		System.out.println(iProc.chrIntervalsMap);
//		ArrayList<String> chrIntervalKeyList = new ArrayList<String>();
//		for (String s : iProc.chrIntervalsMap.keySet()) {		
//			for (int i = 0; i < iProc.chrIntervalsMap.get(s).size(); i++) {
//				System.out.println(iProc.chrIntervalsMap.get(s).get(i));
//				chrIntervalKeyList.add(s + "_" + iProc.chrIntervalsMap.get(s).get(i).keyString());			
//			}
//		}
//		System.out.println(chrIntervalKeyList);
		
		FastaFileWriter1 reader = new FastaFileWriter1();
		File file = new File("E:/work_Documents/Java Workspace/GenomeNavigator1/etc/bear/ref_seq/GCF_000687225.1_UrsMar_1.0_genomic.fna");
		reader.readFile(file.getAbsolutePath(), "", iProc.chrIntervalsMap, faiReader.scaffoldIndexMap, lineLength, 0);
		
		System.out.println("Done");

	}

}


