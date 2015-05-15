package it.dpascotto.drools.examples.TicTacToe;

public class XO {
	
	public final static String X = "X";
	public final static String O = "O";
	public final static String EMPTY = " ";
	
	String val = EMPTY;
	
	public static XO _EMPTY() {
		return new XO(XO.EMPTY);
	}
	
	public static XO _X() {
		return new XO(XO.X);
	}
	
	public static XO _O() {
		return new XO(XO.O);
	}
	
	public boolean isEmpty() {
		return val.equals(EMPTY);
	}
	
	public boolean isX() {
		return val.equals(X);
	}
	
	public boolean isO() {
		return val.equals(O);
	}
	
	
	
	public XO(String aVal) {
		this.val = aVal;
	}
    
    public String toString() {
        return val;
    }


	public String toReadable() {
		if (val.equals(XO.EMPTY)) {
			return "nobody";
		} else if (val.equals(XO.X)) {
			return "human";
		} else if (val.equals(XO.O)) {
			return "computer";
		}
		return "?";
	}
	
	public Object clone() {
		XO cloned = new XO(this.val);
		
		return cloned;
	}
}
