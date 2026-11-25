package com.echo.minesweeper.util;

public class Command {

    public enum Type {
        OPEN,    // 翻开格子
        FLAG,    // 切换插旗
        QUIT,    // 退出游戏
        INVALID  // 无法解析的输入
    }

    private final Type type;        // 命令类型
    private final int row;          // 内部行索引0～8
    private final int col;          // 内部列索引0～8
    private final String message;   // 无效命令的提示

    private Command(Type type, int row, int col, String message) {
        this.type = type;          // 保存命令类型
        this.row = row;            // 保存内部行索引
        this.col = col;            // 保存内部列索引
        this.message = message;    // 保存命令提示
    }

    public static Command open(int row, int col) {
        return new Command(Type.OPEN, row, col, null);
    }

    public static Command flag(int row ,int col) {
        return new Command(Type.FLAG , row , col, null);
    }

    public static Command quit () {
        return new Command(Type.QUIT, -1, -1, null);
    }

    public static Command invalid (String message) {
        return new Command(Type.INVALID, -1, -1, message);
    }

    public Type getType() {        // 取得命令类型
        return type;
    }

    public int getRow() {          // 取得内部行索引
        return row;
    }

    public int getCol() {          // 取得内部列索引
        return col;
    }

    public String getMessage() {   // 取得错误提示
        return message;
    }
}
