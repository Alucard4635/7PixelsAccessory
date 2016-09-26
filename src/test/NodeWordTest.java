package test;

import org.junit.Test;
import org.junit.*;

import dataStructures.NodeWordOfGoods;

public class NodeWordTest {

	@Test
	public void featuresUpdates(){
		
		float[] features= {0f,0f};
		float[] deltaFeatures={1f,1f};
		NodeWordOfGoods testNode = new NodeWordOfGoods("test", features);
		testNode.addFeatures(deltaFeatures);
		float[] result = testNode.getFeatures();
		for (int i = 0; i < result.length; i++) {
			if (result[i]!=deltaFeatures[i]) {
				System.err.println(result[i]);
				}
		}
	}
}
