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
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        /** 如果我要从向上移动开始，那么需要考虑什么？  (1)怪异的合并->向该方向运动时遍历该方向 如果“碰壁”(不同值或者已经合并过)，回退到上一个 null
         *                                     (2)如何将向上的情况推及到所有方向？->   ok
         *                                     (3)分数如何更新？(也许可以放到后面考虑)-> *2? ->怎么合并
         *                                     (4)如何获取最后的位置？*/
        this.board.setViewingPerspective(side);
        String beforeBoard = this.toString();
        checkGameOver();
        DealAllRow();
        String afterBoard = this.toString();
        // 这里的 changed 变量是用来判断是否发生了变化
        // 通过比较 beforeBoard 和 afterBoard

        if (!beforeBoard.equals(afterBoard)) {
            changed = true;
        }
        this.board.setViewingPerspective(Side.NORTH);
        if (changed) {
            setChanged();
        }
        return changed;
    }
    /** 处理所有列 */
    private void DealAllRow(){
        for(int i = 0;i <= size() - 1;i++){
            // 在这里初始化 CheckArr 数组
            CheckArr = new int[board.size()];
            DealOneRow(i);
        }
    }
    /**重新编写 这是一个处理向上情况的对于单列的函数*/
    private void DealOneRow(int i){
        for(int j = size()-2;j >= 0;j--) {
            //处理完了该处位置为null的情况
            if(CheckNull(i,j)){
                continue;
            }
            //处理完了向上全为null的情况
            if(CheckRowNull(i,j)){
                board.move(i,size()-1,board.tile(i,j));
                continue;
            }
            //处理完了向上有不同值的tile的情况
            int TargeRow = ReturnRow(i,j);
            if(NotEqualTile(i,j)){
                board.move(i,TargeRow-1,board.tile(i,j));
            }
            //处理值相同的情况->(1)前面已经发生了合并，(1)前面没发生合并
            else{
                if(CheckArr[TargeRow] == 0){
                    score += board.tile(i,j).value() * 2;
                    CheckArr[TargeRow] = 1;
                    board.move(i,TargeRow,board.tile(i,j));
                }
                else{
                    board.move(i,TargeRow-1,board.tile(i,j));
                }
            }
        }
    }
    /** 检查某个位置是否为null*/
    private boolean CheckNull(int i ,int j){
        return board.tile(i, j) == null;
    }

    /** ��上检查是否全为null*/
    //现在有了一个可以判断某个滑块上方是否都为空的函数
    private boolean CheckRowNull(int i,int j){
        while(j < size() - 1){
            if(board.tile(i,j+1) != null){
                return false;
            }
            else{j +=1;}
        }
        return true;
    }
    /** 向上检查有“墙壁”,应该返回"墙壁"的坐标值*/
    private int ReturnRow(int i,int j){
        while(j < size() - 1){
            if(board.tile(i,j+1) != null){
                break;
                //此时的（i,j）是“墙壁”的坐标，处理
            }
            else{j += 1;}
        }
        return j+1;
    }
    /** 已经无需考虑该值为null，或者向上有null的情况 */
    private boolean NotEqualTile(int i,int j){
        return board.tile(i, j).value() != board.tile(i, ReturnRow(i, j)).value();
    }
    /**处理值相同的情况->(1)前面已经发生了合并，(2)前面没发生合并
     * 创建一个能够记录是否发生过合并的函数，这个函数在每列调用时候更新*/
    private int[] CheckArr;  // 只声明，不立即初始化
    /** 创建一个函数使得如果有发生合并，修改CheckArr*/
    private void ChangeCheckArr(int j){
        CheckArr[j] = 1;
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

