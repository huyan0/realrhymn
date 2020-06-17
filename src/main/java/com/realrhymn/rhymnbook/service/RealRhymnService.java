package com.realrhymn.rhymnbook.service;

import java.util.Collection;
import java.util.List;

import com.realrhymn.rhymnbook.model.Word;

public interface RealRhymnService {
	
	/**
	 * Save s to the database. The new entry's frequency is one.
	 * If s already exist, then its entry's frequency increase by one
	 * s must be in Hanzi and have more than one character;
	 * Return true is s is valid, false otherwise.	
	 */
	public boolean saveWord(String s);	
	
	/**
	 * Save all the words in List<Word> l. Behaves the same as saveWord()
	 */
	public List<String> saveWords(Collection<Word> collection);
	
	/**
	 * Save all the words in List<Word> l. Behaves the same as saveWord()
	 */
	public List<String> saveWordsV2(Collection<String> collection);
	
	/**
	 * Reduce the entry corresponding to String s by one
	 */
	public String deleteWord(String s);
	
	/**
	 * Update the frequency of s by adding incrementFrequency 
	 * if s does not exist, the frequency of s is incrementFrequency 
	 */
	public String changeWordFrequencyBy(String s, int incrementFrequency);

	/**
	 * set the frequency of s to frequency. An entry corresponding to s must exist, else
	 * no change will happen.
	 */
	public String setWordFrequency(String s, int frequency);

	
	/**
	 * Remove w's corresponding entry from the database
	 */
	public String deleteWholeWord(String s);
	
	/**
	 * Get limit number of words with common prefix as w.mp, in descending order by frequency
	 */
	public List<String> getRhymnWord(Word w, int limit);

	

}
