import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;

class HelperFunctions {
	
	/**
	*	Calculates the address for labels. Depending upon the Mnemonics,
	*	the address maybe relative or absolute.
	*	
	*	@param String label The label given as a operand for which the address must be calculate.
	*	@param Line line The line object which has the label as an operand.
	*	@return A String object representing the address.
	*	@throws Exception When the given label cannot be found in the asm source.
	*/
	//TODO: Support AJMP and ACALL Mnemonics. Reduce paramenter list.
	static String calcAddr(String label, Line line) throws Exception {
		
		for(Line temp : Boo.lines) {
			if(temp.parsedLine != null && temp.label != null && temp.label.equals(label)) {
				if(line.m.getClass().getName().endsWith("CALL") || line.m.getClass().getName().equals("LJMP"))
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
	
	/**
	*	Reads lines using Boo.fileName as filename and stores the lines in Boo.lines.
	*	
	*	@param None
	*	@return void
	*	@throws FileNotFoundException If given file is not found.
	*	@throws IOException If file cannot be opened.
	*/
	//TODO: None.
	static void read() {
		
		String line;
		BufferedReader asmSource;
		
		try {
			asmSource = new BufferedReader(new FileReader(Boo.fileName));
			
			for(int i = 1; (line = asmSource.readLine()) != null; i++)
				Boo.lines.add(new Line(line, i));
			
			asmSource.close();
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
	
	/**
	*	Parses the lines read by read(). Removes comments and excess whitespaces.
	*	Parsing is terminated when "END" is encountered.
	*	
	*	@param None
	*	@return void
	*	@throws None
	*/
	//TODO: Support all the other directives (ORG, DB).
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
				
				if(line.equals("")) {
					temp.setError("Hanging label encountered.");
					continue;
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
	
	//TODO: Once ORG directive is supported, "start" value should jump correspondingly.
	static void allocROMAddr() {
		
		int start = 0x0000;
		
		for(Line temp : Boo.lines) {
			if(temp.parsedLine == null) //Allocate address for instructions until "END" directive.
				return;
			
			if(temp.m != null) {
				temp.address = String.format("%4s", Integer.toHexString(start).toUpperCase()).replace(" ", "0");
				start += temp.m.size;
			}
			
			else
				temp.address = ""; //Mnemonic devoid statements do not get any address.
		}
	}
	
	static void deLabelize() {
		
		String[] tokens;
		
		for(Line temp : Boo.lines) {
			if(temp.m != null) {
				if(temp.m.opcode.indexOf(":") != -1) {
					tokens = temp.m.opcode.split(":");
					
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
	
	/**
	*	Creates a file with basename of Boo.fileName with extension ".lst".
	*	The lst content is then written to the file. If an exception occurs, falls back to stdout.
	*	
	*	@param None
	*	@return void
	*	@throws Exception If error in file creation.
	*/
	//TODO: None.
	static void writeToFile() {
		
		try {
			int dotIndex = Boo.fileName.lastIndexOf(".");
			PrintWriter lstFile = new PrintWriter(Boo.fileName.substring(0, dotIndex > 0 ? dotIndex : Boo.fileName.length()) + ".lst", "UTF-8");
			
			for(Line temp : Boo.lines) {
				if(temp.parsedLine == null) //Write to file until "END" directive.
					return;
				
				lstFile.println(String.format("%-4d%-6s%-8s%s", temp.lineNumber, temp.address, temp.m != null ? temp.m.opcode : "", temp.rawLine));
				lstFile.flush();
			}
			
			lstFile.close();
		}
		
		catch(Exception e) {
			System.out.println("Cannot create lst file. Falling back to stdout.");
			printToStdout();
		}
	}
	
	/**
	*	Prints the lst file to stdout.
	*	
	*	@param None
	*	@return void
	*	@throws None
	*/
	//TODO: None.
	static void printToStdout() {
		
		for(Line temp : Boo.lines) {
			if(temp.parsedLine == null) //Write to stdout until "END" directive.
				return;
			
			System.out.println(String.format("%-4d%-6s%-8s%s", temp.lineNumber, temp.address, temp.m != null ? temp.m.opcode : "", temp.rawLine));
		}
	}
}
