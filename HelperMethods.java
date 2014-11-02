import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;

class HelperMethods {
	
	/**
	*	Reads hexcodes.txt and obtains the opcodes along with the proper regex
	*	for the operands. 
	*	
	*	@param None
	*	@return void
	*	@throws FileNotFoundException - can't find hexcodes.txt
	*	@throws IOException - can't open hexcodes.txt
	*/
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
				
				if(Rift.opcodes.containsKey(tokens[0]))
					Rift.opcodes.get(tokens[0]).add(op);
				
				else {
					temp = new ArrayList<Object[]>();
					temp.add(op);
					Rift.opcodes.put(tokens[0], temp);
				}
			}
			
			opSource.close();
		}
		
		catch(Exception e) {
			System.out.println("Can't find hexcodes! Exiting...");
			System.exit(0);
		}
	}
	
	/**
	*	Reads symbols.txt and obtains the pre-defined symbols and their memory
	*	addresses.
	*	
	*	@param None
	*	@return void
	*	@throws FileNotFoundException - can't find symbols.txt
	*	@throws IOException - can't open symbols.txt
	*/
	static void getSymbols() {
		
		String line;
		String[] tokens;
		BufferedReader symbolSource;
		
		try {
			symbolSource = new BufferedReader(new FileReader("symbols.txt"));
			
			while((line = symbolSource.readLine()) != null) {
				tokens = line.split(" ");
				Rift.symbols.put(tokens[0], tokens[1]);
			}
			
			symbolSource.close();
		}
		
		catch(Exception e) {
			System.out.println("Can't find symbols! Exiting...");
			System.exit(0);
		}
	}
	
	/**
	*	Reads lines using Rift.fileName as filename and stores the lines in Rift.lines.
	*	
	*	@param None
	*	@return void
	*	@throws FileNotFoundException - can't find the given file
	*	@throws IOException - can't open the given file
	*/
	static void read() {
		
		String line;
		BufferedReader asmSource;
		
		try {
			asmSource = new BufferedReader(new FileReader(Rift.fileName));
			
			for(int i = 1; (line = asmSource.readLine()) != null; i++)
				Rift.lines.add(new Line(line, i));
			
			asmSource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println(Rift.fileName + " not found.");
			System.exit(0);
		}
		
		catch(IOException e) {
			System.out.println(Rift.fileName + " cannot be opened/closed.");
			System.exit(0);
		}
	}
	
	/**
	*	Parses the lines read by read(). Removes comments and excess whitespaces.
	*	Parsing is terminated when "END" directive is encountered.
	*	
	*	@param None
	*	@return void
	*	@throws None
	*/
	static void parse() {
		
		int index;
		String line;
		String[] tokens;
		final Pattern ORGDIRECTIVE = Pattern.compile("ORG (?:0[A-F]|\\d)[0-9A-F]{0,3}H?");
		final Pattern EQUDIRECTIVE = Pattern.compile("(?:EQU|BIT) [A-Z][0-9A-Z]* (?:(?:#-?)?(?:[01]+B|\\d+D?|\\d[0-9A-F]*H)|#\"\\p{ASCII}+\")");
		
		for(Line temp : Rift.lines) {
			line = temp.rawLine;
			index = line.indexOf(';');
			
			if(index != -1)
				line = line.substring(0, index);
			
			if(line.indexOf('\"') == -1)
				line = line.trim().replaceAll("\\s{2,}", " ").replaceAll("\\s?,\\s?", ",").toUpperCase();
			
			//Maintains the case of ascii data found within quotes.
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
					
					if(Rift.opcodes.containsKey(tokens[1]) || Rift.symbols.containsKey(tokens[1]) || Rift.directives.contains(tokens[1]))
						temp.setError("Illegal symbol name.");
					
					else
						Rift.symbols.put(tokens[1], tokens[2]);
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
					line += (Rift.symbols.containsKey(tokens[i]) ? Rift.symbols.get(tokens[i]) : tokens[i]) + ",";
				
				line = line.substring(0, line.length() - 1);
			}
			
			temp.parsedLine = line;
			
			if(line.equals("END"))
				return;
		}
	}
	
	/**
	*	Tokenizes the parsed lines into mnemonics and operands. A Mnemonic obj
	*	is created for every line with a mnemonic in it. The validate method
	*	tries to amtch the given operands with the regex as defined in hexcodes.txt.
	*	
	*	@param None
	*	@return void
	*	@throws FileNotFoundException If given file is not found.
	*	@throws IOException If file cannot be opened.
	*/
	static void tokenize() {
		
		String line;
		String[] tokens;
		final Pattern LABEL = Pattern.compile("^[A-Z][A-Z0-9]*:.*$");
		
		for(Line temp : Rift.lines) {
			line = temp.parsedLine;
			
			if(line.equals("END"))
				return;
			
			if(line.length() > 0) {
				if(LABEL.matcher(line).matches()) {
					tokens = line.split(": ?", 2);
					temp.label = tokens[0];
					line = tokens[1];
					
					if(Rift.opcodes.containsKey(temp.label) || Rift.symbols.containsKey(temp.label) || Rift.directives.contains(temp.label))
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
	*	Prints errors that were set by other methods. Program stops if errors are
	*	found.
	*
	*	@params None
	*	@return void
	*	@throws None
	*/
	static void printErrors() {
		
		boolean errors = false;
		
		for(Line temp : Rift.lines) {
			if(!temp.errorStatements.isEmpty()) {
				errors = true;
				
				for(String error : temp.errorStatements)
					System.out.println(Rift.fileName + "::" + error);
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
	static void allocROMAddr() {
		
		int start = 0x0000;
		
		for(Line temp : Rift.lines) {
			if(temp.parsedLine == null)
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
				temp.address = "";
		}
	}
	
	/**
	*	Replaces the labels in the opcode with proper hex values.
	*	
	*	@param None
	*	@return void
	*	@throws Exception - label cannot be found in the asm source
	*	@throws Exception - jump range exceeds limit for any label
	*/
	static void deLabelize() {
		
		String[] tokens;
		
		for(Line temp : Rift.lines) {
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
	*	@param String label - The label given as a operand for which the address must be calculate.
	*	@param Line line - The line object which has the label as an operand.
	*	@return A String object representing the address.
	*	@throws Exception - label cannot be found in the asm source
	*	@throws Exception - jump range exceeds limit
	*/
	static String calcAddr(String label, Line line) throws Exception {
		
		for(Line temp : Rift.lines) {
			if(temp.parsedLine.equals("END"))
				break;
			
			if(temp.label != null && temp.label.equals(label)) {
				String mnemonic = line.m.mnemonic;
				
				if(mnemonic.equals("LCALL") || mnemonic.equals("LJMP"))
					return temp.address;
				
				int lineAddress = Integer.parseInt(line.address, 16) + line.m.size;
				int labelAddress = Integer.parseInt(temp.address, 16);
				int jump = labelAddress - lineAddress;
				
				if(mnemonic.equals("SJMP") && (jump < -128 || jump > 127))
					throw new Exception(String.format("Given jump range of %s exceeds limit for SJMP (-128 to 127)", jump));
				
				else if(mnemonic.equals("AJMP") || mnemonic.equals("ACALL")) {
					if(!(labelAddress >= (lineAddress / 2048) * 2048 && labelAddress < (lineAddress / 2048 + 1) * 2048))
						throw new Exception(String.format("Label address is not a part of the %s 2KB block.", mnemonic));
					
					String opcode = Integer.toBinaryString(jump);
					opcode = ("00000000000" + opcode).substring(opcode.length());
					opcode = Integer.toHexString(Integer.parseInt(opcode.substring(0, 3) + (mnemonic.equals("AJMP") ? "0" : "1") + "0001" + opcode.substring(3), 2));
					
					return ("0000" + opcode).substring(opcode.length()).toUpperCase();
				}
				
				String s = Integer.toHexString(jump).toUpperCase();
				
				return ("00" + s).substring(s.length());
			}
		}
		
		throw new Exception("Cannot find label: " + label);
	}

	/**
	*	Creates a list file with basename of Rift.fileName.
	*	
	*	@param None
	*	@return void
	*	@throws Exception - error in file creation
	*/
	static void createLst() {
		
		try {
			int dotIndex = Rift.fileName.lastIndexOf(".");
			PrintWriter lstFile = new PrintWriter(Rift.fileName.substring(0, dotIndex > 0 ? dotIndex : Rift.fileName.length()) + ".lst", "UTF-8");
			
			for(Line line : Rift.lines) {
				if(line.parsedLine == null)
					return;
				
				lstFile.println(String.format("%-8d%-6s%-8s%s", line.lineNumber, line.address, (line.m != null && line.m.mnemonic != null ? line.m.opcode : ""), line.rawLine));
			}
			
			lstFile.flush();
			lstFile.close();
		}
		
		catch(Exception e) {
			System.out.println("Cannot create lst file.");
		}
	}
	
	/**
	*	Creates a hex file with basename of Rift.fileName. 
	*	
	*	@param None
	*	@return void
	*	@throws Exception - error in file creation
	*/
	static void createHex() {
		
		try {
			int checksum;
			String temp;
			int dotIndex = Rift.fileName.lastIndexOf(".");
			PrintWriter hexFile = new PrintWriter(Rift.fileName.substring(0, dotIndex > 0 ? dotIndex : Rift.fileName.length()) + ".hex", "UTF-8");
			
			for(Line line : Rift.lines) {
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
