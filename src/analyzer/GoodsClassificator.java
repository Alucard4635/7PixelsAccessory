package analyzer;

import dataStructures.Good;
import dataStructures.NodeWord;
import dataStructures.TypeOfGoods;

/**
 * @author Andrea 
 * a supervised classifier that recognizes the best type of goods
 */
public class GoodsClassificator implements GoodTypeIdentificator {
	private WordRanker ranker;
	@Override
	public TypeOfGoods reconizeType(Good good) {
		// TODO Auto-generated method stub
		//
		return null;
	}
	
	public void learnType(Good[] goods, String wordDelimiter, float titleIM, float descriptionIM, float boundWeight ){
		for (int i = 0; i < goods.length; i++) {
			Good current = goods[i];
			NodeWord[] keywordsTitle = ranker.learn(current.getTitle(), wordDelimiter, titleIM, boundWeight);
			NodeWord[] keywordsDescription = ranker.learn(current.getDescription(), wordDelimiter,
					descriptionIM, boundWeight);

			TypeOfGoods currType=this.reconizeType(current);
			if (!currType.equals(current.getType())) {
				//TODO Discesa del gradiente
			}
		}
	}

}
