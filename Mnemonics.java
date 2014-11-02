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
	
	String asciify(String s) throws Exception {
		
		String temp = "";
		s = s.substring(1, s.length() - 1);
		
		for(int i = 0; i < s.length(); i++)
			temp += String.format("%2s", Integer.toHexString((int) s.charAt(i))).replace(" ", "0");
		
		return temp;
	}
	
	String hexify(String s) throws Exception {
		
		int temp;
		
		try {
			if(s.charAt(0) == '\"') {
				s = this.asciify(s);
				
				if(this.mnemonic == null)
					return (s.length() % 2 == 0 ? s : "0" + s).toUpperCase();
				
				temp = Integer.parseInt(s, 16);
			}
			
			else if(s.charAt(s.length() - 1) == 'H')
				temp = Integer.parseInt(s.substring(0, s.length() - 1), 16);
			
			else if(s.charAt(s.length() - 1) == 'B')
				temp = Integer.parseInt(s.substring(0, s.length() - 1), 2);
			
			else
				temp = Integer.parseInt(s.replace("D", ""));
			
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
	
	boolean validate(String operands) throws Exception {
		
		Matcher match;
		String operand;
		ArrayList<Object[]> op = Rift.opcodes.get(this.mnemonic);
		
		for(Object[] o : op) {
			match = ((Pattern) o[0]).matcher(operands);
			
			if(match.matches()) {
				this.opcode = (String) o[1];
				this.size = 1 + match.groupCount() + (this.opcode.equals("02") || this.opcode.equals("12") || this.opcode.equals("90") ? 1 : 0);
				
				for(int i = 1; i <= match.groupCount(); i++) {
					operand = match.group(i);
					
					if(Rift.symbols.containsKey(operand))
						this.opcode += Rift.symbols.get(operand);
					
					else if(LABEL.matcher(operand).matches())
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
	
	boolean validate(String operands) throws Exception {
		
		Matcher match;
		final Pattern p = Pattern.compile("(?:(?:-?[01]+B|-?\\d+D?|-?\\d[0-9A-F]*H|\"\\p{ASCII}+\") *(?:, *|$))+");
		
		if(!p.matcher(operands).matches())
			throw new Exception("Invalid operands for DB directive.");
		
		String[] tokens = operands.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$) *");
		
		for(String data : tokens)
			this.opcode += this.hexify(data);
		
		this.size = this.opcode.length() / 2;
		
		if(this.size > 255)
			throw new Exception("DB can handle only 255 bytes of data.");
		
		return true;
	}
}