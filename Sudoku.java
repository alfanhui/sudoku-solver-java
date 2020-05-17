import java.util.*;
import java.io.Console;

/**
{00,01,02} {03,04,05} {06,07,08}
{10,11,12} {13,13,15} {16,17,18}
{20,21,22} {23,24,25} {26,27,28}

{30,31,32} {33,34,35} {36,37,38}
{40,41,42} {43,44,45} {46,47,48}
{50,51,52} {53,54,55} {56,57,58}

{60,61,62} {63,64,65} {66,67,68}
{70,71,72} {73,74,75} {76,77,78}
{80,81,82} {83,84,85} {86,87,88}

**/




public class Sudoku{
    //Declare variables
    private Map<Integer,Character> puzzle;
    private Map<Integer,Character> backupPuzzle;
    private Map<Integer,Integer> notInSquareConstraintMap;

    private List<ArrayList<Character>> candidatePuzzle;
    private List<ArrayList<Character>> container;
    private List<ArrayList<Integer>> swordfishVArray;
    private List<ArrayList<Integer>> swordfishHArray;
    private List<Character> marker;
    private List<Boolean> lookup;

    private final char[] array = new char[] {'1','2','3','4','5','6','7','8','9'};
    private final int[] constraintPuzzleMap = new int[] {0,3,6,30,33,36,60,63,66};
    private final int[] constraintCandidateMap = new int[] {0,3,6,27,30,33,54,57,60};
    private boolean square, vLine, hLine, sword;

    //Set your puzzle here.
    char[] unsolvedPuzzle = new char[] {' ','2',' ','8',' ',' ','6',' ','7',
                                        ' ',' ','7','5','3','1',' ',' ',' ',
                                        ' ',' ',' ',' ',' ','6',' ',' ','8',
                                        ' ','5','1','9',' ','8',' ',' ',' ',
                                        '7',' ',' ',' ','6',' ',' ',' ','2',
                                        ' ',' ',' ','2',' ','4','1','7',' ',
                                        '3',' ',' ','6',' ',' ',' ',' ',' ',
                                        ' ',' ',' ','8','9','5','4',' ',' ',
                                        '4',' ','8',' ','1',' ',' ','2',' '};
    
    //Medium
    // char[] unsolvedPuzzle = new char[] {' ','3',' ',' ',' ',' ',' ',' ','4',
    //                                     ' ',' ',' ','8','3',' ',' ','2',' ',
    //                                     '7',' ',' ',' ',' ','4','6',' ',' ',
    //                                     ' ','2',' ',' ',' ',' ','9',' ','7',
    //                                     ' ',' ','3','9',' ','6','1',' ',' ',
    //                                     '4',' ','7',' ',' ',' ',' ','8',' ',
    //                                     ' ',' ','9','3',' ',' ',' ',' ','1',
    //                                     ' ','7',' ',' ','2','8',' ',' ',' ',
    //                                     '5',' ',' ',' ',' ',' ',' ','6',' '};
    //Hard                                        
    // char[] unsolvedPuzzle = new char[] {'8',' ',' ',' ',' ',' ',' ',' ',' ',
    //                                     ' ',' ','3','6',' ',' ',' ',' ',' ',
    //                                     ' ','7',' ',' ','9',' ','2',' ',' ',
    //                                     ' ','5',' ',' ',' ','7',' ',' ',' ',
    //                                     ' ',' ',' ',' ','4','5','7',' ',' ',
    //                                     ' ',' ',' ','1',' ',' ',' ','3',' ',
    //                                     ' ',' ','1',' ',' ',' ',' ','6','8',
    //                                     ' ',' ','8','5',' ',' ',' ','1',' ',
    //                                     ' ','9',' ',' ',' ',' ','4',' ',' '};



