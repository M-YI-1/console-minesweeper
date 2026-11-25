package com.echo.minesweeper.util;



public class CommandParserTest {
    private static final CommandParser PARSER = new CommandParser();

    public static void main(String[] args) {
        expectOpen("open 1 1", 0, 0);
        expectOpen("o 9 9", 8, 8);
        expectFlag("flag 2 3", 1, 2);
        expectFlag("f 9 1", 8, 0);
        expectQuit("quit");
        expectQuit("q");

        expectInvalid(null);
        expectInvalid("");
        expectInvalid("   ");
        expectInvalid("abc");
        expectInvalid("open");
        expectInvalid("open 1");
        expectInvalid("open 1 a");
        expectInvalid("open a 1");
        expectInvalid("open 0 1");
        expectInvalid("open 1 0");
        expectInvalid("open 10 1");
        expectInvalid("open 1 10");
        expectInvalid("open -1 1");
        expectInvalid("open 1 -1");
        expectInvalid("open 1 1 2");
        expectInvalid("open 999999999999999999999 1");
        expectInvalid("foo 1 1");
        expectInvalid("quit 1 1");

        System.out.println("CommandParserTest: 全部测试通过");
    }

    private static void expectOpen(String input, int row, int col) {
        Command command = PARSER.parse(input, 9, 9);

        if (command.getType() != Command.Type.OPEN
                || command.getRow() != row
                || command.getCol() != col) {
            throw new AssertionError("翻开命令解析错误：" + input);
        }
    }

    private static void expectFlag(String input, int row, int col) {
        Command command = PARSER.parse(input, 9, 9);

        if (command.getType() != Command.Type.FLAG
                || command.getRow() != row
                || command.getCol() != col) {
            throw new AssertionError("插旗命令解析错误：" + input);
        }
    }

    private static void expectQuit(String input) {
        Command command = PARSER.parse(input, 9, 9);

        if (command.getType() != Command.Type.QUIT) {
            throw new AssertionError("退出命令解析错误：" + input);
        }
    }

    private static void expectInvalid(String input) {
        Command command = PARSER.parse(input, 9, 9);

        if (command.getType() != Command.Type.INVALID) {
            throw new AssertionError("非法输入没有被拦截：" + input);
        }
    }
}
