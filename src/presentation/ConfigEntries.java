package presentation;

import java.util.ArrayList;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class ConfigEntries {
	String projectPath;
	String samFilesPath;
	String vcfFilesPath;
	String refFilePath;
	String intervalsFilePath;
	String meanFilesPath;
	boolean useFileNames;
	String fileSampleNameFilePath;
	String sampleNameSpeciesFilePath;
	String speciesNameColorFilePath;
	String vcfType;	
	boolean includeSpeciesInName;
	String refSpecies;
	double maxPValue;
	String pValueFormatIDs;
	String sampleName;
	String meanValues;
	int maxInterval;
	int intervalsSubFilesSize;
	String gffFilePath;
	ArrayList<GFFInfo> gffInfoList;
	
	public String toString(){
		return(" projectPath " + projectPath + "\n"
				+ " samFilesPath " + samFilesPath + "\n"
				+ " vcfFilesPath " + vcfFilesPath + "\n" 
				+ " refFilePath " + refFilePath + "\n"
//				+ " intervalsFilePath " + intervalsFilePath + "\n" 
				+ " meanFilesPath " + meanFilesPath + "\n"
//				+ " useFileNames " + useFileNames + "\n" 
//				+ " fileSampleNameFilePath " + fileSampleNameFilePath + "\n"
//				+ " sampleNameSpeciesFilePath " + sampleNameSpeciesFilePath + "\n" 
//				+ " speciesNameColorFilePath " + speciesNameColorFilePath + "\n" 
//				+ " vcfType " + vcfType + "\n"
				+ " refSpecies " + refSpecies + "\n"
				+ " maxPValue " + maxPValue + "\n"
				+ " pValueFormatIDs " + pValueFormatIDs + "\n"
				+ " meanValues " + meanValues + "\n"
				+ " maxInterval " + maxInterval + "\n"
				+ " intervalsSubFilesSize " + intervalsSubFilesSize + "\n" 
//				+ " includeSpeciesInName " + includeSpeciesInName
				+ " gffInfoList " + gffInfoList);
	}

}