    //Constructor
    //Initialise variables
    public Sudoku(){
        //initialise variables
        puzzle = new HashMap<Integer,Character>();
        puzzle = initMap(puzzle, ' ', 89);

        backupPuzzle = new HashMap<Integer,Character>();
        candidatePuzzle  = new ArrayList<ArrayList<Character>>();

        notInSquareConstraintMap  = new HashMap<Integer,Integer>();
        notInSquareConstraintMap = initMap(notInSquareConstraintMap, 0,89);

        //Initialise notInSquareContraintMap
        //From any point in square, I use this map to lookup the nearest top left number of the square.
        int index = 0;
        for(Integer i = 0; i<89;i++){
            if(i % 10 == 9){
                i++;
                index -=2;
                if(i / 10 == 3 || i / 10 == 6)
                    index+=3;
            }
            if(i % 10 == 3 || i % 10 == 6)
                index++;
            notInSquareConstraintMap.put(i,constraintPuzzleMap[index]);
        }

        square = true;
        vLine = true;
        hLine = true;
        sword = true;
    }

    //Resets whatever map type back to the length sepecified.
    public <T> Map<Integer,T> initMap(Map<Integer,T> m, T clearValue, int length){
        m.clear();
        for(Integer i =0;i<length;i++){
            if(length == 89 && i % 10 == 9)
                i++;
            m.put(i,clearValue);
        }
        return m;
    }

    public void insertPuzzle(){
        for(int i = 0, j =0;i<unsolvedPuzzle.length;i++, j++){
            if(j % 10 == 9)
                j++;
            puzzle.put(j,unsolvedPuzzle[i]);
        }
    }

    //Checks to see if it is a valid move
    public boolean validMove(char x, int loci, Map<Integer,Character> puzzle){
        //Check Row
        int row = loci / 10;
        row = row *10;
        for(int i =0; i<9;i++, row++){
            if (puzzle.get(row) == x)
                return false;
        }
        //Check Column
        int col = loci % 10;
        for(int i =0;i<9;i++,col+=10){
            if(puzzle.get(col) == x)
                return false;
        }

        //Check Square
        int base = notInSquareConstraintMap.get(loci);
        for(int i = 0; i<3;i++){
            for(int j = 0; j<3;j++){
                if(puzzle.get((base)) == x)
                    return false;
                base++;
                }
            base+=7;
            }
        return true;
    }

    //checks if the Sudoku puzzle is all filled in
    public boolean completedSudoku(Map<Integer,Character> puzzle){
        for(Integer key : puzzle.keySet()){
            if(puzzle.get(key) == ' ')
                return false;
            }
        return true;
    }

    //prints Sudoku, taking the a string header and the puzzle to print
    public void printSudoku(String str, Map<Integer,Character> puzzle){
        System.out.print(str + "\n[");
        for(Integer key : puzzle.keySet()){
            if(key % 10 == 0 && key != 0){
                System.out.print("]\n[");
            }
            System.out.print(puzzle.get(key) + ",");
        }
        System.out.print("]\n");
    }

    //This requires a seperate printer since
    //it is based on a different key formation
    public void printCandidates(){
        for(int i = 0; i<candidatePuzzle.size();i++){
            System.out.println("\n" + i);
            if(i % 10 == 9)
                System.out.println();
            System.out.print(candidatePuzzle.get(i));
        }
    }

    //Stores all candidate numbers in an array for that unit.
    public void analyseCandidateReduction(Map<Integer,Character> puzzle){
        candidatePuzzle = new ArrayList<ArrayList<Character>>();
        for(int i = 0, key = 0; i<81;i++, key++){
            if(key %10 == 9)
                key++;
            candidatePuzzle.add(new ArrayList<Character>());
            if(puzzle.get(key) == ' '){
                for(char x : array){
                    if(validMove(x,key,puzzle)){
                        candidatePuzzle.get(i).add(x);
                    }
                }
            }
        }
        applyAnalyse(candidatePuzzle, puzzle);
    }

    //If there is only 1 candidate for a unit, then apply that candidate to puzzle
    public void applyAnalyse(List<ArrayList<Character>> candidatePuzzle, Map<Integer,Character> puzzle){
        for(int i = 0, j =0;i<candidatePuzzle.size();i++, j++){
            if(j % 10 == 9)
                j++;
            if(candidatePuzzle.get(i).size() == 1)
                puzzle.put(j,candidatePuzzle.get(i).get(0));
        }
    }

