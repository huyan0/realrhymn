package com.realrhymn.rhymnbook.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realrhymn.rhymnbook.application.RealRhymnApplication;
import com.realrhymn.rhymnbook.model.Song;
import com.realrhymn.rhymnbook.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RealRhymnApplication.class)
@AutoConfigureMockMvc
public class ControllerTest {
	@Autowired
	MockMvc mvc;
	
	File dir = new File("C:\\Users\\Yang\\Desktop\\scraper\\Python\\lyrics_test");
	
	AtomicInteger song = new AtomicInteger(0);
	
	ObjectMapper m = new ObjectMapper();
	
	class SaveSongTask extends RecursiveAction{
		
		File[] files;
		int start;
		int end;
		
		public SaveSongTask(File[] files, int start, int end) {
			 this.files = files;
			 this.start = start;
			 this.end = end;
		}
		
		@Override
		protected void compute() {
			if(this.start > this.end || this.start - this.end < parallelThreshold) {
				for(int i = start; i <= end; i++) {
					try {
						saveSong(files[i]);
					} catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				invokeAll(new SaveSongTask(this.files,start, start+(end-start) / 2),
						new SaveSongTask(this.files,start+(end-start) / 2 + 1, end));
			}
			
		}
		
	}
class SaveSongTaskV2 extends RecursiveAction{
		
		File[] files;
		int start;
		int end;
		
		public SaveSongTaskV2(File[] files, int start, int end) {
			 this.files = files;
			 this.start = start;
			 this.end = end;
		}
		
		@Override
		protected void compute() {
			if(this.start > this.end || this.start - this.end < parallelThreshold) {
				for(int i = start; i <= end; i++) {
					try {
						saveSongV2(files[i]);
					} catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				invokeAll(new SaveSongTaskV2(this.files,start, start+(end-start) / 2),
						new SaveSongTaskV2(this.files,start+(end-start) / 2 + 1, end));
			}
			
		}
		
	}

	static int parallelThreshold = 250;
	@Test
	public void testSaveSong() throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, Exception {
		song.set(0);
		saveSong(dir.listFiles()[0]);
	}
	
	@Test
	public void testSaveSongV2() throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, Exception {
		song.set(0);
		saveSongV2(dir.listFiles()[0]);
	}
	
	@Test
	public void testSaveSongLoad() throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, Exception {
		ObjectMapper m = new ObjectMapper();
		song.set(0);
		long t = System.nanoTime();
		for(File f : (dir).listFiles()) {
			this.saveSong(f);
		}
		long time = System.nanoTime() - t;
		System.out.println("Time used: " + (double) time / 1_000_000_000);
	}
	
	@Test
	public void testSaveSongLoadV2() throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, Exception {
		ObjectMapper m = new ObjectMapper();
		song.set(0);
		long t = System.nanoTime();
		for(File f : (dir).listFiles()) {
			this.saveSongV2(f);
		}
		long time = System.nanoTime() - t;
		System.out.println("Time used: " + (double) time / 1_000_000_000);
	}
	
	@Test
	public void testSaveSongParallel() {
		long t = System.nanoTime();
		song.set(0);
		ForkJoinPool fjp = new ForkJoinPool();
		fjp.invoke(new SaveSongTask(dir.listFiles(),0,dir.listFiles().length-1));
		long time = System.nanoTime() - t;
		System.out.println("Time used: " + (double) time / 1_000_000_000);
	}
	
	@Test
	public void testSaveSongParallelV2() {
		long t = System.nanoTime();
		song.set(0);
		ForkJoinPool fjp = new ForkJoinPool();
		fjp.invoke(new SaveSongTaskV2(dir.listFiles(),0,dir.listFiles().length-1));
		long time = System.nanoTime() - t;
		System.out.println("Time used: " + (double) time / 1_000_000_000);
	}
	
	@Test
	public void testGetRhymn() throws Exception {
		String s = "战神";
		ResultActions act = mvc.perform( MockMvcRequestBuilders
			      .get("/getrhymn?word=" + s)
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
		String[] res = m.readValue(act.andReturn().getResponse().getContentAsString(),String[].class);
		for(String w : res) {
			System.out.println(w);
		}
		System.out.println(res.length);
	}

	@Test
	public void testDeleteWholeWord() throws Exception {
		String s = "但门";
		mvc.perform( MockMvcRequestBuilders
			      .get("/deleteWhole?word=" + s)
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	/*
	 * Invoked Methods
	 */
	public void saveSong(File f) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, Exception {
		song.incrementAndGet();
		System.out.println("Saving " + song.get() + "th song");
		mvc.perform( MockMvcRequestBuilders
			      .post("/saveSong")
			      .content(m.writeValueAsBytes(m.readValue(f, Song.class)))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	public void saveSongV2(File f) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, Exception {
		song.incrementAndGet();
		System.out.println("Saving " + song.get() + "th song");
		mvc.perform( MockMvcRequestBuilders
			      .post("/saveSongv2")
			      .content(m.writeValueAsBytes(m.readValue(f, Song.class)))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
}
