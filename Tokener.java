import java.io.*;
import java.util.*;
import java.util.regex.*;

class Tokener {
	
	private static Pattern registers = Pattern.compile("^A|(?:R[0-7])$");
	private static Pattern data = Pattern.compile("^#[0-9A-F]{2,2}$");
	private static Pattern address = Pattern.compile("^[0-9A-F]{2,2}$");
	
	private static boolean isRegister(String s) {
		
		return registers.matcher(s).matches();
	}
	
	private static boolean isData(String s) {
		
		return data.matcher(s).matches();
	}
	
	private static boolean isAddress(String s) {
		
		return address.matcher(s).matches();
	}
	
	private static boolean isMovable(String[] s) {
		
		return (isAddress(s[0]) || isRegister(s[0])) && (isAddress(s[1]) || isRegister(s[1]) || isData(s[1]));
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
					
					if(isData(operands[1]))
						System.out.print("data ");
					
					else if(isAddress(operands[1]))
						System.out.print("value at address ");
					
					else if(isRegister(operands[1]))
						System.out.print("value at register ");
						
					System.out.print(operands[1] + " to ");
					
					if(isAddress(operands[0]))
						System.out.print("address ");
					
					else if(isRegister(operands[0]))
						System.out.print("register ");
					
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
