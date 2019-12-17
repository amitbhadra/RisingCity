package risingCity;


//This is each cell of the minheap
class Node 
{
	//The 3 ingredients of Class Node
    int key; 
	Building building;
	RBNode rbnode;
  
    public Node(int item, Building b, RBNode newRBNode) 
    { 
        key = item; 
        building = b;
        rbnode = newRBNode;
    } 
    
    //Setter function for Building
    void setBuilding(Building b) {
    	building = b;
    }

    //Setter function for building
    void setKey(int key) {
    	this.key = key;
    }

    //Getter function for Building
    Building getBuilding() {
    	return building;
    }

    //getter function for key
    int getKey() {
    	return key;
    }
    
    //Set the RedBlack Pointer Node
    void setRBNode(RBNode newRBNode) {
    	rbnode = newRBNode;
    }
    
    //Get the red black pointer node
    RBNode getRBNode() {
    	return rbnode;
    }
} 


//This is the minheap class
public class MinHeap {

	//totaBuildings stores the current no of buildings and the array of nodes
	int totalBuildings;
	Node head[];
	
	MinHeap() {
		head = new Node[2000];
		totalBuildings=0;
	}
	
	MinHeap(int data, int totalTime) {
		Building b = new Building(data, 0, totalTime);
	}

	//Function to heapify Up or percolate up
	public void heapifyUp(int childIndex) {
		
		int parentIndex, tmp;

        if (childIndex != 0) {

              parentIndex = (childIndex-1)/2;
              
              Node parentNode = head[parentIndex];
              Node childNode = head[childIndex];
              
              //If the key of parent we swap and heapify up
              if(parentNode.getKey()>childNode.getKey()) {
            	  head[parentIndex] = childNode;
            	  head[childIndex] = parentNode;
            	  heapifyUp(parentIndex);
              }
              //If the key is same then we check the building numbers
              else if(parentNode.getKey()==childNode.getKey()) {
            	  if(parentNode.getBuilding().getBuildingNum()>childNode.getBuilding().getBuildingNum()) {
            		  head[parentIndex] = childNode;
                	  head[childIndex] = parentNode;
                	  heapifyUp(parentIndex);
            	  }
              }
        }
	}
	
	//Function to Heapify Down
	public void heapifyDown(int i) {
		int smallest = i;
		int l = 2*i +1;
		int r = 2*i +2;

		if(l<totalBuildings) {
			//If the key is less then smallest is found
			if(head[l].getKey()<head[smallest].getKey())
				smallest = l;
			//If the key is same we check for the building num
			else if((head[l].getKey()==head[smallest].getKey())&&
					(head[l].getBuilding().getBuildingNum()<head[smallest].getBuilding().getBuildingNum()))
					smallest = l;
			}
		
		//Same logic for right as left
		if(r<totalBuildings) {
			if(head[r].getKey()<head[smallest].getKey())
				smallest = r;
			else if(head[r].getKey()==head[smallest].getKey()) {
				if(head[r].getBuilding().getBuildingNum()<head[smallest].getBuilding().getBuildingNum())
					smallest = r;
			}
		}
		
		//If this is not i we need to swap
		if(smallest!=i) {
			Node temp = head[i];
			head[i] = head[smallest];
			head[smallest] = temp;
			
			heapifyDown(smallest);
		}
	}

	//At the start of 5 days or when a a building finishes, we call this function to get the new minimum
	public Node removeMin() {
		if(totalBuildings==0) return null;
		Node temp = head[0];
		totalBuildings--;
		if(totalBuildings>0 ) {
			//The way to heapify is replace the 0th node with last node and heapify down
			head[0] = head[totalBuildings];
			//We must heapify down when we extract min
			heapifyDown(0);
		}
		else head[0] = null;
		return temp;
	}
	
	//Insert new Buildings
	public void insert(Building b, RBNode newRBNode, int gc) {
		Node newNode = new Node(0, b, newRBNode);		
		totalBuildings++; 
	    int i = totalBuildings - 1; 
	    head[i] = newNode; 
	    // Fix the min heap property if it is violated 
		heapifyUp(totalBuildings-1);
	}

	//This is overloaded Insert for inserting old Buildings which have been worked for 5 days
	public void insert(Node oldBuildingNode, int gc) {
		if(oldBuildingNode!=null) {
			
			totalBuildings++; 
		    int i = totalBuildings - 1; 
		    head[i] = oldBuildingNode; 
		    // Fix the min heap property if it is violated 
		    heapifyUp(totalBuildings-1);

		}
	}


	
}
