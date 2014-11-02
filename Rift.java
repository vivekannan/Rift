import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

class Line {
	
	String org;
	Mnemonics m;
	String label;
	int lineNumber;
	String address;
	String rawLine;
	String parsedLine;
	ArrayList<String> errorStatements = new ArrayList<String>();
	
	Line(String l, int ln) {
		
		this.rawLine = l;
		this.lineNumber = ln;
	}
	
	void setError(String error) {
		
		this.errorStatements.add(String.format("%d: %s\n%s", this.lineNumber, error, this.rawLine));
	}
}

class Rift {
	
	static String fileName;
	static ArrayList<Line> lines = new ArrayList<Line>();
	static HashMap<String, String> symbols = new HashMap<String, String>();
	static HashMap<String, ArrayList<Object[]>> opcodes = new HashMap<String, ArrayList<Object[]>>();
	static HashSet<String> directives = new HashSet<String>(Arrays.asList("ORG", "END", "BIT", "EQU", "DB"));
	
	public static void main(String args[]) {
		
		if(args.length == 1) {
			fileName = args[0];
			
			HelperMethods.getOpcodes();
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
