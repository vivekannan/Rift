import java.io.*;
import java.util.*;


class Line {
	
	public String rawLine;
	public String parsedLine;
	public int lineNumber;
	public String label = "";
	public String errorStatement = "";
	public Mnemonics mnemonic;
	
	Line(String l, int ln) {
		this.rawLine = l;
		this.lineNumber = ln;
	}
}

class Boo {
	
	static {
		HelperClasses h;
	}
	
	private static String fileName;
	private static List<Line> lines = new ArrayList<Line>();
	
	private static void printErrors() {
		
		for(int i = 0; i < lines.size(); i++) {
			String error = lines.get(i).errorStatement;
			if(error.length() > 0)
				System.out.println(error);
		}
	}
	
	public static void main(String args[]) {
		
		if(args.length > 0) {
			fileName = args[0];
			
			ReadFile.read(fileName, lines);
			Parser.parse(lines);
			Tokenizer.tokenize(fileName, lines);
			
			printErrors();
		}
		
		else
			System.out.println("File name not given.");
	}
}
