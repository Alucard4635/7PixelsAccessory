package dataStructures;

import socialNetwork.HashNode;

public class NodeWord extends HashNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7405269095509204540L;
	int occurence=0;
	
	public NodeWord(String idendt) {
		super(idendt);
		occurence++;
	}
}
