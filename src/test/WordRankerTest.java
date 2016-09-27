package test;

import org.junit.BeforeClass;
import org.junit.Test;

import analyzer.WordRanker;
import dataStructures.Good;
import dataStructures.NodeWordOfGoods;

public class WordRankerTest {
	private static WordRanker ranker;
	@BeforeClass
	public static void init() {
		ranker = new WordRanker();
	}
	@Test
	public void productTest() {
		Good g=new Good("webster",
				"electronic arts arts the sims 3 movie stuff",
				"the sims movie stuff permettera ai vostri sims di utilizzare arredi  mobili"
				+ " e abbigliamento ispirati ai film che hanno reso grande la storia di hollywood."
				+ " il contenuto western e ispirato ai film   spaghetti western   degli anni  60."
				+ " set in stile saloon con",
				13.93f);
		
		float[] features={1f,10f};
		ranker.learn(g.getTitle()," ", 1,1, features);
		ranker.learn(g.getDescription()," ",1, 1, features);

		NodeWordOfGoods[] allKeywords = ranker.getAllKeywords(g.getDescription(), " ");
		for (int i = 0; i < allKeywords.length; i++) {
			System.out.println(allKeywords[i]+" "+allKeywords[i].getOccurence() +" "+ranker.rankWord(allKeywords[i])); 
		}
	}
}
