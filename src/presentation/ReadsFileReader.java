package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author James Kelley
 * Used by GNDP
 */
public class ReadsFileReader {
	
//	public int count = 0;
//	public int sameChrCount = 0;
	
	public void readFile(String file, String sampleName, LinkedHashMap<String, ArrayList<Read>> nameReadsMap, LinkedHashMap<String, Interval> sampleIntervalMap)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, sampleName, nameReadsMap, sampleIntervalMap);
			while (line != null && line.length() > 0) {
				line = reader.readLine();
				processLine(line, sampleName, nameReadsMap, sampleIntervalMap);
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
	
	private void processLine(String line, String sampleName, LinkedHashMap<String, ArrayList<Read>> nameReadsMap, LinkedHashMap<String, Interval> sampleIntervalMap) {
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			String[] splitLine = line.split("\t");
//			for (int i = 0; i < splitLine.length; i++) {
//				System.out.println(splitLine[i]);
//			}	
			String name = splitLine[0];
			//System.out.println(name);
			Read read = new Read();
			read.pos = Integer.parseInt(splitLine[3]);
			//System.out.println(read.pos);
			// correct for index error where index in FASTA file does not match up with SAM files
			read.pos -= 1;
			read.origPos = read.pos;
			read.rname = splitLine[2];
			read.rnext = splitLine[6];
			read.pnext = Integer.parseInt(splitLine[7]);
			read.pnext -= 1;
			read.tlen = Integer.parseInt(splitLine[8]);
			read.sequence = splitLine[9];
			read.id = name;
			read.flag = splitLine[1];
			read.cigar = splitLine[5];
			read.paired = true;
			read.sameChr = false;
			read.mapped = true;
			read.mateMapped = true;
			read.primaryAlignment = true;
			read.fails = false;
			read.duplicate = false;	
			
			boolean graph = true;
			
			if ((Integer.parseInt(read.flag) & 1) == 0) {
				read.paired = false;
			} else {
				read.sameChr = pairedOnSameChr(read);
			}
			
			// if flag information is necessary need to do bitwise comparisons,
			// otherwise code is faster since bitwise check is only necessary
			// if reads with this flag are to be graphed
			if (Constants.FLAG_INFORMATION_NEEDED) {
				if ((Integer.parseInt(read.flag) & 4) != 0) {
					read.mapped = false;
					if (!Constants.GRAPH_UNMAPPED_READS) {
						graph = false;
					}
				}
				if ((Integer.parseInt(read.flag) & 8) != 0) {
					read.mateMapped = false;
				}
				if ((Integer.parseInt(read.flag) & 256) != 0) {
					read.primaryAlignment = false;
					if (!Constants.GRAPH_SECONDARY_ALIGNMENT_READS) {
						graph = false;
					}
				}
				if ((Integer.parseInt(read.flag) & 512) != 0) {
					read.fails = true;
					if (!Constants.GRAPH_NOT_PASSING_READS) {
						graph = false;
					}
				}
				if ((Integer.parseInt(read.flag) & 1024) != 0) {
					read.duplicate = true;
					if (!Constants.GRAPH_DUPLICATE_READS) {
						graph = false;
					}
				}
				if ((Integer.parseInt(read.flag) & 2048) != 0) {
					read.suppAlign = true;
					if (!Constants.GRAPH_SUPPLEMENTARY_ALIGNMENT_READS) {
						graph = false;
					}
				}
			} else {
				// This is necessary to determine if read is to be drawn as paired
				if ((Integer.parseInt(read.flag) & 8) != 0) {
					read.mateMapped = false;
				}
				if (!Constants.GRAPH_UNMAPPED_READS) {
					if ((Integer.parseInt(read.flag) & 4) != 0) {
						graph = false;
					}
				}
				if (!Constants.GRAPH_SECONDARY_ALIGNMENT_READS) {
					if ((Integer.parseInt(read.flag) & 256) != 0) {
						graph = false;
					}
				}
				if (!Constants.GRAPH_NOT_PASSING_READS) {
					if ((Integer.parseInt(read.flag) & 512) != 0) {
						graph = false;
					}
				}
				if (!Constants.GRAPH_DUPLICATE_READS) {
					if ((Integer.parseInt(read.flag) & 1024) != 0) {
						graph = false;
					}
				}
				if (!Constants.GRAPH_SUPPLEMENTARY_ALIGNMENT_READS) {
					if ((Integer.parseInt(read.flag) & 2048) != 0) {
						graph = false;
					}
				}
			}
			
			if (graph) {
				if (nameReadsMap.containsKey(name)) {
					ArrayList<Read> reads = nameReadsMap.get(name);
					reads.add(read);
					nameReadsMap.put(name, reads);
				} else {
					ArrayList<Read> reads = new ArrayList<Read>();
					reads.add(read);
					nameReadsMap.put(name, reads);
				}
//				System.out.println(sampleName);
//				System.out.println(sampleIntervalMap.get(sampleName));
				if (read.paired && read.sameChr) {
					if (Math.abs(read.tlen) < sampleIntervalMap.get(sampleName).start) {
						read.description = "INS";
					} else if (Math.abs(read.tlen) > sampleIntervalMap.get(sampleName).end) {
						read.description = "DEL";
					}
				}
			} 
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	private boolean pairedOnSameChr(Read read) {
		//count += 1;
		if (read.rnext.equals("=") || read.rname.equals(read.rnext)) {
			//sameChrCount += 1;
			return true;
		}
		
		return false;
		
	}
	
	public void processFiles(String directory, String samplePrefix, String sampleSuffix, int firstID, int lastID, 
			String chr, Interval interval, LinkedHashMap<String, Interval> sampleIntervalMap) {
		for (int j = firstID; j <= lastID; j++) {
			File file = new File(directory + samplePrefix + j + "_" + 
					chr + "_" + interval.start + "-" + interval.end + ".sam");
			if (file.exists()) {
				LinkedHashMap<String, ArrayList<Read>> nameReadsMap = new LinkedHashMap<String, ArrayList<Read>>();
				readFile(file.getAbsolutePath(), samplePrefix + j, nameReadsMap, sampleIntervalMap);	
				//System.out.println(nameReadsMap);
			}
		}
	}
	
	public static void main(String args[]) {
		String meanDirectory = "etc/bear/mean_files/";
		String samplePrefix = "SAMN0";
		String sampleSuffix = "_sorted.bam.mean";
		// polar
		int firstSampleIDPolar = 2261802;
		int lastSampleIDPolar = 2261880;
		// brown
		int firstSampleIDBrown = 2256321;
		int lastSampleIDBrown = 2256322;
		
		MeanFilesReader mReader = new MeanFilesReader();
		mReader.processFiles(meanDirectory, samplePrefix, sampleSuffix, firstSampleIDPolar, lastSampleIDPolar);
		mReader.processFiles(meanDirectory, samplePrefix, sampleSuffix, firstSampleIDBrown, lastSampleIDBrown);
		
		String directory = "etc/bear/reads/";
		
//		File iFile = new File("etc/bear/bear_intervals.txt");
//		ChrIntervalsFileReader chrReader = new ChrIntervalsFileReader();
//		chrReader.readFile(iFile.getAbsolutePath());
//		System.out.println(chrReader.chrIntervalsMap);
		
		ReadsFileReader reader = new ReadsFileReader();
		Interval interval = new Interval(11435089, 11435889);
		reader.processFiles(directory, samplePrefix, ".sam", firstSampleIDPolar, lastSampleIDPolar, 
				"NW_007907148.1", interval, mReader.sampleIntervalMap);
		
//		String[] sample_list_brown = {"SAMN02256313", "SAMN02256314", "SAMN02256315", "SAMN02256316", "SAMN02256317", "SAMN02256318", "SAMN02256319", "SAMN02256320", "SAMN02256321", "SAMN02256322"};
//		String[] sample_list_polar = {"SAMN02261805", "SAMN02261819", "SAMN02261840", "SAMN02261845", "SAMN02261851", "SAMN02261853", "SAMN02261854", "SAMN02261858", "SAMN02261865", "SAMN02261868", "SAMN02261870", "SAMN02261878", "SAMN02261880"};
		
//		ReadsFileReader1 reader = new ReadsFileReader1();
//		for (int j = firstSampleIDPolar; j < sample_list_brown.length; j++) {
//			for (String s : chrReader.chrIntervalsMap.keySet()) {
//				ArrayList<Interval> intervalList = chrReader.chrIntervalsMap.get(s);
//				for (int k = 0; k < intervalList.size(); k++) {
//					File file = new File(directory + sample_list_brown[j] + "_" + 
//							s + "_" + intervalList.get(k).start + "-" + intervalList.get(k).end + ".sam");
//					System.out.println(file.getAbsolutePath());
//					if (file.exists()) {
//						LinkedHashMap<String, ArrayList<Read>> nameReadsMap = new LinkedHashMap<String, ArrayList<Read>>();
//						reader.readFile(file.getAbsolutePath(), nameReadsMap);	
//						System.out.println(nameReadsMap);
//					}
//				}
//			}
//		}
		
//		IntervalFileProcessor proc = new IntervalFileProcessor();
//		ArrayList<Interval> intervals = new ArrayList<Interval>();
//		
//		File f = new File(directory + "SAMN04320527-SAMN04320539_bwa_mem_sl_GROM_v1_81t_M_f_b10_chr22_DEL_merged.txt");
//		//proc.readFile(f.getAbsolutePath(), intervals, 0, 1);
//		proc.readFile(f.getAbsolutePath(), intervals, Constants.MERGED_INTERVALS_START_COLUMN_INDEX, Constants.MERGED_INTERVALS_END_COLUMN_INDEX);
//		
//		ArrayList<Interval> missingIntervals = new ArrayList<Interval>();
//		
//		int min = Integer.MAX_VALUE;
//		int max = 0;
//		Interval minInt = null;
//		Interval maxInt = null;
//		for (int i = 0; i < intervals.size(); i++) {
//			//System.out.println(intervals.get(i).size());
//			if (intervals.get(i).size() < min) {
//				min = intervals.get(i).size();
//				minInt = intervals.get(i);
//			}
//			if (intervals.get(i).size() > max) {
//				max = intervals.get(i).size();
//				maxInt = intervals.get(i);
//			}
//			//System.out.println(intervals.get(i));
//			int startIndex = intervals.get(i).start;
//			int endIndex = intervals.get(i).end;
//			if (Constants.CORRECT_MERGED_INTERVALS_INDICES) {
//				startIndex -= 1;
//				endIndex -= 1;
//			}
//			
//			for (int j = firstSampleIndex; j <= lastSampleIndex; j++) {
//				ReadsFileReader1 reader = new ReadsFileReader1();
//				LinkedHashMap<String, ArrayList<Read>> nameReadsMap = new LinkedHashMap<String, ArrayList<Read>>();
//				//File file = new File("etc/SAMN04320527_sorted_chr22_17616706-17617582-228846943.sam");
//				File file = new File(directory + sampleDirPrefix + j + "/" + 
//						sampleDirPrefix + j + startIndex + "_" + endIndex + "_DEL");
//				//System.out.println(file.getAbsolutePath());
//				if (file.exists()) {
////					reader.readFile(file.getAbsolutePath(), nameReadsMap);
////					
////					System.out.println(nameReadsMap);
//				} else {
//					if (!missingIntervals.contains(intervals.get(i))) {
//						missingIntervals.add(intervals.get(i));
//					}
//					//System.out.println(intervals.get(i));
//				}
//			}
//		}
//		
//		System.out.println("min " + min);
//		System.out.println(minInt);
//		System.out.println("max " + max);
//		System.out.println(maxInt);
//		System.out.println(missingIntervals);
//		
////		int startIndex = 18003597;
////		int endIndex = 18004224;
////		if (Constants.CORRECT_MERGED_INTERVALS_INDICES) {
////			startIndex -= 1;
////			endIndex -= 1;
////		}
////		Interval interval = new Interval(startIndex, endIndex);
////		ReadsFileReader reader = new ReadsFileReader();
////		LinkedHashMap<String, ArrayList<Read>> nameReadsMap = new LinkedHashMap<String, ArrayList<Read>>();
////		//File file = new File("etc/SAMN04320527_sorted_chr22_17616706-17617582-228846943.sam");
////		File file = new File(directory + sampleDirPrefix + firstSampleIndex + "/" + 
////				sampleDirPrefix + firstSampleIndex + fileMidName + interval.start + "_" + interval.end + "_DEL");
////		reader.readFile(file.getAbsolutePath(), nameReadsMap);
////		
////		System.out.println(nameReadsMap);
//		
////		ArrayList<String> keys = new ArrayList<String>(nameReadsMap.keySet());
//////		System.out.println(keys.size());
//////		int count = 0;
////		for (int i = 0; i < keys.size(); i++) {
////			ArrayList<Read> readList = nameReadsMap.get(keys.get(i));
//////			if (readList.size() > 1) {
//////				System.out.println(keys.get(i));
//////				System.out.println(readList);
//////			}
////			for (int j = 0; j < readList.size(); j++) {
//////				if (readList.get(j).rnext.equals("=")) {
//////					if (nameReadsMap.get(keys.get(i)).size() > 1) {
//////						System.out.println(nameReadsMap.get(keys.get(i)));
////////						count += 1;
//////					} else {
//////						//System.out.println(nameReadsMap.get(keys.get(i)) + " paired");
//////					}
//////				} else {
//////					//System.out.println(nameReadsMap.get(keys.get(i)) + " unpaired");
//////				}
////			}
////			
////		}
////		System.out.println(count);
	}

}


