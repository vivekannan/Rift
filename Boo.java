import java.util.HashMap;
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
		
		this.errorStatements.add(String.format("::%d: %s\n%s", this.lineNumber, error, this.rawLine));
	}
}

class Boo {
	
	static String fileName;
	static ArrayList<Line> lines = new ArrayList<Line>();
	static HashMap<String, String> symbols = new HashMap<String, String>();
	static HashMap<String, ArrayList<Object[]>> opcodes = new HashMap<String, ArrayList<Object[]>>();
	
	public static void main(String args[]) {
		
		if(args.length > 0) {
			fileName = args[0];
			
			HelperMethods.getOpcodes();
			HelperMethods.getSymbols();
			HelperMethods.read();
			HelperMethods.parse();
			HelperMethods.tokenize();
			
			if(!HelperMethods.printErrors()) {
				HelperMethods.allocROMAddr();
				HelperMethods.deLabelize();
			}
			
			else
				return;
			
			if(!HelperMethods.printErrors())
				HelperMethods.writeToFile();
		}
		
		else
			System.out.println("File name not given.");
	}
}
