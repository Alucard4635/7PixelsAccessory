package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import dataStructures.NodeWordOfGoods;
import dataStructures.TypeOfGoods;
import dataStructures.WordOfGoodsGraph;
import socialNetwork.AbstractNode;
import socialNetwork.DirectionalLink;
import socialNetwork.Graph;

/**
 * @author Andrea
 *Crea un grafo dove misura l'importanza delle parole, e della loro concatenazione
 */
public class WordRanker{
	private int wordCounter=0;//usare la parola con massima occorenza!
	private WordOfGoodsGraph graph=new WordOfGoodsGraph();
	private double maxCount=1;
	
	public void learn(String text,String delimiter,float importanceMultiplier, float boundWeight, float[] deltaFeatures){
		// prendo le parole e ne assimila la possibile tokenizzazione
		StringTokenizer tok= new StringTokenizer(text,delimiter);
		AbstractNode oldNode = null;
		double occurence;
		while (tok.hasMoreTokens()) {
			boolean isNew=false;
			String nextToken = tok.nextToken();
			if (!nextToken.equals("")) {
				if (!graph.containsNode(nextToken)) {
					isNew = true;
					wordCounter++;
				}
				AbstractNode createdNode = graph.getNode(nextToken);
				
				if (createdNode instanceof NodeWordOfGoods) {
					NodeWordOfGoods createdNodeWord=(NodeWordOfGoods) createdNode;
					if (!isNew) {
						createdNodeWord.increaseOccurence(1);
						wordCounter+=(float) 1;
						occurence = createdNodeWord.getOccurence();
						if (occurence>maxCount) {
							maxCount=occurence;
						}
					}

					if (oldNode!=null) {
						createdNodeWord.increaseBound(oldNode, boundWeight);
					}
					createdNodeWord.addFeatures(deltaFeatures);
					
					oldNode=createdNode;
				}
			}
		}
	}

	//returns an array of words in order of increasing importance
	public NodeWordOfGoods[] getKeywords(String text,String deimiter, int first){
		StringTokenizer tok= new StringTokenizer(text, deimiter);
		ArrayList<NodeWordOfGoods> list = getAllKeywords(tok);
		return list.toArray(new NodeWordOfGoods[first]);
	}
	
	public NodeWordOfGoods[] getAllKeywords(String text,String deimiter){
		StringTokenizer tok= new StringTokenizer(text, deimiter);
		ArrayList<NodeWordOfGoods> list = getAllKeywords(tok);
		return list.toArray(new NodeWordOfGoods[list.size()]);
	}
	
	public ArrayList<NodeWordOfGoods> getAllKeywords(StringTokenizer tok) {
		// per la valutazione delle parole di locazioni si verificherà a monte
		ArrayList<NodeWordOfGoods> list=new ArrayList<NodeWordOfGoods>();
		NodeWordOfGoods old = null;
		int index = 0;
		boolean addedWord=false;
		NodeWordOfGoods multiWord=null;
		while (tok.hasMoreTokens()) {
			String nextToken = tok.nextToken();
			if (!nextToken.equals("")) {
				AbstractNode node = graph.getNode(nextToken);
				
				if (node instanceof NodeWordOfGoods) {
					NodeWordOfGoods word = (NodeWordOfGoods) node;
					double rank = rankWord(word);
					/*if (old!=null) {//FIXME!!!!
						double bound = word.getBound(old);
						addedWord = bound/word.getTotalBound()>0.90;
						if (addedWord) {
							if (multiWord==null) {
								multiWord=old;
							}
							if (list.remove(multiWord)) {
								System.out.println("removed "+multiWord.getId() );
							}
							multiWord = new NodeWordOfGoods(multiWord.getId()+" "+word.getId(), word.getFeatures().clone());
							System.out.println("add "+multiWord.getId() );

							multiWord.setOccurence(word.getOccurence());
						}
					}*/
					if (addedWord&&multiWord!=null) {
						index = insertWordDecreasingOrder(list, multiWord, rank);
						addedWord=false;
					}else {
						index = insertWordDecreasingOrder(list, word, rank);
					}
					old=word;
				}
			}
		}
		Collections.reverse(list);
		return list;
	}

	public int insertWordDecreasingOrder(ArrayList<NodeWordOfGoods> list, NodeWordOfGoods word, double rank) {
		int index = 0;
		if (!list.contains(word)) {
			for (NodeWordOfGoods nodeWord : list) {//order by decreasing order
				if (rank<=rankWord(nodeWord)) {
					break;
				}
				index++;
			}
			list.add(index,word);
		}
		return index;
	}
	

	public double rankWord(NodeWordOfGoods word) {
		if (wordCounter==0) {
			return 1;
		}
		double probability = (double)word.getOccurence()/maxCount;
		double other=1-probability;
		return probability*(-Math.log10(probability));//+other*(-Math.log10(other));
	}

	public WordOfGoodsGraph getGraph() {
		return graph;
	}

	public int getWordCounter() {
		return wordCounter;
	}


	
}
