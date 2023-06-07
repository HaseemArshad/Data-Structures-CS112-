package avengers;
/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

    	// WRITE YOUR CODE HERE
        String locateTitanInputFile = args[0];
        String locateTitanOutputFile = args[1];
        StdIn.setFile(locateTitanInputFile);
        StdOut.setFile(locateTitanOutputFile);
 
 
        int amntGen = StdIn.readInt();
        Double[] generator = new Double[amntGen];
        int z=0;
        while(z < amntGen)

        {
            int index = StdIn.readInt();
            generator[index] = StdIn.readDouble();
            z++;
        }
 
        int[][] adjacencyMatrx = new int[amntGen][amntGen];
        for(int a = 0; a < amntGen; a++)
        {
            for(int b = 0; b < amntGen; b++)
            {
                adjacencyMatrx[a][b] = StdIn.readInt();
                System.out.print(adjacencyMatrx[a][b] + " ");
            }
            System.out.println();
        }

        System.out.println();
        for(int i = 0; i < amntGen; i++)
        {
            for(int j = 0; j < amntGen; j++)
            {
                adjacencyMatrx[i][j] /= (generator[i] * generator[j]);
                System.out.print(adjacencyMatrx[i][j] + " ");
            }
            System.out.println();
        }
 
 
        int[] minCost = new int[amntGen];
        Boolean[] DijkstraSet = new Boolean[amntGen];
 
        for(int i = 0; i < amntGen; i++)
        {
            if(i == 0)
            {
                minCost[i] = 0;
            }
            else
            {
                minCost[i] = Integer.MAX_VALUE;
            }
            DijkstraSet[i] = false;
        }
 
        for(int g = 0; g < amntGen-1; g++)
        {
            int minimum = Integer.MAX_VALUE;
            int currentSource = -1;
 
 
            for(int o = 0; o < DijkstraSet.length; o++)
            {
               if(DijkstraSet[o] == false && minCost[o] <= minimum)
                {
                    minimum = minCost[o];
                    currentSource = o;
                }
            }
 
            DijkstraSet[currentSource] = true;
           
            for(int a = 0; a < amntGen; a++)
            {
                if(!DijkstraSet[a] && adjacencyMatrx[currentSource][a] != 0 && minCost[currentSource] != Integer.MAX_VALUE
                && (minCost[currentSource] + adjacencyMatrx[currentSource][a] < minCost[a]))
                {
                    minCost[a] = minCost[currentSource] + adjacencyMatrx[currentSource][a];
                }
            }
        }
 
       
        StdOut.print(minCost[amntGen-1]);
    }
} 
