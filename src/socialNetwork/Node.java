package socialNetwork;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class Node extends AbstractNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 874058324452008749L;
	private Collection<DirectionalLink> adiacencyList;
	
	
	public Node(String idendt) {
		super(idendt);
		adiacencyList=new LinkedList<DirectionalLink>();
	}
	
	public Node() {
	}
	

	public AbstractNode findAdiacentNode(String targetID) {
		for (DirectionalLink directionalLink : adiacencyList) {
			AbstractNode target = directionalLink.getTarget();
			if (target.getId().equals(targetID)) {
				return target;
			}
		}
		return null;
	}

	public Collection<DirectionalLink> getAdiacencyList() {
		return adiacencyList;
	}

	@Override
	protected void addDirectionalLink(DirectionalLink link) {
		adiacencyList.add(link);
		
	}

	@Override
	public DirectionalLink getDirectionalLinkTo(String id) {
		for (DirectionalLink directionalLink : adiacencyList) {
			if (directionalLink.getTarget().getId().equals(id)) {
				return directionalLink;
			}
		}
		return null;
	}

}
