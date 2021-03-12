package presentation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class DataTableFileReader {
	
	String content = "";
	
	public void readFile(String file)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line);
			while (line != null) {
				line = reader.readLine();
				processLine(line);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,                
					"File not found",                
					"File not found Error.",                                
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,                
					"File error",                
					"File error.",                                
					JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void processLine(String line) {
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			//System.out.println(line);
			if (line != null) {
				content += line + "\n";
			}
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		
	}

}