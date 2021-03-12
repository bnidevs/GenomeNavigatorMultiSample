package presentation;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class ConfigConstantsIVP {

	public static final String CONFIG_HEADER_COMMENT1 = "# Values after the equal sign and "
			+ "before comments starting with # can be changed, but descriptions before the "
			+ "equal sign should not be changed and lines should not be moved, added or deleted.";
	public static final String CONFIG_HEADER_COMMENT2 = "# GFF_information is for one file and "
			+ "one feature. Additional GFF files or features from the same file can be added by "
			+ "duplication of the \"GFF_information_1= #\" line and replacing 1 with 2, 3..., "
			+ "e.g. \"GFF_information_2= #\"";
	public static final String CONFIG_HEADER_COMMENT3 = "# Duplication of GFF comments (text after #) "
			+ "is not necessary for additional GFF lines.";
	public static final int CONFIG_HEADER_LENGTH = 3;

	public static final String PROJECT_PATH_DESCRIPTION = "Project_path";
	public static final String PROJECT_PATH_COMMENT = "# Path where output data is written. Required.";
	public static final int PROJECT_PATH_LINE = 0;
	
	public static final String SAM_FILES_PATH_DESCRIPTION = "SAM_files_path";
	public static final String SAM_FILES_PATH_COMMENT = "# Path to location of SAM files to be used. Required.";
	public static final int SAM_FILES_PATH_LINE = 1;
	
	public static final String VCF_FILES_PATH_DESCRIPTION = "VCF_file_path";
	public static final String VCF_FILES_PATH_COMMENT = "# Path to location of VCF4.2 file to be used. Required.";
	public static final int VCF_FILES_PATH_LINE = 2;
	
	public static final String REF_FILE_PATH_DESCRIPTION = "Reference_file_path";
	public static final String REF_FILE_PATH_COMMENT = "# Path to reference file in fasta format that is "
			+ "indexed and fai file must exist in the same directory. Required.";
	public static final int REF_FILE_PATH_LINE = 3;
	
	public static final String MEAN_FILES_PATH_DESCRIPTION = "Mean_files_path";
	public static final String MEAN_FILES_PATH_COMMENT = "# Path to mean file produced by GROM which "
			+ "supplies minimum and maximum insert sizes.";
	public static final int MEAN_FILES_PATH_LINE = 4;
	
	public static final String MIN_MAX_INS_SIZE_DESCRIPTION = "Minimum and Maximum Insert Size";
	public static final String MIN_MAX_INS_SIZE_COMMENT = "# Required if VCF not generated using GROM and mean files "
			+ "not available. Minimum and Maximum Insert Size as Comma-separated values e.g. 400,500";
	public static final int MIN_MAX_INS_SIZE_LINE = 5;

	public static final String VCF_MAX_P_VALUE_DESCRIPTION = "Maximum P-Value for Variants from VCF";
	public static final String VCF_MAX_P_VALUE_COMMENT = "# Optional. Format 1.00E-06, if not provided 0 will be used and all variants will be processed.";
	public static final int VCF_MAX_P_VALUE_LINE = 6;
	
	public static final String VCF_P_VALUE_COLUMNS_DESCRIPTION = "P-Value Column FORMAT IDs";
	public static final String VCF_P_VALUE_COLUMNS_COMMENT = "# Required if specifying a non-zero P-value. Comma-separated values "
			+ "e.g. SPR,EPR. Highest value used for filtering data.";
	public static final int VCF_P_VALUE_COLUMNS_LINE = 7;
	
	public static final String SAMPLE_NAME_DESCRIPTION = "Sample_Name";
	public static final String SAMPLE_NAME_COMMENT = "# Optional. If not provided, file name minus .vcf extension will be used.";
	public static final int SAMPLE_NAME_LINE = 8;
	
	public static final String MAX_INTERVAL_SIZE_DESCRIPTION = "Maximum Interval Size to be displayed in one window";
	public static final String MAX_INTERVAL_SIZE_COMMENT = "# Optional. Default = " + Constants.MAX_INTERVAL_WIDTH + ". Large intervals increase processing time.";
	public static final int MAX_INTERVAL_SIZE_LINE = 9;
	
	public static final String INTERVALS_SUBFILES_SIZE_DESCRIPTION = "Intervals Subfiles Size";
	public static final String INTERVALS_SUBFILES_SIZE_COMMENT = "# Optional. Default = " + Constants.INTERVALS_SUBFILES_SIZE  
			+ ". Denotes the number of intervals where data will be processed concurrently. "
			+ "Increasing the size of subfiles will require more memory.";
	public static final int INTERVALS_SUBFILES_SIZE_LINE = 10;
	
	public static final String REFERENCE_SPECIES_NAME_DESCRIPTION = "Reference species";
	public static final String REFERENCE_SPECIES_NAME_COMMENT = "# Optional, white space separated. This field is used to generate URL when "
			+ "clicking on an exon and will open NCBI page for gene in reference species if species is present in NCBI.";
	public static final int REFERENCE_SPECIES_NAME_LINE = 11;
	
	public static final String GFF_INFO_DESCRIPTION = "GFF_information";
	public static final String GFF_INFO_COMMENT = "# This information is not required but will allow gene information to be plotted. Format: "
			+ "Path to file,feature,color (hexadecimal, e.g. FF0000 for red). If color is not specified default colors will be used.";
	public static final int GFF_INFO_LINE = 12;
	
	
	
}
