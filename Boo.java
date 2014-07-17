import java.io.*;
import java.util.*;


class Line {
	
	public String rawLine;
	public String parsedLine;
	public int lineNumber;
	public String[] tokens;
	public String label;
	
	Line(String l, int ln) {
		this.rawLine = l;
		this.lineNumber = ln;
		this.label = "";
	}
}

class Boo {
	
	private static String fileName;
	private static List<Line> lines = new ArrayList<Line>();
	
	public static String getFileName() {
		
		return fileName;
	}
	
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
	
	public static void main(String args[]) {
		
		new HelperClasses();
		
		if(args.length > 0) {
			fileName = args[0];
			
			ReadFile.read();
			Parser.parse();
			Tokenizer.tokenize();
		}
		
		else {
			System.out.println("File name not given.");
			System.exit(0);
		}
	}
}
