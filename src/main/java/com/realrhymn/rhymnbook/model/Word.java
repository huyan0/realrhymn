package com.realrhymn.rhymnbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.realrhymn.rhymnbook.util.Util;

@DynamoDBTable(tableName = "Word")
@Entity
@Table(name="Word")
public class Word implements Comparable<Word> {
	
	@Id
	//@Column annotation is not working because 
    @Column(name="word")
	@DynamoDBHashKey
	private String word;
	
	@Column(name="frequency")
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "parentNode-frequency-index")
	private int frequency;
	
	//The SyNode that currently contains the word in SyTable
	//By default is ""
	@Column(name="parentNode")
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "parentNode-frequency-index")
	private String parentNode;

	@JsonCreator
	public Word(@JsonProperty("word")String word, @JsonProperty("frequency")int fre,@JsonProperty("parentNode")String parentNode) {
		if(!Util.checkIsHanzi(word)) {
			throw new IllegalArgumentException();
		}
		this.word = word;
		this.frequency = fre;
		this.parentNode = parentNode;
	}
	
	public Word(String word) {
		this(word, 1, Util.wordToMp(word));
	}
	
	public Word(String word, int fre){
		this(word,fre,Util.wordToMp(word));
	}
	public Word() {}
	
	@Override
	public String toString() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	@DynamoDBAttribute
	public String getParentNode() {
		return this.parentNode;
	}

	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}

	
	public String getWord() {
		return word;
	}

 
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass().getName().equals(this.getClass().getName())) {
			return this.getWord().equals(((Word) obj).getWord());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (this.word).hashCode();
	}

	@DynamoDBAttribute
	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public int compareTo(Word o) {
		// TODO Auto-generated method stub
		return this.frequency - o.frequency;
	}		

}
