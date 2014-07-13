import java.io.*;
import java.util.*;

class Boo {
	
	private static List<String> parsedLines = new ArrayList<String>();
	
	public static void main(String args[]) {
		
		if(args.length > 0) {
			parsedLines = Parser.parse(args[0]);
			
			if(parsedLines.size() > 0)
				for(int i = 0; i < parsedLines.size(); i++)
					if(parsedLines.get(i).startsWith("MOV"))
						System.out.println(parsedLines.get(i));
		}
		
		else {
			System.out.println("File name not given.");
			System.exit(0);
		}
	}
}
