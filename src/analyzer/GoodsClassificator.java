package analyzer;

import dataStructures.Good;
import dataStructures.NodeWordOfGoods;
import dataStructures.TypeOfGoods;

/**
 * @author Andrea 
 * a supervised classifier that recognizes the best type of goods
 */
public class GoodsClassificator implements GoodTypeIdentificator {
	private WordRanker ranker=new WordRanker();
	private float titleFeatureWeight=1f;

	@Override
	public TypeOfGoods reconizeType(Good good, String deimiter) {
		
		NodeWordOfGoods[] titleKeywords = ranker.getAllKeywords(good.getTitle(), deimiter);
		NodeWordOfGoods[] descriptionKeywords = ranker.getAllKeywords(good.getDescription(), deimiter);
		float[] sumTitleFeatures = sumFeatures(titleKeywords);
		float[] sumDescriptionFeatures = sumFeatures(descriptionKeywords);
		float[] result = sumTitleFeatures;
		if (sumDescriptionFeatures!=null) {
			result=sumArray(sumDescriptionFeatures, sumTitleFeatures, titleFeatureWeight);
		}
		
		
		
		int reconizedClass = evaluateFeatures(result);
		return TypeOfGoods.values()[reconizedClass];
	}

	private int evaluateFeatures(float[] features) {
		int indexOfMax = maxValue(features);

		return indexOfMax;
	}

	public int maxValue(float[] features) {
		int indexOfMax = 0;
		float current;
		float maxValue = 0;
		for (int i = 0; i < features.length; i++) {
			current = features[i];
			if (current>=maxValue) {
				indexOfMax=i;
				maxValue = features[indexOfMax];
			}
		}
		return indexOfMax;
	}

	public float[] sumFeatures(NodeWordOfGoods[] keywords) {
		NodeWordOfGoods current;
		float[] features;
		float[] totalFeatures=null;
		if (keywords.length<1) {
			return null;
		}
		for (int i = 0; i < keywords.length; i++) {
			current = keywords[i];
			if (current==null) {
				break;
			}
			features = current.getFeatures();
			if (totalFeatures==null) {
				totalFeatures=new float[features.length];
			}
			totalFeatures = sumArray( totalFeatures, features, ranker.rankWord(current));
		}
		return totalFeatures;
	}

	public float[] sumArray(float[] totalFeatures,  float[] features,double feratureWeight) {
		float[] result=totalFeatures.clone();
		if (totalFeatures.length!=features.length) {
			throw new IllegalArgumentException("Different arrays size");
		}
		for (int j = 0; j < result.length; j++) {
			result[j]+=features[j]*feratureWeight;
		}
		return result;
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

	public WordRanker getRanker() {
		return ranker;
	}


}
