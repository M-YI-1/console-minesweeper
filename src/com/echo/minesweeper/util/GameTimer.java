package com.echo.minesweeper.util;

import java.util.concurrent.TimeUnit;

public class GameTimer {

    private long startNanos;
    private long stopNanos;
    private boolean started;
    private boolean running;


    public void start() {
        if (!running) {
            startNanos = System.nanoTime(); // 记录开始时间
            stopNanos = 0;                  // 清除上一次停止时间
            started = true;                 // 标记计时器已启动
            running = true;                 // 标记当前正在运行
        }
    }

    public void stop() {
        if (running) {
            stopNanos = System.nanoTime();
            running = false;
        }
    }

    public long elapsedSeconds() {
        if (!started) {
            return 0;                       // 尚未开始则显示0秒
        }

        long endNanos = running ? System.nanoTime() : stopNanos;

        long elapsedNanos = Math.max(0, endNanos - startNanos);
        return TimeUnit.NANOSECONDS.toSeconds(elapsedNanos);
    }
}