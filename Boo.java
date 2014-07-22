import java.io.*;
import java.util.*;

class Boo {
	
	private static String fileName;
	private static List<Line> lines = new ArrayList<Line>();
	
	private static void printLineErrors() {
		
		String e;
		
		for(int i = 0; i < lines.size(); i++) {
			e = lines.get(i).errorStatement;
			if(e != null)
				System.out.println(e);
		}
	}
	
	public static void main(String args[]) {
		
		if(args.length > 0) {
			fileName = args[0];
			
			ReadFile.read(fileName, lines);
			Parser.parse(lines);
			Tokenizer.tokenize(fileName, lines);
			
			printLineErrors();
		}
		
		else
			System.out.println("File name not given.");
	}
}
