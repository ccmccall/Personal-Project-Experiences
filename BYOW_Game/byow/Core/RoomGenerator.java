package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

public class RoomGenerator {
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */

    int widthX;
    int lengthY;
    long SEED;
    Random RANDOM;

    public RoomGenerator(Long seed) {
        this.widthX = 70;
        this.lengthY = 70;
        this.SEED = seed;
        this.RANDOM = new Random(this.SEED);
    }

    public TETile[][] generateWorld() {
        TETile[][] world = new TETile[this.widthX][this.lengthY];
        String stringSeed = String.valueOf(this.SEED);
        initRoomStorage("N" + stringSeed + "S");
        fillWithNothingTiles(world);
        generateAllRooms(world, this.RANDOM);
        generateAllHallways(world);
        newGenerateAllWalls(world);
        return world;
    }

    public static void fillWithNothingTiles(TETile[][] world) {
        int width = world.length; /*refers to X tiles */
        int length = world[0].length; /*refers to Y tiles */
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < length; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void generateAllRooms(TETile[][] world, Random currRandom) {
        int numRooms = roomNumberGenerator(currRandom);
        for (int i = 0; i < numRooms; i++) {
            addRoom(world, currRandom);
        }
    }

    public static int roomNumberGenerator(Random currRandom) {
        int tileNum = currRandom.nextInt(4);
        switch (tileNum) {
            case 0:
                return 2;
            case 1:
                return 4;
            case 2:
                return 6;
            default:
                return 4;
        }
    }

    public static void addRoom(TETile[][] world, Random currRandom) {
        TETile floor = Tileset.FLOOR;
        int widthX = randomLengthWidth(currRandom); /* meaning X*/
        int lengthY = randomLengthWidth(currRandom); /*meaning Y*/
        ROH.Pos p = randomPositionGenerator(world,  widthX, lengthY, currRandom);
        addRoomHelper(world, floor, p, lengthY, widthX);
    }

    public static ROH.Pos randomPositionGenerator(TETile[][] world, int widthX,
                                                                 int lengthY, Random currRandom) {
        ROH.Pos p = new ROH.Pos(-10, -15);
        RoomRecord record = RoomRecord.getRecord();
        while (record.positionTaken(world, p, widthX, lengthY)) {
            int x = randomCoordGenerator(currRandom);
            int y = randomCoordGenerator(currRandom);
            p = new ROH.Pos(x, y);
        }
        return p;
    }

    public static int randomCoordGenerator(Random currRandom) {
        int tileNum = currRandom.nextInt(14);
        switch (tileNum) {
            case 0:
                return 10;
            case 1:
                return 15;
            case 2:
                return 20;
            case 3:
                return 25;
            case 4:
                return 30;
            case 5:
                return 35;
            case 7:
                return 40;
            case 8:
                return 45;
            case 9:
                return 50;
            case 10:
                return 55;
            case 11:
                return 60;
            case 12:
                return 65;
            case 13:
                return 70;
            default:
                return 55;
        }
    }

    public static void addRoomHelper(TETile[][] world, TETile kind,
                                     ROH.Pos p, int length, int width) {
        /*Note: p is the bottom left corner, or anchor square.
         * Steps:
         * 1. Creates roomhallway object to store imp coordinates.
         * 2. Draws room on tile world.
         * 3. overwrites previous persistence file with updated roomRecord.*/
        TETile addLantern = Tileset.LANTERN_OFF;
        RoomRecord record = RoomRecord.getRecord();
        ROH newRoom = new ROH(p, length, width);
        record.addToRoomRecord(newRoom);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                if (p.x + x == p.x + 3 && p.y + y == p.y + 3) {
                    world[p.x + x][p.y + y] = addLantern;
                    continue;
                }
                world[p.x + x][p.y + y] = kind;
            }
        }
        record.saveRecord();
    }

    private static int randomLengthWidth(Random currRandom) {
        int tileNum = currRandom.nextInt(4);
        switch (tileNum) {
            case 0:
                return 5;
            case 1:
                return 10;
            case 2:
                return 15;
            case 3:
                return 20;
            default:
                return 5;
        }
    }

    public static void initRoomStorage(String seed) {
        setUpPersistance(seed);
    }

    public static void setUpPersistance(String seed) {
        File roomRecord = join(CWD, "Record.txt");
        File worldRecord = join(CWD, "WorldRecord.txt");
        if (roomRecord.exists()) {
            roomRecord.delete();
        }
        if (worldRecord.exists()) {
            worldRecord.delete();
        }
        RoomRecord record = new RoomRecord();
        try {
            roomRecord.createNewFile();
            worldRecord.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeWorldRecord("null");
        record.saveRecord();
    }

    public static void writeWorldRecord(String stringInput) {
        String recordName = "WorldRecord.txt";
        File worldRecord = join(CWD, recordName);
        String storage = stringInput;
        RoomRecord.writeContents(worldRecord, storage);
    }

    public static String retrieveWorldRecord() {
        String recordName = "WorldRecord.txt";
        File worldRecord = join(CWD, recordName);
        return RoomRecord.readContentsAsString(worldRecord);
    }

    public static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    public static void generateHallwayPath(TETile[][] world,
                                           RoomOrHallway start, RoomOrHallway end) {
        RoomOrHallway first;
        RoomOrHallway second;
        //X
        if (start.position.x < end.position.x) {
            first = start;
            second = end;
        } else {
            first = end;
            second = start;
        }
        for (int x = first.position.x; x <= second.position.x; x++) {
            world[x][first.position.y] = Tileset.FLOOR;
            world[x][first.position.y + 1] = Tileset.FLOOR;
            world[x][first.position.y + 2] = Tileset.FLOOR;
        }
        int finalX = second.position.x;
        //Y
        if (start.position.y < end.position.y) {
            first = start;
            second = end;
        } else {
            first = end;
            second = start;
        }
        for (int y = first.position.y; y <= second.position.y + 2; y++) {
            world[finalX][y] = Tileset.FLOOR;
            world[finalX + 1][y] = Tileset.FLOOR;
            world[finalX + 2][y] = Tileset.FLOOR;
        }
    }

    public static void generateAllHallways(TETile[][] world) {
        RoomRecord record = RoomRecord.getRecord();
        LinkedList<RoomOrHallway> copyOfRecord = record.getRoomObjects();
        while (copyOfRecord.size() >= 2) {
            RoomOrHallway room1 = copyOfRecord.getFirst();
            copyOfRecord.remove(room1);
            RoomOrHallway room2 = copyOfRecord.getFirst();
            generateHallwayPath(world, room1, room2);
        }
    }

    public static boolean checkPerimeter(TETile[][] world, int x, int y) {
        if (world[x][y] == Tileset.FLOOR && (y == 0 || x == 0
                || x == world.length - 1 || y == world[0].length - 1)) {
            return true;
        }
        if (world[x][y] == Tileset.NOTHING) {
            return false;
        } else if (x == 0 && y == 0) {
            if (world[x + 1][y] == Tileset.NOTHING) {
                return true;
            }
            if (world[x][y + 1] == Tileset.NOTHING) {
                return true;
            }
        } else if (x == 0 && y != 0) {
            if (world[x + 1][y] == Tileset.NOTHING) {
                return true;
            }
            if (world[x][y + 1] == Tileset.NOTHING) {
                return true;
            }
            if (world[x][y - 1] == Tileset.NOTHING) {
                return true;
            }
        } else if (y == 0 && x != 0) {
            if (world[x + 1][y] == Tileset.NOTHING) {
                return true;
            }
            if (world[x - 1][y] == Tileset.NOTHING) {
                return true;
            }
            if (world[x][y + 1] == Tileset.NOTHING) {
                return true;
            }
        } else {
            if (x + 1 < 70) {
                if (world[x + 1][y] == Tileset.NOTHING) {
                    return true;
                }
            }
            if (world[x - 1][y] == Tileset.NOTHING) {
                return true;
            }
            if (y + 1 < 70) {
                if (world[x][y + 1] == Tileset.NOTHING) {
                    return true;
                }
            }
            if (world[x][y - 1] == Tileset.NOTHING) {
                return true;
            }
        }
        return false;
    }

    public static void newGenerateAllWalls(TETile[][] world) {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                if (checkPerimeter(world, x, y)) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }



    public static void main(String[] args) {

    }

}
