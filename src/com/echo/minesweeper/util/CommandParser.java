package com.echo.minesweeper.util;
import java.util.Locale ;

public class CommandParser {

    public Command parse(String line, int rows, int cols) {
        if (line == null || line.trim().isEmpty()) {
            return Command.invalid("输入不能为空，请重新输入！"); // 处理空行
        }

        String[] parts = line.trim().split("\\s+");
        String action = parts[0].toLowerCase(Locale.ROOT);

        if (parts.length == 1 && (action.equals("q") || action.equals("quit"))) {
            return Command.quit();
        }

        if (parts.length != 3) {
            return Command.invalid("命令格式应为open 行 列 或者 flag 行 列");
        }

        if (!(action.equals("open") || action.equals("o")
                || action.equals("flag") || action.equals("f"))) {
            return Command.invalid("未知操作，只支持 open、flag 和 quit"); // 检查操作名
            //
        }

        int displayRow;                                // 保存玩家输入的行号
        int displayCol;                                // 保存玩家输入的列号

        try {
            displayRow = Integer.parseInt(parts[1]);
            displayCol = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return Command.invalid("行号和列号必须是整数");
        }

        if (displayRow < 1 || displayRow > rows
                || displayCol < 1 || displayCol > cols) {
            return Command.invalid(
                    "坐标范围必须是1到" + rows + "行，1到" + cols + "列"
            );                                          // 在转数组索引前检查范围
        }

        int row = displayRow - 1 ;
        int col = displayCol - 1 ;

        if (action.equals("open") || action.equals("o")) {
            return Command.open(row, col);
        }

        return Command.flag(row, col);

    }
}
