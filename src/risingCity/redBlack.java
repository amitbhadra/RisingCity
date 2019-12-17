package risingCity;

import java.util.List;
import java.util.ArrayList;

//This is 1 RedBlack Node
class RBNode {
	
	//The ingredients are Building, data which is the building num
	//Parent, left and right node and the color of the node
    Building building; 		// building object
    int data; 				// holds the building number
    RBNode parent; 			// this is pointing to the parent
    RBNode left; 			// this is pointing to left child
    RBNode right;			// this is pointing to right child
    int color; 				// This is 1 for Red and 0 for Black
    
    //1 parameterized constructor
    RBNode(int data) {
    	building = new Building(0,0,0);
    }
    
    //Default Constructor
    RBNode(){}

    //Another parameterized constructor used to define a node
    RBNode(Building building)
    {
        this.building = building;
        this.data = building.buildingNum;
    }
}

//This is the class that implements the RedBlack Tree
//Here each Node is a RedBlack node defined above
public class redBlack {
	
	//Store the root of RBT
    private RBNode root;
    
    //This is special dummy leaf nodes
    private RBNode LeafNode;

    //Search the Node that contains the key from the nodeToSearch onwards
    private RBNode searchTreeHelper(RBNode nodeToSearch, int keyToSearch) {
        if (nodeToSearch == LeafNode || keyToSearch == nodeToSearch.data) {
            if(nodeToSearch == LeafNode)
            {
                Building building = new Building(0, 0, 0);
                RBNode dummyNode = new RBNode();
                dummyNode.building = building;
                return dummyNode;
            }

            return nodeToSearch;
        }

        if (keyToSearch < nodeToSearch.data) {
            return searchTreeHelper(nodeToSearch.left, keyToSearch);
        }
        return searchTreeHelper(nodeToSearch.right, keyToSearch);
    }

    //Once a delete action takes place the RedBlack Tree needs to be fixed
    //This is because RBT is a balanced binary tree
    private void fixRedBlackDelete(RBNode nodeToRefactor) {
        RBNode tempNode;
        while (nodeToRefactor != root && nodeToRefactor.color == 0) {
            if (nodeToRefactor == nodeToRefactor.parent.left) {
                tempNode = nodeToRefactor.parent.right;
                if (tempNode.color == 1) {
                    tempNode.color = 0;
                    nodeToRefactor.parent.color = 1;
                    leftRotateTree(nodeToRefactor.parent);
                    tempNode = nodeToRefactor.parent.right;
                }
                if(tempNode==LeafNode)
                	return;
                

                if (tempNode.left.color == 0 && tempNode.right.color == 0) {
                    tempNode.color = 1;
                    nodeToRefactor = nodeToRefactor.parent;
                } else {
                    if (tempNode.right.color == 0) {
                        tempNode.left.color = 0;
                        tempNode.color = 1;
                        rightRotateTree(tempNode);
                        tempNode = nodeToRefactor.parent.right;
                    }

                    tempNode.color = nodeToRefactor.parent.color;
                    nodeToRefactor.parent.color = 0;
                    tempNode.right.color = 0;
                    leftRotateTree(nodeToRefactor.parent);
                    nodeToRefactor = root;
                }
            } else {
                tempNode = nodeToRefactor.parent.left;
                if (tempNode.color == 1) {
                    tempNode.color = 0;
                    nodeToRefactor.parent.color = 1;
                    rightRotateTree(nodeToRefactor.parent);
                    tempNode = nodeToRefactor.parent.left;
                }

                if(tempNode==LeafNode)
                	return;
                if (tempNode.right.color == 0 && tempNode.right.color == 0) {
                    tempNode.color = 1;
                    nodeToRefactor = nodeToRefactor.parent;
                } 
                else {
                    if (tempNode.left.color == 0) {
                        tempNode.right.color = 0;
                        tempNode.color = 1;
                        leftRotateTree(tempNode);
                        tempNode = nodeToRefactor.parent.left;
                    }

                    tempNode.color = nodeToRefactor.parent.color;
                    nodeToRefactor.parent.color = 0;
                    tempNode.left.color = 0;
                    rightRotateTree(nodeToRefactor.parent);
                    nodeToRefactor = root;
                }
            }
        }
        nodeToRefactor.color = 0;
    }

    
    private void transplantRedBlack(RBNode nodeU, RBNode nodeV) {
        if (nodeU.parent == null) {
            root = nodeV;
        } else if (nodeU == nodeU.parent.left){
            nodeU.parent.left = nodeV;
        } else {
            nodeU.parent.right = nodeV;
        }
        nodeV.parent = nodeU.parent;
    }

