package risingCity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class risingCity {
	//Some class variables which will always be active
	Node currentNode;
	boolean finished;
	int tempCount;
	
	
	public static void main(String args[]) throws Exception{
		//Throw exception if more than 1 argument
		if(args.length>1) {
			throw new Exception("ERROR: Too many args!");
		}
		
		//Get absolute path to the PWD
		Path currentRelativePath = Paths.get("");
		String currentAbsolutePath = currentRelativePath.toAbsolutePath().toString();
		
		String fileName = currentAbsolutePath+"/"+args[0];
		//String fileName = currentAbsolutePath+"/input-medium.txt";
		File inputFile = new File(fileName);
		FileWriter outputFileWriter = new FileWriter(currentAbsolutePath+"/output-file.txt");
		
		//create an object of risingCity so that we can call it's functions
		risingCity rc = new risingCity();
		
		//This is the function which will work till all Buildings are built
		rc.WorkTillEnd(inputFile, outputFileWriter);
	}
	
	public void WorkTillEnd(File inputFile, FileWriter outputFileWriter)  throws Exception
	{
		String currentLine="";
		
		//This is the driver class. Create an object of it
		Simulation simulation = new Simulation();
		
		//This is the output file for our project
		simulation.setOutputFile(outputFileWriter);
		
		//We start with clock=1 so that we can work on the 1st day
		int globalCounter = 1;
		
		//This is the temp count which is used to iterate either till 5 or if any building finishes
		int tempCount = 0;
		
		//This is used to store the current Node or Building to work on
		currentNode = null;
		
		//used to contain the words of the input
		String words[];
		
		//Used to contain the day Number which will be there in the input
		int dayNumber;
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		//We iterate until the entire input is read
		while(((currentLine = br.readLine()) != null)) {
			
			if(currentLine.contentEquals("")) continue;
			words = currentLine.split(": ");
			dayNumber = Integer.parseInt(words[0]);
			
			//This loop runs until the global counter is less than the mentioned in input
			while(dayNumber>globalCounter) {
				globalCounter++;
				tempCount = workOneDay(simulation, globalCounter,"");
			}
				
			//This function is used to return if the input is print or input
			int commandexecuted = identifyCommand(words[1]);

			//If 1 then input
			if(commandexecuted==1) {
				simulation.insertBuilding(words[1],globalCounter);
				tempCount = workOneDay(simulation, globalCounter, "");
				globalCounter++;
			}
			
			//If 2 then print
			else if(commandexecuted==2) {
				tempCount = workOneDay(simulation, globalCounter, words[1]);
				globalCounter++;
				
			}
			//Otherwise output error
			else System.out.println("ERROR!!!!!!!!!!!!!!!");
		}
		
		//This runs after the inputfile is over and untill all buildings are built
		while((simulation.minHeap.totalBuildings>0)||(currentNode!=null)) {
			tempCount = workOneDay(simulation, globalCounter, "");
			globalCounter++;
		}
		
		//We must close the printwriter
		simulation.printWriter.close();
	}

	//This is the function to work one day it gets called from WorkTillEnd
	private int workOneDay(Simulation simulation, int globalCounter, String printString) {
		Building currentBuilding;
		
		//If tempCount=0 or if currentNode=null this means that either that 
		//previous building was deleted or 5 days of construction for a building is over
		if(tempCount==0 || currentNode==null) {
			currentNode = simulation.removeMin();
		}
		
		//This runs we have the value of currentNode from previous statement
		if(currentNode!=null) {
			
			//get the Building from the node
			currentBuilding = currentNode.getBuilding();
			
			//We increase the tempCount by 1
			tempCount++;			
			
			//Here we are working one day
			finished = simulation.buildBuilding(currentNode, globalCounter);
			
			//Case when the building is finished building
			if(finished==true) {
				tempCount = 0;
				if(printString.length()>0) {
					simulation.printBuildings(printString);
					printString="";
				}
				simulation.deleteRBNode(currentBuilding.buildingNum);
				currentNode = null;
				String toPrint = "("+currentBuilding.getBuildingNum()+","+globalCounter+")";
				simulation.printWriter.println(toPrint);
			}
			
			//Case when 5 days are worked
			if(tempCount==5) {
				tempCount = 0;
				simulation.reInsertPreviousMin(currentNode, globalCounter);
			}
			
			//Case when print statement is received
			if(printString.length()>0) {
				simulation.printBuildings(printString);
				printString="";
			}
		}
		
		return tempCount;
	}

	//This is just to identify the input command, 1 for Insert and 2 for Print
	private static int identifyCommand(String string) {
		if(string.charAt(0)=='I') return 1;
		else if(string.charAt(0)=='P') return 2;
		else return 0;
	}

}
