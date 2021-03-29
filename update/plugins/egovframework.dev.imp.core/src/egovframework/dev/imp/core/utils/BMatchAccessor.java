package egovframework.dev.imp.core.utils;

public class BMatchAccessor{
	private String matchStr;
	private int start;
	private int length;
	public BMatchAccessor(){
	}
	public BMatchAccessor(String str, int s, int l){
		matchStr=str;
		start = s;
		length = l;
	}
	public String getMatchStr() {
		return matchStr;
	}
	public void setMatchStr(String matchStr) {
		this.matchStr = matchStr;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
}
