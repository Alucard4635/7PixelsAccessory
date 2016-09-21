package dataStructures;

import socialNetwork.AbstractNode;
import socialNetwork.DirectionalLink;
import socialNetwork.HashNode;

public class NodeWord extends HashNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7405269095509204540L;
	private double occurence=1;
	private double totalBound=0;
	public double getOccurence() {
		return occurence;
	}
	
	public double increaseOccurence(float increase){
		return occurence+=increase;
	}
	
	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}
	
	//PS: you can modify array's content!
	public float[] getFeatures() {
		return features;
	}

	private float[] features;
	
	public NodeWord(String idendt,float[] features) {
		super(idendt);
		this.features = features;
	}
	
	public void increaseBound( AbstractNode friend,float deltaBound) {
		DirectionalLink LinkToOld = getDirectionalLinkTo(friend);
		if (LinkToOld!=null) {
			LinkToOld.setWeight(LinkToOld.getWeight()+deltaBound);
		}else {
			directionalLink(friend, deltaBound);
		}
		setTotalBound(getTotalBound() + deltaBound);
	}

	public double getTotalBound() {
		return totalBound;
	}

	private void setTotalBound(double totalBound) {
		this.totalBound = totalBound;
	}

	public double getBound(NodeWord old) {
		return getDirectionalLinkTo(old).getWeight();
	}

}
