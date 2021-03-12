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
 * @author James Kelley
 * Genome Navigator Data Processor
 */
public class SampleVCFFilesProcessor {
	
	public String projectPath = "";
	public boolean processOnlySVs = false;
	
	//String singleSampleSplitChar = ",";
	String singleSampleSplitChar = "\t";
	int count = 1;
	
	String tableContent = "";
	
	int minValue = Integer.MAX_VALUE;
	int maxValue = 0;
	
	int ZERO_P_VALUE_DEFAULT = 1;
	int scaleMultiplier = 5;
	int plotIncrement = 3;
	
	int noDataYPos = 37;
	
	int noDataSpaceIncrement = 20;
//	int noDataMultiplier = 3;
	public int maxIntervalWidth = Constants.MAX_INTERVAL_WIDTH;
	
	// stores entries for clone type such as "01|SAMN02256313"
	public ArrayList<String> cloneTypeList = new ArrayList<String>();
	public ArrayList<LinkedHashMap<String, ArrayList<String>>> cloneListMapList = new ArrayList<LinkedHashMap<String, ArrayList<String>>>();
	public LinkedHashMap<String, ArrayList<SampleData>> sampleIDDataMap = new LinkedHashMap<String, ArrayList<SampleData>>();
	
	private static String formatPrefix = "##FORMAT=<ID=";
	private static String descriptionPrefix = "Description=";
	public static LinkedHashMap<String, String> formatItemsMap = new LinkedHashMap<String, String>();
	
