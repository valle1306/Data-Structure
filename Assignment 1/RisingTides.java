package tides;

import java.util.ArrayList;

/**
 * This class contains methods that provide information about select terrains 
 * using 2D arrays. Uses floodfill to flood given maps and uses that 
 * information to understand the potential impacts. 
 * Instance Variables:
 *  - a double array for all the heights for each cell
 *  - a GridLocation array for the sources of water on empty terrain 
 * 
 * @author Original Creator Keith Scharz (NIFTY STANFORD) 
 * @author Vian Miranda (Rutgers University)
 */
public class RisingTides {

    // Instance variables
    private double[][] terrain;     // an array for all the heights for each cell
    private GridLocation[] sources; // an array for the sources of water on empty terrain 



    /**
     * DO NOT EDIT!
     * Constructor for RisingTides.
     * @param terrain passes in the selected terrain 
     */
    public RisingTides(Terrain terrain) {
        this.terrain = terrain.heights;
        this.sources = terrain.sources;
    }

    /**
     * Find the lowest and highest point of the terrain and output it.
     * 
     * @return double[][], with index 0 and index 1 being the lowest and 
     * highest points of the terrain, respectively
     */
    public double[] elevationExtrema() {

        /* WRITE YOUR CODE BELOW */
        double max=0;
        double min=terrain[0][0];
        for (int i=0; i< terrain.length; i++ ){
            for (int j=0; j<terrain[i].length; j++){
               if (terrain[i][j] > max){
                max = terrain[i][j];
               }
               if (terrain[i][j]< min){
                min = terrain[i][j];
               }
            }
        }
        double[] robbie = new double[2];
        robbie[0] = min;
        robbie[1] = max;
        return robbie;
    }

    /**
     * Implement the floodfill algorithm using the provided terrain and sources.
     * 
     * All water originates from the source GridLocation. If the height of the 
     * water is greater than that of the neighboring terrain, flood the cells. 
     * Repeat iteratively till the neighboring terrain is higher than the water 
     * height.
     * 
     * 
     * @param height of the water
     * @return boolean[][], where flooded cells are true, otherwise false
     */
    public boolean[][] floodedRegionsIn(double height) {
        
        /* WRITE YOUR CODE BELOW */
        //Initialize a boolean 2D array, we will call this array resulting array
        boolean [][]resulting_arr = new boolean[terrain.length][terrain[0].length];
        
        ArrayList<GridLocation> myLocation = new ArrayList<GridLocation>();

        for(int i=0; i< sources.length; i++){
            myLocation.add(sources[i]);
            //step 3
            int row = sources[i].row;
            int col = sources[i].col;
            resulting_arr[row][col] = true;
        }
        //System.out.println(myLocation);
        while (!myLocation.isEmpty()){
            GridLocation element = myLocation.remove(0);
            int row = element.row;
            int col = element.col;
            if((row+1 < terrain.length) && (terrain[row+1][col] <= height) && !resulting_arr[row+1][col]){
                resulting_arr[row+1][col] = true;
                myLocation.add(new GridLocation(row +1, col));
            }
            if((row-1 >=0) && (terrain[row-1][col] <= height) && !resulting_arr[row-1][col]){
                resulting_arr[row-1][col] = true;
                myLocation.add(new GridLocation(row -1, col));
            }
            if((col+1 < terrain[0].length) && (terrain[row][col+1] <= height) && !resulting_arr[row][col+1]){
                resulting_arr[row][col+1] = true;
                myLocation.add(new GridLocation(row, col +1));
            }
            if((col -1 >= 0) && (terrain[row][col-1] <= height) && !resulting_arr[row][col-1]){
                resulting_arr[row][col-1] = true;
                myLocation.add(new GridLocation(row, col -1));
            }
        }
        return resulting_arr;
    }

    /**
     * Checks if a given cell is flooded at a certain water height.
     * 
     * @param height of the water
     * @param cell location 
     * @return boolean, true if cell is flooded, otherwise false
     */
    public boolean isFlooded(double height, GridLocation cell) {
        
        /* WRITE YOUR CODE BELOW */
        int row = cell.row;
        int col = cell.col; 
        boolean[][] regionFlooded = floodedRegionsIn(height);
        if (regionFlooded[row][col]){
            return true;
        }
        return false;         
    }

