package com.echo.minesweeper.controller;

import java.util.Scanner;

import com.echo.minesweeper.service.MinesweeperGame;
import com.echo.minesweeper.util.Command;
import com.echo.minesweeper.view.ConsoleView;
import com.echo.minesweeper.util.CommandParser;
import com.echo.minesweeper.service.ActionResult;
import com.echo.minesweeper.model.Board;
import com.echo.minesweeper.model.Difficulty;
import com.echo.minesweeper.service.GameStatus;
import com.echo.minesweeper.util.GameTimer;

import java.util.Random;


public class GameController {

    private final Scanner scanner ;
    private final ConsoleView view ;
    private final CommandParser parser ;
    private final GameTimer timer; // 记录从首次翻开到游戏结束的秒数

    public GameController() {
        this.scanner = new Scanner(System.in);       // 使用标准控制台输入
        this.parser = new CommandParser();           // 创建命令解析器
        this.view = new ConsoleView();
        this.timer = new GameTimer();
    }

    public void run() {
        view.showWelcome();                         // 显示基本操作说明

        Difficulty difficulty = chooseDifficulty(); // 读取玩家选择的难度

        if (difficulty == null) {
            return;                                 // 玩家取消或输入结束
        }

        Board board = new Board(
                difficulty.getRows(),               // 使用难度对应行数
                difficulty.getCols(),               // 使用难度对应列数
                difficulty.getMineCount()           // 使用难度对应雷数
        );

        MinesweeperGame game = new MinesweeperGame(
                board,
                new Random()                        // 当前游戏使用的随机对象
        );

        while (!game.isGameOver()) {
            view.showGameInfo(
                    difficulty,
                    timer.elapsedSeconds()
            );                                      // 显示难度和实时秒数

            view.showBoard(board);                  // 显示当前棋盘
            view.showPrompt();                      // 提示输入命令

            if (!scanner.hasNextLine()) {
                System.out.println();
                return;                             // 输入结束则安全退出
            }

            String line = scanner.nextLine();       // 完整读取一行命令

            Command command = parser.parse(
                    line,
                    board.getRows(),
                    board.getCols()
            );                                      // 使用当前棋盘范围解析

            switch (command.getType()) {
                case OPEN:
                    handleOpen(game, command);       // 翻开并管理计时
                    break;

                case FLAG:
                    handleFlag(game, command);       // 插旗
                    break;

                case QUIT:
                    timer.stop();                    // 退出时停止计时
                    System.out.println("游戏已退出。");
                    return;

                case INVALID:
                    String message = command.getMessage();
                    view.showError(
                            message == null ? "输入格式错误" : message
                    );
                    break;

                default:
                    view.showError("未知命令！");
                    break;
            }
        }

        view.showBoard(board);                       // 显示最终棋盘

        view.showGameResult(
                game.getStatus(),                   // 第一个参数：游戏状态
                difficulty,                         // 第二个参数：玩家选择的难度
                timer.elapsedSeconds()              // 第三个参数：最终秒数
        );


    }

    private Difficulty chooseDifficulty() {
        view.showDifficultyMenu();

        while (true) {
            view.showDifficultyPrompt();

            if (!scanner.hasNextLine()) {
                System.out.println();
                return null;                         // 输入结束则退出
            }
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    return Difficulty.BEGINNER;       // 9×9，10颗雷

                case "2":
                    return Difficulty.INTERMEDIATE;   // 16×16，40颗雷

                case "3":
                    return Difficulty.EXPERT;         // 16×30，99颗雷

                case "q":
                case "quit":
                    System.out.println("已取消游戏。");
                    return null;                     // 玩家取消选择

                default:
                    view.showError("难度只能输入1、2、3或quit");
                    break;
            }

        }
    }

    private void handleOpen (MinesweeperGame game, Command command) {
        GameStatus previousStatus = game.getStatus();

        ActionResult result = game.openCell(
                command.getRow(),
                command.getCol()
        );

        if (result == ActionResult.SUCCESS
                && previousStatus == GameStatus.READY) {
            timer.start();                           // 首次有效翻开时开始计时
        }

        if (game.isGameOver()) {
            timer.stop();                            // 踩雷或胜利时停止计时
        }

        showOpenFailure(result);
    }

    private void handleFlag(MinesweeperGame game, Command command) {
        ActionResult result = game.flagCell(
                command.getRow(),
                command.getCol()
        );                                           // 执行插旗操作

        showFlagFailure(result);                     // 显示失败原因
    }

    private void showOpenFailure(ActionResult result) {
        switch (result) {
            case SUCCESS:
                break;                        // 成功时无需提示

            case GAME_OVER:
                view.showError("游戏已经结束，不能再翻开格子");
                break;

            case OUT_OF_BOUNDS:
                view.showError("坐标越界，请输入当前棋盘范围内的坐标");
                break;

            case ALREADY_REVEALED:
                view.showError("该格子已经翻开，不能重复操作");
                break;

            case FLAGGED:
                view.showError("该格子已经插旗，请先取消旗帜");
                break;
        }
    }

    private void showFlagFailure(ActionResult result) {
        switch (result) {
            case SUCCESS:
                break;                        // 插旗成功无需提示

            case GAME_OVER:
                view.showError("游戏已经结束，不能再插旗");
                break;

            case OUT_OF_BOUNDS:
                view.showError("坐标越界，请输入当前棋盘范围内的坐标");
                break;

            case ALREADY_REVEALED:
                view.showError("已经翻开的格子不能插旗");
                break;

            case FLAGGED:
                break;                        // 当前插旗操作不会返回该结果
        }
    }



}
