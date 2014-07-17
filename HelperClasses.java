import java.io.*;
import java.util.*;

class HelperClasses {
	
	static {
		new Mnemonics();
	}
}

class ReadFile {
	
	private static BufferedReader assemblySource = null;
	private static String line = new String();
	
	public static void read() {
		
		int i = 0;
		
		try {
			assemblySource = new BufferedReader(new FileReader(Boo.getFileName()));
			
			while((line = assemblySource.readLine()) != null)
				Boo.addLine(new Line(line, ++i));
			
			assemblySource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println("File " + "\"" + Boo.getFileName() + "\"" + " not found.");
		}
			
		catch(IOException e) {
			System.out.println("File " + "\"" + Boo.getFileName() + "\"" + " cannot be opened/closed.");
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
			
			line = line.trim().replaceAll(" +", " ");
			Boo.setParsedLine(line, i);
		}
	}
}

class Tokenizer {
	
	public static void tokenize() {
		
		int lineCount = Boo.getLineCount();
		
		for(int i = 0; i < lineCount; i++) {
			
			String line = Boo.getParsedLine(i);
			
			if(line.length() > 0) {
				String[] tokens = line.split(" ", 2);
				
				if(tokens.length == 2 && tokens[0].equals("MOV")) {
					MOV m = new MOV(tokens[1], true);
					
					if(m.translate() == 0) {
						System.out.println(String.format("%s::%d %s", Boo.getFileName(), i + 1, Boo.getRawLine(i)));
						System.exit(0);
					}
				}
				
				else {
					System.out.println(String.format("%s::%d %s", Boo.getFileName(), i + 1, Boo.getRawLine(i)));
					System.exit(0);
				}
			}
		}
	}
}
