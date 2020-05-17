# sudoku-solver-java

This is a sudoku solver I created in 2015-2016. It check subsquares, virtical and horizontal lines, then runs swordfish technique over to solve. 

## How to run
1. Set your puzzle
    Edit the **unsolvedPuzzle** variable with your unsolved sudoku puzzle.
    ```java
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
    ```
2. Compile
    ```bash
    javac Sudoku.java
    ```
3. Run
    ```bash
    java Sudoku
    ```