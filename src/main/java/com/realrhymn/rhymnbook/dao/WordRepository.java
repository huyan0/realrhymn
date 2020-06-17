package com.realrhymn.rhymnbook.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.realrhymn.rhymnbook.model.Word;

@Repository
@Transactional
public interface WordRepository extends JpaRepository<Word, String>, MySQLCustomRepository{
	
	/**
	 * create a entry with parameters. If word already exist, update its frequency in
	 * table by freq
	 * @param word
	 * @param freq
	 * @param mp
	 */
	@Modifying
	@Query(value="INSERT INTO Word (word, frequency, parent_node) VALUES(?1 ,?2, ?3) ON DUPLICATE KEY UPDATE  frequency = frequency + ?2",nativeQuery=true)
	public void saveWord(String word, int freq, String mp);
	
	/**
	 * reduce the frequency of entry with primary key word by freq; if no such entry exists,
	 * nothing happens.
	 * @param word
	 * @param freq
	 */
	@Modifying
	@Query(value="UPDATE Word SET frequency = frequency - ?2 WHERE word = ?1",nativeQuery=true)
	public void deleteWord(String word,int freq);
	
	/**
	 * delete entry with primary key w from the table;
	 * if no such entry exists, nothing happens.
	 * @param w
	 */
	@Modifying
	@Query(value="DELETE FROM Word WHERE word = ?1",nativeQuery=true)
	public void deleteWholeWord(String w);
	
	/**
	 * Retrieve up to “limit” entries whose parentNode matches exactly with pattern as Word object, in descending frequency order
	 * @param pattern
	 * @param limit
	 * @return
	 */
	@Query(value="SELECT * FROM Word WHERE parent_node = ?1 AND frequency > ?3 ORDER BY frequency DESC LIMIT ?2",nativeQuery=true)
	public List<Word> getWordByMp(String pattern, int limit, int freq);
	
	/**
	 * Retrieve up to “limit” items whose parentNode starts with pattern as Word 
	 * object, ordered by frequency.
	 * @param pattern
	 * @param limit
	 * @return
	 */
	@Query(value="SELECT * FROM Word WHERE parent_node LIKE ?1 AND frequency > ?3 ORDER BY frequency DESC LIMIT ?2",nativeQuery=true)
	public List<Word> getBeginWithMp(String pattern, int limit, int freq);

	
	/**
	 * Set the frequency of w to frequency. If w does not exist, nothing happens 
	 */
	@Modifying
	@Query(value="UPDATE Word SET frequency = ?2 WHERE word = ?1",nativeQuery=true)
	public void setFrequency(String s, int freq);

}
