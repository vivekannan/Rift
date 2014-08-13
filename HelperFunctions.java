import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;

class HelperFunctions {
	
	static String calcAddr(String label, Line line) throws Exception {
		
		for(Line temp : Boo.lines) {
			if(temp.parsedLine != null && temp.label != null && temp.label.equals(label)) {
				if(line.m.getClass().getName().endsWith("CALL"))
					return temp.address;
				
				String s = Integer.toHexString(Integer.parseInt(temp.address, 16) - Integer.parseInt(line.address, 16) - line.m.size).toUpperCase();
				
				return ("00" + s).substring(s.length());
			}
		}
		
		throw new Exception("Cannot find label: " + label);
	}
	
	static void getOpcodes() {
		
		Object[] op;
		String line;
		String[] tokens;
		ArrayList<Object[]> temp;
		BufferedReader opSource;
		
		try {
			opSource = new BufferedReader(new FileReader("hexcodes.txt"));
			
			for(int i = 0; (line = opSource.readLine()) != null; i++) {
				tokens = line.split(" ", 2);
				op = new Object[] {
					Pattern.compile(tokens[1]),
					String.format("%2s", Integer.toHexString(i).toUpperCase()).replace(' ', '0'),
				};
				
				if(Boo.opcodes.containsKey(tokens[0]))
					Boo.opcodes.get(tokens[0]).add(op);
				
				else {
					temp = new ArrayList<Object[]>();
					temp.add(op);
					Boo.opcodes.put(tokens[0], temp);
				}
			}
		}
		
		catch(Exception e) {
			System.out.println("Can't find hex codes! Exiting...");
			System.exit(0);
		}
	}
	
	static void read() {
		
		String line;
		BufferedReader assemblySource;
		
		try {
			assemblySource = new BufferedReader(new FileReader(Boo.fileName));
			
			for(int i = 1; (line = assemblySource.readLine()) != null; i++)
				Boo.lines.add(new Line(line, i));
			
			assemblySource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println(Boo.fileName + " not found.");
			System.exit(0);
		}
		
		catch(IOException e) {
			System.out.println(Boo.fileName + " cannot be opened/closed.");
			System.exit(0);
		}
	}
	
	static void parse() {
		
		String line;
		int commentIndex;
		
		for(Line temp : Boo.lines) {
			line = temp.rawLine;
			commentIndex = line.indexOf(';');
			
			if(commentIndex != -1)
				line = line.substring(0, commentIndex);
			
			line = line.trim().replaceAll("\\s{2,}", " ").replaceAll("\\s?,\\s?", ",");
			
			if(line.equals("END"))
				return;
			
			temp.parsedLine = line.toUpperCase();
		}
	}
	
	static void tokenize() {
		
		String line;
		String[] tokens;
		Pattern label = Pattern.compile("^[A-Z][A-Z0-9]*:.*$");
		
		for(Line temp : Boo.lines) {
			line = temp.parsedLine;
			
			if(line == null)
				return;
			
			if(line.length() > 0) {
				if(label.matcher(line).matches()) {
					tokens = line.split(": ?", 2);
					temp.label = tokens[0];
					line = tokens[1];
				}
				
				tokens = line.split(" ", 2);
				
				try {
					temp.m = (Mnemonics) Class.forName(tokens[0]).newInstance();
					
					if(!temp.m.validate(tokens.length == 2 ? tokens[1] : ""))
						temp.setError("Invalid operand(s) for Mnemonic " + tokens[0]);
				}
				
				catch(ClassNotFoundException e) {
					temp.setError("Unindentified Mnemonic: " + tokens[0]);
				}
				
				catch(Exception e) {
					temp.setError(e.getMessage());
				}
			}
		}
	}
	
	static boolean printErrors() {
		
		String e;
		boolean b = false;
		
		for(Line temp : Boo.lines) {
			e = temp.errorStatement;
			
			if(e != null) {
				b = true;
				System.out.println(Boo.fileName + e);
			}
		}
		
		return b;
	}
	
	static void allocROMAddr() {
		
		int start = 0x0000;
		
		for(Line temp : Boo.lines) {
			if(temp.parsedLine == null)
				return;
			
			if(temp.m != null) {
				temp.address = String.format("%4s", Integer.toHexString(start).toUpperCase()).replace(" ", "0");
				start += temp.m.size;
			}
			
			else
				temp.address = "";
		}
	}
	
	static void deLabelize() {
		
		int index;
		String[] tokens;
		
		for(Line temp : Boo.lines) {
			if(temp.m != null) {
				index = temp.m.opcode.indexOf("[");
				
				if(index != -1) {
					tokens = temp.m.opcode.split("\\[");
					
					try {
						temp.m.opcode = tokens[0] + HelperFunctions.calcAddr(tokens[1], temp);
					}
					
					catch(Exception e) {
						temp.setError(e.getMessage());
					}
				}
			}
		}
	}
	
	static void writeToFile() {
		
		try {
			int dotIndex = Boo.fileName.lastIndexOf(".");
			PrintWriter lstFile = new PrintWriter(Boo.fileName.substring(0, dotIndex > 0 ? dotIndex : Boo.fileName.length()) + ".lst", "UTF-8");
			
			for(Line temp : Boo.lines) {
				if(temp.parsedLine == null)
					return;
				
				lstFile.println(String.format("%-4d%-6s%-8s%s", temp.lineNumber, temp.address, temp.m != null ? temp.m.opcode : "", temp.rawLine));
				lstFile.flush();
			}
			
			lstFile.close();
		}
		
		catch(Exception e) {
			System.out.println("Cannot create lst file. Falling back to stdout.");
			
			for(Line temp : Boo.lines) {
				if(temp.parsedLine == null)
					return;
				
				System.out.println(String.format("%-4d%-6s%-8s%s", temp.lineNumber, temp.address, temp.m != null ? temp.m.opcode : "", temp.rawLine));
			}
		}
	}
}
