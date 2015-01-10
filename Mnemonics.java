import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Mnemonics {
	
	int size;
	String mnemonic;
	String opcode = "";
	final static Pattern LABEL = Pattern.compile("[A-Z][A-Z0-9]*");
	
	Mnemonics() {}
	
	Mnemonics(String mnemonic) throws Exception {
		
		if(!Rift.opcodes.containsKey(mnemonic))
			throw new Exception("Unidentified Mnemonic: " + mnemonic);
		
		this.mnemonic = mnemonic;
	}
	
	String hexify(String s) throws Exception {
		
		int temp;
		
		try {
			if(s.charAt(0) == '"') {
				if(this.mnemonic == null)
					return s.substring(1, s.length() - 1);
				
				temp = Integer.parseInt(s.substring(1, s.length() - 1), 16);
			}
			
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
			
			if((this.opcode.equals("90") && s.length() < 5) || s.length() < 3)
				return ((this.opcode.equals("90") ? "0000" : "00") + s).substring(s.length()).toUpperCase();
		
			throw new Exception();
		}
		
		catch(Exception e) {
			
			if(this.mnemonic == null)
				throw new Exception("Directive DB expects 8-bit data or String.");
			
			throw new Exception(String.format("Mnemonic %s expects %d-bit address/data.", this.mnemonic, this.opcode.equals("90") ? 16 : 8));
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
	boolean validate(String operands) throws Exception {
		
		Matcher match;
		String operand;
		ArrayList<Object[]> op = Rift.opcodes.get(this.mnemonic);
		
		for(Object[] o : op) {
			match = ((Pattern) o[0]).matcher(operands);
			
			if(match.matches()) {
				this.opcode = (String) o[1];
				this.size = (Integer) o[2];
				
				for(int i = 1; i <= match.groupCount(); i++) {
					operand = match.group(i);
					
					if(LABEL.matcher(operand).matches())
						this.opcode += ":" + operand;
					
					else
						this.opcode += this.hexify(operand);
				}
				
				return true;
			}
		}
		
		return false;
	}
}

class DB extends Mnemonics {
	
	static final Pattern DB_DIRECTIVE = Pattern.compile("(?:#?(?:-?(?:[01]+B|\\d+D?|\\d[0-9A-F]*H)|\"[0-9A-F]+\")(?:,|$))+");
	
	boolean validate(String operands) throws Exception {
		
		if(!DB_DIRECTIVE.matcher(operands).matches())
			throw new Exception("Invalid operands for DB directive.");
		
		operands = operands.replace("#", "");
		
		for(String data : operands.split(","))
			this.opcode += this.hexify(data);
		
		this.size = this.opcode.length() / 2;
		
		if(this.size > 16)
			throw new Exception(String.format("DB can handle only 16 bytes of data. Given %d bytes.", this.size));
		
		return true;
	}
}