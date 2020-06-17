package com.realrhymn.rhymnbook.dao;

import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.realrhymn.rhymnbook.model.SyNode;
import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.util.Util;

@Component
public class DynamoSyNodeAccessor {
		
	@Autowired
	DynamoDBMapper mapper;
	@Autowired
	AmazonDynamoDB aDDB;
	
	/*
	 * SyNode must have at least one word in its words field
	 * use addWordToSyNode() to create new nodes
	 */
	public SyNode addSyNode(SyNode s) {
		mapper.save(s);
		return s;
	}
	
	public SyNode getSyNode(String id, int nodeNumber) {
		return mapper.load(SyNode.class, id, nodeNumber);
	}
	
	public SyNode deleteSyNode(SyNode s) {
		mapper.delete(s);
		return s;
	}
	
	/*
	 * Add a list
	 */
	public SyNode addWordToSyNode(Word w) {
		SyNode s = new SyNode();
		s.setId(Util.wordToMp(w.getWord()));
		s.setNodeNumber(1);
		TreeSet<String> set = new TreeSet<String>();
		set.add(w.getWord());
		s.setChildren(set);
		mapper.save(s, new DynamoDBMapperConfig(SaveBehavior.APPEND_SET));
		return s;
	}
	/*
	 * If w does not exist in SyNode, nothing happens
	 * If w is the last word in SyNode, the word will be deleted but an error will be caught
	 * as well. Works for now, might have to come back later.
	 */
	public SyNode deleteWordFromSyNode(Word w) {
		  Table table = (new DynamoDB(aDDB)).getTable("SyNode");

        String mp = Util.wordToMp(w.getWord());
        int nodeNumber = 1;
        TreeSet<String> set = new TreeSet<String>();
		set.add(w.getWord());
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("mp", mp, "nodeNumber", nodeNumber)
        	//.withUpdateExpression(" info.rating = :r, info.plot=:p, info.actors=:a")
            .withUpdateExpression("DELETE words :p")
        	.withValueMap(new ValueMap().withStringSet(":p", set))
            .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
        	//node does not exist
        	// do nothing
            //System.err.println("Unable to update item: " + mp + " " + nodeNumber);
           // System.err.println(e.getMessage());
        }
        return null;
	}
	
	/*
	 * Overloaded methods
	 */
	public SyNode getSyNode(String id) {
		return this.getSyNode(id, 1);
	}
}
