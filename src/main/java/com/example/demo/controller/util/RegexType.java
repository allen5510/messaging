package com.example.demo.controller.util;
//// 包含數字＆字母 => 不能全字母+不能全數字+數字or字母
import com.example.demo.exception.FormatException;

public enum RegexType {
	EMAIL("[a-zA-Z0-9._]+@([a-zA-Z0-9_]+.[a-zA-Z0-9_]+)+"),
	PASSWORD("^(?![a-zA-Z]+$)(?![0-9]+$)[A-Za-z0-9]{8,}$"), // 包含數字＆字母且大於8個字
	NUMBER("[0-9]+");

	private String regex;

	RegexType(String regex) {
		this.regex = regex;
	}

	public boolean isMatchString(String str){
		if(str == null || str.isEmpty()) return false;
		return str.matches(regex);
	}

	public void verifyAndThrow(String str) throws FormatException {
		if(!isMatchString(str)) throw new FormatException(this.name().toLowerCase()+" format is error:"+str);
	}

	public static void verifyStringInRangeAndThrow(String str, int maxLength) throws FormatException {
		if(str == null || str.isEmpty()) throw new FormatException("cannot be empty");
		if(str.length() > maxLength) throw new FormatException(str+" exceed "+maxLength+" characters");
	}
}
