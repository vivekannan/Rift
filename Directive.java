abstract class Directive {
	
	abstract boolean execute(String operands, Line line) throws Exception;
}

class DB extends Directive {
	
	String hexify(String s) throws Exception {
		
		int temp;
		
		try {
			if(s.charAt(0) == '"')
				return s.substring(1, s.length() - 1);
			
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
			
			if(s.length() > 2)
				throw new Exception("Directive DB expects 8-bit data or String.");
			
			return String.format("%2S", s).replace(' ', '0');
		}
		
		catch(NumberFormatException e) {
			throw new Exception("Invalid number for given base.");
		}
	}
	
	boolean execute(String operands, Line line) throws Exception {
		
		int size;
		String temp = "";
		
		for(String data : operands.replace("#", "").split(","))
			temp += this.hexify(data);
		
		size = temp.length() / 2;
		
		if(size > 16)
			throw new Exception(String.format("DB can handle only 16 bytes of data. Given %d bytes.", size));
		
		line.m = new Mnemonics("\"RESERVED\"");
		line.m.size = size;
		line.m.opcode = temp;
		
		return true;
	}
}

class ORG extends Directive {
	
	boolean execute(String operands, Line line) throws Exception {
		
		line.address = operands.replace("H", "");
		
		return true;
	}
}

class BIT extends Directive {
	
	boolean execute(String operands, Line line) throws Exception {
		
		String[] tokens = operands.split(" ");
		
		if(Rift.opcodes.containsKey(tokens[0]) || Rift.symbols.containsKey(tokens[0]) || Rift.directives.containsKey(tokens[0]))
			throw new Exception("Symbol already defined or is a Mnemonic/Directive");
		
		Rift.symbols.put(tokens[0], tokens[1]);
		
		return true;
	}
}

class EQU extends Directive {
	
	boolean execute(String operands, Line line) throws Exception {
		
		String[] tokens = operands.split(" ");
		
		if(Rift.opcodes.containsKey(tokens[0]) || Rift.symbols.containsKey(tokens[0]) || Rift.directives.containsKey(tokens[0]))
			throw new Exception("Symbol already defined or is a Mnemonic/Directive");
		
		Rift.symbols.put(tokens[0], tokens[1]);
		
		return true;
	}
}

class END extends Directive {
	
	boolean execute(String operands, Line line) throws Exception {
		
		line.parsedLine = "";
		
		return false;
	}
}