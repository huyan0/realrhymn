package com.realrhymn.rhymnbook.dao;

import java.util.List;

import com.realrhymn.rhymnbook.model.Word;

public interface MySQLCustomRepository {
	
	/**
	 * For each object in the lsit, create a entry with parameters. If word already exist, update its frequency in
	 * table by freq
	 * @param word
	 * @param freq
	 * @param mp
	 */
	public List<Word> saveWords(List<Word> wordList);
	
	
	/**
	 * For each object in the lsit, create a entry with parameters. If word already exist, update its frequency in
	 * table by freq
	 * @param word
	 * @param freq
	 * @param mp
	 */
	public List<Word> saveWordsV2(List<String> wordList);
}
