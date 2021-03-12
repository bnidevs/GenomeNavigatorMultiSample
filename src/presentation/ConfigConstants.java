package presentation;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class ConfigConstants {
	
	public static final String DEFAULT_USE_FILE_NAMES_ENTRY = "1";
	
	public static final String CONFIG_HEADER_COMMENT1 = "# Values after the equal sign and "
			+ "before comments starting with # can be changed, but descriptions before the "
			+ "equal sign should not be changed and lines should not be moved, added or deleted.";
	public static final String CONFIG_HEADER_COMMENT2 = "# Every sample used must have a SAM "
			+ "file and VCF file that includes data for all intervals in the intervals file.";
	public static final String CONFIG_HEADER_COMMENT3 = "# File_Sample_Name_File_path is "
			+ "required if Sample_Name_Species_file_path is provided and Use_File_Names is false.";
	public static final int CONFIG_HEADER_LENGTH = 3;

	public static final String PROJECT_PATH_DESCRIPTION = "Project_path";
	public static final String PROJECT_PATH_COMMENT = "# path where output data is written. Required.";
	public static final int PROJECT_PATH_LINE = 0;
	
	public static final String SAM_FILES_PATH_DESCRIPTION = "SAM_files_path";
	public static final String SAM_FILES_PATH_COMMENT = "# path to location of SAM files to be used. Required.";
	public static final int SAM_FILES_PATH_LINE = 1;
	
	public static final String VCF_FILES_PATH_DESCRIPTION = "VCF_files_path";
	public static final String VCF_FILES_PATH_COMMENT = "# path to location of VCF4.2 files to be used. Required.";
	public static final int VCF_FILES_PATH_LINE = 2;
	
	public static final String REF_FILE_PATH_DESCRIPTION = "Reference_file_path";
	public static final String REF_FILE_PATH_COMMENT = "# path to reference file in fasta format that is "
			+ "indexed and fai file must exist in the same directory. Required.";
	public static final int REF_FILE_PATH_LINE = 3;
	
	public static final String INTERVALS_FILE_PATH_DESCRIPTION = "Intervals_file_path";
	public static final String INTERVALS_FILE_PATH_COMMENT = "# Text file containing intervals of "
			+ "interest. Tab separated file. Format: chromosome_name (tab) start_position (tab) "
			+ "end_position. Required for multi-sample analysis.";
	public static final int INTERVALS_FILE_PATH_LINE = 4;
	
	public static final String MEAN_FILES_PATH_DESCRIPTION = "Mean_files_path";
	public static final String MEAN_FILES_PATH_COMMENT = "# path to mean files. Required.";
	public static final int MEAN_FILES_PATH_LINE = 5;
	
	public static final String GFF_FILE_PATH_DESCRIPTION = "GFF_file_path";
	public static final String GFF_FILE_PATH_COMMENT = "# path to gff file in NCBI type format. "
			+ "This file is not required but will allow gene information to be plotted.";
	public static final int GFF_FILE_PATH_LINE = 6;
	
	public static final String USE_FILE_NAMES_DESCRIPTION = "Use_File_Names";
	public static final String USE_FILE_NAMES_COMMENT = "#0-false, 1-true. If false sample names "
			+ "must be associated with file names (File_Sample_Name_file_path below).";
	public static final int USE_FILE_NAMES_LINE = 7;
	
	public static final String FILE_SAMPLE_NAME_PATH_DESCRIPTION = "File_Sample_Name_file_path";
	public static final String FILE_SAMPLE_NAME_PATH_COMMENT = "# path that associates VCF file names to sample "
			+ "names. CSV file, Format: fileName,sampleName,alias (optional). If not provided file names are "
			+ "used as sample names. "
			+ "Alias column is used to assign different names to samples to "
			+ "simplify names or for privacy issues such as those related to use of human samples.";
	public static final int FILE_SAMPLE_NAME_PATH_LINE = 8;
	
	public static final String SAMPLE_NAME_SPECIES_PATH_DESCRIPTION = "Sample_Name_Species_file_path";
	public static final String SAMPLE_NAME_SPECIES_PATH_COMMENT = "# path to file that associates sample names "
			+ "to species name. CSV file, Format: sampleName,speciesName. If Use_File_Names is true, file "
			+ "will contain fileName,speciesName.";
	public static final int SAMPLE_NAME_SPECIES_PATH_LINE = 9;
	
	public static final String SPECIES_NAME_COLOR_PATH_DESCRIPTION = "Species_Name_Color_file_path";
	public static final String SPECIES_NAME_COLOR_PATH_COMMENT = "# path to file that associates "
			+ "species names to colors (hex string with no #, example 808080 = gray). If not provided, "
			+ "random colors are used. CSV file, Format: speciesName,color";
	public static final int SPECIES_NAME_COLOR_PATH_LINE = 10;
	
	public static final String VCF_TYPE_DESCRIPTION = "VCF_file type";
	public static final String VCF_TYPE_COMMENT = "# type of VCF file. Default is .vcf but can be SV_INDEL.vcf "
			+ "for example. Entry is useful only if providing a vcf folder to filter files by ending.";
	public static final int VCF_TYPE_LINE = 11;
	
	public static final String INCL_SPEC_TYPE_DESCRIPTION = "Include_Species_in_Name";
	public static final String INCL_SPEC_TYPE_COMMENT = "# 0-false, 1-true. If false sample names must all be unique.";
	public static final int INCL_SPEC_TYPE_LINE = 12;
	
	public static final String REFERENCE_SPECIES_NAME_DESCRIPTION = "Reference species";
	public static final String REFERENCE_SPECIES_NAME_COMMENT = "# Optional, white space separated. This field is used to generate URL when "
			+ "clicking on an exon and will open NCBI page for gene in reference species if species is present in NCBI.";
	public static final int REFERENCE_SPECIES_NAME_LINE = 13;
	
	public static final String VCF_MAX_P_VALUE_DESCRIPTION = "Maximum P-Value for Variants from VCF";
	public static final String VCF_MAX_P_VALUE_COMMENT = "# Optional. Format 1.00E-06, if not provided all variants will be processed.";
	public static final int VCF_MAX_P_VALUE_LINE = 14;
	
	public static final String VCF_P_VALUE_COLUMNS_DESCRIPTION = "P-Value Column FORMAT IDs";
	public static final String VCF_P_VALUE_COLUMNS_COMMENT = "# Required if specifying a non-zero P-value. Comma-separated values "
			+ "e.g. SPR,EPR. Highest value used for filtering data.";
	public static final int VCF_P_VALUE_COLUMNS_LINE = 15;
	
	public static final String MEAN_VALUES_DESCRIPTION = "Minimum and Maximum Insert Size";
	public static final String MEAN_VALUES_COMMENT = "# Required if VCF not generated using GROM. Minimum and Maximum Insert Size as Comma-separated values "
			+ "e.g. 400,500";
	public static final int MEAN_VALUES_LINE = 16;
	
	public static final String MAX_INTERVAL_SIZE_DESCRIPTION = "Maximum Interval Size to be displayed in one window";
	public static final String MAX_INTERVAL_SIZE_COMMENT = "# Optional. Default = " + Constants.MAX_INTERVAL_WIDTH + ". Large intervals increase processing time.";
	public static final int MAX_INTERVAL_SIZE_LINE = 17;
	
	
	
	
}
