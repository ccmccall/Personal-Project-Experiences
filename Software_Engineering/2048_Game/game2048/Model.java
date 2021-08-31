package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /**
     * Current contents of the board.
     */
    private final Board board;
    /**
     * Current score.
     */
    private int score;
    /**
     * Maximum score so far.  Updated when game ends.
     */
    private int maxScore;
    /**
     * True iff game is ended.
     */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /**
     * Largest piece value.
     */
    public static final int MAX_PIECE = 2048;

    /**
     * A new 2048 game on a board of size SIZE with no pieces
     * and score 0.
     */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /**
     * A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes.
     */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /**
     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     * 0 <= COL < size(). Returns null if there is no tile there.
     * Used for testing. Should be deprecated and removed.
     */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /**
     * Return the number of squares on one side of the board.
     * Used for testing. Should be deprecated and removed.
     */
    public int size() {
        return board.size();
    }

    /**
     * Return true iff the game is over (there are no moves, or
     * there is a tile with value 2048 on the board).
     */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /**
     * Return the current score.
     */
    public int score() {
        return score;
    }

    /**
     * Return the current maximum game score (updated at end of game).
     */
    public int maxScore() {
        return maxScore;
    }

    /**
     * Clear the board to empty and reset the score.
     */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /**
     * Add TILE to the board. There must be no Tile currently at the
     * same position.
     */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /**
     * Tilt the board toward SIDE. Return true iff this changes the board.
     * <p>
     * 1. If two Tile objects are adjacent in the direction of motion and have
     * the same value, they are merged into one Tile of twice the original
     * value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     * tilt. So each move, every tile will only ever be part of at most one
     * merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     * value, then the leading two tiles in the direction of motion merge,
     * and the trailing tile does not.
     */
    public boolean tilt(Side side) {
        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        boolean changed;
        changed = false;
        if (atLeastOneMoveExists(this.board)) {
            if (side == side.NORTH) {
                if (tiltNorth(this.board)) {
                changed=true;}
            } else {
                this.board.setViewingPerspective(side);
                if (tiltNorth(this.board)) {
                    changed=true;
                }
                this.board.setViewingPerspective(side.NORTH);
            }
            }
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    /*where last bracket for tilt was originally*/
    }

    public boolean tiltNorth(Board b) { /*write as a boolean? */
        boolean northTiltChanged=false;
        for (int col = 0; col < size(); col += 1) {
            boolean checkHasChanged = applyToFullCol(b, col);
            if (checkHasChanged) {
                northTiltChanged= true;
            }
        }
        return northTiltChanged;
    }

