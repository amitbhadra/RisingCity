package risingCity;

/*This is the default Building 
class that will be used as the 
base Building class for the entire
project. */

public class Building {
	//Building number, executed time and total time are the 3 metrics for a Building
	int buildingNum;
	int executed_time;
	int total_time;
	
	//default constructor
	Building() {
		buildingNum = 0;
		executed_time = 0;
		total_time = 0;
	}
	
	//Parameterized Constructor
	Building(int buildingNum, int executed_time, int total_time) {
		this.buildingNum = buildingNum;
		this.executed_time= executed_time;
		this.total_time = total_time;
	}
	
	//Setter Function for Executed Time
	void setExecutedTime(int executed_time) {
		this.executed_time = executed_time;
	}
	
	//Getter Function for Executed Time
	int getExecutedTime() {
		return this.executed_time;
	}
	
	//Getter Function for Building Number
	int getBuildingNum() {
		return this.buildingNum;
	}
	
	//Getter Function for Total time of a building
	int getTotalTime() {
		return this.total_time;
	}
	
	//If we need to change the executed Time when we increment it
	void changeExecutedTime(int change) {
		int newExecutionTime = this.executed_time+change;
		setExecutedTime(newExecutionTime);
	}
}
