package com.realrhymn.rhymnbook.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.promeg.pinyinhelper.Pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Util {
	
	
	public static int frequencyThreshhold = 0;
	public static double exactMatchNumber = 0.5;
	public static double descendentMatchNumber = 0.3;
	public static double parentMatchNumber = 0.2;
	public static HashSet<String> mulRhymn = new HashSet<String>(Arrays.asList("ai ei ui ao ou iu ie ve er an en in un vn ang eng ing ong".split(" ")));
	public static HashSet<String> singleRhymn = new HashSet<String>(Arrays.asList("a o e i u v".split(" ")));
	public static HashSet<String> singleSound = new HashSet<String>(Arrays.asList("b p m f d t n l g k h j q x zh ch sh r z c s y w ".split(" ")));
	
	public static <T> void storeAsJson(T t, File f) {
		ObjectMapper m = new ObjectMapper();
	    m.configure(SerializationFeature.INDENT_OUTPUT,true);

		try {
			m.writeValue(f, t);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static <T> void storeAsJson(T t, String s) {
		File f = new File(s);
		Util.storeAsJson(t, f);
	}
	
	public static <T> T readFile(String fname, Class<T> c) {
		return readFile(new File(fname), c);
	}
	
	public static <T> T readFile(File fname, Class<T> c) {
		ObjectMapper m = new ObjectMapper();
		try {
			T s = (T) m.readValue(fname, c);
			return s;
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> ArrayList<String> iterableToStringList(Iterable<T> hs) {
		ArrayList<String> res = new ArrayList<String>();
		for(T t : hs) {
			res.add(t.toString());
		}
		return res;
	}
	
	public static <T> void printIterable(Iterable<T> l) {
		for(T t : l) {
			System.out.println(t.toString());
		}
	}
	
	public static String wordToMp(String word) {
		HanyuPinyinOutputFormat f = new HanyuPinyinOutputFormat();
		f.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		f.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		f.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuilder s = new StringBuilder();
		char[] wordArr = word.trim().toCharArray(); 
		for(int i = wordArr.length - 1; i >=0; i--) {
			char c = wordArr[i];
			if(Pinyin.isChinese(c)) {
				try {
					s.append("-");
					s.append(Util.filterRhymn(PinyinHelper.toHanyuPinyinStringArray(c,f)[0]));	
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				return "";
			}
		}
		return s.toString();
	}
	
	public static String filterRhymn(String pinyin) {
		if(pinyin.length() >= 3 && mulRhymn.contains(pinyin.substring(pinyin.length() - 3))) {
			return pinyin.substring(pinyin.length() - 3);
		}	
		if(pinyin.length() >= 2 && mulRhymn.contains(pinyin.substring(pinyin.length() - 2))) {
			return pinyin.substring(pinyin.length() - 2);
		}
		if(pinyin.length() >= 3 && singleRhymn.contains(pinyin.substring(pinyin.length() - 1))) {
			return pinyin.substring(pinyin.length() - 1);
		}	
		if(pinyin.length() >= 2 && singleRhymn.contains(pinyin.substring(pinyin.length() - 1))) {
			return pinyin.substring(pinyin.length() - 1);
		}
		return pinyin;
	}
	
	public static boolean checkIsHanzi(String word) {
		for(char c : word.toCharArray()) {
			if(!Pinyin.isChinese(c)) {
				return false;
			}
		}
		return true;
	}
	
	public static String getMpParent(String mp) {
		return mp.substring(0,mp.lastIndexOf('-')).length() == 1 ? mp : mp.substring(0,mp.lastIndexOf('-'));
	}
	
	public static boolean checkIsMp(String mp) {
		return mp.startsWith("-");
	}
	public static boolean isValidWord(String word) {
		return Util.checkIsHanzi(word) && word.length() > 1;
	}
}
