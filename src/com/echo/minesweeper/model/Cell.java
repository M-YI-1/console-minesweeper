package com.echo.minesweeper.model;

import java.util.Objects;

public class Cell {
    private final int row ;
    private final int col ;
    private boolean mine ;
    private int adjacentMines ;
    private CellState state ;

    public Cell(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("格子坐标不能为负 ！");
        }

        this.row = row;
        this.col = col;
        this.mine = false;
        this.adjacentMines = 0;
        this.state = CellState.COVERED;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isMine() {
        return mine;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public CellState getState() {
        return state;
    }

    public boolean isCovered () {
        return state == CellState.COVERED;
    }

    public boolean isRevealed () {
        return state == CellState.REVEALED;
    }

    public boolean isFlagged () {
        return state == CellState.FLAGGED;
    }

    void setMine( boolean mine) {
        this.mine = mine;
    }

    void setAdjacentMines ( int adjacentMines ){
        if(adjacentMines < 0 || adjacentMines > 8){
            throw new IllegalArgumentException("相邻地雷数不能小于0或大于8！");
        }
        this.adjacentMines = adjacentMines ;
    }

    public void setState ( CellState state) {
        this.state = Objects.requireNonNull(state , "状态不能为空！");
    }
}
