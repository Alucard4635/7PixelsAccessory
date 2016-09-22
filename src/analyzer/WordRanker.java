package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import dataStructures.NodeWord;
import dataStructures.TypeOfGoods;
import dataStructures.WordGraph;
import socialNetwork.AbstractNode;
import socialNetwork.DirectionalLink;
import socialNetwork.Graph;

/**
 * @author Andrea
 *Crea un grafo dove misura l'importanza delle parole, e della loro concatenazione
 */
public class WordRanker{
	private int wordCounter=0;
	private WordGraph graph=new WordGraph();
	
	public void learn(String text,String delimiter,float importanceMultiplier, float deltaWeight){
		// prendo le parole e ne assimila la possibile tokenizzazione
		StringTokenizer tok= new StringTokenizer(text,delimiter);
		AbstractNode oldNode = null;
		while (tok.hasMoreTokens()) {
			String nextToken = tok.nextToken();
			if (!nextToken.equals("")) {
				if (!graph.containsNode(nextToken)) {
					wordCounter++;
				}
				AbstractNode createdNode = graph.getNode(nextToken);
				
				if (createdNode instanceof NodeWord) {
					NodeWord createdNodeWord=(NodeWord) createdNode;
					createdNodeWord.increaseOccurence(1/importanceMultiplier);

					if (oldNode!=null) {
						createdNodeWord.increaseBound(oldNode, deltaWeight);
					}
					
					oldNode=createdNode;
				}
			}
		}
	}

	//returns an array of words in order of increasing importance
	public NodeWord[] getKeywords(String text,String deimiter, int first){
		StringTokenizer tok= new StringTokenizer(text, deimiter);
		ArrayList<NodeWord> list = getAllWordsByRank(tok);
		return list.toArray(new NodeWord[first]);
	}
	
	public NodeWord[] getAllKeywords(String text,String deimiter){
		StringTokenizer tok= new StringTokenizer(text, deimiter);
		ArrayList<NodeWord> list = getAllWordsByRank(tok);
		return list.toArray(new NodeWord[list.size()]);
	}
	
	public ArrayList<NodeWord> getAllWordsByRank(StringTokenizer tok) {// aggiungere un enumerativo (o simile) per collegare i tipi e le loro relazioni
		ArrayList<NodeWord> list=new ArrayList<NodeWord>();
		NodeWord old = null;
		while (tok.hasMoreTokens()) {
			String nextToken = tok.nextToken();
			if (!nextToken.equals("")) {
				AbstractNode node = graph.getNode(nextToken);
				
				if (node instanceof NodeWord) {
					NodeWord word = (NodeWord) node;
					double rank = rankWord(word);
					if (old!=null) {
						double bound = word.getBound(old);
						//se è alto si costuisce una nuova parola
					}
					int index = 0;
					
					boolean eq=false;
					for (NodeWord nodeWord : list) {//order by decreasing order
						if (rank<=rankWord(nodeWord)) {
							if (nodeWord.equals(word)) {
								eq=true;
								break;
							}
							break;
						}
						index++;
					}
					if (!eq) {
						list.add(index,word);
						eq=false;
					}
					old=word;
				}
			}
		}
		Collections.reverse(list);
		return list;
	}
	

	public double rankWord(NodeWord word) {
		try {
			double probability = (double)word.getOccurence()/wordCounter;
			return -Math.log10(probability)/Math.log10(2);
		} catch (ArithmeticException e) {
			return 1;
		}
	}

	public WordGraph getGraph() {
		return graph;
	}

	
}
