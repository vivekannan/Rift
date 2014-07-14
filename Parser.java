import java.io.*;
import java.util.*;

//Parses a given .asm file by removing all comments and striping all extra whitespaces.
//Parsed lines are then given to the Tokener class.

class Parser {
	
	private static BufferedReader assemblySource = null;
	private static String line = new String();
	private static List<String> parsedLines = new ArrayList<String>();
	
	public static List<String> parse(String fileName) {
		
		try {
			assemblySource = new BufferedReader(new FileReader(fileName));
			
			while((line = assemblySource.readLine()) != null) {
				int commentIndex = line.indexOf(';');
				
				if(commentIndex == 0)
					continue;
				
				else {
					if(commentIndex != -1)
						line = line.substring(0, commentIndex);
					
					line = line.trim().replaceAll(" +", " ");
				}
				
				if(line.length() > 0)
					parsedLines.add(line);
			}
			
			assemblySource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}
			
		catch(IOException e) {
			System.out.println("File cannot be opened/closed.");
		}
		
		return parsedLines;
	}
}
