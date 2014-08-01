import java.lang.reflect.Constructor;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

class HelperClasses {
	
	static HashMap<String, List<Pattern>> hexCodes = new HashMap<String, List<Pattern>>();
	
	static {
		try {
			String line;
			String[] tokens;
			Pattern p;
			BufferedReader hexSource = new BufferedReader(new FileReader("hexcodes.txt"));
			
			while((line = hexSource.readLine()) != null) {
				tokens = line.split(" ", 2);
				p = Pattern.compile(tokens[1]);
				
				if(hexCodes.containsKey(tokens[0]))
					hexCodes.get(tokens[0]).add(p);
					
				else {
					List<Pattern> temp = new ArrayList<Pattern>();
					temp.add(p);
					hexCodes.put(tokens[0], temp);
				}
			}
		}
		
		catch(Exception e) {
			System.out.println("Can't find hex codes! Exiting...");
			System.exit(0);
		}
	}
	
	static void read() {
		
		int i = 0;
		String line;
		BufferedReader assemblySource;
		
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
		Pattern label = Pattern.compile("^[A-Z][A-Z0-9]*:.*$");
		
		for(int i = 0; i < Boo.lines.size(); i++) {
			
			temp = Boo.lines.get(i);
			line = temp.parsedLine;
			
			if(line.length() > 0) {
				
				if(label.matcher(line).matches()) {
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
