package test;

import org.junit.Test;

import analyzer.*;
import dataStructures.Good;
import dataStructures.NodeWordOfGoods;

import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class GoodsClassificatorTest {
	static GoodsClassificator identificator;
	
	@BeforeClass
	public static void identify() {
		identificator = new GoodsClassificator();
	}
	
	@Test
	public void sumFeatures() {
		int iterations=10;
		float[] features={1f,1f,1f};
		NodeWordOfGoods[] keywords=new NodeWordOfGoods[iterations];
		for (int i = 0; i < keywords.length; i++) {
			keywords[i]=new NodeWordOfGoods("ID"+i, features);;
		}
		float[] sumFeatures = identificator.sumFeatures(keywords);
		for (int i = 0; i < sumFeatures.length; i++) {
			assertEquals(iterations,sumFeatures[i], 0.00000001);
		}
	}
	
	@Test
	public void sumArray(){
		float[] features={1f,1f,1f};
		float[] totalFeatures={1f,1f,1f};
		identificator.sumArray(totalFeatures, features, 1);
		for (int i = 0; i < totalFeatures.length; i++) {
			assertEquals(2.0f,totalFeatures[i], 0.00000001);
		}
	}
	
}
