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
	
	public static void read(String fileName) {
		
		int i = 0;
		
		try {
			assemblySource = new BufferedReader(new FileReader(fileName));
			
			while((line = assemblySource.readLine()) != null)
				Boo.addLine(new Line(line, ++i));
			
			assemblySource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println(fileName + " not found.");
		}
			
		catch(IOException e) {
			System.out.println(fileName + " cannot be opened/closed.");
		}
	}
}

class Parser {
	
	private static String line = new String();
	
	public static void parse() {
		
		int commentIndex;
		int lineCount = Boo.getLineCount();
		
		for(int i = 0; i < lineCount; i++) {
			
			String line = Boo.getRawLine(i);
			commentIndex = line.indexOf(';');
			
			if(commentIndex != -1)
				line = line.substring(0, commentIndex);
			
			line = line.trim().replaceAll("\\s+", " ");
			Boo.setParsedLine(line, i);
		}
	}
}

class Tokenizer {
	
	public static void tokenize(String fileName) {
		
		int lineCount = Boo.getLineCount();
		Mnemonics m;
		
		for(int i = 0; i < lineCount; i++) {
			
			String line = Boo.getParsedLine(i);
			
			if(line.length() > 0) {
				String[] tokens = line.split(" ", 2);
				
				if(tokens.length == 2) {
					
					if(tokens[0].equals("MOV"))
						m = new MOV(tokens[1], true);
					
					else if(tokens[0].equals("ADD"))
						m = new ADD(tokens[1]);
					
					else if(tokens[0].equals("INC"))
						m = new INC(tokens[1]);
					
					else {
						Boo.setErrorStatement(String.format("%s::%d %s", fileName, i + 1, Boo.getRawLine(i)), i);
						continue;
					}
					
					if(m.translate() == 0)
						Boo.setErrorStatement(String.format("%s::%d %s", fileName, i + 1, Boo.getRawLine(i)), i);
				}
				
				else
					Boo.setErrorStatement(String.format("%s::%d %s", fileName, i + 1, Boo.getRawLine(i)), i);
			}
		}
	}
}
