package com.realrhymn.rhymnbook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.realrhymn.rhymnbook.model.Song;
import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.service.RealRhymnService;
import com.realrhymn.rhymnbook.util.Util;

@Controller
public class WordController {
	
	@Autowired
	RealRhymnService svc;
	
	@RequestMapping("/index")
	public String showIndex() {
		return "index";
	}
	
	@RequestMapping(value="/saveWord", method=RequestMethod.GET)
	public @ResponseBody String saveWord(@RequestParam("word") String word, HttpServletResponse response){
			if(svc.saveWord(word)) {
				response.setStatus(200);
				return "Word " + word + "added";	
			} else {
				response.setStatus(400);
				return "Invalid input: " + word;
			}
			
		
	}
	
	@RequestMapping(value="/rhymn",method=RequestMethod.GET)
	public @ResponseBody List<String> getRhymnWords(@RequestParam("word")String word, @RequestParam(value="limit",required=false,defaultValue="100") int limit, HttpServletResponse response){
		if(Util.checkIsHanzi(word)) {
			response.setStatus(200);
			return svc.getRhymnWord(new Word(word), limit);
		} else {
			response.setStatus(400);
			return null;
		}
	}
	
	@RequestMapping(value="/saveSong", method=RequestMethod.POST)
	public @ResponseBody List<String> saveSong(@RequestBody Song s){
		Map<String,Word> res = new HashMap<String,Word>();
		for(String word : s.getPhrasePinyinDict()) {
			if(Util.isValidWord(word)) {
				if(!res.containsKey(word)) {
					res.put(word, new Word(word));
				} else {
					res.get(word).setFrequency(res.get(word).getFrequency()+1);
				}
			}
		}
		svc.saveWords(res.values());
		return Util.iterableToStringList(res.keySet());
	}
	
	@RequestMapping(value="/saveSongv2", method=RequestMethod.POST)
	public @ResponseBody List<String> saveSongV2(@RequestBody Song s){
		List<String> res = new ArrayList<String>();
		for(String word : s.getPhrasePinyinDict()) {
			if(Util.isValidWord(word)) {
				res.add(word);
			}
		}
		svc.saveWordsV2(res);
		return res;
	}
	
	@RequestMapping(value="/getrhymn",method=RequestMethod.GET)
	public @ResponseBody List<String> getRhymn(@RequestParam("word") String word) {
		return svc.getRhymnWord(new Word(word), 100);
	}
	
	@RequestMapping(value="deleteWhole", method=RequestMethod.GET)
	public @ResponseBody String deleteWholeWord(@RequestParam("word") String word, HttpServletResponse res) {
		svc.deleteWholeWord(word);
		return "Done";
	}
 }
