package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class FAIChrIndexWriter {
	int count = 0;
	String content = "";
	
	public void processFile(String inFilePath, String outFilePath, ArrayList<String> scaffolds)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(inFilePath);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String line = reader.readLine();
			processLine(line, scaffolds);
			while (line != null) {
				line = reader.readLine();
				processLine(line, scaffolds);
			}
			fis.close();
			//System.out.println(content);
			File out = new File(outFilePath);
			if (!out.exists()) {
				TextFileWriter writer = new TextFileWriter();
				//String content = "";
				writer.writeFile(outFilePath, content);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processLine(String line, ArrayList<String> scaffolds) {
		if (line != null) {
			count += 1;
			if (line.contains(">")) {
				//System.out.println(line);
				String[] splitLine = line.split(" ");
				//System.out.println(splitLine[0]);
				for (int i = 0; i < scaffolds.size(); i++) {
					String scaffoldEntry = ">" + scaffolds.get(i);
					if (splitLine[0].equals(scaffoldEntry)) {
						content += scaffolds.get(i) + "\t" + count + "\n";
						System.out.println("chr " + scaffolds.get(i));
					}
				}
			} 
		}
	}
	
	public static void main(String args[]) {
//		File file1 = new File("E:/bear/GCF_000687225.1_UrsMar_1.0_genomic.fna.fai");
//		FAIFileReader faiReader = new FAIFileReader();
//		faiReader.readFile(file1.getAbsolutePath());
//		ArrayList<String> scaffolds = faiReader.scaffolds;
//		
//		File inFilePath = new File("E:/bear/GCF_000687225.1_UrsMar_1.0_genomic.fna");
//		String outFilePath = "E:/bear/GCF_000687225.1_UrsMar_1.0_genomic.fna.fai.indexes.txt";
//		FAIChrIndexWriter writer = new FAIChrIndexWriter();
//		writer.processFile(inFilePath.getAbsolutePath(), outFilePath, scaffolds);
		
		// panda ref
//		File file1 = new File("etc/bear/ref_panda/panda.scafSeq.gapFilled.noMito.GeneScaffold.fa.fai");
//		FAIFileReader faiReader = new FAIFileReader();
//		faiReader.readFile(file1.getAbsolutePath());
//		ArrayList<String> scaffolds = faiReader.scaffolds;
//		
//		File inFilePath = new File("etc/bear/ref_panda/panda.scafSeq.gapFilled.noMito.GeneScaffold.fa");
//		String outFilePath = "etc/bear/ref_panda/panda.scafSeq.gapFilled.noMito.GeneScaffold.fa.fai.indexes.txt";
//		FAIChrIndexWriter writer = new FAIChrIndexWriter();
//		writer.processFile(inFilePath.getAbsolutePath(), outFilePath, scaffolds);
		
		// brown ref
		File file1 = new File("etc/bear/ref_brown/GCF_003584765.1_ASM358476v1_genomic.fna.fai");
		FAIFileReader faiReader = new FAIFileReader();
		faiReader.readFile(file1.getAbsolutePath());
		ArrayList<String> scaffolds = faiReader.scaffolds;
		
		File inFilePath = new File("etc/bear/ref_brown/GCF_003584765.1_ASM358476v1_genomic.fna");
		String outFilePath = "etc/bear/ref_brown/GCF_003584765.1_ASM358476v1_genomic.fna.fai.indexes.txt";
		FAIChrIndexWriter writer = new FAIChrIndexWriter();
		writer.processFile(inFilePath.getAbsolutePath(), outFilePath, scaffolds);
	}

}
