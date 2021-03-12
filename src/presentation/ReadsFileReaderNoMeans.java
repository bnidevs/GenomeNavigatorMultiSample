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
public class ReadsFileReaderNoMeans {
	
//	public int count = 0;
//	public int sameChrCount = 0;
	
	public void readFile(String file, String sampleName, LinkedHashMap<String, ArrayList<Read>> nameReadsMap)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, sampleName, nameReadsMap);
			while (line != null && line.length() > 0) {
				line = reader.readLine();
				processLine(line, sampleName, nameReadsMap);
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
	
	private void processLine(String line, String sampleName, LinkedHashMap<String, ArrayList<Read>> nameReadsMap) {
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
				readFile(file.getAbsolutePath(), samplePrefix + j, nameReadsMap);	
				//System.out.println(nameReadsMap);
			}
		}
	}
	
}
