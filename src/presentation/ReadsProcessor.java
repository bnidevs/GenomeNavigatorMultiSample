package presentation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class ReadsProcessor {
	
	//public int offscreenCount = 0;
	public HashMap<String, Integer> readDepthMap = new HashMap<String, Integer>();
	public HashMap<String, Integer> indelInsDepthMap = new HashMap<String, Integer>();
	public HashMap<String, Integer> indelDelDepthMap = new HashMap<String, Integer>();
	IntComparator ic = new IntComparator();
	
	boolean splitReadTypes = ProjectConstants.SPLIT_READ_TYPES;
	
	public void processReads(LinkedHashMap<String, ArrayList<Read>> nameReadsMap, 
			String sampleID, LinkedHashMap<String, Interval> sampleIntervalMap,
			LinkedHashMap<String, ArrayList<SampleData>> sampleIDDataMap) {
//		System.out.println("sample ID data map " +  sampleIDDataMap.get(sampleID));
//		System.out.println(sampleIntervalMap);
//		System.out.println(sampleIntervalMap.get(sampleID));
		Interval range = sampleIntervalMap.get(sampleID);
		ArrayList<String> keys = new ArrayList<String>(nameReadsMap.keySet());
		for (int i = 0; i < keys.size(); i++) {
			ArrayList<Read> readList = nameReadsMap.get(keys.get(i));
			if (readList.size() > 2) {
				for (int j = 0; j < readList.size(); j++) {
					//Read read = readList.get(j);
					//System.out.println(readList);
				}
			} else if (readList.size() == 2) {
				if (readList.get(0).tlen > 0 && readList.get(1).tlen < 0 &&
						readList.get(0).origPos == readList.get(1).pnext &&
						readList.get(1).origPos == readList.get(0).pnext) {
					readList.get(0).side = "left";
					readList.get(1).side = "right";
//					System.out.println(readList.get(0).flag);
//					System.out.println(readList.get(1).flag);
				} else {
					//System.out.println(readList);
				}
//				System.out.println(readList.get(0));
//				System.out.println(readList.get(1));
				// inversion, both forward or both reverse
				if ((Integer.parseInt(readList.get(0).flag) & 16) == 0 && (Integer.parseInt(readList.get(0).flag) & 32) == 0) {
					readList.get(0).description = Constants.INVERSION_FORWARD_TYPE;
					readList.get(1).description = Constants.INVERSION_FORWARD_TYPE;
				} else if ((Integer.parseInt(readList.get(0).flag) & 16) != 0 && (Integer.parseInt(readList.get(0).flag) & 32) != 0) {
					readList.get(0).description = Constants.INVERSION_REVERSE_TYPE;
					readList.get(1).description = Constants.INVERSION_REVERSE_TYPE;
				} else if ((Integer.parseInt(readList.get(0).flag) == 81 && Integer.parseInt(readList.get(1).flag) == 161) ||
						(Integer.parseInt(readList.get(0).flag) == 145 && Integer.parseInt(readList.get(1).flag) == 97)) {
					readList.get(0).description = Constants.DUPLICATION_TYPE;
					readList.get(1).description = Constants.DUPLICATION_TYPE;
				} else {
					if ((Integer.parseInt(readList.get(0).flag) & 16) == 0 && (Integer.parseInt(readList.get(0).flag) & 32) != 0
							&& (Integer.parseInt(readList.get(1).flag) & 16) != 0 && (Integer.parseInt(readList.get(1).flag) & 32) == 0) {
						if (Math.abs(readList.get(0).tlen) < range.start && Math.abs(readList.get(1).tlen) < range.start) {
							readList.get(0).description = Constants.INSERTION_TYPE;
							readList.get(1).description = Constants.INSERTION_TYPE;
						} else if (Math.abs(readList.get(0).tlen) > range.end && Math.abs(readList.get(1).tlen) > range.end) {
							readList.get(0).description = Constants.DELETION_TYPE;
							readList.get(1).description = Constants.DELETION_TYPE;
						} else {
							readList.get(0).description = "";
							readList.get(1).description = "";
						}
					}
				}
//				System.out.println("d " + readList.get(0).description);
//				System.out.println("d " + readList.get(1).description);
			} 
		}
		//System.out.println(flags);
		//System.out.println(nameReadsMap);
	}
	
	public String createReadsFileData(LinkedHashMap<String, ArrayList<Read>> nameReadsMap,  
			int left, int right, int shadeLeft, int shadeRight,  
			HashMap<String, String> intervalsSubsequenceMap, String chr, 
			LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap,
			LinkedHashMap<String, HashMap<String, ArrayList<GeneralFeatureData>>> chrIntervalGeneDataMap,
			LinkedHashMap<String, ArrayList<GeneralFeatureData>> chrIntervalGFDataListMap, int count) {
		Interval interval = new Interval(left, right);
		String intervalKey = interval.keyString();
		//if (chr.length() > 0) {
			intervalKey = chr + "_" + intervalKey;
		//}
		String refSeq = intervalsSubsequenceMap.get(intervalKey);	
		return createReadsFileDataFromSeq(nameReadsMap, left, right, shadeLeft, shadeRight,  
				refSeq, chr, chrIntervalsMap, chrIntervalGeneDataMap,
				chrIntervalGFDataListMap, count);
	}
	
	public String createReadsFileDataFromSeq(LinkedHashMap<String, ArrayList<Read>> nameReadsMap,  
			int left, int right, int shadeLeft, int shadeRight,  
			String refSeq, String chr, 
			LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap,
			LinkedHashMap<String, HashMap<String, ArrayList<GeneralFeatureData>>> chrIntervalGeneDataMap,
			LinkedHashMap<String, ArrayList<GeneralFeatureData>> chrIntervalGFDataListMap, int count) {
		//System.out.println(chr);
		int keyStart = left;
		int keyEnd = right;
//		if (Constants.CORRECT_MERGED_INTERVALS_INDICES) {
//			keyStart += 1;
//			keyEnd += 1;
//		}
		
		Interval mergedInterval = new Interval(keyStart, keyEnd);
//		System.out.println("merged Map " + mergedIntervalsRangeIntervalsMap);
//		System.out.println("key string " + mergedInterval.keyString());
//		Interval interval = mergedIntervalsRangeIntervalsMap.get(mergedInterval.keyString());
//		left = interval.start;
//		right = interval.end;
		Interval interval = new Interval(left, right);
		String content = "";
		ArrayList<String> entries = new ArrayList<String>();
		ArrayList<String> noSupportEntries = new ArrayList<String>();
		ArrayList<String> diffChrEntries = new ArrayList<String>();
		if (Constants.ADJUST_INDICES) {
			content += (left + 1) + "\n";
			content += right + "\n";
		} else {
			content += left + "\n";
			content += (right - 1) + "\n";
		}
		
		//String refSeq = "";
		String seqEntry = "";
		
		//System.out.println(nameReadsMap);
		//int midpoint = (left + right)/2;
		//System.out.println(midpoint);
		ArrayList<String> keys = new ArrayList<String>(nameReadsMap.keySet());
		//System.out.println(keys.size());
		if (keys.size() == 0) {
			content += "1" + "\n";
			content += "02|reads" + "\n";
			content += "1" + "\n";
			content += "0|0|||FFFFFF";
		} else {
//			content += "0" + "\n";
//			content += "0" + "\n";
			String intervalKey = mergedInterval.keyString();
			if (chr.length() > 0) {
				intervalKey = chr + "_" + intervalKey;
			}
			//System.out.println(intervalKey);
			//System.out.println("diff "  + (right - left));
//			if (intervalsSubsequenceMap.containsKey(intervalKey)) {
				//refSeq = intervalsSubsequenceMap.get(intervalKey);
				//System.out.println(" seq length " + seq.length());
//				if (splitReadTypes) {
//					int numSeq = 1;
//					if (entries.size() > 0) {
//						numSeq += 1;
//					}
//					if (noSupportEntries.size() > 0) {
//						numSeq += 1;
//					}
//					if (diffChrEntries.size() > 0) {
//						numSeq += 1;
//					}
//					seqEntry = numSeq + "\n";
//				} else {
//					seqEntry += "2" + "\n";
//				}
//				seqEntry += "02|" + Constants.REF_NAME_READS_FILE + "\n";
//				seqEntry += "1" + "\n";
//				
				int refStart = interval.start;
				int refEnd = interval.end - 1;
				if (Constants.ADJUST_INDICES) {
					//refStart += 1;
					refEnd += 1;
				}
//				
//				seqEntry += refStart + "|" + refEnd + "|" + refSeq + "|" + Constants.REF_ID_READS_FILE + "||dna" + "\n"; 
//				//seqEntry += refStart + "|" + refEnd + "|" + refSeq.substring(1) + "|" + Constants.REF_ID_READS_FILE + "||dna" + "\n"; 
//				//content += refStart + "|" + refEnd + "|" + refSeq.substring(1) + "|" + Constants.REF_ID_READS_FILE + "||dna" + "\n"; 
				// ensure all indices have an entry
				for (int j = refStart - 1; j <= refStart + refSeq.length(); j++) {
					readDepthMap.put(Integer.toString(j), 0);
				}
				//System.out.println(readDepthMap);
//			} else {
//				seqEntry += "1" + "\n";
//				//content += "1" + "\n";
//			}
		}
		for (int i = 0; i < keys.size(); i++) {
			ArrayList<Read> readList = nameReadsMap.get(keys.get(i));
			processSequences(readList);
			Read startRead = readList.get(0);
			Read endRead = readList.get(readList.size() - 1);
			int endReadEnd = endRead.pos + endRead.processedSeq.length();
			int endIndex = right;
			if (ProjectConstants.ADJUST_READS_RIGHT_END) {
				int range = right - left;
				endIndex = right + (int)(ProjectConstants.READS_RIGHT_END_CORRECTION*range);
			}
			if (startRead.pos >= endIndex || endReadEnd <= left) {
				// ignore reads that would not be displayed to make files much smaller
			} else {
				String type = "dna";
				String color = Constants.DEFAULT_GRAY_COLOR_STRING;
				// if any reads overlap write each to separate line for now, will probably need advanced processing
				//boolean overlap = false;
				String sequence = "";
				String spacesString = "";
				for (int j = 0; j < readList.size(); j++) {
					String seq = readList.get(j).processedSeq;
					int refStart = left;
					String replacedSeq = processMismatches(refSeq, readList.get(j).processedSeq, refStart, readList.get(j).pos, left);
					readList.get(j).replacedSeq = replacedSeq;
					//System.out.println(readList.get(j).replacedSeq);
					int start = readList.get(j).pos;
					int end = start + seq.length();
					String insertionString = "";
					if (readList.get(j).insertionPositions != null) {
						insertionString = createInsertionString(readList.get(j).insertionPositions, 0, start);
					}
					if (readList.get(j).paired && readList.get(j).sameChr) {	
					//if (readList.get(j).rnext.equals("=")) {
						// paired, two or more present
						if (readList.size() > 1) {
							if (readList.size() == 2) {
								int startReadStart = startRead.pos;
								String startReadSeq = startRead.processedSeq;
								int startReadEnd = startReadStart + startReadSeq.length();
								int endReadStart = endRead.pos;
								if (startReadEnd > left && endReadStart > right) {
									String name = keys.get(i);
									if (readList.get(j).side.equals("left")) {
										type = "dna-pr_off";
										name = keys.get(i) + Constants.MATE_INFO_SPLIT_CHAR + endReadStart + "-" + endReadEnd;
										//offscreenCount += 1;
									} else {
										type = "dna";
									}
									color = getColorFromDescription(readList.get(j).description, readList.get(j).description);
									String entry = createEntry(readList.get(j).pos, (readList.get(j).pos + readList.get(j).processedSeq.length()),
											replacedSeq, name, color, type += insertionString, left, right);	
									if (entry.length() > 0) {
										if (splitReadTypes) {
											if (color.equals(Constants.DEFAULT_GRAY_COLOR_STRING)) {
												noSupportEntries.add(entry);
											} else {
												entries.add(entry);
											}
										} else {
											entries.add(entry);
										}
									}
								} else if (startReadEnd < left && endReadStart < right) {
									String name = keys.get(i);
									if (readList.get(j).side.equals("right")) {
										type = "dna-pl_off";
										name = keys.get(i) + Constants.MATE_INFO_SPLIT_CHAR + startReadStart + "-" + startReadEnd;
										//offscreenCount += 1;
									} else {
										type = "dna";
									}
									color = getColorFromDescription(readList.get(j).description, readList.get(j).description);
									String entry = createEntry(readList.get(j).pos, (readList.get(j).pos + readList.get(j).processedSeq.length()),
											replacedSeq, name, color, type += insertionString, left, right);
									if (entry.length() > 0) {
										if (splitReadTypes) {
											if (color.equals(Constants.DEFAULT_GRAY_COLOR_STRING)) {
												noSupportEntries.add(entry);
											} else {
												entries.add(entry);
											}
										} else {
											entries.add(entry);
										}
									}
								} else {
									if (j == 1 && startReadEnd > left && endReadStart < right) {
										type = "dna-pair";
										int startPos = startReadStart;
										String placeholder = Constants.SEQUENCE_PLACEHOLDER;
										sequence = readList.get(j - 1).replacedSeq + placeholder + readList.get(j).replacedSeq; 
										//sequence += replacedSeq + placeholder;
										int previousEnd = readList.get(j - 1).pos + readList.get(j - 1).processedSeq.length();
										int diff = readList.get(j).pos - previousEnd;
										if (diff > 0) {
											spacesString += diff;
											type += spacesString;
											color = getColorFromDescription(readList.get(j - 1).description, readList.get(j).description);
											insertionString = createInsertionStringFromList(readList, start);
											String entry = createEntry(startPos, (endReadStart + endRead.processedSeq.length()),
													sequence, keys.get(i), color, type += insertionString, left, right);	
											if (entry.length() > 0) {
												if (splitReadTypes) {
													if (color.equals(Constants.DEFAULT_GRAY_COLOR_STRING)) {
														noSupportEntries.add(entry);
													} else {
														entries.add(entry);
													}
												} else {
													entries.add(entry);
												}
											}
										} else {
											type = "dna";
											String prevEntry = createEntry(readList.get(j - 1).pos, previousEnd,
													readList.get(j - 1).replacedSeq, keys.get(i), color, type += insertionString, left, right);
											if (prevEntry.length() > 0) {
												entries.add(prevEntry);
											}
											type = "dna";
//											insertionString = "";
//											if (readList.get(j).insertionPositions != null) {
//												insertionString = createInsertionString(readList.get(j).insertionPositions, 0);
//												System.out.println(insertionString);
//											}
											String entry = createEntry(readList.get(j).pos, (endReadStart + endRead.processedSeq.length()),
													endRead.replacedSeq, keys.get(i), color, type += insertionString, left, right);
											if (entry.length() > 0) {
												if (splitReadTypes) {
													if (color.equals(Constants.DEFAULT_GRAY_COLOR_STRING)) {
														noSupportEntries.add(entry);
													} else {
														entries.add(entry);
													}
												} else {
													entries.add(entry);
												}
											}
										}
									}
								}
							}
						} else {
							// paired but only one present
							color = getColorFromDescription(readList.get(j).description, readList.get(j).description);
							if (readList.get(j).tlen < 0) {
								if (start > left) {
									type = "dna-pl_off";
									//offscreenCount += 1;
								}
							} else if (readList.get(j).tlen > 0) {
								if (end < right) {
									type = "dna-pr_off";
									//offscreenCount += 1;
								}
							}
							
							String entry = createEntry(readList.get(j).pos, (readList.get(j).pos + readList.get(j).processedSeq.length()),
									replacedSeq, keys.get(i), color, type += insertionString, left, right);	
							if (entry.length() > 0) {
								if (splitReadTypes) {
									if (color.equals(Constants.DEFAULT_GRAY_COLOR_STRING)) {
										noSupportEntries.add(entry);
									} else {
										entries.add(entry);
									}
								} else {
									entries.add(entry);
								}
							}
						}
					} else {
						String name = keys.get(i);
						if (readList.get(j).paired && !readList.get(j).sameChr) {
							color = Constants.READ_MATE_DIFF_CHR_COLOR_STRING;
							insertionString = "";
							name = keys.get(i) + Constants.MATE_INFO_SPLIT_CHAR + readList.get(j).rnext;
						}
						String entry = createEntry(readList.get(j).pos, (readList.get(j).pos + readList.get(j).processedSeq.length()),
								replacedSeq, name, color, type += insertionString, left, right);	
						if (entry.length() > 0) {
							if (splitReadTypes) {
								diffChrEntries.add(entry);
							} else {
								entries.add(entry);
							}
						}
					}
					//System.out.println(type);
				}
			}
		}
		//System.out.println(readDepthMap);
		if (readDepthMap.size() > 0) {
			int max = 0;
			content += "1" + "\n";
			content += "1|plot" + "\n";
			// line entry is counted in total
			content += (readDepthMap.size() + 1) + "\n";
			ArrayList<Integer> indices = new ArrayList<Integer>();
			for (String s:readDepthMap.keySet()) {
				int depth = readDepthMap.get(s);
				if (depth > max) {
					max = depth;
				}
				indices.add(Integer.parseInt(s));
			}
			Collections.sort(indices);
			content += "0|" + max + "|" + Constants.PLOT_HEIGHT + "|hist|" + Constants.PLOT_AXIS_COLOR_STRING + "|" + "\n";
			for (int k = 0; k < indices.size(); k++) {
				int depthEntry = readDepthMap.get(Integer.toString(indices.get(k)));
				content += indices.get(k) + "|" + depthEntry + "|Read_Depth|" + depthEntry + "|" + Constants.PLOT_COLOR_STRING + "|" + "\n";
			}
			content += "0" + "\n";
		} else {
			content += "0" + "\n";
			content += "0" + "\n";
		}
		
		if (chrIntervalGFDataListMap.size() > 0) {
			content += "1\n";
			content += "01|" + Constants.EXON_SAMPLE_NAME + "\n";
			ArrayList<String> exonEntries = new ArrayList<String>();
			
			System.out.println(chrIntervalGeneDataMap);
			if (chrIntervalGeneDataMap.containsKey(chr)) {
				for (String gene : chrIntervalGeneDataMap.get(chr).keySet()) {
					ArrayList<GeneralFeatureData> gfdList = chrIntervalGeneDataMap.get(chr).get(gene);
					if (gfdList.get(0).strand.equals("-")) {
						for (int g = gfdList.size() - 1; g >= 0; g--) {	
							Interval exonInterval = new Interval(gfdList.get(g).start, gfdList.get(g).end);
							if (UtilityMethods.isIntervalPortionVisible(exonInterval, chrIntervalsMap.get(chr).get(count))) {
								String entry = gfdList.get(g).start + "|" + gfdList.get(g).end + "|" 
										+ gene + "|" + gene + "|" + Constants.EXON_COLOR_STRING + "\n";
								exonEntries.add(entry);
							}
						}
					} else {
						for (int g = 0; g < gfdList.size(); g++) {
							Interval exonInterval = new Interval(gfdList.get(g).start, gfdList.get(g).end);
							if (UtilityMethods.isIntervalPortionVisible(exonInterval, chrIntervalsMap.get(chr).get(count))) {
								String entry = gfdList.get(g).start + "|" + gfdList.get(g).end + "|" 
										+ gene + "|" + gene + "|" + Constants.EXON_COLOR_STRING + "\n";
								exonEntries.add(entry);
							}
						}
					}
				}
			} else {
//				content += "1" + "\n";
//				content += "0|0||-|FFFFFF0" + "\n";
//				content += "0" + "\n";
			}

			content += exonEntries.size() + "\n";
			for (int e = 0; e < exonEntries.size(); e++) {
				content += exonEntries.get(e);
			}
			//System.out.println(chrIntervalGFDataListMap.get(chr));
//			content += (chrIntervalGFDataListMap.get(chr).size() + 1) + "\n";
//			content += chrIntervalsMap.get(chr).get(count).start + "|" + chrIntervalsMap.get(chr).get(count).end
//					+ "||" + chrIntervalGFDataListMap.get(chr).size() + "|DER BROWSER\n";
//			for (int g = 0; g < chrIntervalGFDataListMap.get(chr).size(); g++) {
////				String strand = chrIntervalGFDataListMap.get(chr).get(g).strand;
//				Interval exonInterval = chrIntervalGFDataListMap.get(chr).get(g).createInterval();
////				if (strand.equals("-")) {
////					content += exonInterval.end + "|" + exonInterval.start + "|" 
////							+ chrIntervalGFDataListMap.get(chr).get(g).attributesMap.get("gene").get(0) 
////							+ "|" + chrIntervalGFDataListMap.get(chr).get(g).strand + "|" + Constants.EXON_COLOR_STRING + "\n";
////				} else {
//					content += exonInterval.start + "|" + exonInterval.end + "|" 
//							+ chrIntervalGFDataListMap.get(chr).get(g).attributesMap.get("gene").get(0) 
//							+ "|" + chrIntervalGFDataListMap.get(chr).get(g).strand + "|" + Constants.EXON_COLOR_STRING + "\n";
////				}
//			}
			content += "0" + "\n";
		} else {
			content += "0" + "\n";
			content += "0" + "\n";
		}
		
		if (splitReadTypes) {
			int numSeq = 1;
			if (entries.size() > 0) {
				numSeq += 1;
			}
			if (noSupportEntries.size() > 0) {
				numSeq += 1;
			}
			if (diffChrEntries.size() > 0) {
				numSeq += 1;
			}
			seqEntry = numSeq + "\n";
		} else {
			seqEntry += "2" + "\n";
		}
		seqEntry += "02|" + Constants.REF_NAME_READS_FILE + "\n";
		seqEntry += "1" + "\n";
		
		int refStart = interval.start;
		int refEnd = interval.end - 1;
		if (Constants.ADJUST_INDICES) {
			//refStart += 1;
			refEnd += 1;
		}
		
		seqEntry += refStart + "|" + refEnd + "|" + refSeq + "|" + Constants.REF_ID_READS_FILE + "||dna" + "\n"; 
		//seqEntry += refStart + "|" + refEnd + "|" + refSeq.substring(1) + "|" + Constants.REF_ID_READS_FILE + "||dna" + "\n"; 
		//content += refStart + "|" + refEnd + "|" + refSeq.substring(1) + "|" + Constants.REF_ID_READS_FILE + "||dna" + "\n"; 
		content += seqEntry;
//		content += "02|reads" + "\n";
//		if (entries.size() == 0) {
//			content += "1" + "\n";
//			content += "-1|-1||||dna" + "\n";
//		} else {
//			entries = sortedEntries(entries);
//			//System.out.println(sortedEntries(entries));
//			//Collections.sort(entries);
//			content += entries.size() + "\n";
//			for (int i = 0; i < entries.size(); i++) {
//				content += entries.get(i);
//			}	
//		}
		if (splitReadTypes) {
			if (entries.size() > 0) {
				content += "02|" + Constants.READS_SUPPORTING_VARIANT_STRIPE_DESCRIPTION + "\n";
				if (entries.size() == 0) {
					content += "1" + "\n";
					content += "-1|-1||||dna" + "\n";
				} else {
					entries = sortedEntries(entries);
					//System.out.println(sortedEntries(entries));
					//Collections.sort(entries);
					content += entries.size() + "\n";
					for (int i = 0; i < entries.size(); i++) {
						content += entries.get(i);
					}	
				}
			}
//			content += "03|reads-no_variant" + "\n";
//			if (noSupportEntries.size() == 0) {
//				content += "1" + "\n";
//				content += "-1|-1||||dna" + "\n";
//			} else {
//				noSupportEntries = sortedEntries(noSupportEntries);
//				//System.out.println(sortedEntries(noSupportEntries));
//				//Collections.sort(noSupportEntries);
//				content += noSupportEntries.size() + "\n";
//				for (int i = 0; i < noSupportEntries.size(); i++) {
//					content += noSupportEntries.get(i);
//				}	
//			}
			if (noSupportEntries.size() > 0) {
				content += "03|" + Constants.READS_NOT_SUPPORTING_VARIANT_STRIPE_DESCRIPTION + "\n";
				noSupportEntries = sortedEntries(noSupportEntries);
				//System.out.println(sortedEntries(noSupportEntries));
				//Collections.sort(noSupportEntries);
				content += noSupportEntries.size() + "\n";
				for (int i = 0; i < noSupportEntries.size(); i++) {
					content += noSupportEntries.get(i);
				}
			}
			if (diffChrEntries.size() > 0) {
				content += "04|" + Constants.READS_MATES_DIFF_CHR_STRIPE_DESCRIPTION + "\n";
				diffChrEntries = sortedEntries(diffChrEntries);
				//System.out.println(sortedEntries(diffChrEntries));
				//Collections.sort(diffChrEntries);
				content += diffChrEntries.size() + "\n";
				for (int i = 0; i < diffChrEntries.size(); i++) {
					content += diffChrEntries.get(i);
				}	
			}
//			content += "04|reads-mate-diff_chr" + "\n";
//			if (diffChrEntries.size() == 0) {
//				content += "1" + "\n";
//				content += "-1|-1||||dna" + "\n";
//			} else {
//				diffChrEntries = sortedEntries(diffChrEntries);
//				//System.out.println(sortedEntries(diffChrEntries));
//				//Collections.sort(diffChrEntries);
//				content += diffChrEntries.size() + "\n";
//				for (int i = 0; i < diffChrEntries.size(); i++) {
//					content += diffChrEntries.get(i);
//				}	
//			}
		} else {
			content += "02|" + Constants.READS_STRIPE_DESCRIPTION + "\n";
			if (entries.size() == 0) {
				content += "1" + "\n";
				content += "-1|-1||||dna" + "\n";
			} else {
				entries = sortedEntries(entries);
				//System.out.println(sortedEntries(entries));
				//Collections.sort(entries);
				content += entries.size() + "\n";
				for (int i = 0; i < entries.size(); i++) {
					content += entries.get(i);
				}	
			}
		}
		if (shadeLeft > -1 && shadeRight > -1) {
			content += "1" + "\n";
			content += "05|shades" + "\n";
			content += keys.size() + "\n";
			content += shadeLeft + "|" + shadeRight + "|||" + Constants.SHADE_COLOR_STRING + "|shades";
		} else {
			content += "0" + "\n";
			content += "0" + "\n";
		}
//		if (shadesList.size() == 0) {
//			content += "0" + "\n";
//			content += "0" + "\n";
//		} else {
//			content += shadesList.size() + "\n";
//			for (int s = 0; s < shadesList.size(); s++) {
//				int shadeLeft = shadesList.get(s).start;
//				int shadeRight = shadesList.get(s).end;
//				//if (shadeLeft > -1 && shadeRight > -1) {
//					content += "05|shades" + "\n";
//					content += keys.size() + "\n";
//					content += shadeLeft + "|" + shadeRight + "|||" + Constants.SHADE_COLOR_STRING + "|shades";
//				//}
//			}
//		}
		//System.out.println(content);
		return content;
	}
	
	private String createEntry(int start, int end, String sequence, String id, String color, String type, int left, int right) {
		String entry = "";
		// something is happening in the processing of sequences where some entries are getting through
		// that are not within the range of the window
		if (end <= left || start >= right) {
			
		} else {
			entry = start + "|" + (end - 1) + "|" + sequence + "|" + id + "|" + color + "|" + type + "\n";
		}
		return entry;
	}
	
	private String createInsertionStringFromList(ArrayList<Read> readList, int pos) {
		String entry = "";
		int startPos = readList.get(0).pos;
		entry += createInsertionString(readList.get(0).insertionPositions, 0, pos);
		for (int i = 1; i < readList.size(); i++) {
			int diff = readList.get(i).pos - startPos;
			entry += createInsertionString(readList.get(i).insertionPositions, diff, pos);
		}
		return entry;
	}
	
	private String createInsertionString(ArrayList<Integer> insertionPositions, int startIndex, int pos) {
		String entry = "";
		for (int i = 0; i < insertionPositions.size(); i++) {
			entry += "-i" + (insertionPositions.get(i) + startIndex);
			int insPos = pos + insertionPositions.get(i) + startIndex;
			String insPosStr = Integer.toString(insPos);
			if (indelInsDepthMap.containsKey(insPosStr)) {
				int depth = indelInsDepthMap.get(insPosStr);
				depth += 1; 
				indelInsDepthMap.put(insPosStr, depth);
			} else {
				indelInsDepthMap.put(insPosStr, 1);
			}
		}
		return entry;
	}
	
	// based on http://bioinformatics.cvr.ac.uk/blog/java-cigar-parser-for-sam-format/
	public void processSequences(ArrayList<Read> readList) {
		for (int i = 0; i < readList.size(); i++) {
			String seq = readList.get(i).sequence;
//			System.out.println(seq);
			String processedSeq = "";
			int startPos = 1;
			int insertionPos = 1;
			ArrayList<String> cigarValues = splitCIGAR(readList.get(i).cigar);
//			System.out.println(cigarValues);
			readList.get(i).insertionPositions = new ArrayList<Integer>();
			for (int j = 0; j < cigarValues.size(); j++) {
				String cVal = cigarValues.get(j);
//				System.out.println(cVal);
				int cLen = Integer.parseInt(cVal.substring(0,cVal.length()-1));
				char cLetter = cVal.toUpperCase().charAt(cVal.length()-1);
//				System.out.println("c " + cLen);
//				System.out.println("s " + startPos);
				switch (cLetter){
				case 'M':          
				case 'X': 
				case '=':
					processedSeq += seq.substring(startPos - 1, startPos + cLen - 1);
					startPos += cLen;	
					insertionPos += cLen;
					break;
				case 'S':
					processedSeq += seq.substring(startPos - 1, startPos + cLen - 1).toLowerCase();
					startPos += cLen;
					insertionPos += cLen;
					// soft clipping not part of alignment, need to adjust position if soft clipping is at the beginning
					if (j == 0)
					readList.get(i).pos -= cLen;
					break;
				case 'I':
					//System.out.println(readList.get(i).toString());
					//System.out.println(readList.get(i).cigar);
					//System.out.println(insertionPos);
					readList.get(i).insertionPositions.add(insertionPos - 1);
					startPos += cLen;
					break;
				case 'H':
					break;
				case 'D':
					char[] charArray = new char[cLen];
					Arrays.fill(charArray, Constants.DELETION_PLACEHOLDER_CHAR);
					String str = new String(charArray);
					processedSeq += str;
					String pos = Integer.toString((readList.get(i).pos + processedSeq.length()));
					if (indelDelDepthMap.containsKey(pos)) {
						int depth = indelDelDepthMap.get(pos);
						depth += 1; 
						indelDelDepthMap.put(pos, depth);
					} else {
						indelDelDepthMap.put(pos, 1);
					}
					insertionPos += cLen;
					break;
				case 'P':
					char[] charArrayP = new char[cLen];
					Arrays.fill(charArrayP, Constants.P_PLACEHOLDER_CHAR);
					String strP = new String(charArrayP);
					processedSeq += strP;
					insertionPos += cLen;
					break;
				case 'N':
					char[] charArrayN = new char[cLen];
					Arrays.fill(charArrayN, Constants.N_PLACEHOLDER_CHAR);
					String strN = new String(charArrayN);
					processedSeq += strN;
					insertionPos += cLen;
					break;
				}	
			}
			readList.get(i).processedSeq = processedSeq;
		}
	}
	
	// from http://bioinformatics.cvr.ac.uk/blog/java-cigar-parser-for-sam-format/
	/*A function to split the CIGAR value into a list of CIGAR elements*/
	private ArrayList<String> splitCIGAR(String cigarString) {
	    //One cigar component is a number of any digit, followed by a letter or =
		Pattern cigarPattern = Pattern.compile("[\\d]+[a-zA-Z|=]");
	    ArrayList<String> cigarElems = new ArrayList<String>();
	    Matcher matcher = cigarPattern.matcher(cigarString);
	    while (matcher.find()) {
		cigarElems.add( matcher.group() );
	    }
	    return cigarElems;
	}
	
	public String processMismatches(String ref, String seq, int refStart, int seqStart, int left) {
		int diff = seqStart - refStart;
		//System.out.println(diff);
		
		for (int i = 0; i < seq.length(); i++) {
			String refPos = Integer.toString(left + diff + i);
//			char refChar = ref.charAt(diff + i);
//			char seqChar = seq.charAt(i);
//			System.out.println("ref " + refChar);
//			System.out.println("seq " + seqChar);
	
			if ((i + 1) <= seq.length() && (diff + i + 1) <= ref.length() && (diff + i) > 0) {
				String refBase = ref.substring(diff + i, diff + i + 1);
				String seqBase = seq.substring(i, i + 1);
//				System.out.println("ref " + refBase);
//				System.out.println("seq " + seqBase);
				if (seqBase.equals("A") || seqBase.equals("T") ||
						seqBase.equals("G") || seqBase.equals("C")) {
//				if (seqBase.toUpperCase().equals("A") || seqBase.toUpperCase().equals("T") ||
//						seqBase.toUpperCase().equals("G") || seqBase.toUpperCase().equals("C")) {
					if (readDepthMap.containsKey(refPos)) {
						int count = readDepthMap.get(refPos);
						count += 1;
						readDepthMap.put(refPos, count);
					}
				}
				
				if (!seqBase.toUpperCase().equals(refBase)) {
//					System.out.println("ref " + refBase);
//					System.out.println("seq mm " + seqBase);
//					System.out.println("ref pos " + refPos);
					StringBuilder sb = new StringBuilder(seq);
					seqBase = seqBase.toLowerCase();
					sb.setCharAt(i, seqBase.toCharArray()[0]);
					seq = sb.toString();
//					System.out.println(seq);
				}
			}
		}
		//System.out.println(seq);
		return seq;
	}
	
	private String getColorFromDescription(String description1, String description2) {
		if (description1 != null && description2 != null && description1.equals(description2)) {
			if (description1.equals(Constants.DELETION_TYPE)) {
				return Constants.DELETION_COLOR_STRING;
			} else if (description1.equals(Constants.INSERTION_TYPE)) {
				return Constants.INSERTION_COLOR_STRING;
			} else if (description1.equals(Constants.INVERSION_TYPE) || description1.equals(Constants.INVERSION_FORWARD_TYPE) || description1.equals(Constants.INVERSION_REVERSE_TYPE)) {
				return Constants.INVERSION_COLOR_STRING;
			} else if (description1.equals(Constants.DUPLICATION_TYPE)) {
				return Constants.DUPLICATION_COLOR_STRING;
			}
		}
	
		return Constants.DEFAULT_GRAY_COLOR_STRING;
	}
	
	private ArrayList<String> sortedEntries(ArrayList<String> entries) {
		ArrayList<String> sortedEntries = new ArrayList<String>();
		
		ArrayList<String> fromValuesList = new ArrayList<String>();
		HashMap<String, ArrayList<String>> fromValuesMap = new HashMap<String, ArrayList<String>>();
		for (int i = 0; i < entries.size(); i++) {
			String[] entry = entries.get(i).split("\\|");
			if (!fromValuesList.contains(entry[0])) {
				fromValuesList.add(entry[0]);
			}
			if (!fromValuesMap.containsKey(entry[0])) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(entries.get(i));
				fromValuesMap.put(entry[0], list);
			} else {
				ArrayList<String> list = fromValuesMap.get(entry[0]);
				list.add(entries.get(i));
				fromValuesMap.put(entry[0], list);
			}
		}
		Collections.sort(fromValuesList, ic);
		//System.out.println(fromValuesList);
		
		for (int j = 0; j < fromValuesList.size(); j++) {
			for (int k = 0; k < fromValuesMap.get(fromValuesList.get(j)).size(); k++) {
				sortedEntries.add(fromValuesMap.get(fromValuesList.get(j)).get(k));
			}
		}
		
		return sortedEntries;
	}
	
	public static void main(String args[]) {
		
	}
	
}