	public void readFile(String file, HashMap<String, String> fileSampleNameMap,
			HashMap<String, String> sampleSpeciesMap, 
			HashMap<String, String> speciesColorMap, 
			ArrayList<SampleData> sampleDataList, 
			LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap,
			LinkedHashMap<String, ArrayList<Integer>> chrIntervalLinesMap,
			LinkedHashMap<String, ArrayList<String>> cloneListMap) { 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			File sampleFile = new File(file);
			String sampleName = sampleFile.getName();
			if (fileSampleNameMap.containsKey(sampleName)) {
				if (fileSampleNameMap.get(sampleName) != null && fileSampleNameMap.get(sampleName).length() > 0) {
					sampleName = fileSampleNameMap.get(sampleName);
				}
			}
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			if (line != null) {
				processLine(line, sampleName, sampleSpeciesMap, speciesColorMap, sampleDataList, chrIntervalsMap, chrIntervalLinesMap, cloneListMap);
			}
			while (line != null) {
				line = reader.readLine();
				//System.out.println(line);
				if (line != null) {
					processLine(line, sampleName, sampleSpeciesMap, speciesColorMap, sampleDataList, chrIntervalsMap, chrIntervalLinesMap, cloneListMap);
				}
				count += 1;
			}
			fis.close();
			String cloneType = "01" + "|" + sampleName;
			//System.out.println(cloneType);
			cloneTypeList.add(cloneType);
			cloneListMapList.add(cloneListMap);
			sampleIDDataMap.put(sampleName, sampleDataList);
			System.out.println(formatItemsMap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processLine(String line, String sampleName,
			HashMap<String, String> sampleSpeciesMap, 
			HashMap<String, String> speciesColorMap, 
			ArrayList<SampleData> sampleDataList, 
			LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap, 
			LinkedHashMap<String, ArrayList<Integer>> chrIntervalLinesMap,
			LinkedHashMap<String, ArrayList<String>> cloneListMap) {
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			if (line != null && line.length() > 0) {
				if (line.startsWith("#")) {
					if (line.startsWith(formatPrefix)) {
						//System.out.println(line);
						String[] values = line.split(",");
						String type = values[0].substring(formatPrefix.length());
						//System.out.println(type);
						String descriptionItem = values[values.length - 1];	
						String description = descriptionItem.substring(descriptionPrefix.length()).replaceAll("\"", "");
						//System.out.println(description);
						formatItemsMap.put(type, description.replace(">", ""));
					}					
				} else {
					//System.out.println(line);
					String[] splitLine = line.split(singleSampleSplitChar);
					if (splitLine.length > 9) {
						String[] values = splitLine[9].split(":");
						String ref = splitLine[3];
						String alt = splitLine[4];
						//System.out.println(alt);
						String type = splitLine[4]; 
						String info = splitLine[7];
						//
						String[] infoValues = info.split(";");
						String keys = splitLine[8];

						String insertionStr = "";
						String insertionEntry = "";

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
							// correct vcf format - alt is what replaces ref but starts with base before ins
							insertionStr = alt.substring(1);
							insertionEntry = insertionStr  + " Size=";
						} else if (type.equals("INS")) {
							end = start;
						} else {
							//end += 1;
						}
						int width = end - start;

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

						if (chrIntervalsMap.get(chr) != null) {
							ArrayList<Interval> intervalList = chrIntervalsMap.get(chr);
							//System.out.println(intervalList);
							ArrayList<Integer> intervalLineList = chrIntervalLinesMap.get(chr);
							//System.out.println(intervalLineList);
							boolean overlap = false;
							Interval interval = new Interval(start, end);
							Interval testInterval = null;
							int intervalNum = 0;
							for (int i = 0; i < intervalList.size(); i++) {
								if ((interval.end >= intervalList.get(i).start && interval.end <= intervalList.get(i).end) || 
										(interval.start <= intervalList.get(i).end && interval.start >= intervalList.get(i).start)) {
									testInterval = intervalList.get(i);
									overlap = true;
									intervalNum = intervalLineList.get(i);
								} 							
							}
							if (overlap && testInterval != null) {
								if (width <= maxIntervalWidth) {
									String keyString = chr + "_" + testInterval.keyString();
									//System.out.println("s " + splitLine[0]);
									SampleData sampleData = new SampleData();
									sampleData.start = start;
									sampleData.end = end;
									sampleData.startPValue = startPValue;
									sampleData.endPValue = endPValue;
									sampleData.type = type;
									sampleData.chr = chr;
									sampleData.insertionStr = insertionStr;
									sampleData.vcfLine = line;
									sampleDataList.add(sampleData);
									//System.out.println(sampleData);

									String color = Constants.DEFAULT_SPECIES_COLOR;								
									String speciesName = Constants.DEFAULT_SPECIES_NAME;
									if (sampleSpeciesMap.containsKey(sampleName)) {
										speciesName = sampleSpeciesMap.get(sampleName);
									}								
									if (speciesColorMap.containsKey(speciesName)) {
										color = speciesColorMap.get(speciesName);
									}	
									if (type.equals("INS") || type.equals("INDEL_INS")) {
										color = Constants.INSERTION_SYMBOL_COLOR;
									}
									int startEntry = start;
									int endEntry = end;
									if (type.equals("INS") || type.equals("INDEL_INS")) {
										startEntry = start - Constants.INSERTION_SYMBOL_START_CORRECTION + 1;
										endEntry = end + Constants.INSERTION_SYMBOL_END_CORRECTION + 1;
									}
									String cloneEntry = startEntry + "|" + endEntry + "|" + 
											UtilityMethods.createSampleName(sampleName, type) + "|" + chr + "|" + color;
									if (start < minValue) {
										minValue = start;
									}
									if (end > maxValue) {
										maxValue = end;
									}
									if (cloneListMap.get(keyString) == null) {
										ArrayList<String> cloneList = new ArrayList<String>();
										cloneList.add(cloneEntry);
										cloneListMap.put(keyString, cloneList);
									} else {
										ArrayList<String> cloneList = cloneListMap.get(keyString);
										cloneList.add(cloneEntry);
										cloneListMap.put(keyString, cloneList);
									}

									String insertionLengthEntry = "";
									if (insertionStr.length() > 0) {
										insertionLengthEntry = Integer.toString(insertionStr.length());
									}
									if (processOnlySVs) {
										if (Constants.svOnlyNamesList.contains(type)) {
											tableContent += sampleName + "\t" + chr + "\t" + type + "\t" + start + "\t" + end + "\t" + "" + "\t" + intervalNum + "\t" + insertionEntry + insertionLengthEntry + "\n";  
										}									
									} else {
										tableContent += sampleName + "\t" + chr + "\t" + type + "\t" + start + "\t" + end + "\t" + "" + "\t" + intervalNum + "\t" + insertionEntry + insertionLengthEntry + "\n";  
									}								
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createPointsData(String pointsFilePrefix, 
			LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap, 
			HashMap<String, String> sampleSpeciesMap,
			HashMap<String, String> speciesColorMap) {
		
		int pointEnd = Constants.POINT_SIZE/2;
		ArrayList<String> keys = new ArrayList<String>(sampleIDDataMap.keySet());
		//Collections.sort(keys);
		
		for (String str : chrIntervalsMap.keySet()) {
			for (int j = 0; j < chrIntervalsMap.get(str).size(); j++) {
				//System.out.println(chrIntervalsMap.get(str).get(j));
				ArrayList<String> entriesList = new ArrayList<String>();
				ArrayList<String> processedSamples = new ArrayList<String>();
				ArrayList<String> noDataSamples = new ArrayList<String>();
				int start = chrIntervalsMap.get(str).get(j).start;
				int end = chrIntervalsMap.get(str).get(j).end;
				
				//int width = chrIntervalsMap.get(str).get(j).size();
				//if (width < Constants.MAX_INTERVAL_WIDTH && start > startRange && end < endRange) {
				//if (width <= Constants.MAX_INTERVAL_WIDTH_BEAR) {
				int midpoint = (start + end)/2;
				//Interval interval = UtilityMethods.calculateRangeFromInterval(chrIntervalsMap.get(str).get(j));
				Interval interval = chrIntervalsMap.get(str).get(j);
				int left = interval.start;
				int right = interval.end;

				//mergedIntervals += start + "\t" + end + "\n";

				int range = right - left;
				//					System.out.println("range " + range);

				if (left < 0) {
					left = 0;
				}

				for (String s : keys) {
					for (int i = 0; i < sampleIDDataMap.get(s).size(); i++) {
						if (sampleIDDataMap.get(s).get(i).start >= start && 
								sampleIDDataMap.get(s).get(i).end <= end) {

							int startValue = sampleIDDataMap.get(s).get(i).start;
							int startValue2 = startValue + pointEnd;
							int endValue = sampleIDDataMap.get(s).get(i).end;
							int endValue2 = endValue + pointEnd;

							double startPValue = Double.parseDouble(sampleIDDataMap.get(s).get(i).startPValue);
							double endPValue = Double.parseDouble(sampleIDDataMap.get(s).get(i).endPValue);
							double startGraphValue = ZERO_P_VALUE_DEFAULT;
							double endGraphValue = ZERO_P_VALUE_DEFAULT;
							if (startPValue > 0) {
								startGraphValue = offset(-Math.log10(startPValue));
							} 
							if (endPValue > 0) {
								endGraphValue = offset(-Math.log10(endPValue));
							} 

							String color = Constants.DEFAULT_SPECIES_COLOR;
							String speciesName = Constants.DEFAULT_SPECIES_NAME;
							if (sampleSpeciesMap.containsKey(s)) {
								speciesName = sampleSpeciesMap.get(s);
							}								
							if (speciesColorMap.containsKey(speciesName)) {
								color = speciesColorMap.get(speciesName);
							}	

							String entry1 = startValue + "|" + startValue2 + "|" + s + "|" + startGraphValue + "|" + color + "|" + endValue;
							entriesList.add(entry1);
							String entry2 = endValue + "|" + endValue2 + "|" + s + "|" + endGraphValue + "|" + color + "|" + startValue;
							entriesList.add(entry2);

							processedSamples.add(s);
						}
					}
				}

				for (String s1 : keys) {
					if (!processedSamples.contains(s1)) {
						noDataSamples.add(s1);
					}
				}

				if (noDataSamples.size()*24 > range) {
					left = (int) (midpoint - noDataSamples.size()*12);
					right = (int) (midpoint + noDataSamples.size()*12);
				}

				int avgLeft = (left + midpoint)/2;
				int avgRight = (right + midpoint)/2;

				int w = range/3;
				if (noDataSamples.size() > 0) {
					noDataSpaceIncrement = w/noDataSamples.size();
				}

				int startLeft = avgLeft - w/2;
				int startRight = avgRight - w/2;

				String content = "";

				content += left + "\n";
				content += right + "\n";

				content += "1" + "\n";
				content += "01|" + "\n";

				content += (entriesList.size() + noDataSamples.size()*2 + 2) + "\n";
				// add 2 dummy entries for now since first point always gets plotted at y = 1
				content += "-1|-1||0|FFFFFF|" + "\n";
				content += "-1|-1||0|FFFFFF|" + "\n";

				for (int k = 0; k < entriesList.size(); k++) {
					content += entriesList.get(k) + "\n";
				}

				for (int k = 0; k < noDataSamples.size(); k++) {
					String speciesName = sampleSpeciesMap.get(noDataSamples.get(k));
					String color = speciesColorMap.get(speciesName);
					content += startLeft + "|" + (startLeft + pointEnd) + "|" + noDataSamples.get(k) 
							+ "|" + noDataYPos + "|" + color
							+ "|" + startRight + "\n";
					content += startRight + "|" + (startRight + pointEnd) + "|" + noDataSamples.get(k) 
							+ "|" + noDataYPos + "|" + color
							+ "|" + startLeft + "\n";
					startLeft += noDataSpaceIncrement;
					startRight += noDataSpaceIncrement;
				}

				content += "0" + "\n";
				content += "0" + "\n";
				content += "0";

				String path = pointsFilePrefix + str + "_" + chrIntervalsMap.get(str).get(j).keyString() + ".txt";

//				System.out.println("p " + path);
//				System.out.println("c " + content);
				File out = new File(path);
//				if (!out.exists()) {
					TextFileWriter writer = new TextFileWriter();
//					String content = "";
					writer.writeFile(path, content);
//					}
				//}
			}
		}
	}
	
	private double offset(double negLogPValue) {
		return 36 - (scaleMultiplier*negLogPValue/plotIncrement);
	}
	
	public static void main(String args[]) {
		boolean allowFileOverwrite = true;
		boolean writeSingleSampleChrFile = true;
		boolean writeMultiSampleFiles = true;	
		
		ConfigFileReader configReader = new ConfigFileReader();
		configReader.readFile("polar_ref-pigh_xrcc-input/polar_ref-pigh_xrcc.conf");
		System.out.println(configReader.configEntries);
		
		SampleVCFFilesProcessor proc = new SampleVCFFilesProcessor();
		
		HashMap<String, String> sampleSpeciesMap = new HashMap<String, String>();
		HashMap<String, String> speciesColorMap = new HashMap<String, String>();
		
		// input files
		String vcfFileType = Constants.DEFAULT_VCF_FILE_TYPE;
		if (configReader.configEntries.vcfType.length() > 0) {
			vcfFileType = configReader.configEntries.vcfType;
		}		
	
		File gFile = new File(configReader.configEntries.gffFilePath);
		
		// output files
		String projectPath = configReader.configEntries.projectPath;
		proc.projectPath = projectPath;
		String varDirectory = projectPath + Constants.VAR_DIRECTORY;
		String pointsDirectory = projectPath + Constants.POINTS_DIRECTORY;

		File dataDir = new File(projectPath);
		if (!dataDir.exists()) {
			dataDir.mkdir();
		}
		File varDir = new File(varDirectory);
		if (!varDir.exists()) {
			varDir.mkdir();
		}
		
		LinkedHashMap<String, String> fileSampleNameMap = new LinkedHashMap<String, String>();
		String fileSampleFileName = configReader.configEntries.fileSampleNameFilePath;
		if (fileSampleFileName.length() > 0) {
			// associate files with sample names 
			fileSampleNameMap = UtilityMethods.fileSampleNameMap(fileSampleFileName);
			System.out.println(fileSampleNameMap);
		} else {
			if (!configReader.configEntries.useFileNames) {
				System.out.println("Error. File Sample Name file needed or use file names should be 1");
			}
		}		
//		
//		// use all files with a given ending in vcf directory if file sample map does not exist
		ArrayList<String> fileNameList = UtilityMethods.fileList(configReader.configEntries.vcfFilesPath, vcfFileType);
		System.out.println(fileNameList);
//		if (configReader.configEntries.useFileNames) {
//			// This is a hack, putting the same entry for key and value, but it will work if 
//			// the sample species file uses file names for samples
//			for (int i = 0; i < fileNameList.size(); i++) {
//				fileSampleNameMap.put(fileNameList.get(i), fileNameList.get(i));
//			}
//			System.out.println(fileSampleNameMap);
//		}
//		// This will only be size 0 if error condition above occurred
		if (fileSampleNameMap.size() > 0) {
			fileNameList = new ArrayList<String>(fileSampleNameMap.keySet());
			// process species file for color code
			CSVSpeciesFileReader reader = new CSVSpeciesFileReader();		
			String sampleSpeciesFileName = configReader.configEntries.sampleNameSpeciesFilePath;
			if (sampleSpeciesFileName.length() > 0) {
				File f = new File(sampleSpeciesFileName);
				if (f.exists()) {
					reader.readFile(f.getAbsolutePath(), sampleSpeciesMap);
					//System.out.println(sampleSpeciesMap);
				}			
			}
			
			String speciesColorFileName = configReader.configEntries.speciesNameColorFilePath;
			// file given
			if (speciesColorFileName.length() > 0) {
				// associate sample with type (species, etc.)
				speciesColorMap = UtilityMethods.speciesColorMap(speciesColorFileName);
				//System.out.println(speciesColorMap);
			} else {
				// sample species file exists
				if (sampleSpeciesMap.size() > 0) {
					if (sampleSpeciesMap.size() <= Constants.DEFAULT_SPECIES_COLORS.length) {
						// assign random colors
						int index = 0;
						for (String sample : sampleSpeciesMap.keySet()) {
							speciesColorMap.put(sampleSpeciesMap.get(sample), Constants.DEFAULT_SPECIES_COLORS[index]);
							index += 1;
						}
					} else {
						System.out.println("Unable to provide random colors for more than " + Constants.DEFAULT_SPECIES_COLORS.length + " samples.");
					}
				}			
			}
		} 
				
		IntervalFileChrFileProcessor iProc = new IntervalFileChrFileProcessor();
		
		File iFile = new File(configReader.configEntries.intervalsFilePath + Constants.INTERVALS_FILENAME);
		iProc.readFile(iFile.getAbsolutePath(), 1, 2);
		// Intervals file is necessary for viewer, but output directory does not exist before
		// processing data, so intervals file is copied here with a constant name
		UtilityMethods.copyFile(iFile, new File(projectPath + Constants.INTERVALS_FILENAME));
		
		LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap = iProc.chrIntervalsMap;
		LinkedHashMap<String, ArrayList<Integer>> chrIntervalLinesMap = iProc.chrIntervalLinesMap;
//		
//		GFFFileReader gffReader = new GFFFileReader();
//		gffReader.readFile(gFile.getAbsolutePath(), iProc.chrIntervalsMap);
//		
//		// gff file is necessary for viewer, but output directory does not exist before
//		// processing data, so gf file is copied here with a constant name
//		UtilityMethods.copyFile(gFile, new File(projectPath + Constants.GFF_FILENAME));
		
		//LinkedHashMap<String, HashMap<String, ArrayList<GeneralFeatureData>>> chrIntervalGeneDataMap = gffReader.createChrIntervalGeneDataMap();
		
		for (int i = 0; i < fileNameList.size(); i++) {
			ArrayList<SampleData> sampleDataList = new ArrayList<SampleData>();
			LinkedHashMap<String, ArrayList<String>> cloneListMap = new LinkedHashMap<String, ArrayList<String>>();
			File file = new File(configReader.configEntries.vcfFilesPath + fileNameList.get(i));
			System.out.println(file.getAbsolutePath());
			if (file.exists()) {
				proc.readFile(file.getAbsolutePath(), fileSampleNameMap, sampleSpeciesMap, 
						speciesColorMap, sampleDataList, chrIntervalsMap, chrIntervalLinesMap, cloneListMap);
				System.out.println(file.getAbsolutePath());
			}
		}
		
		/*-----------------------------------------------------------------------*/
		// Printout for file for DataTableFrame and derBrowser (first window)
		/*-----------------------------------------------------------------------*/
		
		
		System.out.println("clone map " + proc.cloneListMapList);
		System.out.println(chrIntervalsMap);
		System.out.println(chrIntervalLinesMap);
		//System.out.println(proc.cloneListMap);
		
		if (writeSingleSampleChrFile) {
			for (String s : chrIntervalsMap.keySet()) {
				for (int i = 0; i < chrIntervalsMap.get(s).size(); i++) {
					String keyString = s + "_" + chrIntervalsMap.get(s).get(i).keyString();
					String content = "";
					content += chrIntervalsMap.get(s).get(i).start + "\n";
					content += chrIntervalsMap.get(s).get(i).end + "\n";
					
//					if (gffReader.chrIntervalGFDataListMap.size() > 0) {
//						content += "1\n";
//						content += "01|" + Constants.EXON_SAMPLE_NAME + "\n";
//						
//						ArrayList<String> exonEntries = new ArrayList<String>();
//
//						for (String gene : chrIntervalGeneDataMap.get(s).keySet()) {
//							ArrayList<GeneralFeatureData> gfdList = chrIntervalGeneDataMap.get(s).get(gene);
//							if (gfdList.get(0).strand.equals("-")) {
//								for (int g = gfdList.size() - 1; g >= 0; g--) {	
//									Interval exonInterval = new Interval(gfdList.get(g).start, gfdList.get(g).end);
//									if (UtilityMethods.isIntervalPortionVisible(exonInterval, chrIntervalsMap.get(s).get(i))) {
//										String entry = gfdList.get(g).start + "|" + gfdList.get(g).end + "|" 
//												+ gene + "|" + gene + "|" + Constants.EXON_COLOR_STRING + "\n";
//										exonEntries.add(entry);
//									}
//								}
//							} else {
//								for (int g = 0; g < gfdList.size(); g++) {
//									Interval exonInterval = new Interval(gfdList.get(g).start, gfdList.get(g).end);
//									if (UtilityMethods.isIntervalPortionVisible(exonInterval, chrIntervalsMap.get(s).get(i))) {
//										String entry = gfdList.get(g).start + "|" + gfdList.get(g).end + "|" 
//												+ gene + "|" + gene + "|" + Constants.EXON_COLOR_STRING + "\n";
//										exonEntries.add(entry);
//									}
//								}
//							}
//						}
//						content += exonEntries.size() + "\n";
//						for (int e = 0; e < exonEntries.size(); e++) {
//							content += exonEntries.get(e);
//						}
//						
//						content += "0" + "\n";
//					} else {
					// write dummy entry if no gff data
						content += "1" + "\n";
						content += "04|Exons" + "\n";
						content += "1" + "\n";
						content += "0|0||-|FFFFFF0" + "\n";
						content += "0" + "\n";
//						content += "0" + "\n";
//						content += "0" + "\n";
//					}
						
					content += proc.cloneTypeList.size() + "\n";
					for (int j = 0; j < proc.cloneTypeList.size(); j++) {
						content += proc.cloneTypeList.get(j) + "\n";
						if (proc.cloneListMapList.get(j).get(keyString) != null && proc.cloneListMapList.get(j).get(keyString).size() > 0) {
							content += proc.cloneListMapList.get(j).get(keyString).size() + "\n";
							for (int k = 0; k < proc.cloneListMapList.get(j).get(keyString).size(); k++) {
								content += proc.cloneListMapList.get(j).get(keyString).get(k) + "\n";
							}
						} else {
							content += "1" + "\n";
							content += "-1|-1|||" + "\n";
						}
					}
					// clone shade entry
//					System.out.println("0");
					content += "0" + "\n";
					//System.out.println(content);

					String path = varDirectory + keyString + ".txt";
					//String path = "etc/test.txt";
					
					//System.out.println("writing");
					//System.out.println(path);
					File out = new File(path);
					boolean writeFile = true;
					if (!allowFileOverwrite) {
						if (out.exists()) {
							writeFile = false;
						}
					}
					if (writeFile) {
						TextFileWriter writer = new TextFileWriter();
						writer.writeFile(path, content);
					}					
				}
			}
			String dataTablePath = projectPath + Constants.DATA_TABLE_FILENAME;
			System.out.println(dataTablePath);
			TextFileWriter writer = new TextFileWriter();
			writer.writeFile(dataTablePath, proc.tableContent);
		}
		
		/*-----------------------------------------------------------------------*/
		// Write points files
		/*-----------------------------------------------------------------------*/
		if (writeMultiSampleFiles) {						
			File dir = new File(pointsDirectory);
			if (!dir.exists()) {
				dir.mkdir();
			}
			
			String pointsFilePrefix = pointsDirectory;
			proc.createPointsData(pointsFilePrefix, chrIntervalsMap, sampleSpeciesMap, speciesColorMap);
		}
		System.out.println("Done");
	}
	
}

