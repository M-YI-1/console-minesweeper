package com.echo.minesweeper.service;
import com.echo.minesweeper.model.Cell;
import com.echo.minesweeper.model.CellState; // 修改格子状态
import java.util.LinkedList;                 // 使用BFS队列
import com.echo.minesweeper.model.Board;
import java.util.*;
public class MinesweeperGame {
    private final Board board;
    private final Random random;
    private final int safeCellCount;
    private int revealedSafeCells;
    private GameStatus status;

    public MinesweeperGame() {
        this(new Board(), new Random());
    }



    public MinesweeperGame(Board board, Random random) {
        this.board = Objects.requireNonNull(board, "棋盘不能为空");
        this.random = Objects.requireNonNull(random, "随机对象不能为空");

        this.safeCellCount = board.getRows() * board.getCols()
                - board.getMineCount();             // 非雷格总数
        this.revealedSafeCells = 0;                 // 开局尚未翻开安全格
        this.status = GameStatus.READY;             // 游戏处于准备状态
    }

    public Board getBoard() {
        return board;
    }

    public ActionResult openCell(int row, int col) {
        if (isGameOver()) {
            return ActionResult.GAME_OVER;
        }

        if (!board.isInside(row, col)) {
            return ActionResult.OUT_OF_BOUNDS;
        }

        Cell cell = board.getCell(row, col);

        if(cell.isFlagged()) {
            return ActionResult.FLAGGED;
        }



        if (cell.isRevealed()) {             // 已翻开或已插旗都不能翻开
            return ActionResult.ALREADY_REVEALED;
        }

        ensureMinesPlaced(row, col);         // 确认允许翻开后才首次布雷
        status = GameStatus.RUNNING;         // 游戏正式开始

        if (cell.isMine()) {                 // 玩家翻到了地雷
            status = GameStatus.LOST;        // 立即进入失败状态
            board.revealAllMines();          // 公开所有地雷位置
            return ActionResult.SUCCESS;
        }

        revealFrom(cell);                    // 展开当前格和可能的空白区
        checkWin();                          // 检查安全格是否全部翻开
        return ActionResult.SUCCESS;
    }


    private void ensureMinesPlaced(int firstRow, int firstCol) {
        if (!board.isInside(firstRow, firstCol))
            throw new IllegalArgumentException("坐标越界");
        if (!board.isMinesPlaced())
            board.placeMines(firstRow, firstCol, random);
    }

    public GameStatus getStatus() {       // 让控制器读取当前状态
        return status;
    }

    public boolean isGameOver() {         // 胜利或失败都表示游戏结束
        return status == GameStatus.WON
                || status == GameStatus.LOST;
    }

    private void revealFrom(Cell startCell) {
        LinkedList<Cell> queue = new LinkedList<>();
        queue.addLast(startCell);

        while (!queue.isEmpty()) {                   // 队列为空时停止
            Cell current = queue.removeFirst();      // 从队头取出一个格子

            if (!current.isCovered()) {              // 已处理、已插旗则跳过
                continue;
            }

            current.setState(CellState.REVEALED);    // 将当前格设为已翻开
            revealedSafeCells++;                     // 安全格计数加1

            if (current.getAdjacentMines() != 0) {
                continue;                            // 数字格是展开边界，不再扩邻
            }

            addNeighborsToQueue(current, queue);     // 空白格继续展开周围8格
        }


    }

    private void addNeighborsToQueue(Cell current, LinkedList<Cell> queue) {
        int row = current.getRow();
        int col = current.getCol();

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }

                int neighborRow = row + rowOffset;   // 计算邻居行号
                int neighborCol = col + colOffset;   // 计算邻居列号

                if (!board.isInside(neighborRow, neighborCol)) {
                    continue;
                }

                Cell neighbor = board.getCell(neighborRow, neighborCol);

                if (neighbor.isCovered()) {          // 只处理尚未翻开的邻居
                    queue.addLast(neighbor);         // 将邻居加入队尾
                }
            }
        }
    }

    public ActionResult flagCell (int row , int col) {
        if(isGameOver()){
            return ActionResult.GAME_OVER ;
        }

        if(!board.isInside ( row , col )) {
            return ActionResult.OUT_OF_BOUNDS ;
        }

        Cell cell = board.getCell (row , col) ;

        if(cell.isRevealed()) {
            return ActionResult.ALREADY_REVEALED ;
        }

        if(cell.isFlagged ()) {
            cell.setState(CellState.COVERED);
        }else {
            cell.setState(CellState.FLAGGED);
        }

        return ActionResult.SUCCESS;
    }

    private void checkWin() {
        if (revealedSafeCells == safeCellCount) {    // 所有安全格都翻开了
            status = GameStatus.WON;                 // 判定游戏胜利
        }
    }

}

