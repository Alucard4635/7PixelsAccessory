package analyzer;

import dataStructures.Good;
import dataStructures.NodeWord;
import dataStructures.TypeOfGoods;

/**
 * @author Andrea 
 * a supervised classifier that recognizes the best type of goods
 */
public class GoodsClassificator implements GoodTypeIdentificator {
	WordRanker ranker;
	@Override
	public TypeOfGoods reconizeType(Good good) {
		// TODO Auto-generated method stub
		//
		return null;
	}
	
	public void learnType(Good[] goods){
		for (int i = 0; i < goods.length; i++) {
			Good current = goods[i];
			NodeWord[] keywords = ranker.getKeywords(current.getTitle(), null, i);//TODO
			TypeOfGoods currType=this.reconizeType(current);
			if (!currType.equals(current.getType())) {
				//TODO Discesa del gradiente
			}
		}
	}

}
