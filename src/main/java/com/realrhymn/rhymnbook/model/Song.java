package com.realrhymn.rhymnbook.model;

//C:\Users\Yang\Desktop\scraper\Python\lyrics_v1
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Song {
	@JsonProperty("songName")
	private String songName;
	@JsonProperty("artistName")
	private String artistName;
	@JsonProperty("lyric")
	private String lyric;
	@JsonProperty("phrasePinyinDict")
	private ArrayList<String> phrasePinyinDict;
	
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public String getLyric() {
		return lyric;
	}
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}
	public ArrayList<String> getPhrasePinyinDict() {
		return phrasePinyinDict;
	}
	public void setPhrasePinyinDict(ArrayList<String> phrasePinyinDict) {
		this.phrasePinyinDict = phrasePinyinDict;
	}
	@Override
	public String toString() {
		return "Song [songName=" + songName + ", artistName=" + artistName + ", lyric=" + lyric + ", phrasePinyinDict=" + phrasePinyinDict
				+ "]";
	}
}
