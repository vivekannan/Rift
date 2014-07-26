import java.io.*;
import java.util.*;
import java.lang.reflect.Constructor;

class HelperClasses {
}

class Line {
	
	public String rawLine;
	public String parsedLine;
	public String label;
	public int lineNumber;
	public String errorStatement;
	public Mnemonics m;
	
	Line(String l, int ln) {
		this.rawLine = l;
		this.lineNumber = ln;
	}
	
	public void setError(String error, String fileName) {
		
		this.errorStatement = String.format("%s::%d: %s\n%s", fileName, this.lineNumber, error, this.rawLine);
	}
}

class ReadFile {
	
	private static BufferedReader assemblySource = null;
	private static String line;
	
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
	
	public static void parse(List<Line> lines) {
		
		Line temp;
		String line;
		int commentIndex;
		
		for(int i = 0; i < lines.size(); i++) {
			
			temp = lines.get(i);
			line = temp.rawLine;
			commentIndex = line.indexOf(';');
			
			if(commentIndex != -1)
				line = line.substring(0, commentIndex);
			
			temp.parsedLine = line.trim().replaceAll("\\s{2,}", " ").replaceAll("\\s?,\\s?", ",");
		}
	}
}

class Tokenizer {
	
	public static void tokenize(String fileName, List<Line> lines) {
		
		Line temp;
		String line;
		String[] tokens;
		
		for(int i = 0; i < lines.size(); i++) {
			
			temp = lines.get(i);
			line = temp.parsedLine;
			
			if(line.length() > 0) {
				
				if(line.matches("^[A-Z][A-Z0-9]*: .*$")) {
					String[] s = line.split(": ", 2);
					temp.label = s[0];
					line = s[1];
				}
				
				tokens = line.split(" ", 2);
				
				if(tokens.length == 2) {
					try {
						
						temp.m = (Mnemonics) Class.forName(tokens[0]).getConstructor(String.class).newInstance(tokens[1]);
						
						if(!temp.m.validate())
							temp.setError("Invalid operand(s) for Mnemonic " + tokens[0], fileName);
					}
					
					catch(Exception e) {
						temp.setError("Unindentified Mnemonic: " + tokens[0], fileName);
					}
				}
				
				else if(tokens[0].equals("NOP"))
					continue;
				
				else if(tokens[0].equals("END"))
					return;
				
				else
					temp.setError("Unidentified statement.", fileName);
			}
		}
	}
}
