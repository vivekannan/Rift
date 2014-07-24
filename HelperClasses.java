import java.io.*;
import java.util.*;
import java.lang.reflect.Constructor;

class HelperClasses {
}

class Line {
	
	public String rawLine;
	public String parsedLine;
	public int lineNumber;
	public String errorStatement;
	
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
			
			temp.parsedLine = line.trim().replaceAll("\\s+", " ").replaceAll("\\s?,\\s?", ",");
		}
	}
}

class Tokenizer {
	
	public static void tokenize(String fileName, List<Line> lines) {
		
		Mnemonics m;
		Line temp;
		Constructor c;
		
		for(int i = 0; i < lines.size(); i++) {
			
			temp = lines.get(i);
			String line = temp.parsedLine;
			
			if(line.length() > 0) {
				String[] tokens = line.split(" ", 2);
				
				if(tokens.length == 2) {
					try {
						
						m = (Mnemonics) Class.forName(tokens[0]).getConstructor(String.class).newInstance(tokens[1]);
						
						if(!m.validate())
							temp.setError("Invalid operand(s) for Mnemonic " + tokens[0], fileName);
					}
					
					catch(Exception e) {
						temp.setError("Unindentified Mnemonic " + tokens[0], fileName);
					}
				}
				
				else if(tokens[0].equals("END"))
						System.exit(0);
				
				else
					temp.setError("Unidentified statement.", fileName);
			}
		}
	}
}
