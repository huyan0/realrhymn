package com.realrhymn.rhymnbook.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.realrhymn.rhymnbook.dao.DynamoWordAccessor;
import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.util.Util;

public class RealRhymnServiceImplDynamoDB implements RealRhymnService {
	@Autowired
	DynamoWordAccessor dynamoRepo;
	
	@Override
	public boolean saveWord(String s) {
		if(s.length() < 1 || !Util.checkIsHanzi(s)) {
			return false;
		}
		Word w = new Word(s);
		dynamoRepo.saveWord(w);
		return true;
	}

	@Override
	public String deleteWord(String s) {
		Word w = new Word(s);
		dynamoRepo.deleteWord(w);
		return s;
	}

	@Override
	public String changeWordFrequencyBy(String s, int incrementFrequency) {
		Word w = new Word(s,incrementFrequency);
		dynamoRepo.saveWord(w);
		return s;
	}

	@Override
	public String deleteWholeWord(String s) {
		dynamoRepo.deleteWholeWord(s);
		return s;
	}
	
	@Override
	public String setWordFrequency(String s, int frequency) {
		dynamoRepo.setWord(new Word(s,frequency));
		return null;
	}
	
	
	@Override
	public List<String> getRhymnWord(Word w, int limit) {
		HashSet<Word> res = new HashSet<Word>();		
		res.addAll(dynamoRepo.getBeginWithMp(w.getParentNode(), limit*4));
		List<Word> l = new LinkedList();
		l.addAll(res);
		Collections.sort(l);
		return Util.iterableToStringList(l.subList(0, limit));	
	}


	@Override
	public List<String> saveWords(Collection<Word> collection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> saveWordsV2(Collection<String> collection) {
		// TODO Auto-generated method stub
		return null;
	}	
}
