package com.realrhymn.rhymnbook.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.util.Util;
@Component
public class DynamoWordAccessor {
	
	@Autowired
	DynamoDBMapper mapper;
	@Autowired
	AmazonDynamoDB aDDB;

	/**
	 * Create an item in the database. if the item exist, then its frequency is incremented
	 * by the frequency of w
	 * @param w
	 * @return
	 */
	public Word saveWord(Word w) {
		this.updateWordFrequency(w.getWord(), w.getFrequency(),true);
		return w;
	}
	
	/**
	 * Retrieve an object represents the same word as w, but perhaps with different frequency
	 * @param w
	 * @return
	 */
	public Word getWord(Word w) {
		return mapper.load(w);
	}
	
	/**
	 * Reduce the frequency of item with primary key word by freq; if no such item exists,
	 * nothing happens.
	 * @param word
	 * @param freq
	 */
	public Word deleteWord(Word w) {
		this.updateWordFrequency(w.getWord(), Math.negateExact(w.getFrequency()),false);
		return w;
	}
	/**
	 * Update the frequency of the item corresponding to w to w's frequency.
	 * If no corresponding item exists, then nothing happens  
	 * @param w
	 * @return
	 */
	public Word setWord(Word w) {
		//mapper.save(w, new DynamoDBMapperConfig(SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));
		 Table table = (new DynamoDB(aDDB)).getTable("Word");
		 String word = w.getWord();
		 int value = w.getFrequency();
	        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("word", word)
	        	//.withUpdateExpression(" info.rating = :r, info.plot=:p, info.actors=:a")
	            .withUpdateExpression("SET frequency = frequency + :p")
	        	.withValueMap(new ValueMap().withNumber(":p", value))
	            .withReturnValues(ReturnValue.UPDATED_NEW);

	        try {
	            System.out.println("Updating the item...");
	            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
	            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
	        }
	        catch (Exception e) {
		        return null;
	        }
		return w;
	}
	/**
	 * Change the frequency of item corresponding to word by increment. If saveNewItem is true
	 * and no item corresponds to word, a new item will be created; otherwise nothing happens.
	 * @param word
	 * @param increment
	 * @param saveNewItem
	 * @return
	 */
	public Word updateWordFrequency(String word, int increment, boolean saveNewItem) {
		 Table table = (new DynamoDB(aDDB)).getTable("Word");

	        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("word", word)
	        	//.withUpdateExpression(" info.rating = :r, info.plot=:p, info.actors=:a")
	            .withUpdateExpression("SET frequency = frequency + :p")
	        	.withValueMap(new ValueMap().withNumber(":p", increment))
	            .withReturnValues(ReturnValue.UPDATED_NEW);

	        try {
	            System.out.println("Updating the item...");
	            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
	            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
	        }
	        catch (Exception e) {
	        	if(saveNewItem) {
	        		mapper.save(new Word(word,increment));
	        	}
	        }
	        return null;
	}
	/**
	 * Retrieve up to “limit” items whose parentNode starts with pattern as Word 
	 * object, order not guaranteed.
	 * @param pattern
	 * @param limit
	 * @return
	 */
	public List<Word> getBeginWithMp(String pattern, int limit){
		HashMap<String,AttributeValue> expressionAttributeValues = new HashMap<String,AttributeValue>();
		expressionAttributeValues.put(":val", new AttributeValue().withS(pattern));
		expressionAttributeValues.put(":thrsh", new AttributeValue().withN(Util.frequencyThreshhold+""));
		
		DynamoDBScanExpression expr = new DynamoDBScanExpression()
				.withIndexName("parentNode-frequency-index")
				.withFilterExpression("begins_with(parentNode, :val) and frequency > :thrsh")
				.withExpressionAttributeValues(expressionAttributeValues)
				.withLimit(limit);
		return mapper.scan(Word.class, expr);
	}
	/**
	 * Retrieve up to “limit” items whose parentNode matches exactly with pattern as Word 
	 * object, in descending frequency order
	 * @param pattern
	 * @param limit
	 * @return
	 */
	public List<Word> getWordByMp(String pattern, int limit){
		HashMap<String,AttributeValue> expressionAttributeValues = new HashMap<String,AttributeValue>();
		expressionAttributeValues.put(":thrsh", new AttributeValue().withN(Util.frequencyThreshhold+""));
		expressionAttributeValues.put(":val", new AttributeValue().withS(pattern));
		DynamoDBQueryExpression<Word> expr = new DynamoDBQueryExpression<Word>()
				.withIndexName("parentNode-frequency-index")
				.withKeyConditionExpression("parentNode = :val and frequency > :thrsh")
				.withExpressionAttributeValues(expressionAttributeValues)
				.withScanIndexForward(false)
				.withLimit(limit);
		
		expr.setConsistentRead(false);
		return mapper.queryPage(Word.class, expr).getResults();
	}

	/**
	 * DO NOT USE.
	 * @return
	 */
	public List<Word> batchSaveWord(){
		return null;
	}
	
	/**
	 * DO NOT USE.
	 * @return
	 */
	public List<Word> batchUpdateWord() {
		return null;
	}
	/**
	 * delete item with primary key w from the table;
	 * if no such entry exists, nothing happens.
	 * @param w
	 */
	public Word deleteWholeWord(String s) {
		mapper.delete(new Word(s));
		return new Word(s);
	}
}
