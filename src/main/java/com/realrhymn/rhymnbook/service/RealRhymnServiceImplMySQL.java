package com.realrhymn.rhymnbook.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realrhymn.rhymnbook.dao.WordRepository;
import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.util.Util;

@Service
public class RealRhymnServiceImplMySQL implements RealRhymnService {
	
	@Autowired
	WordRepository wordRepo;
	
	@Override
	public boolean saveWord(String s) {
		if(s.length() < 1 || !Util.checkIsHanzi(s)) {
			return false;
		}
		Word w = new Word(s);
		wordRepo.saveWord(w.getWord(),w.getFrequency(),w.getParentNode());
		return true;
	}

	@Override
	public String deleteWord(String s) {
		Word w = new Word(s);
		wordRepo.deleteWord(s, 1);
		return s;
	}

	@Override
	public String changeWordFrequencyBy(String s, int incrementFrequency) {
		Word w = new Word(s,incrementFrequency);
		wordRepo.saveWord(s, incrementFrequency, Util.wordToMp(s));
		return s;
	}

	@Override
	public String deleteWholeWord(String s) {
		wordRepo.deleteWholeWord(s);
		return s;
	}
	
	@Override
	public String setWordFrequency(String s, int frequency) {
		wordRepo.setFrequency(s, frequency);
		return null;
	}
	
	
	@Override
	public List<String> getRhymnWord(Word w, int limit) {
		HashSet<Word> res = new HashSet<Word>();
		res.addAll(wordRepo.getWordByMp(w.getParentNode(), (int)(limit*Util.exactMatchNumber), Util.frequencyThreshhold));
		res.addAll(wordRepo.getBeginWithMp(w.getParentNode()+"%", (int)(limit*Util.exactMatchNumber), Util.frequencyThreshhold));
		res.addAll(wordRepo.getWordByMp(Util.getMpParent(w.getParentNode()), (int)(limit*Util.parentMatchNumber), Util.frequencyThreshhold));		
		List<Word> l = new LinkedList();
		l.addAll(res);
		Collections.sort(l);
		return Util.iterableToStringList(l);	
	}

	@Override
	public List<String> saveWords(Collection<Word> l) {
		List<Word> list = new ArrayList<Word>();
		list.addAll(l);
		wordRepo.saveWords(list);
		return null;
	}		
	@Override
	public List<String> saveWordsV2(Collection<String> li) {
		List<String> l = new ArrayList<String>();
		l.addAll(li);
		List<String> list;
		int queryLength = l.size();
		for(int i = 0; i < l.size(); i+=queryLength) {
			list = new ArrayList<String>();
			for(int j = 0; j < queryLength && i+j < l.size(); j++) {
				list.add(l.get(j+i));
			}
			wordRepo.saveWordsV2(list);
		}
		return null;
	}	

}
