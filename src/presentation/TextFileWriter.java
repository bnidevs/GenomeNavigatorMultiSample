package presentation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * Used by GNDP and variant prediction
 *
 */
// based on Mkyong.com How to write to file in Java – BufferedWriter
public class TextFileWriter {

	//private static final String FILENAME = "etc/filename.txt";
	
	public void writeFile(String path, String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(content);

			//System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

	public static void main(String[] args) {
		
//		TextFileWriter writer = new TextFileWriter();
//		String content = "test";
//		File dir = new File("etc/");
//		if (!dir.exists()) {
//			dir.mkdir();
//		}
//		writer.writeFile("etc1/filename.txt", content);

	}

}
