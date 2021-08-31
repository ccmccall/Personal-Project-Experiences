package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import static byow.TileEngine.Tileset.*;
import java.awt.*;
import java.util.LinkedList;

public class Engine {

    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 70;
    public static final int HEIGHT = 70;
    TETile avatarTile = AVATAR;
    String keysTyped = "";
    ROH.Pos avatarPosition;
    boolean isLoadedGame = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public TETile[][] interactWithKeyboard() {
        //Setup Main Menu
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        drawMenu();

        //Interact with User Input
        String seedValue = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String next = Character.toString(StdDraw.nextKeyTyped());
                if (next.equals("n") || next.equals("N")) {
                    //Prompt user to enter random seed
                    StdDraw.text(35, 15, "Enter random seed (S to submit)");
                    StdDraw.show();
                    RoomGenerator.writeWorldRecord("N");
                } else if (next.equals("l") || next.equals("L")) {
                    /*this is the part where you reload the saved string combo */
                    // no edge cases needed, all inputs are valid
                    // no need to save L or Q
                    String keyRecord = RoomGenerator.retrieveWorldRecord();
                    /*should change interact with string board */
                    TETile[][] board = interactWithInputString(keyRecord);
                    TERenderer terNew = new TERenderer();
                    terNew.initialize(70, 70 + 2); // + 2 for HUD
                    terNew.renderFrame(board);
                    return board;

                } else if (next.equals("q") || next.equals("Q")) {
                    //System.out.println("Quit");
                    System.exit(0);
                } else if (next.equals("s") || next.equals("S")) {
                    //Generate new world with seed
                    keysTyped = keysTyped + next;
                    Long seedLong = Long.parseLong(seedValue);
                    RoomGenerator instanceObject = new RoomGenerator(seedLong);
                    TETile[][] board = instanceObject.generateWorld();
                    TERenderer terNew = new TERenderer();
                    terNew.initialize(70, 70 + 2); // + 2 for HUD
                    terNew.renderFrame(board);
                    return board;
                } else if (next.equals("a") || next.equals("A")) {
                    //Customize Avatar
                    StdDraw.text(35, 10, "Enter $, %, &, or @ to submit new avatar");
                    StdDraw.show();
                } else if (next.equals("$")) {
                    //Customize Avatar
                    this.avatarTile = AVATAR4;
                    StdDraw.text(35, 5, "Avatar Submitted!");
                    StdDraw.show();
                } else if (next.equals("%")) {
                    //Customize Avatar
                    this.avatarTile = AVATAR2;
                    StdDraw.text(35, 5, "Avatar Submitted!");
                    StdDraw.show();
                } else if (next.equals("&")) {
                    //Customize Avatar
                    this.avatarTile = AVATAR3;
                    StdDraw.text(35, 5, "Avatar Submitted!");
                    StdDraw.show();
                } else if (next.equals("@")) {
                    //Customize Avatar
                    this.avatarTile = AVATAR;
                    StdDraw.text(35, 5, "Avatar Submitted!");
                    StdDraw.show();
                } else {
                    //Add number to the random seed string
                    //System.out.println(seedValue);
                    seedValue = seedValue + next;
                }
                if (!next.equals("l") && !next.equals(":") & !next.equals("q")) {
                    keysTyped = keysTyped + next;
                }
            }
        }
    }

    public TETile[][] lanternOffCase(TETile[][] world,
                                     ROH.Pos avatar, String movement) {
        int dx = -200;
        int dy = -200;
        if (movement.equals("W") || movement.equals("w")) {
            dx = 0;
            dy = 1;
        } else if (movement.equals("S")  || movement.equals("s")) {
            dx = 0;
            dy = -1;
        } else if (movement.equals("A")  || movement.equals("a")) {
            dx = -1;
            dy = 0;
        } else if (movement.equals("D")  || movement.equals("d")) {
            dx = 1;
            dy = 0;
        }
        world[avatar.x + dx][avatar.y + dy] = Tileset.LANTERN_ON;
        for (int i = -4; i < 4; i++) {
            for (int j = -4; j < 4; j++) {
                if (avatar.x + dx + i < 70 && avatar.x + dx + i >= 0) {
                    if (avatar.y + dy + j < 70 && avatar.y + dy + j >= 0) {
                        if (world[avatar.x + dx + i][avatar.y + dy + j] == Tileset.FLOOR) {
                            world[avatar.x + dx + i][avatar.y + dy + j] = Tileset.FLOOR_LIGHT;
                        }
                    }
                }
            }
        }
        return world;
    }

    public TETile[][] lanternOnCase(TETile[][] world, ROH.Pos avatar,
                                    String movement) {
        int dx = -200;
        int dy = -200;
        if (movement.equals("W") || movement.equals("w")) {
            dx = 0;
            dy = 1;
        } else if (movement.equals("S")  || movement.equals("s")) {
            dx = 0;
            dy = -1;
        } else if (movement.equals("A")  || movement.equals("a")) {
            dx = -1;
            dy = 0;
        } else if (movement.equals("D")  || movement.equals("d")) {
            dx = 1;
            dy = 0;
        }
        world[avatar.x + dx][avatar.y + dy] = Tileset.LANTERN_OFF;
        for (int i = -4; i < 4; i++) {
            for (int j = -4; j < 4; j++) {
                if (avatar.x + dx + i < 70 && avatar.x + dx + i >= 0) {
                    if (avatar.y + dy + j < 70 && avatar.y + dy + j >= 0) {
                        if (world[avatar.x + dx + i][avatar.y + dy + j] == Tileset.FLOOR_LIGHT) {
                            world[avatar.x + dx + i][avatar.y + dy + j] = Tileset.FLOOR;
                        }
                    }
                }
            }
        }
        return world;
    }

    public void WInput(TETile[][] w,
                               ROH.Pos ap) {
        ROH.Pos avatar = ap;
        boolean quit = false;
        LinkedList<TETile> previousTile = new LinkedList<TETile>();
        previousTile.addLast(Tileset.FLOOR);
        if (!isLoadedGame) {
            w[placeAvatar(w, "String").x][placeAvatar(w, "String").y] = avatarTile;
        } else {
            w[avatarPosition.x][avatarPosition.y] = avatarTile;
        }
        while (true) {
            String tileOnMouse = "";
            try {
                tileOnMouse = w[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()].description();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid option");
            }
            StdDraw.setPenColor(Color.white);
            StdDraw.text(2, 71, tileOnMouse);
            StdDraw.show();
            StdDraw.pause(10);
            ter.renderFrame(w);

            if (StdDraw.hasNextKeyTyped()) {
                String next = Character.toString(StdDraw.nextKeyTyped());
                if (next.equals("w") || next.equals("W")) {
                    if (avatar.y + 1 < 70) {
                        if (w[avatar.x][avatar.y + 1] != Tileset.WALL) {
                            if (w[avatar.x][avatar.y + 1] == Tileset.LANTERN_OFF) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR_LIGHT);
                                previousTile.addLast(Tileset.LANTERN_ON);
                                w = lanternOffCase(w, avatar, "W");
                            } else if (w[avatar.x][avatar.y + 1] == Tileset.LANTERN_ON) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR);
                                previousTile.addLast(Tileset.LANTERN_OFF);
                                w = lanternOnCase(w, avatar, "W");
                            } else if (w[avatar.x][avatar.y + 1] == Tileset.FLOOR_LIGHT) {
                                previousTile.addLast(Tileset.FLOOR_LIGHT);
                            } else if (w[avatar.x][avatar.y + 1] == Tileset.FLOOR) {
                                previousTile.addLast(Tileset.FLOOR);
                            }
                            w[avatar.x][avatar.y + 1] = avatarTile;
                            w[avatar.x][avatar.y] = previousTile.remove();
                            avatar = new ROH.Pos(avatar.x, avatar.y + 1);
                            ter.renderFrame(w);
                        }
                    }
                } else if (next.equals("s") || next.equals("S")) {
                    if (avatar.y - 1 < 70) {
                        if (w[avatar.x][avatar.y - 1] != Tileset.WALL) {
                            if (w[avatar.x][avatar.y - 1] == Tileset.LANTERN_OFF) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR_LIGHT);
                                previousTile.addLast(Tileset.LANTERN_ON);
                                w = lanternOffCase(w, avatar, "S");
                            } else if (w[avatar.x][avatar.y - 1] == Tileset.FLOOR) {
                                previousTile.addLast(Tileset.FLOOR);
                            } else if (w[avatar.x][avatar.y - 1] == Tileset.LANTERN_ON) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR);
                                previousTile.addLast(Tileset.LANTERN_OFF);
                                w = lanternOnCase(w, avatar, "S");
                            } else if (w[avatar.x][avatar.y - 1] == Tileset.FLOOR_LIGHT) {
                                previousTile.addLast(Tileset.FLOOR_LIGHT);
                            }
                            w[avatar.x][avatar.y - 1] = avatarTile;
                            w[avatar.x][avatar.y] = previousTile.remove();
                            avatar = new ROH.Pos(avatar.x, avatar.y - 1);
                            ter.renderFrame(w);
                        }
                    }
                } else if (next.equals("a") || next.equals("A")) {
                    //The left movement case
                    if (avatar.x - 1 < 70) {
                        if (w[avatar.x - 1][avatar.y] != Tileset.WALL) {
                            if (w[avatar.x - 1][avatar.y] == Tileset.LANTERN_OFF) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR_LIGHT);
                                previousTile.addLast(Tileset.LANTERN_ON);
                                w = lanternOffCase(w, avatar, "A");
                            } else if (w[avatar.x - 1][avatar.y] == Tileset.FLOOR) {
                                previousTile.addLast(Tileset.FLOOR);
                            } else if (w[avatar.x - 1][avatar.y] == Tileset.LANTERN_ON) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR);
                                previousTile.addLast(Tileset.LANTERN_OFF);
                                w = lanternOnCase(w, avatar, "A");
                            } else if (w[avatar.x - 1][avatar.y] == Tileset.FLOOR_LIGHT) {
                                previousTile.addLast(Tileset.FLOOR_LIGHT);
                            }
                            w[avatar.x - 1][avatar.y] = avatarTile;
                            w[avatar.x][avatar.y] = previousTile.remove();
                            avatar = new ROH.Pos(avatar.x - 1, avatar.y);
                            ter.renderFrame(w);
                        }
                    }
                } else if (next.equals("d") || next.equals("D")) {
                    if (avatar.x + 1 < 70) {
                        if (w[avatar.x + 1][avatar.y] != Tileset.WALL) {
                            if (w[avatar.x + 1][avatar.y] == Tileset.LANTERN_OFF) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR_LIGHT);
                                previousTile.addLast(Tileset.LANTERN_ON);
                                w = lanternOffCase(w, avatar, "D");
                            } else if (w[avatar.x + 1][avatar.y] == Tileset.FLOOR) {
                                previousTile.addLast(Tileset.FLOOR);
                            } else if (w[avatar.x + 1][avatar.y] == Tileset.LANTERN_ON) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR);
                                previousTile.addLast(Tileset.LANTERN_OFF);
                                w = lanternOnCase(w, avatar, "D");
                            } else if (w[avatar.x + 1][avatar.y] == Tileset.FLOOR_LIGHT) {
                                previousTile.addLast(Tileset.FLOOR_LIGHT);
                            }
                            w[avatar.x + 1][avatar.y] = avatarTile;
                            w[avatar.x][avatar.y] = previousTile.remove();
                            avatar = new ROH.Pos(avatar.x + 1, avatar.y);
                            ter.renderFrame(w);
                        }
                    }
                } else if (next.equals(":")) {
                    quit = true;
                } else if ((next.equals("Q") || next.equals("q")) && quit) {
                    RoomGenerator.writeWorldRecord(keysTyped);
                    System.exit(0);
                }
                if (!quit) {
                    keysTyped = keysTyped + next;
                }
                avatarPosition = avatar;
            }
        }
    }

