import java.io.*;
import java.util.*;

class HelperClasses {
	
	static {
		Mnemonics m;
	}
}

class ReadFile {
	
	private static BufferedReader assemblySource = null;
	private static String line = new String();
	
	public static void read(String fileName, List<Line> lines) {
		
		int i = 0;
		
		try {
			assemblySource = new BufferedReader(new FileReader(fileName));
			
			while((line = assemblySource.readLine()) != null)
				lines.add(new Line(line, ++i));
			
			assemblySource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println(fileName + " not found.");
			System.exit(0);
		}
			
		catch(IOException e) {
			System.out.println(fileName + " cannot be opened/closed.");
			System.exit(0);
		}
	}
}

class Parser {
	
	private static String line = new String();
	
	public static void parse(List<Line> lines) {
		
		int commentIndex;
		Line temp;
		
		for(int i = 0; i < lines.size(); i++) {
			
			temp = lines.get(i);
			String line = temp.rawLine;
			commentIndex = line.indexOf(';');
			
			if(commentIndex != -1)
				line = line.substring(0, commentIndex);
			
			temp.parsedLine = line.trim().replaceAll("\\s+", " ");;
			lines.set(i, temp);
		}
	}
}

class Tokenizer {
	
	public static void tokenize(String fileName, List<Line> lines) {
		
		Mnemonics m;
		Line temp;
		
		for(int i = 0; i < lines.size(); i++) {
			
			temp = lines.get(i);
			String line = temp.parsedLine;
			
			if(line.length() > 0) {
				String[] tokens = line.split(" ", 2);
				
				if(tokens.length == 2) {
					
					if(tokens[0].equals("MOV") || tokens[0].equals("MOVX")) // same as below.
						m = new MOV(tokens[1]);
					
					else if(tokens[0].equals("ADD") || tokens[0].equals("ADDC") || tokens[0].equals("SUBB")) // Pass respective arguments to ADD class to differentitae b/w the instructions.
						m = new ADD(tokens[1]);
					
					else if(tokens[0].equals("INC") || tokens[0].equals("DEC")) // same as above
						m = new INC(tokens[1]);
					
					else {
						temp.errorStatement = String.format("%s::%d %s", fileName, i + 1, temp.rawLine);
						continue;
					}
					
					if(!m.translate())
						temp.errorStatement = String.format("%s::%d %s", fileName, i + 1, temp.rawLine);
				}
				
				else
					temp.errorStatement = String.format("%s::%d %s", fileName, i + 1, temp.rawLine);
			}
		}
	}
}