public boolean applyToFullCol(Board b, int col) {  /*also write as a boolean? */
    int[ ] lastTileMerged= new int[1];
    lastTileMerged[0]=200;
    boolean z=false;
    for (int row = 3; row > -1; row -= 1) {
        if (b.tile(col, row)!=null) {    /*if there is a tile in the current space */
            if (checkCanMoveUp(b, col, row)) { /*and it can move up */
                z=true;
                if (checkTileAbove(b, col, row)) {
                    if (checkPotentialMerge(b, col, row, lastTileMerged))  {
                        mergeWithUpperTile(b, col, row, lastTileMerged);
                    } else { /*if space but can't merge with neighbor tile */
                        moveIntoNextOpenSpace(b, col, row);
                    }
                } else { /*if there is no tile above */
                    Tile currentTile=b.tile(col, row);
                    b.move(col, 3, currentTile);
                }
            } else {  //**aka no open space above */
                if (checkTileAbove(b, col, row)) {
                    if (checkPotentialMerge(b, col, row, lastTileMerged)) {
                        mergeWithUpperTile(b, col, row, lastTileMerged);
                        z=true;
                    }
                }
            }
        }
    }
    return z;
}

    public void moveIntoNextOpenSpace(Board b, int col, int row) {
        Tile currentTile = b.tile(col, row);
        for (int upperRow = row + 1; upperRow < size(); upperRow += 1) {
            if (b.tile(col, upperRow) != null) {
                    b.move(col, upperRow-1, currentTile);
                    break;
                }
            }
        }



    public void mergeWithUpperTile(Board b, int col, int row, int[] x) {
        Tile currentTile=b.tile(col, row);
        for (int upperRow=row+1; upperRow<size(); upperRow+=1) {
            if (b.tile(col, upperRow)!=null) {
                b.move(col, upperRow, currentTile);
                x[0]= upperRow;
                this.score+=b.tile(col, upperRow).value();
                break;
            }
            }
    }

    public boolean checkPotentialMerge(Board b, int col, int row, int[] x) {
        Tile currentTile=b.tile(col, row);
        for (int upperRow=row+1; upperRow<size(); upperRow+=1) {
            if (b.tile(col, upperRow)!=null) {
                Tile neighborTile=b.tile(col, upperRow);
                if (x[0]==upperRow) { /*meaning it was recorded as last merge*/
                    return false;
                } else{
                    if (currentTile.value()==neighborTile.value() && x[0]!=upperRow) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*move returns true if we have performed a merge operation
    * instance variable to see if upper tile has been merged
    * array [col, row] last place merged;
    * each merge will check if these two col and row integers are the same */

    public boolean checkTileAbove(Board b, int col, int row) {
        for (int upperRow=row+1; upperRow<size(); upperRow+=1) {
            if (b.tile(col, upperRow)!=null) {
                return true;
            }
            }
        return false;
        }

    public boolean checkCanMoveUp(Board b, int col, int row){
        for (int upperRow=row+1; upperRow<size(); upperRow+=1) {
            if (b.tile(col, upperRow)==null) {
                return true;
            }
        }
        return false;
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
    /**split into categories of completely empty, not completely empty, and didn't pass either, being all full (return false) */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        /** b.tile(int col, int row)*/
        if (CompletelyEmpty(b)==true){
            return true;
        }else if (CompletelyEmpty(b)==false) {
            for (int i = 0; i < 4; i = i + 1) {
                for (int j = 0; j < 4; j = j + 1) {
                    if (b.tile(i, j) == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**helper function for 'EmptySpaceExists' */
    public static boolean CompletelyEmpty(Board b) {
        for (int i=0; i<4; i=i+1){
            for (int j=0; j<4; j=j+1){
                if (b.tile(i, j)!=null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE.
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for (int i = 0; i < 4; i = i + 1) {
            for (int j = 0; j < 4; j = j + 1) {
                if (b.tile(i, j) != null) {
                    if (b.tile(i, j).value()==MAX_PIECE) {
                        return true;
                    }

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
        /**I chose to break into two parts, if passes atLeastOneOpenSpace test, then would pass. If passes
         * atLeastOneAdjacentTile test, then would return true. If neither of these if clauses pass,
         * then return false as does not meet requirements for having any moves left*/
    public static boolean atLeastOneMoveExists(Board b) {
        if (atLeastOneOpenSpace(b)) {
            return true;
        } else if (atLeastOneAdjacentTile(b)) {
            return true;
        } else {
            return false;
        }
    }
        /**helper function for "atLeastOneMoveExists"
     * if contains one null space will return true*/
    public static boolean atLeastOneOpenSpace(Board b) {
        for (int i=0; i<4; i=i+1) {
            for (int j=0; j<4;j=j+1) {
                if (b.tile(i, j) == null) {
                    return true;
                }
            }
        }
        return false;
        }

    public static boolean atLeastOneAdjacentTile(Board b) {
        for (int i=0; i<4; i=i+1) {
            for (int j = 0; j < 3; j = j + 1) {
               if (b.tile(j, i).value()==b.tile(j+1, i).value()) {
                   return true;
               }
                            }
                        }
        for (int i=0; i<3; i=i+1) {
            for (int j = 0; j < 4; j = j + 1) {
                if (b.tile(j, i).value()==b.tile(j, i+1).value()) {
                    return true;
                }
            }
        }
        return false;
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
