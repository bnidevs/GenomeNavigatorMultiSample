package presentation;

import java.util.ArrayList;

public class ShellScriptCreator {
	
	public String jarName = "gndp-single-no_gff092619a.jar";
	public String configName = "brown_bear-14-polar_ref-amarel_br14-5000test.conf";
	String suffixes = "";
	
	//public String partition = "p_ccib_1";
	public String partition = "p_grigorie_1";
	
	public String time = "72:00:00";
	public String jValue = "gndp";
	public String mem = "120000";
	
	private String line1 = "#!/bin/bash";
	private String line2 = "#SBATCH -J ";
	private String line3 = "#SBATCH --partition=";
	private String line4 = "#SBATCH -N1";
	private String line5 = "#SBATCH -n1";
	private String line6 = "#SBATCH --mem=";
	private String line7 = "#SBATCH --exclusive";
	private String line8 = "#SBATCH --time=";
	private String line9 = "";
	private String line10 = "date";
	public String loadJavaLine = "module load java";
	private String endLine1 = "wait";
	private String endLine2 = "date";
	
	private String content = "";
	private int splitLineNum = 8;
	
	public void writeFile(String outFile, ArrayList<Interval> intervalsList) {
		if (partition.equals("nonpre")) {
			mem = "100000";
			// 3 day time limit for nonpre
			time = "72:00:00";
		}
			
		content += line1 + "\n";
		content += line2 + jValue + "\n";
		content += line3 + partition + "\n";
		content += line4 + "\n";
		content += line5 + "\n";
		content += line6 + mem + "\n";
		content += line7 + "\n";
		content += line8 + time + "\n";
		content += line9 + "\n";
		content += line10 + "\n";
		content += loadJavaLine + "\n";
		
		for (int i = 0; i < intervalsList.size(); i++) {
			content += sampleLine(intervalsList.get(i));
			if ((i + 1) % splitLineNum == 0) {
				content += "wait\n";
			}
		}
		content += endLine1 + "\n";
		content += endLine2 + "\n";
		System.out.println(content);
		TextFileWriter writer = new TextFileWriter();
		writer.writeFile(outFile, content);
		writer.writeFile("interval_suffixes.txt", suffixes);
	}
	
	private String sampleLine(Interval interval) {
		suffixes += interval.keyString() + "\n";
		return "java -jar " + jarName + " " + configName + " \"" + interval.keyString() + "\" & " + "\n";
	}
	
	public static void main(String args[]) {
		ShellScriptCreator creator = new ShellScriptCreator();
		
		
		creator.partition = "p_grigorie_1";
		//creator.partition = "nonpre";
		//creator.mem = "";
		//creator.time = "100:00:00";
		
		System.out.println(creator.content);
		
	}

}
