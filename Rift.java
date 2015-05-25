import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class Opcode {
	
	Pattern p;
	int opSize;
	String opcode;
	ArrayList<Integer> operandSize;
	
	Opcode(String line, int lineNumber) throws PatternSyntaxException, NumberFormatException, Exception {
		String[] tokens = line.split(" ", -1);
		
		if(tokens.length < 3)
			throw new Exception();
		
		this.p = Pattern.compile(tokens[1]);
		this.opSize = Integer.parseInt(tokens[2]);
		this.opcode = String.format("%2X", lineNumber).replace(' ', '0');
		
		if(tokens.length > 3) {
			this.operandSize = new ArrayList<Integer>();
			
			for(int i = 3; i < tokens.length; i++)
				this.operandSize.add(Integer.parseInt(tokens[i]));
		}
		
		if(Rift.opcodes.containsKey(tokens[0]))
			Rift.opcodes.get(tokens[0]).add(this);
			
		else {
			ArrayList<Opcode> temp = new ArrayList<Opcode>();
			temp.add(this);
			Rift.opcodes.put(tokens[0], temp);
		}
	}
}

class Line {
	
	Mnemonics m;
	String label;
	String address;
	String rawLine;
	String parsedLine;
	
	ArrayList<String> errorStatements;
	
	Line(String l) {
		
		this.rawLine = l;
	}
	
	void setError(String error) {
		
		if(this.errorStatements == null)
			this.errorStatements= new ArrayList<String>();
		
		this.errorStatements.add(error);
	}
}

class Rift {
	
	static String fileName;
	static ArrayList<Line> lines = new ArrayList<Line>();
	static HashMap<String, String> symbols = new HashMap<String, String>();
	static HashMap<String, Pattern> directives = new HashMap<String, Pattern>();
	static HashMap<String, ArrayList<Opcode>> opcodes = new HashMap<String, ArrayList<Opcode>>();
	
	public static void main(String args[]) {
		
		if(args.length == 1) {
			fileName = args[0];
			
			HelperMethods.getOpcodes();
			HelperMethods.getDirectives();
			HelperMethods.getSymbols();
			
			HelperMethods.read();
			HelperMethods.parse();
			
			HelperMethods.tokenize();
			HelperMethods.printErrors();
			
			HelperMethods.allocROMAddr();
			HelperMethods.printErrors();
			
			HelperMethods.deLabelize();
			HelperMethods.printErrors();
			
			HelperMethods.createLst();
			HelperMethods.createHex();
		}
		
		else if(args.length > 1)
			System.out.println("Rift expects a single argument (asm source).");
		
		else
			System.out.println("File name not given.");
	}
}
