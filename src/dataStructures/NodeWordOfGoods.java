package dataStructures;

import socialNetwork.AbstractNode;
import socialNetwork.DirectionalLink;
import socialNetwork.HashNode;

public class NodeWordOfGoods extends HashNode {
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
	
	public void setOccurence(double occurence) {
		this.occurence = occurence;
	}
	
	//PS: you can modify array's content!
	public float[] getFeatures() {
		double squareSum = 0;
		float[] normalizatedFeatures = features.clone();
		for (int i = 0; i < normalizatedFeatures.length; i++) {
			squareSum+=normalizatedFeatures[i]*normalizatedFeatures[i];
		}
		squareSum=Math.sqrt(squareSum);
		if (squareSum<=0) {
			return normalizatedFeatures;
		}
		for (int i = 0; i < normalizatedFeatures.length; i++) {
			normalizatedFeatures[i]/=squareSum;
		}
		return normalizatedFeatures;
	}

	private float[] features;
	
	public NodeWordOfGoods(String idendt,float[] features) {
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
		totalBound+=deltaBound;
	}

	public double getTotalBound() {
		return totalBound;
	}

	private void setTotalBound(double totalBound) {
		this.totalBound = totalBound;
	}

	public double getBound(NodeWordOfGoods old) {
		DirectionalLink directionalLinkTo = getDirectionalLinkTo(old);
		if (directionalLinkTo==null) {
			return 0;
		}
		return directionalLinkTo.getWeight();
	}

	public void addFeatures(float[] deltaFeatures) {
		//double squareSum = 0;
		for (int i = 0; i < deltaFeatures.length; i++) {
			features[i]+=deltaFeatures[i];
			//squareSum+=features[i]*features[i];
		}
		/*squareSum=Math.sqrt(squareSum);
		for (int i = 0; i < deltaFeatures.length; i++) {
			features[i]/=squareSum;
		}*/
		
		
	}
	
	@Override
	public String toString() {
		String stringSuper = super.toString()+" "+occurence+"[";
		for (int i = 0; i < features.length; i++) {
			stringSuper+=features[i]+",";
		}
		return stringSuper.substring(0, stringSuper.length()-1)+"]";
	}

}
