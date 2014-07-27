import java.util.ArrayList;
import java.util.List;

class Line {
	
	String rawLine;
	String parsedLine;
	String label;
	int lineNumber;
	String errorStatement;
	Mnemonics m;
	
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
	static List<Line> lines = new ArrayList<Line>();
	
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
			
			HelperClasses.read();
			HelperClasses.parse();
			HelperClasses.tokenize();
			
			if(!printLineErrors())
				System.out.println("All systems nominal.");
		}
		
		else
			System.out.println("File name not given.");
	}
}
