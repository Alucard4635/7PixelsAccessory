package test;

import org.junit.Test;

import dataStructures.Good;

public class WordRankerTest {
	@Test
	private void productTest() {
		Good g=new Good("webster",
				"electronic arts the sims 3 movie stuff",
				"the sims movie stuff permettera ai vostri sims di utilizzare arredi  mobili"
				+ " e abbigliamento ispirati ai film che hanno reso grande la storia di hollywood."
				+ " il contenuto western e ispirato ai film   spaghetti western   degli anni  60."
				+ " set in stile saloon con",
				13.93f);
		//getKeywords "the sims", "the sims movie stuff", "sims"
		//Product, contain more accessories "arredi  mobili e abbigliamento ispirati ai film"
	}
}