    //This is the helper class for red black tree delete
    //This calls the transplant function
    private void deleteRedBlackNodeHelper(RBNode nodeToDelete, int key) {
        RBNode nodeZ = LeafNode;
        RBNode nodeX, nodeY;
        while (nodeToDelete != LeafNode){
            if (nodeToDelete.data == key) {
                nodeZ = nodeToDelete;
            }

            if (nodeToDelete.data <= key) {
                nodeToDelete = nodeToDelete.right;
            } else {
                nodeToDelete = nodeToDelete.left;
            }
        }

        if (nodeZ == LeafNode) {
            return;
        }

        nodeY = nodeZ;
        int yOriginalColor = nodeY.color;
        if (nodeZ.left == LeafNode) {
            nodeX = nodeZ.right;
            transplantRedBlack(nodeZ, nodeZ.right);
        } else if (nodeZ.right == LeafNode) {
            nodeX = nodeZ.left;
            transplantRedBlack(nodeZ, nodeZ.left);
        } else {
            nodeY = findMinimumNode(nodeZ.right);
            yOriginalColor = nodeY.color;
            nodeX = nodeY.right;
            if (nodeY.parent == nodeZ) {
                nodeX.parent = nodeY;
            } else {
                transplantRedBlack(nodeY, nodeY.right);
                nodeY.right = nodeZ.right;
                nodeY.right.parent = nodeY;
            }

            transplantRedBlack(nodeZ, nodeY);
            nodeY.left = nodeZ.left;
            nodeY.left.parent = nodeY;
            nodeY.color = nodeZ.color;
        }
        if (yOriginalColor == 0){
            fixRedBlackDelete(nodeX);
        }
    }

    //Once a new RB Node is inserted, we need to fix the Red black tree
    //If it is no more height or color balanced
    private void fixRedBlackInsert(RBNode nodeA) {
        RBNode nodeB;
        while (nodeA.parent.color == 1) {
            if (nodeA.parent == nodeA.parent.parent.right) {
                nodeB = nodeA.parent.parent.left; 
                if (nodeB.color == 1) {
                    nodeB.color = 0;
                    nodeA.parent.color = 0;
                    nodeA.parent.parent.color = 1;
                    nodeA = nodeA.parent.parent;
                } else {
                    if (nodeA == nodeA.parent.left) {
                        nodeA = nodeA.parent;
                        rightRotateTree(nodeA);
                    }
                    nodeA.parent.color = 0;
                    nodeA.parent.parent.color = 1;
                    leftRotateTree(nodeA.parent.parent);
                }
            } else {
                nodeB = nodeA.parent.parent.right;

                if (nodeB.color == 1) {
                    nodeB.color = 0;
                    nodeA.parent.color = 0;
                    nodeA.parent.parent.color = 1;
                    nodeA = nodeA.parent.parent;
                } else {
                    if (nodeA == nodeA.parent.right) {
                        nodeA = nodeA.parent;
                        leftRotateTree(nodeA);
                    }
                    nodeA.parent.color = 0;
                    nodeA.parent.parent.color = 1;
                    rightRotateTree(nodeA.parent.parent);
                }
            }
            if (nodeA == root) {
                break;
            }
        }
        root.color = 0;
    }

    //Default class for Red Black, we define initially by 0
    public redBlack() {
        LeafNode = new RBNode(0);
        LeafNode.color = 0;
        LeafNode.left = null;
        LeafNode.right = null;
        root = LeafNode;
    }

    //Search the RedBlack tree for a value or key
    public RBNode searchTree(int k) {
        return searchTreeHelper(this.root, k);
    }
    
    //Search the Tree in a particular Range.
    //This calls the print range helper function
    public List<RBNode> searchTreeRange(int low, int high)
    {
        List<RBNode> result = new ArrayList<>();
        searchTreeRangeHelper(this.root, result, low, high);

        return  result;
    }

