package com.echo.minesweeper.view;

import com.echo.minesweeper.model.Board;      // 需要读取棋盘和格子
import com.echo.minesweeper.model.Cell;       // 需要读取格子状态
import com.echo.minesweeper.model.Difficulty;
import com.echo.minesweeper.service.GameStatus;
import com.echo.minesweeper.util.AnsiColor;

public class ConsoleView {

    private static final int CELL_WIDTH = 4; // 表头、行号和每个格子统一宽度
    private final boolean ansiEnabled;

    public ConsoleView () {
        this(System.getenv("NO_COLOR") == null) ;
    }

    public ConsoleView (boolean ansiEnabled) {
        this.ansiEnabled = ansiEnabled;
    }
    public void showWelcome () {
        System.out.println("========== 控制台扫雷 =========="); // 显示标题
        System.out.println("翻开格子：open 行 列，或 o 行 列"); // 操作说明
        System.out.println("切换插旗：flag 行 列，或 f 行 列"); // 插旗说明
        System.out.println("退出游戏：quit，或 q");             // 退出说明
    }

    public void showBoard (Board board) {
        StringBuilder output = new StringBuilder();

        output.append(System.lineSeparator());
        output.append(" ".repeat(CELL_WIDTH));

        for (int col = 1 ; col <= board.getCols() ; col++) {
            output.append(columnHeader(col));
        }

        output.append(System.lineSeparator());

        for (int row = 0 ; row < board.getRows() ; row++){
            output.append(String.format("%2d  ",row + 1));

            for(int col = 0 ; col < board.getCols() ; col++){
                Cell cell = board.getCell(row,col);
                output.append(cellText(cell));
            }
            output.append(System.lineSeparator());
        }

        System.out.println(output);
    }

    private String columnHeader(int col) {
        String number = String.valueOf(col);  // 把列号转换为字符串
        int totalPadding = CELL_WIDTH - number.length(); // 每列固定宽度
        int leftPadding = totalPadding / 2;     // 左侧空格
        int rightPadding = totalPadding - leftPadding; // 右侧空格

        return " ".repeat(leftPadding)
                + number
                + " ".repeat(rightPadding);
    }

    private String cellText (Cell cell) {
        if (cell.isFlagged()) {
            return "[F] ";
        }
        if (!cell.isRevealed()){
            return "[?] ";
        }
        if (cell.isMine()) {
            return "[*] ";
        }
        int count = cell.getAdjacentMines();

        if(count == 0) {
            return "[ ] " ;
        }

        String numberText = "[" + count + "] " ;
        if(!ansiEnabled) return numberText;

        return AnsiColor.forNumber(count).colorize(numberText);
    }


    public void showDifficultyMenu () {

        System.out.println();
        System.out.println("请选择难度：");

        Difficulty[] difficulties = Difficulty.values();

        for (int i = 0; i < difficulties.length; i++) {
            Difficulty difficulty = difficulties[i];

            System.out.println(
                    (i + 1) + ". "
                            + difficulty.getDisplayName()
                            + "："
                            + difficulty.getDescription()
            );
        }
    }

    public void showDifficultyPrompt () {
        System.out.println("请输入难度编号：");
        System.out.flush();
    }

    public void showGameInfo (Difficulty difficulty , long elapsedSeconds) {
        System.out.println();
        System.out.println("当前难度:" + difficulty.getDisplayName() + " | 当前用时：" + elapsedSeconds + "秒");
    }

    public void showPrompt () {
        System.out.print("请输入命令：");
        System.out.flush();              // 立即显示提示并等待玩家输入
    }
    public void showError (String message) {
        System.out.println("错误：" + message);
    }

    public void showGameResult (GameStatus status,
                                Difficulty difficulty,
                                long elapsedSeconds) {

        System.out.println();
        System.out.println("========== 游戏结果 ==========");
        System.out.println("难度：" + difficulty.getDisplayName());

        if (status == GameStatus.WON) {
            System.out.println("结果：胜利");
            System.out.println("所有非地雷格均已翻开。");
        } else if (status == GameStatus.LOST) {
            System.out.println("结果：失败");
            System.out.println("所有地雷位置已经用[*]标出。");
        }

        System.out.println("本局用时：" + elapsedSeconds + "秒");

    }


}
