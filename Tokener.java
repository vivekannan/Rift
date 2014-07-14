import java.io.*;
import java.util.*;
import java.util.regex.*;

class MOV {
	
}

class Tokener {
	
	private static Pattern registerDirect = Pattern.compile("^A|(?:R[0-7])$");
	private static Pattern registerIndirect = Pattern.compile("^@(?:A|R[0-7])$");
	private static Pattern immediate = Pattern.compile("^#(?:[0-9A-F]{2,2})|(?:[0-9A-F]{4,4})$");
	private static Pattern addressDirect = Pattern.compile("^(?:[0-9A-F]{2,2})|(?:[0-9A-F]{4,4})$");
	
	private static boolean isRegisterDirect(String s) {
		
		return registerDirect.matcher(s).matches();
	}
	
	private static boolean isImmediate(String s) {
		
		return immediate.matcher(s).matches();
	}
	
	private static boolean isAddressDirect(String s) {
		
		return addressDirect.matcher(s).matches();
	}
	
	private static boolean isRegisterIndirect(String s) {
		
		return registerIndirect.matcher(s).matches();
	}
	
	private static boolean isMovable(String[] s) {
		
		return (isRegisterDirect(s[0]) || isRegisterIndirect(s[0]) || isAddressDirect(s[0])) && (isRegisterDirect(s[1]) || isRegisterIndirect(s[1]) || isAddressDirect(s[1]) || isImmediate(s[1]));
	}
	
	public static void tokenize(List<String> parsedLines) {
		
		String[] buffer;
		String[] operands;
		
		for(int i = 0; i < parsedLines.size(); i++) {
			buffer = parsedLines.get(i).split(" ");
			
			if(buffer[0].equals("MOV")) {
				
				operands = buffer[1].split(",");
				
				if(operands.length != 2) {
					System.out.println("Error! MOV takes exactly 2 operands, given " + operands.length);
					return ;
				}
				
				else if(isMovable(operands)) {
					System.out.print("Moving ");
					
					if(isImmediate(operands[1]))
						System.out.print("data ");
					
					else if(isAddressDirect(operands[1]))
						System.out.print("value at address ");
					
					else if(isRegisterDirect(operands[1]))
						System.out.print("value at register ");
						
					else if(isRegisterIndirect(operands[1]))
						System.out.print("value pointed by register ");
					
					System.out.print(operands[1].replace('@', '\0').replace('#', '\0') + " to ");
					
					if(isAddressDirect(operands[0]))
						System.out.print("address ");
					
					else if(isRegisterDirect(operands[0]))
						System.out.print("register ");
						
					else if(isRegisterIndirect(operands[0]))
						System.out.print("location pointed by register ");
					
					System.out.println(operands[0]);
				}
				
				else {
					System.out.println("Invalid operands.");
					return ;
				}
			}
			
			else {
				System.out.println("Only MOV is supported!");
				return ;
			}
		}
	}
}
