import java.util.ArrayList;
import java.util.HashMap;

class Line {
	
	Mnemonics m;
	String label;
	int lineNumber;
	String rawLine;
	String parsedLine;
	String errorStatement;
	
	Line(String l, int ln) {
		this.rawLine = l;
		this.lineNumber = ln;
	}
	
	void setError(String error) {
		
		this.errorStatement = String.format("::%d: %s\n%s", this.lineNumber, error, this.rawLine);
	}
}

class Boo {
	
	static String fileName;
	static ArrayList<Line> lines = new ArrayList<Line>();
	static HashMap<String, ArrayList<Object[]>> hexCodes = new HashMap<String, ArrayList<Object[]>>();
	
	static boolean printLineErrors() {
		
		boolean errors = false;
		String e;
		
		for(int i = 0; i < lines.size(); i++) {
			
			e = lines.get(i).errorStatement;
			
			if(e != null) {
				errors = true;
				System.out.println(fileName + e);
			}
		}
		
		return errors;
	}
	
	public static void main(String args[]) {
		
		if(args.length > 0) {
			fileName = args[0];
			
			HelperClasses.getOpcodes();
			HelperClasses.read();
			HelperClasses.parse();
			HelperClasses.tokenize();
			
			printLineErrors();
		}
		
		else
			System.out.println("File name not given.");
	}
}
