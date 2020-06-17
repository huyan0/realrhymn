package com.realrhymn.rhymnbook.test;

import static org.junit.Assert.assertTrue;

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
import com.realrhymn.rhymnbook.application.RealRhymnApplication;
import com.realrhymn.rhymnbook.dao.DynamoSyNodeAccessor;
import com.realrhymn.rhymnbook.dao.DynamoWordAccessor;
import com.realrhymn.rhymnbook.dao.WordRepository;
import com.realrhymn.rhymnbook.model.SyNode;
import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RealRhymnApplication.class)
public class DynamoDBTests {
	
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
	 * DynamoWordAccessor Test
	 */
	@Test
	public void testAddWord() {
		wdb.saveWord(new Word(word1));
		wdb.saveWord(new Word(word2));
	}
	@Test
	public void testDeleteWord() {
		wdb.saveWord(new Word(word1));
		wdb.saveWord(new Word(word2));
		wdb.deleteWord(new Word(word2));
		wdb.deleteWord(new Word(word1));
	}
	@Test
	public void testGetWord() {
		wdb.saveWord(new Word(word1));
		wdb.getWord(new Word(word1));
	}
	@Test
	public void testUpdateFrequency() {
		wdb.updateWordFrequency(word1, 2, true);
	}
	@Test
	public void testGetBeignWithMp() {
		wdb.saveWord(new Word(word1));
		wdb.saveWord(new Word(word2));
		List<Word> l = wdb.getBeginWithMp(new Word(word1).getParentNode(), 1);
		assertTrue("Retrieved incorrect number of item", l.size() == 1);
		assertTrue("Correct word not retrieved", l.contains(new Word(word1)) || l.contains(new Word(word2)));
	}
	@Test
	public void testGetWordByMp() {
		wdb.saveWord(new Word(word1));
		wdb.saveWord(new Word(word2));
		List<Word> l = wdb.getWordByMp(new Word(word1).getParentNode(), 1);
		assertTrue("Retrieved incorrect number of item: " +l.size() , l.size() == 1);
		assertTrue("Correct word not retrieved", l.contains(new Word(word1)) || l.contains(new Word(word2)));
	}
	/*
	 * DynamoSyNodeAccessor Test
	 */
	@Test
	public void testAddSyNode() {
		SyNode sy = new SyNode("_an_e");;
		sy.getChildren().add("测验");
		sdb.addSyNode(sy);
	}
	
    @Test
    public void testRetrieveSynode() {
    	sdb.addWordToSyNode(new Word(word1));
    	SyNode s = sdb.getSyNode("_en_an");
    	System.out.println(s.toString());
    }

    @Test
    public void testAddToSyNode() {
    	Word w1 = new Word(word1);
    	SyNode s = new SyNode(Util.wordToMp(w1.getWord()));
    	sdb.addWordToSyNode(w1);
    	sdb.addWordToSyNode(new Word(word2));
    }
    
    @Test
    public void testAddToUnknownSyNode() {
    	Word w1 = new Word("战鱼");
    	sdb.addWordToSyNode(w1);
    }
    
 
 
    @Test
    public void testDeleteWordFromSyNode() {
    	sdb.addWordToSyNode(new Word(word2));
    	sdb.addWordToSyNode(new Word(word1));
    	sdb.deleteWordFromSyNode(new Word(word2));
    	sdb.deleteWordFromSyNode(new Word(word1));
    }
    
   
    /*
     * References
     */
    public void testSaveSyNodeManual() {

        Table table = (new DynamoDB(aDDB)).getTable("SyNode");
        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("plot", "Nothing happens at all.");
        infoMap.put("rating", 0);

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                .putItem(new Item().withPrimaryKey("mp", "_a", "nodeNumber", 1).withMap("info", infoMap));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        }
        catch (Exception e) {
            System.err.println("Unable to add item: ");
            System.err.println(e.getMessage());
        }
    }
    
}