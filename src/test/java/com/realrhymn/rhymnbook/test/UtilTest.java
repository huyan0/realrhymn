package com.realrhymn.rhymnbook.test;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.util.Util;

public class UtilTest {
	  /*
	    * Other test
	    */
	
		@Test
		public void testWordEquals() {
			assertTrue(((new Word("战神")).getClass().getName()).equals(((new Word("犯人")).getClass().getName())));
			assertTrue("Word.equals() failed",(new Word("战神")).equals(new Word("战神")));
			TreeSet<Word> s = new TreeSet<Word>();
			s.add(new Word("战神"));
			s.add(new Word("战神"));
			assertTrue(s.size() == 1);
			}
	    @Test
	    public void testWordToPinyin() {
	    	Set<String> rSet = new HashSet<String>();
	    	rSet.addAll(Util.mulRhymn);
	    	rSet.addAll(Util.singleRhymn);
	    	for(String sound : Util.singleSound) {
	    		for(String r : rSet) {
	    			String pinyin = sound + r;
	    			assertTrue("Match Failed: " + pinyin + " yield " + Util.filterRhymn(pinyin) ,Util.filterRhymn(pinyin).equals(r));
	    			for(String rr : Util.mulRhymn) {
	        			pinyin = sound+r+rr;
	        			assertTrue("Match Failed: " + pinyin + " yield " + Util.filterRhymn(pinyin),Util.filterRhymn(pinyin).equals(rr));
	        		}
	    		}
	    	}
	    }
	    @Test
	    public void testInvalidInput() {
	    	try {
	    		new Word("hello");
	    	} catch(Exception e) {
	    		
	    	}
	    		
	    }
	    
}
