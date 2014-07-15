import java.io.*;
import java.util.*;

class Boo {
	
	private static List<String> parsedLines = new ArrayList<String>();
	private static String fileName;
	
	public static String getFileName() {
		return fileName;
	}
	
	public static void main(String args[]) {
		
		if(args.length > 0) {
			fileName = args[0];
			
			parsedLines = Parser.parse(fileName);
			
			if(parsedLines.size() > 0)
				Tokenizer.tokenize(parsedLines);
		}
		
		else {
			System.out.println("File name not given.");
			System.exit(0);
		}
	}
}
