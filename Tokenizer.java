import java.io.*;
import java.util.*;

//Receives parsed lines from Boo and tokenizes them and further creates Mnemonics objects as per Opcodes.java if the parsed lines are error free.

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