    //Analyse for commen items in their squares.
    //Its a 2 step process, once we have all the candidates per square, we need to then go through again and insert unique values.
    //The container temporary holds all candidate values for a square
    //If there is a unique candidate found, marker holds this value for square.
    //Upon 2nd pass-through, unique candidates are applied to their location in their squares.
    public void analyseCommonSquare(Map<Integer,Character> puzzle){
        square = true;
        analyseCandidateReduction(puzzle);
        container = new ArrayList<ArrayList<Character>>();
        marker = new ArrayList<Character>();
        for(int k = 0; k < 2; k++){ //2 step process, grab data and apply
            for(int index = 0; index< 9; index++){ //for each mini square
                container.add(new ArrayList<Character>());
                int baseP = constraintPuzzleMap[index];
                int baseC = constraintCandidateMap[index];
                for(int i = 0; i<3;i++){
                    for(int j = 0; j<3;j++){
                        if(k ==0){ //first step
                            container.get(index).addAll(candidatePuzzle.get(baseC));
                        }else{ //second step
                            if(candidatePuzzle.get(baseC).contains(marker.get(index)))
                                //Warning! Just because it can go in a square, need to check whether its already in row/column!
                                if(validMove(marker.get(index),baseP,puzzle))
                                    puzzle.put(baseP,marker.get(index));
                        }
                        baseP+=1;
                        baseC+=1;
                    }
                    baseP+=7;
                    baseC+=6;
                    }
                }
            //now find single numbers in arrays for each square
            if (k == 0){
                markUniqueInContainer();
            }
        }
        if(numOfElements(marker,'0')==9)
            square = false;
    }

    public void analyseCommonVLine(Map<Integer,Character> puzzle){
        analyseCandidateReduction(puzzle);
        container = new ArrayList<ArrayList<Character>>();
        marker = new ArrayList<Character>();
        for(int l = 0; l<9;l++) //initialise container
            container.add(new ArrayList<Character>());

        for(int k = 0; k<2;k++){
            for(int i = 0, key = 0; i<81;i++, key++){
                if(key %10 == 9)
                    key++;
                if(k ==0)
                    container.get(key%10).addAll(candidatePuzzle.get(i));
                else{
                    if(candidatePuzzle.get(i).contains(marker.get(key%10))){
                        if(validMove(marker.get(key%10),key,puzzle))
                            puzzle.put(key,marker.get(key%10));
                    }
                }
            }
            if (k == 0){
                markUniqueInContainer();
            }
        }
        if(numOfElements(marker,'0')==9)
            vLine = false;
    }


    public void analyseCommonHLine(Map<Integer,Character> puzzle){
        hLine = false;
        analyseCandidateReduction(puzzle);
        container = new ArrayList<ArrayList<Character>>();
        marker = new ArrayList<Character>();
        for(int l = 0; l<9;l++)
            container.add(new ArrayList<Character>());
        for(int k = 0; k<2;k++){
            for(int i = 0, key = 0; i<81;i++, key++){
                if(key %10 == 9)
                    key++;
                if(k ==0){
                    container.get(key/10).addAll(candidatePuzzle.get(i));
                }else{
                    if(candidatePuzzle.get(i).contains(marker.get(key/10)))
                        if(validMove(marker.get(key/10),key,puzzle))
                            puzzle.put(key,marker.get(key/10));
                }
            }
            if (k == 0){
                markUniqueInContainer();
            }
        }
        if(numOfElements(marker,'0')==9)
            hLine = false;
    }

    public void markUniqueInContainer(){
        for(int key = 0; key < container.size(); key++){
            boolean found = false;
            for(char x : array){
                if(numOfElements(container.get(key),x) == 1){
                    marker.add(x);
                    found = true;
                    break;
                }
            }
            if(found == false)
                marker.add('0');
        }
    }

