package com.realrhymn.rhymnbook.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.realrhymn.rhymnbook.model.Word;
import com.realrhymn.rhymnbook.util.Util;

@Transactional
public class MySQLCustomRepositoryImpl implements MySQLCustomRepository{
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Override
	public List<Word> saveWords(List<Word> wordList) {
		
		StringBuilder str = new StringBuilder();
		str.append("INSERT INTO Word (word, frequency, parent_node) VALUES (?,?,?) ON DUPLICATE KEY UPDATE  frequency = frequency + ?");
		this.jdbcTemplate.batchUpdate(str.toString(),
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setString(1, wordList.get(i).getWord());
						ps.setInt(2, wordList.get(i).getFrequency());
						ps.setString(3, wordList.get(i).getParentNode());
						ps.setInt(4, wordList.get(i).getFrequency());
					}

					@Override
					public int getBatchSize() {
						return wordList.size();
					}
			
		});
		return wordList;
		
	}
	@Override
	public List<Word> saveWordsV2(List<String> wordList) {
		StringBuilder str = new StringBuilder();
		str.append("INSERT INTO Word VALUES");
		for(String s : wordList) {
			if(Util.isValidWord(s)) {
				str.append("(");
				str.append("\""+ s + "\"");
				str.append(",");
				str.append("1");
				str.append(",");
				str.append("\""+ Util.wordToMp(s) + "\"");
				str.append(")");
				str.append(",");
			}
		}
		str.deleteCharAt(str.length()-1);
		str.append(" ON DUPLICATE KEY UPDATE frequency = frequency + 1");
		this.jdbcTemplate.update(str.toString());
		return null;
	}

}
