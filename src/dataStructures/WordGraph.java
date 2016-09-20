package dataStructures;

import socialNetwork.AbstractNode;
import socialNetwork.Graph;
import socialNetwork.HashFocus;

public class WordGraph extends Graph {

	@Override
	public AbstractNode createANode(String id) {
		return new NodeWord(id);
	}

	@Override
	public HashFocus createAFocus(String id) {
		return new HashFocus(id);
	}
	
}
