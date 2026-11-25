# Console Minesweeper

回声实验室 2026 年 Java 方向招新阶段性考核项目。

## 项目名称与选题

- 项目名称：Console Minesweeper
- 选题：控制台扫雷游戏（Minesweeper）
- 运行环境：标准控制台或终端

项目支持首击安全、翻开、插旗、空白区域级联展开、胜负判定、输入校验、ANSI 数字颜色、计时和自定义难度。

## 功能说明

### 基础规则

- 默认初级棋盘为 `9×9`，包含 `10` 颗地雷。
- 每个格子具有未翻开、已翻开和已插旗三种状态。
- 第一次有效翻开后才动态布雷，首击位置永远不会是地雷。
- 翻开周围雷数为 `0` 的格子时，使用 BFS 自动展开相邻区域。
- 已翻开格不能重复翻开或插旗。
- 已插旗格不能直接翻开，需要先取消旗帜。
- 踩雷后游戏结束，并公开全部地雷位置。
- 所有非地雷格都翻开后，游戏胜利。

### 难度

| 编号 | 难度 | 棋盘 | 地雷 |
|---|---|---|---:|
| 1 | 初级 | 9 行 × 9 列 | 10 |
| 2 | 中级 | 16 行 × 16 列 | 40 |
| 3 | 高级 | 16 行 × 30 列 | 99 |

高级难度采用经典扫雷尺寸，在较窄的控制台中可能需要横向滚动。

### 附加功能

- 首次有效翻开开始计时，胜利或失败时停止。
- 数字使用 ANSI 颜色区分：
  - `1`：蓝色
  - `2`：绿色
  - `3`：红色
  - `4～8`：紫色、黄色、青色、白色、灰色
- 设置环境变量 `NO_COLOR` 可以关闭颜色。

## 操作命令

| 操作 | 完整命令 | 简写 |
|---|---|---|
| 翻开格子 | `open 行 列` | `o 行 列` |
| 切换插旗 | `flag 行 列` | `f 行 列` |
| 退出游戏 | `quit` | `q` |

坐标从 `1` 开始。例如，初级棋盘左上角为：

```text
open 1 1
```

## 系统架构

```text
src
└── com
    └── echo
        └── minesweeper
            ├── App.java
            ├── controller
            │   └── GameController.java
            ├── model
            │   ├── Board.java
            │   ├── Cell.java
            │   ├── CellState.java
            │   └── Difficulty.java
            ├── service
            │   ├── ActionResult.java
            │   ├── GameStatus.java
            │   └── MinesweeperGame.java
            ├── util
            │   ├── AnsiColor.java
            │   ├── Command.java
            │   ├── CommandParser.java
            │   ├── CommandParserTest.java
            │   └── GameTimer.java
            └── view
                └── ConsoleView.java
```

### 主要类职责

| 类 | 所属包 | 职责 |
|---|---|---|
| `App` | `com.echo.minesweeper` | 程序入口，只负责创建并启动控制器 |
| `GameController` | `controller` | 读取输入、选择难度、调用业务层和控制游戏循环 |
| `Board` | `model` | 保存棋盘尺寸、格子和地雷数量，执行布雷与相邻雷数计算 |
| `Cell` | `model` | 保存单个格子的坐标、地雷、相邻雷数和状态 |
| `CellState` | `model` | 定义格子状态枚举 |
| `Difficulty` | `model` | 保存初级、中级和高级难度配置 |
| `MinesweeperGame` | `service` | 实现翻开、插旗、BFS 级联展开和胜负判定 |
| `GameStatus` | `service` | 定义准备、运行、胜利和失败状态 |
| `ActionResult` | `service` | 表达操作成功、坐标越界、已翻开、已插旗等结果 |
| `ConsoleView` | `view` | 显示难度菜单、棋盘、提示、颜色、计时和游戏结果 |
| `Command` | `util` | 保存解析后的命令 |
| `CommandParser` | `util` | 解析并校验控制台命令 |
| `GameTimer` | `util` | 使用单调时钟统计一局游戏的秒数 |
| `AnsiColor` | `util` | 提供数字对应的 ANSI 颜色 |
| `CommandParserTest` | `util` | 手工执行命令解析测试 |

`model` 不负责输入输出，`view` 不修改游戏数据，`service` 不直接打印棋盘，`controller` 只协调各模块。

## 编译与运行

### 环境要求

- JDK 21
- PowerShell、命令提示符或 IDEA 内置终端

### 编译全部源码

在项目根目录执行：

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem src -Recurse -Filter *.java).FullName
```

### 运行游戏

```powershell
java -cp out com.echo.minesweeper.App
```

### 运行命令解析测试

正式源码和测试类位于同一来源目录，可以执行：

```powershell
java -cp out com.echo.minesweeper.util.CommandParserTest
```

预期输出：

```text
CommandParserTest: 全部测试通过
```

### 关闭 ANSI 颜色

在 PowerShell 中执行：

```powershell
$env:NO_COLOR="1"
java -cp out com.echo.minesweeper.App
```

## 棋盘显示

每个格子的可见宽度固定为四个字符：

| 内容 | 含义 |
|---|---|
| `[?] ` | 未翻开 |
| `[F] ` | 已插旗 |
| `[ ] ` | 已翻开且周围没有雷 |
| `[1] ` 到 `[8] ` | 已翻开且显示相邻雷数 |
| `[*] ` | 游戏失败后公开的地雷 |

ANSI 控制代码不会占用可见宽度，因此开启颜色后仍保持行列对齐。

## 输入校验

程序使用 `Scanner.nextLine()` 读取完整的一行，不直接使用 `Scanner.nextInt()`。

`CommandParser` 会处理：

- 空输入和纯空格。
- 参数数量错误。
- 未知操作。
- 字母坐标。
- 超大整数。
- 零、负数和超出当前棋盘范围的坐标。
- `NumberFormatException`。

游戏服务还会检查格子状态，避免重复翻开、重复插旗和操作已结束的游戏。

## 开发难点与解决体会

### 首击安全

如果启动程序时立即随机布雷，就无法保证第一次翻开的格子安全。项目将布雷延迟到第一次有效翻开之后，并从候选位置中排除首击坐标，再打乱候选列表选择固定数量的地雷。

### 空白区域展开

级联展开使用 `LinkedList` 实现 BFS。只有周围雷数为 `0` 的格子才继续扩展邻居，数字格作为展开边界。处理每个格子前检查是否仍未翻开，避免重复处理插旗格和已翻开格。

### 坐标转换

玩家输入从 `1` 开始，二维数组索引从 `0` 开始。项目在命令解析阶段统一完成范围检查和 `-1` 转换，游戏层只接收内部索引。

### 输入健壮性

所有整数转换都放在 `try-catch` 中，并在访问数组前完成坐标范围校验。错误输入只显示提示，不改变棋盘，也不会导致程序退出。

### 棋盘对齐

表头、行号和每个格子统一为四个可见字符宽。两位数表头会进行居中处理，ANSI 颜色只包裹格子内容，因此不会影响布局。

## Git 与发布

项目根目录已经配置 `.gitignore`，用于忽略：

```text
.idea/
.vscode/
*.iml
bin/
out/
target/
*.class
```

项目使用 `main` 作为主分支，并按项目配置、模型、业务逻辑、工具类和交互层分阶段提交。

公开仓库地址：

```text
https://github.com/M-YI-1/console-minesweeper
```
