import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;

class HelperMethods {
	
	/**
	*	Reads hexcodes.gr and obtains the opcodes along with the proper regex for
	*	the operands. 
	*	
	*	@param None
	*	@return void
	*	@throws FileNotFoundException - can't find hexcodes.gr
	*	@throws IOException - can't open hexcodes.gr
	*	@throws ArrayIndexOutOfBoundsException - invalid format
	*	@throws NumberFormatException - invalid opcode size
	*/
	static void getOpcodes() {
		
		int i = 0;
		String line = "";
		
		try{
			Object[] op;
			String[] tokens;
			ArrayList<Object[]> temp;
			
			BufferedReader opSource = new BufferedReader(new FileReader("hexcodes.gr"));
			
			for(i = 0; (line = opSource.readLine()) != null; i++) {
				tokens = line.split(" ", -1);
				
				if(tokens.length != 3)
					throw new Exception();
				
				op = new Object[] {
					Pattern.compile(tokens[1]),
					String.format("%2X", i).replace(' ', '0'),
					Integer.parseInt(tokens[2]),
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
		
		catch(FileNotFoundException e) {
			System.out.println("Can't find hexcodes!");
			System.exit(-1);
		}
		
		catch(IOException e) {
			System.out.println("Can't read hexcodes!");
			System.exit(-2);
		}
		
		catch(NumberFormatException e) {
			System.out.println(String.format("hexcodes.gr::%d:Invalid size for opcode\n%s.", i + 1, line));
			System.exit(-4);
		}
		
		catch(Exception e) {
			System.out.println(String.format("hexcodes.gr::%d:Invalid format for hexcode grammar declaration. Expecting MNEMONIC REGEX SIZE.\n%s", i + 1, line));
			System.exit(-3);
		}
	}
	
	/**
	*	Reads directives.gr and gets the pre-defined grammar for different
	*	directives.
	*	
	*	@param None
	*	@return void
	*	@throws FileNotFoundException - can't find directives.gr
	*	@throws IOException - can't open directives.gr
	*	@throws ArrayIndexOutOfBoundsException - invalid format
	*	@throws ClassNotFoundException - class for a directive not defined
	*/
	static void getDirectives() {
		
		int i = 0;
		String line = "";
		
		try {
			String[] tokens;
			
			BufferedReader directiveSource = new BufferedReader(new FileReader("directives.gr"));
			
			for(i = 1; (line = directiveSource.readLine()) != null; i++) {
				tokens = line.split(" ", 2);
				
				if(tokens.length != 2)
					throw new Exception();
				
				//Check if class for directive is defined.
				Class.forName(tokens[0]);
				
				Rift.directives.put(tokens[0], Pattern.compile(tokens[1]));
			}
			
			directiveSource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println("Can't find directives.gr!");
			System.exit(-5);
		}
		
		catch(IOException e) {
			System.out.println("Can't read directives.gr!");
			System.exit(-6);
		}
		
		catch(ClassNotFoundException e) {
			System.out.println(String.format("directive.gr::%d:Class for directive not defined.\n%s", i, line));
			System.exit(-8);
		}
		
		catch(Exception e) {
			System.out.println(String.format("directives.gr::%d:Invalid format for directive grammar. Expecting DIRECTIVE REGEX\n%s", i, line));
			System.exit(-7);
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
	*	@throws ArrayIndexOutOfBoundsException - invalid format
	*/
	static void getSymbols() {
		
		int i = 0;
		String line = "";
		
		try {
			String[] tokens;
			
			BufferedReader symbolSource = new BufferedReader(new FileReader("symbols.txt"));
			
			for(i = 0; (line = symbolSource.readLine()) != null; i++) {
				tokens = line.split(" ");
				
				if(tokens.length != 2)
					throw new Exception();
				
				Rift.symbols.put(tokens[0], tokens[1]);
			}
			
			symbolSource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println("Can't find symbols.txt!");
			System.exit(-9);
		}
		
		catch(IOException e) {
			System.out.println("Can't read symbols.txt!");
			System.exit(-10);
		}
		
		catch(Exception e) {
			System.out.println(String.format("symbols.txt::%d:Invalid format for symbol definition. Expecting SYMBOL VALUE.\n%s", (i + 1), line));
			System.exit(-11);
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
		
		try {
			String line;
			BufferedReader asmSource = new BufferedReader(new FileReader(Rift.fileName));
			
			for(int i = 1; (line = asmSource.readLine()) != null; i++)
				Rift.lines.add(new Line(line));
			
			asmSource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println(Rift.fileName + " not found.");
			System.exit(-12);
		}
		
		catch(IOException e) {
			System.out.println(Rift.fileName + " cannot be opened/closed.");
			System.exit(-13);
		}
	}
	
	/**
	*	Converts the characters in the given string to equivalent ascii value.
	*	The string is assumed to be double quoted properly.
	*	
	*	@param String s - The string to be converted.
	*	@return String - The ascii equivalent of the given string.
	*	@throws None
	*/
	static String asciify(String s) {
		
		String temp = "";
		
		for(int i = 1; i < s.length() - 1; i++)
			temp += String.format("%2X", (int) s.charAt(i)).replace(' ', '0');
		
		return String.format("\"%s\"", temp);
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
		Matcher m;
		String temp;
		String[] tokens;
		
		final Pattern LABEL = Pattern.compile("^[A-Z][A-Z0-9]* ?:.*$");
		final Pattern ASCII_DATA = Pattern.compile("\"\\p{ASCII}+?\"");
		
		for(Line line : Rift.lines) {
			temp = line.rawLine;
			m = ASCII_DATA.matcher(temp);
			
			//Match ascii data within quotes and convert to hex.
			while(m.find())
				temp = temp.replace(m.group(), HelperMethods.asciify(m.group()));
			
			//Remove all characters after first ";" (inclusive).
			if((index = temp.indexOf(';')) != -1)
				temp = temp.substring(0, index);
			
			//Remove excessive whitespaces and convert statement to standard form.
			temp = temp.trim().replaceAll("\\s{2,}", " ").replaceAll("\\s?,\\s?", ",").toUpperCase();
			
			if(LABEL.matcher(temp).matches()) {
				tokens = temp.split(": ?", 2);
				line.label = tokens[0].trim();
				temp = tokens[1];
				
				if(Rift.opcodes.containsKey(line.label) || Rift.symbols.containsKey(line.label) || Rift.directives.containsKey(line.label))
					line.setError("Illegal label name.");
			}
			
			if(temp.equals("")) {
				line.parsedLine = "";
				continue;
			}
			
			//Replace all symbols with predefined values.
			index = temp.lastIndexOf(" ") + 1;
			tokens = temp.substring(index).split(",");
			temp = temp.substring(0, index);
			
			for(int i = 0; i < tokens.length; i++)
				temp += (Rift.symbols.containsKey(tokens[i]) ? Rift.symbols.get(tokens[i]) : tokens[i]) + ",";
			
			temp = temp.substring(0, temp.length() - 1);
			tokens = temp.split(" ", 2);
			
			if(tokens.length == 1)
				tokens = new String[] { tokens[0], "" };
			
			if(Rift.directives.containsKey(tokens[0])) {
				try {
					Rift.d = (Directives) Class.forName(tokens[0]).newInstance();
					
					if(!Rift.directives.get(tokens[0]).matcher(tokens[1]).matches()) {
						line.setError("Invalid operands for " + tokens[0] + " directive.");
						continue;
					}
					
					if(!Rift.d.execute(tokens[1], line))
						return;
				}
				
				catch(Exception e) {
					line.setError(e.getMessage());
				}
				
				temp = "";
			}
			
			line.parsedLine = temp;
		}
	}
	
	/**
	*	Tokenizes the parsed lines into mnemonics and operands. A Mnemonic obj
	*	is created for every line with a mnemonic in it. The validate method
	*	tries to match the given operands with the regex as defined in hexcodes.gr.
	*	
	*	@param None
	*	@return void
	*/
	static void tokenize() {
		
		String temp;
		String[] tokens;
		
		for(Line line : Rift.lines) {
			if(line.parsedLine == null)
				return;
			
			temp = line.parsedLine;
			
			if(temp.length() > 0) {
				tokens = temp.split(" ", 2);
				
				try {
					line.m = new Mnemonics(tokens[0]);
					
					if(!line.m.validate(tokens.length == 2 ? tokens[1] : ""))
						line.setError("Invalid operand(s) for Mnemonic " + tokens[0]);
				}
				
				catch(Exception e) {
					line.setError(e.getMessage());
				}
			}
		}
	}
	
	/**
	*	Prints errors that were set by other methods. Program stops if errors are
	*	found.
	*	
	*	@param None
	*	@return void
	*	@throws None
	*/
	static void printErrors() {
		
		boolean errors = false;
		
		for(int i = 0; i < Rift.lines.size(); i++) {
			
			Line line = Rift.lines.get(i);
			
			if(!line.errorStatements.isEmpty()) {
				errors = true;
				
				for(String error : line.errorStatements)
					System.out.println(Rift.fileName + "::" + (i + 1) + error);
			}
		}
		
		if(errors)
			System.exit(-14);
	}
	
	/**
	*	Allocates address to statements with proper instruction.
	*
	*	@param None
	*	@return void
	*	@throws None
	*/
	static void allocROMAddr() {
		
		int start = 0x0000;
		
		for(Line line : Rift.lines) {
			if(line.parsedLine == null)
				return;
			
			if(line.address != null) {
				if(Integer.parseInt(line.address, 16) < start) {
					line.setError("Address changes must be progressive.");
					return;
				}
				
				start = Integer.parseInt(line.address, 16);
				line.address = null;
			}
			
			else if(line.m != null) {
				line.address = String.format("%4X", start).replace(' ', '0');
				start += line.m.size;
				
				if(start > 0xFFFF) {
					line.setError("Internal ROM full.");
					return;
				}
			}
			
			else if(line.label != null)
				line.address = String.format("%4X", start).replace(' ', '0');
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
		
		for(Line line : Rift.lines) {
			if(line.parsedLine == null)
				return;
			
			if(line.m != null) {
				if(line.m.opcode.indexOf(":") != -1) {
					tokens = line.m.opcode.split(":");
					
					try {
						if(line.m.mnemonic.equals("AJMP") || line.m.mnemonic.equals("ACALL"))
							line.m.opcode = HelperMethods.calcAddr(tokens[1], line);
						
						else
							line.m.opcode = tokens[0] + HelperMethods.calcAddr(tokens[1], line);
					}
					
					catch(Exception e) {
						line.setError(e.getMessage());
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
	static String calcAddr(String label, Line l) throws Exception {
		//BEWARE! BLACK MAGIC AHEAD!! ;)
		for(Line line : Rift.lines) {
			if(line.parsedLine == null)
				break;
			
			if(line.label != null && line.label.equals(label)) {
				String mnemonic = l.m.mnemonic;
				
				if(mnemonic.equals("LCALL") || mnemonic.equals("LJMP"))
					return line.address;
				
				int labelAddress = Integer.parseInt(line.address, 16);
				int lineAddress = Integer.parseInt(l.address, 16) + l.m.size;
				int jump = labelAddress - lineAddress;
				
				if(mnemonic.equals("SJMP") && (jump < -128 || jump > 127))
					throw new Exception(String.format("Given jump range of %s exceeds limit for SJMP (-128 to 127)", jump));
				
				else if(mnemonic.equals("AJMP") || mnemonic.equals("ACALL")) {
					if(!(labelAddress >= (lineAddress / 2048) * 2048 && labelAddress < (lineAddress / 2048 + 1) * 2048))
						throw new Exception(String.format("Label address is not a part of the %s 2KB block.", mnemonic));
					
					String opcode = String.format("%16s", Integer.toBinaryString(labelAddress)).replace(' ', '0');
					opcode = opcode.substring(5, 8) + (mnemonic.equals("AJMP") ? "0" : "1") + "0001" + opcode.substring(8, 16);
					
					return String.format("%4X", Integer.parseInt(opcode, 2)).replace(' ', '0');
				}
				
				return String.format("%2X", jump).replace(' ', '0');
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
			
			for(int i = 0; i < Rift.lines.size(); i++) {
				Line line = Rift.lines.get(i);
				
				if(line.parsedLine == null)
					break;
				
				lstFile.println(String.format("%-8d%-6s%-7s %s", i + 1, line.address != null ? line.address : "", line.m != null ? line.m.opcode : "", line.rawLine));
			}
			
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
					temp = String.format("%2X%S00%S", line.m.size, line.address, line.m.opcode).replace(' ', '0');
					
					for(int i = 0; i < temp.length() / 2; i++)
						checksum += Integer.parseInt(temp.substring(2 * i, 2 * (i + 1)), 16);
					
					hexFile.println(":" + temp + Integer.toHexString(0x100 - checksum % 256).toUpperCase());
				}
			}
			
			hexFile.println(":00000001FF");
			hexFile.close();
		}
		
		catch(Exception e) {
			System.out.println("Cannot create hex file.");
		}
	}
}