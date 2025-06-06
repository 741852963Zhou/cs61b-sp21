package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    /**
     * 将棋盘向 SIDE 方向倾斜。如果此操作改变了棋盘，则返回 true。
     *
     * 规则:
     * 1. 瓷砖尽可能地滑动。
     * 2. 如果两个相邻（在移动方向上）的瓷砖具有相同的值，
     * 它们会合并。
     * 3. 一个合并后的瓷砖在同一次倾斜中不能再次合并。
     * 4. 合并会增加分数。
     * 5. 对于三个相同值的瓷砖，移动方向上更远的瓷砖会先合并。
     */
    public boolean tilt(Side side) {
        boolean changed = false; // 跟踪变化的唯一方法。
        board.setViewingPerspective(side); // 设置棋盘视角，这是一个好主意，保留它。

        // 处理每一列（因为我们总是朝北看）
        for (int c = 0; c < board.size(); c++) {
            if (processColumn(c)) {
                changed = true; // 如果 *任何* 列发生变化，则棋盘发生变化。
            }
        }

        board.setViewingPerspective(Side.NORTH); // 重置视角，很好。

        checkGameOver(); // 在移动 *之后* 检查游戏是否结束。

        if (changed) {
            setChanged(); // 仅在需要时通知观察者。
        }
        return changed;
    }

    /**
     * 假设视角为北（NORTH），处理单一一列 (c)。
     * 瓷砖向上移动（朝向 size - 1）。
     * 如果列被改变，则返回 true。
     */
    private boolean processColumn(int c) {
        boolean columnChanged = false; // 标志位，跟踪此列是否发生变化。
        // 跟踪某个行的瓷砖是否在 *此轮* 倾斜中 *已经* 合并过。
        // 这是一个局部变量，不是糟糕的实例变量。
        boolean[] merged = new boolean[board.size()];

        // 我们从第二顶行（size - 2）向下迭代。
        // 为什么？因为下面的瓷砖需要根据上面的情况来决定去哪里。
        for (int r = board.size() - 2; r >= 0; r--) {
            Tile t = board.tile(c, r); // 获取当前位置的瓷砖。

            if (t == null) {
                continue; // 没有瓷砖可以移动。
            }

            int targetRow = r; // 从当前行开始。

            // --- 步骤 1: 滑动 (规则 1) ---
            // 找到这个瓷砖能滑到的最高行。
            // 当它碰到顶部或另一个瓷砖时停止。
            while (targetRow + 1 < board.size() && board.tile(c, targetRow + 1) == null) {
                targetRow++;
            }

            // --- 步骤 2: 检查合并 (规则 2 & 3) ---
            // 现在，`targetRow` 是最高的空位（或当前位置）。
            // 让我们检查是否可以与它 *上方* 的瓷砖合并。
            int nextRow = targetRow + 1; // 上方的行。
            if (nextRow < board.size()) { // 检查是否在棋盘内。
                Tile above = board.tile(c, nextRow); // 获取上方的瓷砖。
                // 检查合并条件：值相同 并且 上方的瓷砖 *尚未* 合并。
                if (above.value() == t.value() && !merged[nextRow]) {
                    // --- 执行合并 ---
                    board.move(c, nextRow, t); // 移动并合并。
                    score += t.value() * 2; // 更新分数 (规则 4)。
                    merged[nextRow] = true;   // 标记为已合并 (规则 3)。
                    columnChanged = true;     // 棋盘已改变。
                    continue; // 处理这个瓷砖完毕，继续下一个。
                }
            }

            // --- 步骤 3: 移动 (如果没合并但滑动了) ---
            // 如果我们没有合并，但可以向上移动（`targetRow` 不是原来的 `r`）。
            if (targetRow != r) {
                // 是的，移动到最高的空位。
                board.move(c, targetRow, t);
                columnChanged = true; // 棋盘已改变。
            }
            // 如果我们没有合并也没有移动，什么也不发生，changed 保持 false。
        }
        return columnChanged; // 返回此列是否发生了变化。
    }





    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for(int i = 0;i < b.size(); i++) {
            for(int j = 0;j < b.size();j++) {
                if(b.tile(i,j) == null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for(int i = 0;i < b.size(); i++) {
            for(int j = 0;j < b.size();j++) {
                if(b.tile(i,j) == null)
                {
                    continue;
                }
                if(b.tile(i,j).value() == MAX_PIECE)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function
        if(help_exists_check(0,0, b,0))
        {
            return true;
        }
        return false;
    }
    private static boolean help_exists_check(int i,int j,Board b,int check_value){
        if(b.tile(i,j) == null || b.tile(i,j).value() == check_value) {
            return true;
        }
        else{
            boolean result1 = false,result2 = false;
            if(i+1 < b.size()) {
                result1 = help_exists_check(i+1,j, b,b.tile(i,j).value());
            }
            if(j+1 < b.size()){
                result2 = help_exists_check(i,j+1, b,b.tile(i,j).value());
            }
            if(result1 || result2){
                return true;
            }
            else{
                return false;
            }
        }
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}

