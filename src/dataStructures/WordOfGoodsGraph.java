package dataStructures;

import socialNetwork.AbstractNode;
import socialNetwork.Graph;
import socialNetwork.HashFocus;

public class WordOfGoodsGraph extends Graph {

	/**
	 * 
	 */
	private static final long serialVersionUID = -954313839125173227L;

	@Override
	public AbstractNode createANode(String id) {
		return new NodeWordOfGoods(id, new float[TypeOfGoods.values().length]);
	}

	@Override
	public HashFocus createAFocus(String id) {
		return new HashFocus(id);
	}
	
}
