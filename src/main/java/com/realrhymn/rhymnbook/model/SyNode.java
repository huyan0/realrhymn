package com.realrhymn.rhymnbook.model;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * A SyNode represents a collection of children whose syllables are the same.
 * the id of a SyNode is a series of syllables of any children with in it in reverse order.
 */
@DynamoDBTable(tableName="SyNode")
public class SyNode {
	
	/*
	 * Fields
	 */	
   //DynamoDB mapper does not support TreeSet
	private Set<String> children;

	private String id;
	
	private int nodeNumber;
	
	/*
	 * Constructors
	 */
	@JsonCreator
	public SyNode(@JsonProperty("children")Set<String> children, @JsonProperty("id") String id,@JsonProperty("nodeNumebr") int nodeNumber) {
		this.children = children;
		this.id = id;
		this.nodeNumber = nodeNumber;
	}
	
	public SyNode(String id) {
		this(new HashSet<String>(), id, 1);
	}
	
	public SyNode() {}
	
	@DynamoDBAttribute
	public Set<String> getChildren() {
		return children;
	}

	public void setChildren(Set<String> children) {
		this.children = children;
	}
	
	@DynamoDBHashKey(attributeName="mp")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@DynamoDBRangeKey
	public int getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}
	
	
}
