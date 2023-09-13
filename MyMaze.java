// Riley Thompson

import java.util.Random;
import java.util.Scanner;

public class MyMaze {
    Cell[][] maze;
    int startRow;
    int endRow;

    // Creates each cell in the maze
    public MyMaze(int rows, int cols, int startRow, int endRow) {
        this.maze = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.maze[i][j] = new Cell();
            }
        }
        this.startRow = startRow;
        this.endRow = endRow;
        this.maze[startRow][0].setVisited(true);
    }

    // Create a new maze using an algorithm to form an empty maze
    public static MyMaze makeMaze() {
        Scanner s = new Scanner(System.in);
        System.out.println("Please enter two values, between 5-20 (inclusive), for the row and the column, with a space in between: ");
        String[] userVals = new String[2];
        String vals = s.nextLine();
        userVals = vals.split(" ");
        int rows = Integer.parseInt(userVals[0]);
        int cols = Integer.parseInt(userVals[1]);
        if(rows < 5 || rows > 20 || cols < 5 || cols > 20){
            throw new ArrayIndexOutOfBoundsException("Invalid Dimensions"); //if user types invalid dimensions
        }
        Random random = new Random();
        int startRow = random.nextInt(rows);
        int endRow = random.nextInt(rows);
        MyMaze m1 = new MyMaze(rows, cols, startRow, endRow);
        Stack1Gen<int[]> mazeS = new Stack1Gen();
        int[] coords = {startRow, 0};
        mazeS.push(coords);
        while (mazeS.isEmpty() == false) {
            int[] temp = mazeS.top();
            int[][] direct = new int[3][];
            int counter = 0; //counts non-null elements in direct array
            if (temp[0] >= m1.maze.length || temp[1] >= m1.maze[0].length) { //index out of bounds
                mazeS.pop();
            }
            else {
                if(temp[0]-1 < m1.maze.length && temp[0]-1 >= 0){
                    if (m1.maze[temp[0] - 1][temp[1]].getVisited() == false) { //up
                        direct[counter] = new int[] {temp[0] - 1, temp[1]};
                        counter++;
                    }
                }
                if(temp[1]+1 < m1.maze[0].length && temp[1]+1 >= 0){
                    if (m1.maze[temp[0]][temp[1] + 1].getVisited() == false) { //right
                        direct[counter] = new int[] {temp[0], temp[1]+1};
                        counter++;
                    }
                }
                if(temp[0]+1 < m1.maze.length && temp[0]+1 >= 0){
                    if (m1.maze[temp[0] + 1][temp[1]].getVisited() == false) { //down
                        direct[counter] = new int[] {temp[0]+1, temp[1]};
                        counter++;
                    }
                }
                if(temp[1]-1 < m1.maze[0].length && temp[1]-1 >= 0){
                    if (m1.maze[temp[0]][temp[1] - 1].getVisited() == false) { //left
                        direct[counter] = new int[] {temp[0], temp[1]-1};
                        counter++;
                    }
                }
                if(counter == 0){ //dead end
                    mazeS.pop();
                }
                if(counter != 0){
                    int randIndex = random.nextInt(counter); //choose a random index of direct
                    int[] nCoords = {direct[randIndex][0], direct[randIndex][1]};
                    mazeS.push(nCoords); //pushes random neighbor's coords
                    m1.maze[nCoords[0]][nCoords[1]].setVisited(true);
                    if(temp[0] < nCoords[0]){
                        m1.maze[temp[0]][temp[1]].setBottom(false); //remove bottom of original
                    }
                    if(temp[0] > nCoords[0]){
                        m1.maze[nCoords[0]][nCoords[1]].setBottom(false);
                    }
                    if(temp[1] < nCoords[1]){
                        m1.maze[temp[0]][temp[1]].setRight(false);
                    }
                    if(temp[1] > nCoords[1]){
                        m1.maze[nCoords[0]][nCoords[1]].setRight(false);
                    }
                }
            }
        }
        for (int i = 0; i < m1.maze.length; i++) {
            for (int j = 0; j < m1.maze[0].length; j++) {
                m1.maze[i][j].setVisited(false);
            }
        }
        return m1;
    }
        // Print a representation of the maze to the terminal
        public void printMaze(boolean path){
            String result = "|";
            for(int t = 0; t < this.maze[0].length; t++){
                result += "---|";
            }
            result += "\n";
            for (int i = 0; i < this.maze.length *2; i++) {
                int j = 0;
                if(i == this.startRow && j == 0){
                    result += " ";
                }
                else{
                    result += "|";
                }
                for (j = 0; j < this.maze[0].length; j++) {
                    if(i % 2 != 0) {
                        if(this.maze[(i-1)/2][j].getBottom() == false){
                            result += "   |";
                        }
                        else{
                            result += "---|";
                        }
                    }
                    if(i % 2 == 0){
                        if(this.maze[i/2][j].getVisited() == true){
                            if(i == this.endRow && j == this.maze[0].length -1){
                                result += " *  ";
                            }
                            else if(this.maze[i/2][j].getRight() == true){
                                result += " * |";
                            }
                            else{
                                result += " *  ";
                            }
                        }
                        if (this.maze[i/2][j].getVisited() == false){
                            if(i == this.endRow && j == this.maze[0].length -1){
                                result += " *  ";
                            }
                            else if(this.maze[i/2][j].getRight() == true){
                                result+= "   |";
                            }
                            else{
                                result += "    ";
                            }
                        }
                    }
                    if(j == this.maze[0].length -1){
                        result += "\n";
                    }
                }
            }
            System.out.println(result);
        }
        // Solve the maze using algorithm to reach the end of maze
        public void solveMaze () {
//            Q1Gen mazeQ = new Q1Gen();
            Q1Gen<int[]> mazeQ = new Q1Gen();
            int[] coords = {startRow, 0};
            mazeQ.add(coords);
            while (mazeQ.length() != 0) {
                int[] temp = mazeQ.remove();
                this.maze[temp[0]][temp[1]].setVisited(true);
                int[] end = {endRow, (this.maze[0].length - 1)};
                if (temp[0] == end[0] && temp[1] == end[1]) { //check if maze has been solved
                    break;
                } else {
                    if (temp[0] - 1 < this.maze.length && temp[0] - 1 >= 0) {
                        if (this.maze[temp[0] - 1][temp[1]].getVisited() == false) { //up
                            int[] curr = {temp[0] - 1, temp[1]};
                            mazeQ.add(curr);
                        }
                    }
                    if (temp[1] + 1 < this.maze[0].length && temp[1] + 1 >= 0) {
                        if (this.maze[temp[0]][temp[1] + 1].getVisited() == false) { //right
                            int[] curr = {temp[0], temp[1] + 1};
                            mazeQ.add(curr);
                        }
                    }
                    if (temp[0] + 1 < this.maze.length && temp[0] + 1 >= 0) {
                        if (this.maze[temp[0] + 1][temp[1]].getVisited() == false) { //down
                            int[] curr = {temp[0] + 1, temp[1]};
                            mazeQ.add(curr);
                        }
                    }
                    if (temp[1] - 1 < this.maze[0].length && temp[1] - 1 >= 0) {
                        if (this.maze[temp[0]][temp[1] - 1].getVisited() == false) { //left
                            int[] curr = {temp[0], temp[1] - 1};
                            mazeQ.add(curr);
                        }
                    }
                }
            }
            this.printMaze(true);
        }

        public static void main (String[]args){
            /* Make and solve maze */
            MyMaze m1 = makeMaze();
            m1.solveMaze();
        }
    }
