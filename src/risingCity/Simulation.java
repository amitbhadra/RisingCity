package risingCity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//This is the main Driver class of the Project
//risingCity calls this class and then Simulation calls wither minHeap or RedBlack
public class Simulation {
	
	MinHeap minHeap;
	redBlack redblack;
	PrintWriter printWriter;
	
	Simulation() throws IOException {
		minHeap = new MinHeap();
		redblack = new redBlack();
	}

	//Set the printwriter to output file
	public void setOutputFile(FileWriter outputFile) {
		printWriter = new PrintWriter(outputFile);
	}
	
	//Driver class that calls remove min of minheap
	public Node removeMin() {
		return minHeap.removeMin();
	}

	//Driver class that works on one building by increasing its executed time
	public boolean buildBuilding(Node currentBuildingNode, int globalCounter) {
		int newExecTime = currentBuildingNode.getBuilding().getExecutedTime()+1;
		currentBuildingNode.getBuilding().setExecutedTime(newExecTime);
		currentBuildingNode.setKey(newExecTime);
		if(newExecTime==currentBuildingNode.getBuilding().getTotalTime())
			return true; //this means this Building has finished building and we need to remove it
		else 
			return false;
	}

	//If a building finishes work we output this to file
	private void printFinishedBuilding(int currentBuilding, int globalCounter) {
		String printString = "("+String.valueOf(currentBuilding)+","+String.valueOf(globalCounter)+")";
		printWriter.print(printString);
	}

	//Whenever a new Building is inserted first we insert to RedBlack so that we 
	//get the pointer to RBNode and use it to initialise the minheap with the pointer
	//After imserting we heapifyUp to maintain the heap property
	private void insert(int newBuildingNumber, int newTotalTime, int gc) throws Exception {
		Building b = new Building(newBuildingNumber, 0, newTotalTime);
		RBNode found = redblack.searchTree(newBuildingNumber);
		if(found.building.buildingNum!=0)
			throw new Exception("Building already present!");
		RBNode newRBNode = redblack.insert(b);
		minHeap.insert(b, newRBNode, gc);
		//redBlack.inset(b);
	}

	//Driver class that calls the RedBlack Print function
	private void printIndex(int index) {
		printWriter.println(redblack.displayBuilding(index));
	}

	//Driver class that calls printbetween of redBlack
	private void printBetweenIndexes(int firstIndex, int lastIndex) {
		printWriter.println(redblack.displayBuildlingRange(firstIndex, lastIndex));
	}

	//Preprocessing for print function
	public void printBuildings(String string) {		
		String words[]= string.split("\\(");
		String firstWord = words[0];
		String secondWord = words[1].substring(0, words[1].length() -1);
		
		if(secondWord.contains(",")) {
			String indexes[] = secondWord.split(",");
			int firstIndex = Integer.parseInt(indexes[0]);
			int lastIndex = Integer.parseInt(indexes[1]);
			printBetweenIndexes(firstIndex, lastIndex);
		}
		else {
			int index = Integer.parseInt(secondWord);
			printIndex(index);
		}
	}

	//Preprocessing for Insert function
	public void insertBuilding(String string, int gc) throws Exception {
		String words[]= string.split("\\(");
		String firstWord = words[0];
		String secondWord = words[1].substring(0, words[1].length() -1);
		
		String indexes[] = secondWord.split(",");
		int newBuildingNumber = Integer.parseInt(indexes[0]);
		int newTotalTime = Integer.parseInt(indexes[1]);
		insert(newBuildingNumber, newTotalTime, gc);
		
	}

	
	//Driver class that calls MinHeap to reinsert the building we had worked on before this
	public void reInsertPreviousMin(Node insertNode, int gc) {
		minHeap.insert(insertNode, gc);
	}

	//Driver function that calls the function to delete from RedBlack Tree
	public void deleteRBNode(int buildingNum) {
		redblack.deleteRedBlackNode(buildingNum);	
	}

}
