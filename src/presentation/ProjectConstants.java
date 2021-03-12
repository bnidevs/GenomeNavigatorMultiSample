package presentation;

public class ProjectConstants {

	// true for Single Sample, false for Multi Sample
	public static final boolean DRAW_DATA_AVAILABLE_READ_BORDERS = true;
	//public static final boolean DRAW_DATA_AVAILABLE_READ_BORDERS = false;
	
	// currently only used in testing derBrowser1
	public static final boolean READS_WINDOW_RANGE_FROM_VARIANT = false;
	//public static boolean READS_WINDOW_RANGE_FROM_VARIANT = true;
	
	// False for Single Sample, True for Multi Sample
	public static final boolean MERGE_INTERVALS = false;
	//public static boolean MERGE_INTERVALS = true;

	public static final boolean ADJUST_READS_RIGHT_END = false;
	
	// potential fix for objects not reaching right end
	//public static final boolean ADJUST_READS_RIGHT_END = true;
	public static final double READS_RIGHT_END_CORRECTION = 0.3;

	// True for Single Sample
	public static final boolean SPLIT_READ_TYPES = true;
	//public static final boolean SPLIT_READ_TYPES = false;

	// This is for backward compatibility for old projects where fasta files were generated
	// using Java. In future all files should be created from samtools which is much faster
	//public static final boolean REF_FROM_JAVA = true;
	// False for files created using samtools
	public static final boolean REF_FROM_JAVA = false;

	// used in VCF readers for backwards compatibility for old versions of GROM where chr was put to lower case
	public static final boolean CHR_TO_UPPER_CASE = false;
	//public static final boolean CHR_TO_UPPER_CASE = true;

}
