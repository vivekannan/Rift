import java.io.*;
import java.util.*;


class Line {
	
	public String rawLine;
	public String parsedLine;
	public int lineNumber;
	public String label;
	public String errorStatement;
	public Mnemonics mnemonic;
	
	Line(String l, int ln) {
		this.rawLine = l;
		this.lineNumber = ln;
		this.label = "";
		this.errorStatement = "";
	}
}

class Boo {
	
	static {
		HelperClasses h;
	}
	
	private static String fileName;
	private static List<Line> lines = new ArrayList<Line>();
	
	public static void addLine(Line line) {
		
		lines.add(line);
	}
	
	public static int getLineCount() {
		
		return lines.size();
	}
	
	public static String getRawLine(int i) {
		
		return lines.get(i).rawLine;
	}
	
	public static String getParsedLine(int i){
		
		return lines.get(i).parsedLine;
	}
	
	public static void setParsedLine(String line, int i) {
		
		Line l = lines.get(i);
		l.parsedLine = line;
		lines.set(i, l);
	}
	
	public static void setErrorStatement(String error, int i) {
		
		Line l = lines.get(i);
		l.errorStatement = error;
		lines.set(i, l);
	}
	
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
			
			ReadFile.read(fileName);
			Parser.parse();
			Tokenizer.tokenize(fileName);
			
			printErrors();
		}
		
		else
			System.out.println("File name not given.");
	}
}
