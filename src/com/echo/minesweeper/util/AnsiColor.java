package com.echo.minesweeper.util;

public enum AnsiColor {
    RESET("\u001B[0m"),               // 恢复终端默认颜色
    BLUE("\u001B[34m"),               // 数字1使用蓝色
    GREEN("\u001B[32m"),              // 数字2使用绿色
    RED("\u001B[31m"),                // 数字3使用红色
    PURPLE("\u001B[35m"),             // 数字4使用紫色
    YELLOW("\u001B[33m"),             // 数字5使用黄色
    CYAN("\u001B[36m"),               // 数字6使用青色
    WHITE("\u001B[37m"),              // 数字7使用白色
    GRAY("\u001B[90m");               // 数字8使用灰色

    private final String code;

    AnsiColor(String code) {
        this.code = code;
    }

    public String colorize (String text) {
        return code + text + RESET.code;
    }

    public static AnsiColor forNumber(int number) {
        switch (number) {
            case 1:
                return BLUE;              // 1显示蓝色
            case 2:
                return GREEN;             // 2显示绿色
            case 3:
                return RED;               // 3显示红色
            case 4:
                return PURPLE;            // 4显示紫色
            case 5:
                return YELLOW;            // 5显示黄色
            case 6:
                return CYAN;              // 6显示青色
            case 7:
                return WHITE;             // 7显示白色
            case 8:
                return GRAY;              // 8显示灰色
            default:
                return RESET;             // 其他情况不使用颜色
        }
    }
}
