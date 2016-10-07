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
				"1 2 2 3 4 3 4 3 4 5 4 7",
				"5 5 5 6 6 6 6 5 6 6 7 7 7 7 77 7 7 7 7 7 7 7 77 7 7 7 7 7 7 7 7 7 7 7 77"
				,13.93f);
		
		float[] features={1f,10f};
		ranker.learn(g.getTitle()," ", 1,1, features);
		ranker.learn(g.getDescription()," ",1, 1, features);

		NodeWordOfGoods[] allKeywords = ranker.getAllKeywords(g.getTitle(), " ");
		for (int i = 0; i < allKeywords.length; i++) {
			System.out.println(allKeywords[i].getId()+" "+allKeywords[i].getOccurence() +" "+ranker.rankWord(allKeywords[i])); 
		}
		System.out.println(ranker.getWordCounter());
	}
}
