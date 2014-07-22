import java.io.*;
import java.util.*;

class HelperClasses {
}

class Line {
	
	public String rawLine;
	public String parsedLine;
	public int lineNumber;
	public String label;
	public String errorStatement;
	
	Line(String l, int ln) {
		this.rawLine = l;
		this.lineNumber = ln;
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
					
					if(tokens[0].equals("MOV"))
						m = new MOV(tokens[1]);
					
					else if(tokens[0].equals("MOVC"))
						m = new MOVC(tokens[1]);
					
					else if(tokens[0].equals("MOVX"))
						m = new MOVX(tokens[1]);
					
					else if(tokens[0].equals("ADD"))
						m = new ADD(tokens[1]);
					
					else if(tokens[0].equals("ADDC"))
						m = new ADDC(tokens[1]);
					
					else if(tokens[0].equals("SUBB"))
						m = new SUBB(tokens[1]);
					
					else if(tokens[0].equals("INC"))
						m = new INC(tokens[1]);
					
					else if(tokens[0].equals("DEC"))
						m = new DEC(tokens[1]);
					
					else if(tokens[0].equals("MUL"))
						m = new MUL(tokens[1]);
					
					else if(tokens[0].equals("DIV"))
						m = new DIV(tokens[1]);
					
					else if(tokens[0].equals("DA"))
						m = new DA(tokens[1]);
					
					else if(tokens[0].equals("ANL"))
						m = new ANL(tokens[1]);
					
					else if(tokens[0].equals("ORL"))
						m = new ORL(tokens[1]);
					
					else if(tokens[0].equals("XRL"))
						m = new XRL(tokens[1]);
					
					else if(tokens[0].equals("PUSH"))
						m = new PUSH(tokens[1]);
						
					else if(tokens[0].equals("POP"))
						m = new POP(tokens[1]);
					
					else {
						temp.errorStatement = String.format("%s:%d: %s\n%s", fileName, i + 1, "Unindentified Mnemonic " + tokens[0], temp.rawLine);
						continue;
					}
					
					if(!m.validate())
						temp.errorStatement = String.format("%s:%d: %s\n%s", fileName, i + 1, "Invalid operand(s) for Mnemonic " + tokens[0], temp.rawLine);
				}
				
				else if(tokens[0].equals("END"))
					System.exit(0);
				
				else
					temp.errorStatement = String.format("%s:%d: %s\n%s", fileName, i + 1, "Unidentified statement.", temp.rawLine);
			}
		}
	}
}
