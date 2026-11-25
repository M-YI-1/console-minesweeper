package com.echo.minesweeper.model;

public enum Difficulty {

    BEGINNER(                         // 初级难度
            "初级",
            9,                        // 9行
            9,                        // 9列
            10                        // 10颗雷
    ),
    INTERMEDIATE(                     // 中级难度
            "中级",
            16,                       // 16行
            16,                       // 16列
            40                        // 40颗雷
    ),
    EXPERT(                           // 高级难度
            "高级",
            16,                       // 16行
            30,                       // 30列
            99                        // 99颗雷
    );

    private final String displayName; // 面板显示名称
    private final int rows;           // 棋盘行数
    private final int cols;           // 棋盘列数
    private final int mineCount;      // 地雷数量

    Difficulty(String displayName, int rows, int cols, int mineCount) {
        this.displayName = displayName;
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
    }

    public String getDisplayName() {
        return displayName;           // 返回中文难度名称
    }

    public int getRows() {
        return rows;                  // 返回本难度行数
    }

    public int getCols() {
        return cols;                  // 返回本难度列数
    }

    public int getMineCount() {
        return mineCount;             // 返回本地雷数量
    }

    public String getDescription() {
        return rows + "×" + cols + "，" + mineCount + "颗雷";
    }
}

