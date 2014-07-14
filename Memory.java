import java.util.regex.*;

class Memory {
}

class InternalRAM {
}

class ExternalRAM {
}

class InternalROM {
}

class ExternalROM {
}

class SpecialFunctionRegisters {
	
	private static short PC;
	private static short DPTR;
	private static byte SP;
	private static byte A;
	private static byte[] R = new byte[7];
	private static byte PSW;
}

class GeneralPurposeRegisters {
}

class addressingTypes {
	
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
