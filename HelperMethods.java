import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;

class HelperMethods {
	
	static void getOpcodes() {
		
		Object[] op;
		String line;
		String[] tokens;
		BufferedReader opSource;
		ArrayList<Object[]> temp;
		
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
			
			opSource.close();
		}
		
		catch(Exception e) {
			System.out.println("Can't find hex codes! Exiting...");
			System.exit(0);
		}
	}
	
	static void getSymbols() {
		
		String line;
		String[] tokens;
		BufferedReader symbolSource;
		
		try {
			symbolSource = new BufferedReader(new FileReader("symbols.txt"));
			
			while((line = symbolSource.readLine()) != null) {
				tokens = line.split(" ");
				Boo.symbols.put(tokens[0], tokens[1]);
			}
			
			symbolSource.close();
		}
		
		catch(Exception e) {
			System.out.println("Can't find symbols! Exiting...");
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
	//TODO: Support all the other directives (DB, EQU, BIT). Have seperate hashmap for directives.
	static void parse() {
		
		int index;
		String line;
		String[] tokens;
		final Pattern ORGDIRECTIVE = Pattern.compile("ORG (?:0[A-F]|\\d)[0-9A-F]{0,3}H?");
		final Pattern EQUDIRECTIVE = Pattern.compile("(?:EQU|BIT) [A-Z][0-9A-Z]* (?:(?:#-?)?(?:[01]+B|\\d+D?|\\d[0-9A-F]*H)|#\"\\p{ASCII}+\")");
		
		for(Line temp : Boo.lines) {
			line = temp.rawLine;
			index = line.indexOf(';');
			
			if(index != -1)
				line = line.substring(0, index);
			
			if(line.indexOf('\"') == -1)
				line = line.trim().replaceAll("\\s{2,}", " ").replaceAll("\\s?,\\s?", ",").toUpperCase();
			
			else {
				tokens = line.split("\"", 2);
				tokens[0] = tokens[0].replaceAll("\\s{2,}", " ").replaceAll("\\s?,\\s?", ",").toUpperCase();
				line = (tokens[0] + "\"" + tokens[1]).trim();
			}
			
			if(line.startsWith("BIT ") || line.startsWith("EQU ") || line.startsWith("ORG ")) {
				if(ORGDIRECTIVE.matcher(line).matches()) {
					tokens = line.split(" ");
					temp.org = tokens[1].replace("H", "");
				}
				
				else if (EQUDIRECTIVE.matcher(line).matches()) {
					tokens = line.split(" ");
					
					if(Boo.opcodes.containsKey(tokens[1]) || Boo.symbols.containsKey(tokens[1]) || Boo.directives.contains(tokens[1]))
						temp.setError("Illegal symbol name.");
					
					else
						Boo.symbols.put(tokens[1], tokens[2]);
				}
				
				else
					temp.setError("Invalid operands for " + line.split(" ")[0] + " directive.");
				
				line = "";
			}
			
			else {
				index = line.lastIndexOf(" ") + 1;
				tokens = line.substring(index).split(",");   //Splits data such as ",,". Big bug.
				line = line.substring(0, index);
				
				for(int i = 0; i < tokens.length; i++)
					line += (Boo.symbols.containsKey(tokens[i]) ? Boo.symbols.get(tokens[i]) : tokens[i]) + ",";
				
				line = line.substring(0, line.length() - 1);
			}
			
			temp.parsedLine = line;
			
			if(line.equals("END"))
				return;
		}
	}
	
	//TODO: Beautify.
	static void tokenize() {
		
		String line;
		String[] tokens;
		final Pattern LABEL = Pattern.compile("^[A-Z][A-Z0-9]*:.*$");
		
		for(Line temp : Boo.lines) {
			line = temp.parsedLine;
			
			if(line.equals("END"))
				return;
			
			if(line.length() > 0) {
				if(LABEL.matcher(line).matches()) {
					tokens = line.split(": ?", 2);
					temp.label = tokens[0];
					line = tokens[1];
					
					if(Boo.opcodes.containsKey(temp.label) || Boo.symbols.containsKey(temp.label) || Boo.directives.contains(temp.label))
						temp.setError("Illegal label name.");
				}
				
				if(line.equals(""))
					continue;
				
				tokens = line.split(" ", 2);
				
				try {
					temp.m = (tokens[0].equals("DB")) ? new DB() : new Mnemonics(tokens[0]);
					
					if(!temp.m.validate(tokens.length == 2 ? tokens[1] : ""))
						temp.setError("Invalid operand(s) for Mnemonic " + tokens[0]);
				}
				
				catch(Exception e) {
					temp.setError(e.getMessage());
				}
			}
		}
	}
	
	/**
	*	Prints errors that were set by other methods. Returns true if errors are present.
	*
	*	@params None
	*	@return boolean
	*	@throws None
	*/
	//TODO: None.
	static void printErrors() {
		
		boolean errors = false;
		
		for(Line temp : Boo.lines) {
			if(!temp.errorStatements.isEmpty()) {
				errors = true;
				
				for(String error : temp.errorStatements)
					System.out.println(Boo.fileName + "::" + error);
			}
		}
		
		if(errors)
			System.exit(0);
	}
	
	/**
	*	Allocates address to statements with proper instruction.
	*
	*	@params None
	*	@return void
	*	@throws None
	*/
	//TODO: None.
	static void allocROMAddr() {
		
		int start = 0x0000;
		
		for(Line temp : Boo.lines) {
			if(temp.parsedLine == null) //Allocate address for instructions until "END" directive.
				return;
			
			if(temp.org != null)
				start = Integer.parseInt(temp.org, 16);
			
			if(temp.m != null) {
				temp.address = String.format("%4s", Integer.toHexString(start).toUpperCase()).replace(" ", "0");
				start += temp.m.size;
				
				if(start > 0xFFFF) {
					temp.setError("Internal ROM full.");
					return;
				}
			}
			
			else if(temp.label != null)
				temp.address = String.format("%4s", Integer.toHexString(start).toUpperCase()).replace(" ", "0");
			
			else
				temp.address = ""; //Mnemonic devoid statements do not get any address.
		}
	}
	
	static void deLabelize() {
		
		String[] tokens;
		
		for(Line temp : Boo.lines) {
			if(temp.parsedLine.equals("END"))
				return;
			
			if(temp.m != null) {
				if(temp.m.opcode.indexOf(":") != -1) {
					tokens = temp.m.opcode.split(":");
					
					try {
						if(temp.m.getClass().getName().equals("AJMP") || temp.m.getClass().getName().equals("ACALL"))
							temp.m.opcode = HelperMethods.calcAddr(tokens[1], temp);
						else
							temp.m.opcode = tokens[0] + HelperMethods.calcAddr(tokens[1], temp);
					}
					
					catch(Exception e) {
						temp.setError(e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	*	Calculates the address for labels. Depending upon the Mnemonics, the address maybe relative or absolute.
	*	
	*	@param String label The label given as a operand for which the address must be calculate.
	*	@param Line line The line object which has the label as an operand.
	*	@return A String object representing the address.
	*	@throws Exception When the given label cannot be found in the asm source.
	*	@throws Exception When jump range for SJMP exceeds limit.
	*/
	//TODO: Beautify.
	static String calcAddr(String label, Line line) throws Exception {
		
		for(Line temp : Boo.lines) {
			if(temp.parsedLine.equals("END"))
				break;
			
			if(temp.label != null && temp.label.equals(label)) {
				String className = line.m.getClass().getName();
				
				if(className.equals("LCALL") || className.equals("LJMP"))
					return temp.address;
				
				int lineAddress = Integer.parseInt(line.address, 16) + line.m.size;
				int labelAddress = Integer.parseInt(temp.address, 16);
				int jump = labelAddress - lineAddress;
				
				if(className.equals("SJMP") && (jump < -128 || jump > 127))
					throw new Exception(String.format("Given jump range of %s exceeds limit for SJMP (-128 to 127)", jump));
				
				//Not sure about jump range of AJMP.
				else if(className.equals("AJMP") || className.equals("ACALL")) {
					if(!(labelAddress >= (lineAddress / 2048) * 2048 && labelAddress < (lineAddress / 2048 + 1) * 2048))
						throw new Exception(String.format("Label address is not a part of the %s 2KB block.", className));
					
					String opcode = Integer.toBinaryString(jump);
					opcode = ("00000000000" + opcode).substring(opcode.length());
					opcode = Integer.toHexString(Integer.parseInt(opcode.substring(0, 3) + (className.equals("AJMP") ? "0" : "1") + "0001" + opcode.substring(3), 2));
					
					return ("0000" + opcode).substring(opcode.length()).toUpperCase();
				}
				
				String s = Integer.toHexString(jump).toUpperCase();
				
				return ("00" + s).substring(s.length());
			}
		}
		
		throw new Exception("Cannot find label: " + label);
	}

	/**
	*	Creates a file with basename of Boo.fileName with extension ".lst".
	*	The lst content is then written to the file. If an exception occurs, displays a message.
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
			
			for(Line line : Boo.lines) {
				if(line.parsedLine == null) //Write to file until "END" directive.
					return;
				
				lstFile.println(String.format("%-8d%-6s%-8s%s", line.lineNumber, line.address, (line.m != null && !line.m.getClass().getName().equals("DB")) ? line.m.opcode : "", line.rawLine));
			}
			
			lstFile.flush();
			lstFile.close();
		}
		
		catch(Exception e) {
			System.out.println("Cannot create lst file.");
		}
	}
	
	static void createHex() {
		
		try {
			int checksum;
			String temp;
			int dotIndex = Boo.fileName.lastIndexOf(".");
			PrintWriter hexFile = new PrintWriter(Boo.fileName.substring(0, dotIndex > 0 ? dotIndex : Boo.fileName.length()) + ".hex", "UTF-8");
			
			for(Line line : Boo.lines) {
				if(line.parsedLine == null)
					break;
				
				checksum = 0;
				
				if(line.m != null) {
					temp = String.format("%2S%s00%S", Integer.toHexString(line.m.opcode.length() / 2), line.address, line.m.opcode).replace(' ', '0');
					
					for(int i = 0; i < temp.length() / 2; i++)
						checksum += Integer.parseInt(temp.substring(2 * i, 2 * (i + 1)), 16);
					
					hexFile.println(":" + temp + Integer.toHexString(checksum % 256).toUpperCase());
				}
			}
			
			hexFile.println(":00000001FF");
			hexFile.flush();
			hexFile.close();
		}
		
		catch(Exception e) {
			System.out.println("Cannot create hex file.");
		}
	}
}
