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
 * Genome Navigator Data Processor
 * Used in Single Sample mode to create intervals from variants from VCF file
 */
public class IntervalsFromVCFProcessor {
	
	public static String projectPath = "";
	private static String splitChar = "\t";
	public String content = "";
//	private static String formatPrefix = "##FORMAT=<ID=";
//	private static String descriptionPrefix = "Description=";
//	public static LinkedHashMap<String, String> formatItemsMap = new LinkedHashMap<String, String>();
	public boolean writeIntervals = true;
	
	public void readFile(String file, String outFile, double maxPValue) { 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			if (line != null) {
				processLine(line, maxPValue);
			}
			while (line != null) {
				line = reader.readLine();
				//System.out.println(line);
				if (line != null) {
					processLine(line, maxPValue);
				}
			}
			fis.close();
			//System.out.println(formatItemsMap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processLine(String line, double maxPValue) {
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			if (line != null && line.length() > 0) {
				if (line.startsWith("#")) {					
//					if (line.startsWith(formatPrefix)) {
//						//System.out.println(line);
//						String[] values = line.split(",");
//						String type = values[0].substring(formatPrefix.length());
//						//System.out.println(type);
//						String descriptionItem = values[values.length - 1];	
//						String description = descriptionItem.substring(descriptionPrefix.length()).replaceAll("\"", "");
//						//System.out.println(description);
//						formatItemsMap.put(type, description.replace(">", ""));
//					}
				} else {
					String[] splitLine = line.split(splitChar);
					if (splitLine.length > 9) {
						String[] values = splitLine[9].split(":");
						String type = splitLine[4]; 
						String info = splitLine[7];
						String[] infoValues = info.split(";");
						String keys = splitLine[8];
						if (type.startsWith("<")) {
							type = splitLine[4].substring(1, splitLine[4].length() - 1);
						} else {
							String infoType = "TYPE=";	
							// fixes bug in GROM v1.0.2 where indels have SVTYPE in entry
							String svInfoType = "SVTYPE=";
//							System.out.println(info);
//							System.out.println(infoValues[0]);
							if (infoValues[0].contains(svInfoType)) {
								type = infoValues[0].substring(svInfoType.length());
							} else {
								type = infoValues[0].substring(infoType.length());
							}					
						}

						//System.out.println(type);
						String chr = splitLine[0];
						if (ProjectConstants.CHR_TO_UPPER_CASE) {
							chr = splitLine[0].toUpperCase();
						}
						int start = Integer.parseInt(splitLine[1]);
						start += 1;
						String s = "END=";
						int end = Integer.parseInt(infoValues[1].substring(s.length())); 
						if (type.equals("INDEL_INS")) {
							end = start;
						} else if (type.equals("INS")) {
							end = start;
						} else {
							end += 1;
						}
						
						String startPValue = "0";
						String endPValue = "0";

						if (keys.startsWith("GT:SPR:EPR")) {
							startPValue = values[1];
							endPValue = values[2];
							// indel ins
						} else if (keys.startsWith("GT:SPR:SEV")) {
							startPValue = values[1];
							endPValue = values[1];
						}
						
						if (Double.parseDouble(startPValue) > maxPValue || Double.parseDouble(endPValue) > maxPValue) {
							
						} else {
//							Interval interval = UtilityMethods.readsWindowRangeForIntervalsFile(start, end);
//							String entry = chr + "\t" + interval.start + "\t" + interval.end + "\n";
//							//System.out.println("entry " + entry);
//							content += entry;
							ArrayList<Interval> intervals = UtilityMethods.readsWindowRanges(start, end);
							Interval vcfInterval = new Interval(start, end);
							if (vcfInterval.size() > Constants.MAX_INTERVAL_WIDTH) {
								System.out.println(chr);
								System.out.println("vcf " + vcfInterval.toString());
							}
							for (int i = 0; i < intervals.size(); i++) {
								String entry = chr + "\t" + intervals.get(i).start + "\t" + intervals.get(i).end + "\n";
								//System.out.println("entry " + entry);
								if (intervals.size() > 1) {
									//System.out.println(entry);
								}
								content += entry;
							}
						}
					}
				}
				//System.out.println(line);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		String outFile = projectPath + Constants.INTERVALS_FILENAME;
		double maxPValue = 0;
		IntervalsFromVCFProcessor intervFromVCFProc = new IntervalsFromVCFProcessor();
//		IntervalsFromVCFProcessor.readFile("/scratch/jimkell/GROM-bear/GROM-polar_ref/GROM_bear_polar_ref-e10-6/SAMN02256314_polar_ref_GROM-97-master010819a._h-e10-6.SV.vcf", 
//				outFile, maxPValue);
		intervFromVCFProc.readFile("C:/Users/rutgers/Documents/Java_Workspace/GenomeNavigator1/etc/bear/GROM_bear_polar_ref-e10-6/SAMN02256314_polar_ref_GROM-97-master010819a._h-e10-6.SV.vcf", 
				outFile, maxPValue);
		TextFileWriter writer = new TextFileWriter();
		writer.writeFile(outFile, intervFromVCFProc.content);
		System.out.println("Intervals Done");
		
//		try
//		{
//			Runtime r = Runtime.getRuntime();
//			Process p = r.exec("module load samtools");
//			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			p.waitFor();
//			String line = "";
//			while (br.ready())
//				System.out.println(br.readLine());
//		}
//		catch (Exception e)
//		{
//			String cause = e.getMessage();
//			if (cause.equals("python: not found"))
//				System.out.println("No python interpreter found.");
//		}
//		
//		try
//		{
//			Runtime r = Runtime.getRuntime();
//			Process p = r.exec("python get_sam_files_from_bam-no_mates.py brown_bear-14-polar_ref-amarel_config.conf /scratch/jimkell/polar_bear_ref/br14 bear_bam_file_sample-polar_ref-14.csv");
//			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			p.waitFor();
//			String line = "";
//			while (br.ready())
//				System.out.println(br.readLine());
//		}
//		catch (Exception e)
//		{
//			String cause = e.getMessage();
//			if (cause.equals("python: not found"))
//				System.out.println("No python interpreter found.");
//		}
		
		System.out.println("Done");
	}
	
}
