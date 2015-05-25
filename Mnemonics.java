import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Mnemonics {
	
	int size;
	String opcode;
	String mnemonic;
	final static Pattern LABEL = Pattern.compile("[A-Z][A-Z\\d]*");
	
	Mnemonics(String line) throws Exception {
		
		String[] tokens = line.split(" ", 2);
		
		if(!Rift.opcodes.containsKey(tokens[0]))
			throw new Exception("Unidentified Mnemonic: " + tokens[0]);
		
		this.mnemonic = tokens[0];
		this.validate(tokens.length == 1 ? "" : tokens[1]);
	}
	
	String hexify(String s, int i) throws Exception {
		
		int temp;
		
		try {
			if(s.charAt(0) == '"')
				temp = Integer.parseInt(s.substring(1, s.length() - 1), 16);
			
			else if(s.charAt(s.length() - 1) == 'H')
				temp = Integer.parseInt(s.substring(0, s.length() - 1), 16);
			
			else if(s.charAt(s.length() - 1) == 'B')
				temp = Integer.parseInt(s.substring(0, s.length() - 1), 2);
			
			else
				temp = Integer.parseInt(s.replace("D", ""));
			
			//If number is negative, add shift value to obtain two's complement.
			if(temp < 0)
				temp += temp > -255 ? 256 : 65536;
			
			s = Integer.toHexString(temp);
			
			if(s.length() > i * 2)
				throw new Exception(String.format("Mnemonic %s expects %d-byte address/data.", this.mnemonic, i));
			
			return String.format("%" + i * 2 + "S", s).replace(' ', '0');
		}
		
		catch(NumberFormatException e) {
			throw new Exception("Invalid number for given base.");
		}
	}
	
	/**
	*	Receives the operands for a particular Mnemonic and tries to match the
	*	operands with the predefined regex given in hexcodes.txt.
	*	
	*	@param String operands - The operands parsed from the statement.
	*	@return boolean - True, if operands are proper else false.
	*	@throws Exception - Passes exception thrown by hexify().
	*/
	void validate(String operands) throws Exception {
		
		Matcher match;
		String operand;
		ArrayList<Opcode> op = Rift.opcodes.get(this.mnemonic);
		
		for(Opcode o : op) {
			match = o.p.matcher(operands);
			
			if(match.matches()) {
				this.opcode = o.opcode;
				this.size = o.opSize;
				
				for(int i = 1; i <= match.groupCount(); i++) {
					operand = match.group(i);
					
					if(LABEL.matcher(operand).matches())
						this.opcode += ":" + operand;
					
					else
						this.opcode += this.hexify(operand, o.operandSize.get(i - 1));
				}
				
				return;
			}
		}
		
		throw new Exception("Invalid operand(s) for Mnemonic " + this.mnemonic);
	}
}