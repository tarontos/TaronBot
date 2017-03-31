package com.company.TaronBot.NEAT.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * A node is the basic element of neural network
 */
public class Node {
	static long uniqueUniversalNodeIdentificationCount = 0; // for keeping track of the current next UUNIN to createa node with
	final long uniqueUniversalNodeIdentificationNumber; // for node comparison in mating. Globally unique
	int nodeOrderNumber; // for use in calculation networks. Prevent backtracking
	ArrayList<Node> inputNodes; // to get previous node values
	ArrayList<Node> outputNodes; // for tracking purposes only
	ArrayList<Double> inputNodeWeights;
	double value; // updated by caluclate
	int operationID;
	int NodeHeight;


	public long getUniqueUniversalNodeIdentificationNumber() {
		return uniqueUniversalNodeIdentificationNumber;
	}


	/**
	 *
	 * sets the node order number. To be used if necessary in network restructuring
	 *
	 */
	public void setNodeOrderNumber(int nodeOrderNumber) {
		this.nodeOrderNumber = nodeOrderNumber;
	}


	/**
	 * returns the order number of the node.
	 *
	 * @return
	 */
	public int getNodeOrderNumber() {
		return nodeOrderNumber;
	}


	/**
	 * creates a node with an id number and a UUNIN
	 *
	 * @param nodeUUNIN
	 * @param nodeOrderNumber
	 */
	public Node(int nodeUUNIN, int nodeOrderNumber) {
		uniqueUniversalNodeIdentificationNumber = nodeUUNIN;
		this.nodeOrderNumber = nodeOrderNumber;
		inputNodes = new ArrayList<Node>();
		outputNodes = new ArrayList<Node>();
		inputNodeWeights = new ArrayList<>();
		value = 0;
	}


	/**
	 * adds a node to the input nodes of this node. //this node will set itself as an output node of Node n //sets weight as weight
	 */
	public void addInputNode(Node node, double weight) {
		inputNodes.add(node);
		inputNodeWeights.add(weight);
		node.addOutputNode(this);
	}


	/**
	 * adds a node to the input nodes of this node. //this node will set itself as an output node of Node n //default weight 0
	 */
	public void addInputNode(Node node) {
		addInputNode(node, 0.0);
		// adds the node with a weight of 0;
	}


	/**
	 * will add a node to the list of nodes referencing this node. //to be used in conjunction with mating and pruning algorithms.
	 */
	private void addOutputNode(Node node) {
		outputNodes.add(node);
	}


	/**
	 * //removes a node from the list of nodes to use.
	 */
	public void removeInputNode(Node node) {
		int nodeNumber = inputNodes.indexOf(node);
		inputNodes.remove(nodeNumber);
		inputNodeWeights.remove(nodeNumber);
		node.removeOutputNode(this);
	}


	/**
	 * remove a node from the list of nodes that it is used by
	 *
	 * @param node
	 */
	private void removeOutputNode(Node node) {
		outputNodes.remove(node);
	}


	/**
	 * this will add the entire list of nodes for a network, or required nodes to the given list.
	 * 
	 * recursion and overlap is prevented by setting the operation ID as required.
	 * 
	 * @param list
	 *            List<Node> list of nodes to add this and all recursice nodes to.
	 * @param operationID
	 *            int value checked to prevent overlap
	 */
	public void addToListRecursivly(List<Node> list, int operationID) {
		// if have not run the operation on this node yet, do.
		if (this.operationID != operationID) {
			this.operationID = operationID;
			list.add(this);
			for (Node n : inputNodes) {
				n.addToListRecursivly(list, operationID);
			}
		}
	}


	/**
	 * goes through every node in the list and adds it multiplied by it's respective weight to the sum //this sum is then run through the tansig function.
	 */
	public void calculateNode() {
		double sum = 0;

		for (int i = 0; i < inputNodes.size(); i++) {
			sum += inputNodes.get(i).getValue() * inputNodeWeights.get(i);
		}
		value = Math.tanh(sum);
	}


	public double getValue() {
		return value;
	}


	public void changeNodeWeight(Node node, double newWeight) {
		inputNodeWeights.set(inputNodes.indexOf(node), newWeight);
	}


	public ArrayList<Node> getReferencingNodes() {
		return outputNodes;
	}


	public ArrayList<Node> getReferencedByNodes() {
		return inputNodes;
	}


	public int getReferencingNodesSize() {
		return outputNodes.size();
	}


	public int getReferencedByNodesSize() {
		return inputNodes.size();
	}


	@Override
	public boolean equals(Object b) {
		if (b instanceof Node && ((Node) b).inputNodes.size() == inputNodes.size()) {

			for (int i = 0; i < inputNodes.size(); i++) {
				if (inputNodes.get(i).getUniqueUniversalNodeIdentificationNumber() != ((Node) b).inputNodes.get(i).getUniqueUniversalNodeIdentificationNumber()) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}


	public int getOperationID() {
		return operationID;
	}


	public int getNodeHeight() {
		return NodeHeight;
	}


	public void setNodeHeight(int nodeHeight) {
		NodeHeight = nodeHeight;
	}
}