    public <T> int numOfElements(List<T> list, T element){
        int counter = 0;
        for(T item: list){
            if(item == element)
                counter+=1;
        }
        return counter;
    }

    public void swordfishCheck(){
        analyseCandidateReduction(puzzle);
        swordfishVArray  = new ArrayList<ArrayList<Integer>>();
        swordfishHArray  = new ArrayList<ArrayList<Integer>>();
         for(int l = 0; l<9;l++){ //initialise container
            swordfishVArray.add(new ArrayList<Integer>());
            swordfishHArray.add(new ArrayList<Integer>());
        }
        ArrayList<Integer> vTemp, hTemp;
        int v, h =0;
        for(int key = 0; key<81; key++){
            for(int i = 0; i<9;i++){ //for each number possible
                if(candidatePuzzle.get(key).contains(array[i])){ //if it is a candidate, save its row and column seperately
                    v = key % 10;
                    h = key / 10; //will round down I believe...
                    swordfishVArray.get(i).add(v); swordfishHArray.get(i).add(h);
                }
            }
        }
        //Reset lookup values
        lookup = new ArrayList<Boolean>(9);
        for(int i= 0;i<array.length;i++)
            lookup.add(i,false);

        int vCounter, hCounter;
        for(int i = 0; i <9; i++){
            if (swordfishVArray.get(i).size() == 0) //it doesn't matter which map, they are identifiable with each other.
                continue;
            for(int l = 0; l<9;l++){// pair items, if there are less or more than 2 of a number, then it is not valid for swordfishing
                vCounter = numOfElements(swordfishVArray.get(i),l);
                hCounter = numOfElements(swordfishHArray.get(i),l);
                if(vCounter == 2 || hCounter == 2){
                    lookup.set(i, true);
                }
                else if (vCounter == 0 || hCounter == 0) //not in list, its ok
                    continue;
                else{
                    lookup.set(i, false);
                    break;
                }
            }
        }
    }

    public Map<Integer,Character> applySwordfish(char x, Map<Integer,Character> puzzle){
        for(int i = 0, key = 0; i < 81; i++, key++){
            if(key % 10 == 9)
                key++;
            if (candidatePuzzle.get(i).size() >2)//swordfish won't risk brute force if there are more than 2 candidates
                    continue;
            for (Character item : candidatePuzzle.get(i)){
                if (item == x){
                    if(validMove(x, key, puzzle)){
                        System.out.println("Placing " + x + " at location " + key);
                        puzzle.put(key, x);
                        return puzzle;
                    }
                }
            }
        }
        return puzzle;
    }

    public void solve(){
        insertPuzzle();
        printSudoku("Original problem", puzzle);
        //analyse using candidate reduction
        while(square || vLine || hLine){ //while there was still changes being made
            analyseCommonSquare(puzzle);
            analyseCommonVLine(puzzle);
            analyseCommonHLine(puzzle);
        }
        printSudoku("Candidate analyse checked..", puzzle);

        if(!completedSudoku(puzzle)){ //if still not completed, swordfish.
            System.out.println("Still not completed.\n Swordfishing..");
            while(sword){
                backupPuzzle = puzzle;
                swordfishCheck();
                if(lookup.contains(true)){
                    for(int i = 0; i<lookup.size();i++){
                        if(lookup.get(i) == true){
                            //reset variables for analyse
                            backupPuzzle = applySwordfish(array[i], backupPuzzle);
                            printSudoku("Swordfished: ", backupPuzzle); //since swordfish only fills a unit, we need to do candidate checks again to complete swordfish
                            analyseCommonSquare(backupPuzzle);
                            analyseCommonVLine(backupPuzzle);
                            analyseCommonHLine(backupPuzzle);
                           if(completedSudoku(backupPuzzle))
                                break;
                        }
                    }
                }else
                    sword = false;
                puzzle = backupPuzzle;
            }
        }
        printSudoku("results:" , puzzle);
    }


    public static void main(String[] args){
        Sudoku s = new Sudoku();
        s.solve();
    }

}