    //Given a particular node and a range, it does In order traversal and adds the value 
    //to the List result
    private void searchTreeRangeHelper(RBNode node, List<RBNode> result, int low, int high) {
        if (node == LeafNode)
            return;

        int compareLow = low - node.building.buildingNum;
        int compareHigh = high - node.building.buildingNum;

        if(compareLow < 0)
            searchTreeRangeHelper(node.left, result, low, high);

        if (compareLow <= 0 && compareHigh >= 0)
            result.add(node);
        
        if (compareHigh > 0)
            searchTreeRangeHelper(node.right, result, low, high);
    }

    //Find the smallest RBNode
    public RBNode findMinimumNode(RBNode node) {
        while (node.left != LeafNode) {
            node = node.left;
        }
        return node;
    }

    //Find the largest RB Node
    public RBNode findMaximumNode(RBNode node) {
        while (node.right != LeafNode) {
            node = node.right;
        }
        return node;
    }

    //Left Rotate the RB Tree starting from a node
    public void leftRotateTree(RBNode nodeA) {
        RBNode nodeB = nodeA.right;
        nodeA.right = nodeB.left;
        if (nodeB.left != LeafNode) {
            nodeB.left.parent = nodeA;
        }
        nodeB.parent = nodeA.parent;
        if (nodeA.parent == null) {
            this.root = nodeB;
        } else if (nodeA == nodeA.parent.left) {
            nodeA.parent.left = nodeB;
        } else {
            nodeA.parent.right = nodeB;
        }
        nodeB.left = nodeA;
        nodeA.parent = nodeB;
    }

    //Right Rotate the RB Tree starting from a node
    public void rightRotateTree(RBNode nodeA) {
        RBNode nodeB = nodeA.left;
        nodeA.left = nodeB.right;
        if (nodeB.right != LeafNode) {
            nodeB.right.parent = nodeA;
        }
        nodeB.parent = nodeA.parent;
        if (nodeA.parent == null) {
            this.root = nodeB;
        } else if (nodeA == nodeA.parent.right) {
            nodeA.parent.right = nodeB;
        } else {
            nodeA.parent.left = nodeB;
        }
        nodeB.right = nodeA;
        nodeA.parent = nodeB;
    }

    //This function gets called from the Driver class to insert a new building
    public RBNode insert(Building b) {
    	//It works by creating a new RB Node with the particular building
    	//and the buildingnum as key
        RBNode node = new RBNode();
        node.building = b;  // assign the building object to the node parameter
        node.parent = null;
        node.data = b.getBuildingNum();
        node.left = LeafNode;
        node.right = LeafNode;
        node.color = 1; // The colour of new nodes must always be red before fixing the tree

        RBNode nodeY = null;
        RBNode nodeX = this.root;

        while (nodeX != LeafNode) {
            nodeY = nodeX;
            if (node.data < nodeX.data) {
                nodeX = nodeX.left;
            } else {
                nodeX = nodeX.right;
            }
        }

        node.parent = nodeY;
        if (nodeY == null) {
            root = node;
        } else if (node.data < nodeY.data) {
            nodeY.left = node;
        } else {
            nodeY.right = node;
        }

        if (node.parent == null){
            node.color = 0;
            return node;
        }

        if (node.parent.parent == null) {
            return node;
        }

        //Fix the RB Tree if required after Insert
        fixRedBlackInsert(node);
        
        return node;
    }

    //Returns the RB Root
    public RBNode getRoot(){
        return this.root;
    }

    //Called from Driver class. This calls the helper class which performs the function
    public void deleteRedBlackNode(int data) {
        deleteRedBlackNodeHelper(this.root, data);
    }

    //If the input is Print(x) returns the correct print string and
    //Driver will output the string to output.txt
    public String displayBuilding(int buildingNum) {
        RBNode result = searchTree(buildingNum);
        String output = "(" + result.building.buildingNum + "," + result.building.executed_time + "," + result.building.total_time + ")";
        //System.out.println("(" + result.building.buildingNum + result.building.executed_time + result.building.total_time + ")");
        return output;
    }

    //If the input is Print(x,y) returns the correct print string and
    //Driver will output the string to output.txt
    public String displayBuildlingRange(int low, int high) {
        List<RBNode> searchRangeResult =  searchTreeRange(low, high);
        StringBuilder sb = new StringBuilder();
        for(RBNode i : searchRangeResult) {
            sb.append("(").append(i.building.buildingNum).append(",").append(i.building.executed_time).append(",").append(i.building.total_time).append(")").append(",");
        }
        String result = sb.substring(0, sb.length() - 1);
        return result;
    }
}
