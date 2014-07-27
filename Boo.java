import java.util.ArrayList;
import java.util.List;

class Boo {
	
	private static String fileName;
	private static List<Line> lines = new ArrayList<Line>();
	
	private static boolean printLineErrors() {
		
		boolean errors = false;
		String e;
		
		for(int i = 0; i < lines.size(); i++) {
			e = lines.get(i).errorStatement;
			if(e != null) {
				errors = true;
				System.out.println(e);
			}
		}
		
		return errors;
	}
	
	public static void main(String args[]) {
		
		if(args.length > 0) {
			fileName = args[0];
			
			ReadFile.read(fileName, lines);
			Parser.parse(lines);
			Tokenizer.tokenize(fileName, lines);
			
			if(!printLineErrors())
				System.out.println("All systems nominal.");
		}
		
		else
			System.out.println("File name not given.");
	}
}
