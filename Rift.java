import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Pattern;

class Line {
	
	Mnemonics m;
	
	String label;
	String address;
	String rawLine;
	String parsedLine;
	
	ArrayList<String> errorStatements = new ArrayList<String>();
	
	Line(String l) {
		
		this.rawLine = l;
	}
	
	void setError(String error) {
		
		this.errorStatements.add(String.format(": %s\n%s", error, this.rawLine));
	}
}

class Rift {
	
	static Directives d;
	static String fileName;
	static ArrayList<Line> lines = new ArrayList<Line>();
	static HashMap<String, String> symbols = new HashMap<String, String>();
	static HashMap<String, Pattern> directives = new HashMap<String, Pattern>();
	static HashMap<String, ArrayList<Object[]>> opcodes = new HashMap<String, ArrayList<Object[]>>();
	
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
