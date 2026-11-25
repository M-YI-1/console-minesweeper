package com.echo.minesweeper.model;
//一个Board对象表示一个棋盘，包含棋盘的大小、地雷数量等信息
import java.util.*;

public class Board {
    public static final int DEFAULT_ROWS = 9;    //默认9行
    public static final int DEFAULT_COLS = 9;    //默认9列
    public static final int DEFAULT_MINE_COUNT = 10;  // 默认需要10颗雷

    private final int rows;    //棋盘行数
    private final int cols;          // 保存本局实际列数
    private final int mineCount;     // 保存本局要求的地雷总数
    private final Cell[][] cells;    // 二维数组保存全部格子
    private boolean minesPlaced;

    public Board( ) {
        this( DEFAULT_ROWS, DEFAULT_COLS, DEFAULT_MINE_COUNT );
    }

    public Board( int rows, int cols, int mineCount ) {
        if(rows <= 0 || cols <= 0){
            throw new IllegalArgumentException("棋盘行数和列数必须大于0！");
        }
        if(mineCount < 0 || mineCount >= rows * cols){
            throw new IllegalArgumentException("地雷数量不合法，必须至少保留一个安全格！");
        }
        
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        this.cells = new Cell[rows][cols];

        createCells();

    }
    private void createCells () {
        for ( int row = 0 ; row < rows ; row ++){
            for( int col = 0 ; col < cols ; col ++){
                cells[row][col] = new Cell(row , col);
            }
        }
    }
    // 获取棋盘的行数
    public int getRows() {
        return rows;
    }
    // 获取棋盘的列数
    public int getCols() {
        return cols;
    }
    // 获取地雷数量
    public int getMineCount() {
        return mineCount;
    }
    // 判断坐标是否在棋盘内
    public boolean isInside ( int row , int col ){
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
    // 获取指定坐标的格子
    public Cell getCell ( int row , int col ){
        if(!isInside( row , col )){
            throw new IllegalArgumentException ("坐标超出了棋盘范围！");
        }
        return cells[row][col];
    }

    public void placeMine(int row , int col ) {
        getCell(row, col).setMine(true);
    }

    public int countAdjacentMines (int row , int col ){
        if(!isInside (  row ,  col)) {
            throw new IllegalArgumentException ("坐标超出了棋盘范围！");
        }

        int count = 0 ;
        for ( int rowOffset = -1 ; rowOffset <= 1 ; rowOffset ++){
            for (int colOffset = -1 ; colOffset <= 1 ; colOffset ++){
                if(rowOffset == 0 && colOffset == 0)
                    continue;

                int neighborRow = row + rowOffset;
                int neighborCol = col + colOffset;

                if(isInside(neighborRow, neighborCol) && cells[neighborRow][neighborCol].isMine()){
                    count ++ ;
                }
            }
        }
        return count;
    }

    public void recalculateAdjacentMines () {
        for ( int row = 0 ; row < rows ; row ++){
            for( int col = 0 ; col < cols ; col ++){
                int count = countAdjacentMines(row, col);
                cells[row][col].setAdjacentMines(count);
            }
        }
    }

    public void placeMines ( int firstRow , int firstCol , Random random ) {
        if ( !isInside( firstRow ,  firstCol ) ){
            throw new IllegalArgumentException ("坐标超出了棋盘范围！");
        }
        if (minesPlaced) {
            throw new IllegalStateException("本局布雷已完成。");
        }

        List<Cell> candidates = new ArrayList<>();

        for ( int row = 0 ; row < rows ; row ++ ){
            for ( int col = 0; col < cols ; col ++){
                if ( row == firstRow && col == firstCol ){
                    continue ;
                }

                candidates.add(cells [row][col]);
            }
        }
        Collections.shuffle(candidates,random);

        for (int i = 0 ; i < mineCount ; i ++) {
            candidates.get(i).setMine(true);
        }
        recalculateAdjacentMines();
        minesPlaced = true;
    }

    public boolean isMinesPlaced (){
        return minesPlaced;
    }

    public int countMines(){
        int count = 0;
        for(int row = 0 ; row < rows ; row ++){
            for(int col = 0 ; col < cols ; col ++){
                if (cells[row][col].isMine()){
                    count ++ ;
                }
            }
        }
        return count;
    }

    public void revealAllMines () {

        for (int row = 0; row < rows; row++) {        // 遍历全部行
            for (int col = 0; col < cols; col++) {    // 遍历全部列
                Cell cell = cells[row][col];         // 取得当前格子

                if (cell.isMine()) {                 // 当前格是地雷
                    cell.setState(CellState.REVEALED); // 公开地雷位置
                }
            }
        }
    }
}
