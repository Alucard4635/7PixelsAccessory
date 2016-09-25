package analyzer;

import dataStructures.Good;
import dataStructures.NodeWordOfGoods;
import dataStructures.TypeOfGoods;

/**
 * @author Andrea 
 * a supervised classifier that recognizes the best type of goods
 */
public class GoodsClassificator implements GoodTypeIdentificator {
	private WordRanker ranker;
	private float titleFeatureWeight;

	@Override
	public TypeOfGoods reconizeType(Good good, String deimiter) {
		
		NodeWordOfGoods[] titleKeywords = ranker.getAllKeywords(good.getTitle(), deimiter);
		NodeWordOfGoods[] descriptionKeywords = ranker.getAllKeywords(good.getDescription(), deimiter);
		
		float[] sumTitleFeatures = sumFeatures(titleKeywords);
		float[] sumDescriptionFeatures = sumFeatures(descriptionKeywords);
		
		sumArray(sumDescriptionFeatures, sumTitleFeatures, titleFeatureWeight);
		
		int indexOfMax = 0;
		for (int i = 0; i < sumDescriptionFeatures.length; i++) {
			float current = sumDescriptionFeatures[i];
			float maxValue = sumDescriptionFeatures[indexOfMax];
			if (current>maxValue) {
				indexOfMax=i;
			}
		}
		return TypeOfGoods.values()[indexOfMax];
	}

	public float[] sumFeatures(NodeWordOfGoods[] titleKeywords) {
		NodeWordOfGoods current;
		float[] features;
		float[] totalFeatures=null;

		for (int i = 0; i < titleKeywords.length; i++) {
			current = titleKeywords[i];
			features = current.getFeatures();
			if (totalFeatures==null) {
				totalFeatures=features.clone();
			}else {
				sumArray( totalFeatures, features, ranker.rankWord(current));
			}
		}
		return totalFeatures;
	}

	public void sumArray(float[] totalFeatures,  float[] features,double feratureWeight) {
		if (totalFeatures.length!=features.length) {
			throw new IllegalArgumentException("Different arrays size");
		}
		for (int j = 0; j < totalFeatures.length; j++) {
			totalFeatures[j]+=features[j]*feratureWeight;
		}
	}
	
	public void learnType(Good[] goods, String wordDelimiter, float titleIM, float descriptionIM, float boundWeight ){
		for (int i = 0; i < goods.length; i++) {
			Good current = goods[i];
			
			float[] deltaFeatures=new float[TypeOfGoods.values().length];
			TypeOfGoods type = current.getType(wordDelimiter);
			deltaFeatures[type.ordinal()]+=1;
			
			ranker.learn(current.getTitle(), wordDelimiter, titleIM, boundWeight, deltaFeatures);
			ranker.learn(current.getDescription(), wordDelimiter,
					descriptionIM, boundWeight, deltaFeatures);

		}
	}

}