    /**
     * Given the water height and a GridLocation find the difference between 
     * the chosen cells height and the water height.
     * 
     * If the return value is negative, the Driver will display "meters below"
     * If the return value is positive, the Driver will display "meters above"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param cell location
     * @return double, representing how high/deep a cell is above/below water
     */
    public double heightAboveWater(double height, GridLocation cell) {
        
        /* WRITE YOUR CODE BELOW */
        int row = cell.row;
        int col = cell.col;
        double heightDifference = terrain[row][col] - height;
        return heightDifference;
        
    }

    /**
     * Total land available (not underwater) given a certain water height.
     * 
     * @param height of the water
     * @return int, representing every cell above water
     */
    public int totalVisibleLand(double height) {
        boolean[][] flood = floodedRegionsIn(height);
        int count=0;
        for (int i=0; i < flood.length; i++){
            for (int j=0; j< flood[0].length; j++){
                if (!flood[i][j]){
                    count += 1;
                }
            }
        }
        return count;
    } 


    /**
     * Given 2 heights, find the difference in land available at each height. 
     * 
     * If the return value is negative, the Driver will display "Will gain"
     * If the return value is positive, the Driver will display "Will lose"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param newHeight the future height of the water
     * @return int, representing the amount of land lost or gained
     */
    public int landLost(double height, double newHeight) {
        
        /* WRITE YOUR CODE BELOW */
        int landDifference = -totalVisibleLand(newHeight) + totalVisibleLand(height);
        return landDifference;

    }

    /**
     * Count the total number of islands on the flooded terrain.
     * 
     * Parts of the terrain are considered "islands" if they are completely 
     * surround by water in all 8-directions. Should there be a direction (ie. 
     * left corner) where a certain piece of land is connected to another 
     * landmass, this should be considered as one island. A better example 
     * would be if there were two landmasses connected by one cell. Although 
     * seemingly two islands, after further inspection it should be realized 
     * this is one single island. Only if this connection were to be removed 
     * (height of water increased) should these two landmasses be considered 
     * two separate islands.
     * 
     * @param height of the water
     * @return int, representing the total number of islands
     */
    public int numOfIslands(double height) {
        
        /* WRITE YOUR CODE BELOW */
        boolean[][] regionFlooded = floodedRegionsIn(height);
        //1. start by setting each cell's parent to itself
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(terrain.length, terrain[0].length);

        for (int i=0; i< terrain.length; i++){
            for (int j=0; j< terrain[0].length; j++){
                GridLocation p = new GridLocation(i, j);
                //if cell is not flooded, union them
                if (!regionFlooded[i][j]){
                        //check all of the sides
                    if (i+1< terrain.length && !regionFlooded[i+1][j]){
                        uf.union(p, new GridLocation(i+1,j));
                    }
                    if (i>0 && !regionFlooded[i-1][j]){
                        uf.union(p, new GridLocation(i-1, j));
                    }
                    if (j+1 < terrain[0].length && !regionFlooded[i][j+1]){
                        uf.union(p, new GridLocation(i, j+1));
                    }
                    if (j>0 && !regionFlooded[i][j-1]){
                        uf.union(p, new GridLocation(i, j-1));
                    }
                    if (i+1< terrain.length && j+1<terrain[0].length && !regionFlooded[i+1][j+1]){
                        uf.union(p, new GridLocation(i+1, j+1));
                    }
                    if (j+1< terrain[0].length && i>0 && regionFlooded[i-1][j+1]){
                        uf.union(p, new GridLocation(i-1,j+1) );
                    }
                    if (i+1< terrain.length && j>0 && !regionFlooded[i+1][j-1]){
                        uf.union(p, new GridLocation(i+1, j-1));
                    }
                    if (i>0 && j>0 &&  !regionFlooded[i-1][j-1]){
                        uf.union(p, new GridLocation(i-1, j-1));
                    }
                }
            }
        }
        //count the nodes
        int countIsland =0;
        for (int row=0; row< terrain.length; row++){
            for (int col=0; col< terrain[0].length; col++){
                if (!regionFlooded[row][col]){
                    GridLocation current = new GridLocation(row, col);
                    GridLocation islandRoot = uf.find(current);
                    if (current.equals(islandRoot)) {
                    countIsland++;
                }
                }
        
            }
    }
    return countIsland;


}


}



    


            





            
