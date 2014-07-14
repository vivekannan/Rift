import java.io.*;
import java.util.*;
import java.util.regex.*;

class addressing {
	
	private static Pattern registerDirect = Pattern.compile("^A|(?:R[0-7])$");
	private static Pattern registerIndirect = Pattern.compile("^@(?:A|R[0-7])$");
	private static Pattern immediate = Pattern.compile("^#[0-9A-F]{2,2}$");
	private static Pattern addressDirect = Pattern.compile("^[0-9A-F]{2,2}$");
	
	public static boolean isRegisterDirect(String s) {
		
		return registerDirect.matcher(s).matches();
	}
	
	public static boolean isImmediate(String s) {
		
		return immediate.matcher(s).matches();
	}
	
	public static boolean isAddressDirect(String s) {
		
		return addressDirect.matcher(s).matches();
	}
	
	public static boolean isRegisterIndirect(String s) {
		
		return registerIndirect.matcher(s).matches();
	}
}

class Tokenizer {
	
	public static void tokenize(List<String> parsedLines) {
		
		new Opcodes();
		
		String[] buffer;
		String[] operands;
		
		for(int i = 0; i < parsedLines.size(); i++) {
			buffer = parsedLines.get(i).split(" ");
			
			if(buffer[0].equals("MOV")) {
				new MOV(buffer[1], true);
			}
			
			else {
				System.out.println("Only MOV is supported!");
				return ;
			}
		}
	}
}