//Does not place the avatar on the board, just finds the coordinate with the bottom left floor tile
    public ROH.Pos placeAvatar(TETile[][] world, String type) {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                if (world[x][y] == FLOOR) {
                    if (type.equals("Keyboard")) {
                        ter.renderFrame(world);
                    }
                    return new ROH.Pos(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */


    public boolean worldRecordNull() {
        String worldRecord = RoomGenerator.retrieveWorldRecord();
        if (worldRecord.equals("null")) {
            return true;
        }
        return false;
    }

    public TETile[][] interactWithInputString(String in) {
        /*Will all be valid strings */
        //Returns N123 so everything excluding S
        boolean isLG = false;
        String allSeedandMovements = in;
        String firstChar = String.valueOf(allSeedandMovements.charAt(0));
        if (firstChar.equals("A")) {
            //Customize Avatar
            String seedValue = getSeed(allSeedandMovements);
            String avatarChar = String.valueOf(seedValue.charAt(1));
            if (avatarChar.equals("$")) {
                this.avatarTile = AVATAR4;
            } else if (avatarChar.equals("%")) {
                this.avatarTile = AVATAR2;
            } else if (avatarChar.equals("&")) {
                this.avatarTile = AVATAR3;
            } else if (avatarChar.equals("@")) {
                this.avatarTile = AVATAR;
            }
            int length = seedValue.length();
            seedValue = seedValue.substring(3, length);
        }
        TETile[][] board = new TETile[70][70];
        LinkedList<TETile> previousTile = new LinkedList<TETile>();
        if (firstChar.equals("N") || firstChar.equals("n")) {
            RoomGenerator.writeWorldRecord("nothing");
            String seedValue = getSeed(allSeedandMovements);
            int length = seedValue.length();
            seedValue = seedValue.substring(1, length);
            Long seedLong = Long.parseLong(seedValue);
            RoomGenerator instanceObject = new RoomGenerator(seedLong);
            board = instanceObject.generateWorld();
            avatarPosition = placeAvatar(board, "String");
            previousTile.addLast(FLOOR);
        }
        String storedWorldRecord = "test";
        //Update this with proper connected version
        if (!worldRecordNull()) {
            storedWorldRecord = RoomGenerator.retrieveWorldRecord();
        }
        //Now to check whether loaded game or new game
        if (firstChar.equals("l")) {
            isLG = true;
            //Not sure if recursive call of this is the right move here.
            Object[] results = loadInputString(storedWorldRecord);
            board = (TETile[][]) results[0];
            previousTile.addLast((TETile) results[1]);
            int length = in.length();
            allSeedandMovements = in.substring(1, length);
            firstChar = String.valueOf(allSeedandMovements.charAt(0));
            avatarPosition = findAvatarPosition(board);
            //Then runs the rest of the program on the remaining portion of the board
            // after the previous changes to board were applied with a recursive call.
        }
        String seedValue = getSeed(allSeedandMovements);
        //Confirms only the number portion of the seed is chosen.
        if (("N" + seedValue + "S").equals(in) | ("n" + seedValue + "s").equals(in)) {
            return board;
        } else {
            //This is the component that runs the prograom on the string inputs
            String keysOnly = "";
            if (!isLG) {
                keysOnly = getKeys(in);
            } else {
                keysOnly = allSeedandMovements;
            }
            while (keysOnly.length() > 0) {
                String key = firstChar(keysOnly);
                int dx = -2000;
                int dy = -2000;
                if (isMovement(key)) {
                    if (key.equals("w") || key.equals("W")) {
                        dx = 0;
                        dy = 1;
                    } else if (key.equals("s") || key.equals("S")) {
                        dx = 0;
                        dy = -1;
                    } else if (key.equals("a") || key.equals("A")) {
                        dx = -1;
                        dy = 0;
                    } else if (key.equals("d") || key.equals("D")) {
                        dx = 1;
                        dy = 0;
                    }
                    //Up key movement process
                    if (areValidMovements(avatarPosition.x + dx, avatarPosition.y + dy)) {
                        if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                != Tileset.WALL) {
                            if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                    == Tileset.LANTERN_OFF) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR_LIGHT);
                                previousTile.addLast(Tileset.LANTERN_ON);
                                board = lanternOffCase(board, avatarPosition, key);
                            } else if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                    == Tileset.LANTERN_ON) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR);
                                previousTile.addLast(Tileset.LANTERN_OFF);
                                board = lanternOnCase(board, avatarPosition, key);
                            } else if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                    == Tileset.FLOOR_LIGHT) {
                                previousTile.addLast(Tileset.FLOOR_LIGHT);
                            } else if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                    == Tileset.FLOOR) {
                                previousTile.addLast(Tileset.FLOOR);
                            }

                            board[avatarPosition.x][avatarPosition.y] = previousTile.remove();
                            avatarPosition = moveAvatar(avatarPosition, dx, dy);
                        //Place avatar on new tile
                            // board[avatarPosition.x][avatarPosition.y] = avatarTile;

                        }
                    }
                //Last step is to remove the key from the remaining keysOnly String

                    keysOnly = notFirstChar(keysOnly);
                } else if (loadQuitSave(key)) {
                    if (key.equals(":")) {
                        int length = allSeedandMovements.length();
                        String notColonQ = allSeedandMovements.substring(0, length - 2);
                        if (isLG) {
                            RoomGenerator.writeWorldRecord(
                                    storedWorldRecord + notColonQ);
                        } else {
                            isLG = true;
                            RoomGenerator.writeWorldRecord(notColonQ);
                        }
                    }
                    keysOnly = notFirstChar(keysOnly);
                }
            }
            return board;
        }
    }

    public Object[] loadInputString(String input) {
        String seedValue = getSeed(input);
        char chair = seedValue.charAt(0);
        String firstChar = String.valueOf(chair);
        if (firstChar.equals("A")) {
            //Customize Avatar
            String avatarChar = String.valueOf(seedValue.charAt(1));
            if (avatarChar.equals("$")) {
                this.avatarTile = AVATAR4;
            } else if (avatarChar.equals("%")) {
                this.avatarTile = AVATAR2;
            } else if (avatarChar.equals("&")) {
                this.avatarTile = AVATAR3;
            } else if (avatarChar.equals("@")) {
                this.avatarTile = AVATAR;
            }
            int length = seedValue.length();
            seedValue = seedValue.substring(3, length);
        }
        //Now to check whether loaded game or new game
        TETile[][] board = new TETile[70][70];
        //Confirms only the number portion of the seed is chosen.
        if (firstChar.equals("N") || firstChar.equals("n")) {
            int length = seedValue.length();
            seedValue = seedValue.substring(1, length);
            Long seedLong = Long.parseLong(seedValue);
            RoomGenerator instanceObject = new RoomGenerator(seedLong);
            board = instanceObject.generateWorld();
            avatarPosition = placeAvatar(board, "String");
        }
        LinkedList<TETile> previousTile = new LinkedList<TETile>();
        previousTile.addLast(FLOOR);
        if (("N" + seedValue + "S").equals(input) | ("n" + seedValue + "s").equals(input)) {
            return board;
        } else {
            //This is the component that runs the prograom on the string inputs
            String keysOnly = getKeys(input);
            while (keysOnly.length() > 0) {
                String key = firstChar(keysOnly);
                int dx = -2000;
                int dy = -2000;
                if (isMovement(key)) {
                    if (key.equals("w") || key.equals("W")) {
                        dx = 0;
                        dy = 1;
                    } else if (key.equals("s") || key.equals("S")) {
                        dx = 0;
                        dy = -1;
                    } else if (key.equals("a") || key.equals("A")) {
                        dx = -1;
                        dy = 0;
                    } else if (key.equals("d") || key.equals("D")) {
                        dx = 1;
                        dy = 0;
                    }
                    //Up key movement process
                    if (areValidMovements(avatarPosition.x + dx, avatarPosition.y + dy)) {
                        if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                != Tileset.WALL) {
                            if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                    == Tileset.LANTERN_OFF) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR_LIGHT);
                                previousTile.addLast(Tileset.LANTERN_ON);
                                board = lanternOffCase(board, avatarPosition, key);
                            } else if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                    == Tileset.LANTERN_ON) {
                                previousTile.remove();
                                previousTile.addFirst(Tileset.FLOOR);
                                previousTile.addLast(Tileset.LANTERN_OFF);
                                board = lanternOnCase(board, avatarPosition, key);
                            } else if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                    == Tileset.FLOOR_LIGHT) {
                                previousTile.addLast(Tileset.FLOOR_LIGHT);
                            } else if (board[avatarPosition.x + dx][avatarPosition.y + dy]
                                    == Tileset.FLOOR) {
                                previousTile.addLast(Tileset.FLOOR);
                            }
                            //fill old avatar square with correct previous tile, change avatarPosition value
                            //then make new avatarposition tile on board be correct avatar tyle.
                            board[avatarPosition.x][avatarPosition.y] = previousTile.remove();
                            avatarPosition = moveAvatar(avatarPosition, dx, dy);
                            //Place avatar on new tile
                            board[avatarPosition.x][avatarPosition.y] = avatarTile;
                        }
                    }
                    //Last step is to remove the key from the remaining keysOnly String
                    keysOnly = notFirstChar(keysOnly);
                } else if (loadQuitSave(key)) {
                    //Essentially do not inteact with these keys for input String
                    keysOnly = notFirstChar(keysOnly);
                }
            }
            Object[] returnArray = new Object[2];
            returnArray[0] = board;
            returnArray[1] = previousTile.remove();
            return returnArray;
        }
    }

    public ROH.Pos findAvatarPosition(TETile[][] board) {
        int width = board.length; /*refers to X tiles */
        int length = board[0].length; /*refers to Y tiles */
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                if (board[x][y] == avatarTile) {
                    return new ROH.Pos(x, y);
                }
            }
        }
        return new ROH.Pos(-200, -200);
    }

    public ROH.Pos moveAvatar(ROH.Pos avatar, int dx, int dy) {
        avatar.x += dx;
        avatar.y += dy;
        return avatar;
    }

    public boolean areValidMovements(int x, int y) {
        if (x < 70 && x >= 0) {
            if (y < 70 && y >= 0) {
                return true;
            }
        }
        return false;
    }

    public boolean loadQuitSave(String key) {
        if (key.equals("L") | key.equals("l")) {
            return true;
        }
        if (key.equals("Q") | key.equals("q")) {
            return true;
        }
        if (key.equals(":")) {
            return true;
        }
        return false;
    }

    public boolean isMovement(String key) {
        if (key.equals("a")) {
            return true;
        }
        if (key.equals("w")) {
            return true;
        }
        if (key.equals("d")) {
            return true;
        }
        if (key.equals("s")) {
            return true;
        }
        return false;
    }

    public String notFirstChar(String input) {
        int length = input.length();
        if (length == 1) {
            return "";
        }
        return input.substring(1, length);
    }
    public String firstChar(String input) {
        return String.valueOf(input.charAt(0));
    }

    public String getSeed(String worldRecord) {
        int endSeedIndex = 2;
        for (int i = 1; i < worldRecord.length(); i++) {
            String charAt = String.valueOf(worldRecord.charAt(i));
            if (charAt.equals("S") | charAt.equals("s")) {
                endSeedIndex = i;
                break;
            }
        }
        return worldRecord.substring(0, endSeedIndex);
    }

    public String getKeys(String worldRecord) {
        int endSeedIndex = 2;
        for (int i = 1; i < worldRecord.length(); i++) {
            String charAt = String.valueOf(worldRecord.charAt(i));
            if (charAt.equals("S") | charAt.equals("s")) {
                endSeedIndex = i;
                break;
            }
        }
        int recordLength = worldRecord.length();
        return worldRecord.substring(endSeedIndex + 1, recordLength);
    }

    public void drawMenu() {
        //Display Main Menu
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(35, 60, "CS61B: The Game");
        StdDraw.show();
        Font font2 = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font2);
        StdDraw.text(35, 35, "New Game (N)");
        StdDraw.show();
        StdDraw.text(35, 30, "Load Game (L)");
        StdDraw.show();
        StdDraw.text(35, 25, "Quit (Q)");
        StdDraw.show();
        StdDraw.text(35, 20, "Customize Avatar (A)");
        StdDraw.show();

    }

}

