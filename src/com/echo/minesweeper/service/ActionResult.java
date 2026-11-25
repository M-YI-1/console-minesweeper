package com.echo.minesweeper.service;

public enum ActionResult {

    SUCCESS,            // 操作成功
    GAME_OVER,          // 游戏已经胜利或失败
    OUT_OF_BOUNDS,      // 坐标越界
    ALREADY_REVEALED,   // 格子已经翻开
    FLAGGED             // 格子已经插旗，不能直接翻开
}
