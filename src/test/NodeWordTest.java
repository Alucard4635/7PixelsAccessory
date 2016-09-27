package test;

import org.junit.Test;
import org.junit.*;

import dataStructures.NodeWordOfGoods;

public class NodeWordTest {

	@Test
	public void featuresUpdates(){
		
		float[] features= {0f,0f};
		float[] deltaFeatures={1f,0f};
		NodeWordOfGoods testNode = new NodeWordOfGoods("test", features);
		testNode.addFeatures(deltaFeatures);
		deltaFeatures[0]=0f;
		deltaFeatures[1]=1f;
		testNode.addFeatures(deltaFeatures);

		float[] result = testNode.getFeatures();
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
	}
}
