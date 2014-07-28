import java.lang.reflect.Constructor;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

class HelperClasses {
	
	static void read() {
		
		int i = 0;
		String line;
		BufferedReader assemblySource = null;
		
		try {
			assemblySource = new BufferedReader(new FileReader(Boo.fileName));
			
			while((line = assemblySource.readLine()) != null)
				Boo.lines.add(new Line(line, ++i));
			
			assemblySource.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println(Boo.fileName + " not found.");
			System.exit(0);
		}
		
		catch(IOException e) {
			System.out.println(Boo.fileName + " cannot be opened/closed.");
			System.exit(0);
		}
	}
	
	static void parse() {
		
		Line temp;
		String line;
		int commentIndex;
		
		for(int i = 0; i < Boo.lines.size(); i++) {
			
			temp = Boo.lines.get(i);
			line = temp.rawLine;
			commentIndex = line.indexOf(';');
			
			if(commentIndex != -1)
				line = line.substring(0, commentIndex);
			
			temp.parsedLine = line.trim().replaceAll("\\s{2,}", " ").replaceAll("\\s?,\\s?", ",");
		}
	}
	
	static void tokenize() {
		
		Line temp;
		String line;
		String[] tokens;
		
		for(int i = 0; i < Boo.lines.size(); i++) {
			
			temp = Boo.lines.get(i);
			line = temp.parsedLine;
			
			if(line.length() > 0) {
				
				if(line.matches("^[A-Z][A-Z0-9]*:.*$")) {
					tokens = line.split(": ?", 2);
					temp.label = tokens[0];
					line = tokens[1];
				}
				
				tokens = line.split(" ", 2);
				
				try {
					if(tokens.length == 2) {
						temp.m = (Mnemonics) Class.forName(tokens[0]).getConstructor(String.class).newInstance(tokens[1]);
						
						if(!temp.m.validate())
							temp.setError("Invalid operand(s) for Mnemonic " + tokens[0]);
					}
					
					else if(tokens.length == 1) {
						if(tokens[0].equals("END"))
							return;
						
						temp.m = (Mnemonics) Class.forName(tokens[0]).newInstance();
					}
					
					else
						temp.setError("Unidentified statement.");
				}
				
				catch(Exception e) {
					temp.setError("Unindentified Mnemonic: " + tokens[0]);
				}
			}
		}
	}
}
