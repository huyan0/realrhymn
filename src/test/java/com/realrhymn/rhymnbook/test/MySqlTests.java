package com.realrhymn.rhymnbook.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realrhymn.rhymnbook.application.RealRhymnApplication;
import com.realrhymn.rhymnbook.dao.DynamoSyNodeAccessor;
import com.realrhymn.rhymnbook.dao.DynamoWordAccessor;
import com.realrhymn.rhymnbook.dao.WordRepository;
import com.realrhymn.rhymnbook.model.Song;
import com.realrhymn.rhymnbook.model.SyNode;
import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RealRhymnApplication.class)
public class MySqlTests {
	
	String word1 = "战神";
	String word2 = "犯人";		
	@Autowired
	DynamoSyNodeAccessor sdb;
	
	@Autowired
	DynamoWordAccessor wdb;
	
	@Autowired
	AmazonDynamoDB aDDB;
	
	@Autowired
	WordRepository wrdb;
	/*
	 * WordRepository tests
	 */
	@Test
	public void testSaveWordMySql() {
		wrdb.saveWord(word1,1,Util.wordToMp(word1));
		wrdb.saveWord(word1,-1,Util.wordToMp(word2));
	}
	
	@Test
	public void testDeleteWordMySQL() {
		wrdb.saveWord(word1,1,Util.wordToMp(word1));
		wrdb.deleteWord(word1,1);
	}
	
	@Test
	public void testDeleteWholeWord() {
		wrdb.saveWord(word1,1,Util.wordToMp(word1));
		wrdb.deleteWholeWord(word1);
	}
	
	@Test
	public void testGetWordByMp() {
		wrdb.saveWord(word1,1,Util.wordToMp(word1));
		wrdb.saveWord(word2,1,Util.wordToMp(word2));
		List<Word> l = wrdb.getWordByMp(Util.wordToMp(word1), 2,Util.frequencyThreshhold);
		String res = "";
		for(Word w :l) {
			res += l.toString() + " ";
		}
		assertTrue("Results: " + res, l.size()==2);
	}
	
	@Test
	public void testSaveWords() throws JsonParseException, JsonMappingException, IOException {
		File dir = new File("C:\\Users\\Yang\\Desktop\\scraper\\Python\\lyrics_test");
		File f = dir.listFiles()[0];
		
		ObjectMapper m = new ObjectMapper();

		Song s  = m.readValue(f, Song.class);
		
		ArrayList<Word> wordList = new ArrayList<Word>();
		
		int size = 0;
		for(String str : s.getPhrasePinyinDict()) {
			try {
				wordList.add(new Word(str));
				//size++;
			} catch (Exception e) {
				
			}
			
		}
		System.out.println(size);
		
		
		wrdb.saveWords(wordList);
	}
}
