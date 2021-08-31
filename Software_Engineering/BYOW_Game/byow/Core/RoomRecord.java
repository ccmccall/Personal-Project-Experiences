package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class RoomRecord implements Serializable {
    private LinkedList<RoomOrHallway> roomObjects;

    public RoomRecord() {
        this.roomObjects = new LinkedList<RoomOrHallway>();
    }
    public LinkedList<RoomOrHallway> getRoomObjects() {
        return this.roomObjects;
    }

    public static RoomRecord getRecord() {
        File roomRecord = join(RoomGenerator.CWD, "Record.txt");
        return readObject(roomRecord, RoomRecord.class);
    }

    public void saveRecord() {
        String recordName = "Record.txt";
        this.saveToFile(recordName);
    }

    public boolean usedByPreviousRoom(TETile[][] world,
                                      ROH.Pos p,
                                      int widthX, int lengthY) {
        if (this.roomObjects.size() == 0) {
            return false;
        }
        for (int dy = 0; dy < lengthY - 1; dy++) {
            for (int dx = 0; dx < widthX - 1; dx++) {
                /*checking the tiles of the given shape. */
                if (world[p.x + dx][p.y + dy] == Tileset.FLOOR) {
                    return true;
                }
                /*these four are checking the 4 surrounding
                tiles so have no adjacent rooms */
                if (p.x + dx + 1 < 70) {
                    if ((world[p.x + dx + 1][p.y + dy] == Tileset.FLOOR)) {
                        return true;
                    }
                }
                if (p.x + dx - 1 >= 0) {
                    if ((world[p.x + dx - 1][p.y + dy] == Tileset.FLOOR)) {
                        return true;
                    }
                }
                if (p.y + dy - 1 >= 0) {
                    if ((world[p.x + dx][p.y + dy - 1] == Tileset.FLOOR)) {
                        return true;
                    }
                }
                if (p.y + dy + 1 < 70) {
                    if ((world[p.x + dx][p.y + dy + 1] == Tileset.FLOOR)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean positionTaken(TETile[][] world, ROH.Pos p, int widthX,
                                 int lengthY) {
        /*This method ensures no coordinate collisions
        * with previous rooms.
        * Additionally checks that no position generated goes off the map.*/
        if (goesOffMap(world, p, widthX, lengthY)) {
            return true;
        }
        return usedByPreviousRoom(world, p, widthX, lengthY);
    }

    public boolean goesOffMap(TETile[][] world, ROH.Pos p, int widthX,
                              int lengthY) {
        int worldWidth = world.length; /* refers to X*/
        int worldLength = world[0].length; /* refers to Y */
        for (int x = 0; x < widthX; x++) {
            if (p.x < 0) {
                return true;
            }
            if (p.x + x >= worldWidth) {
                return true;
            }
        }
        for (int y = 0; y < lengthY; y++) {
            if (p.y < 0) {
                return true;
            }
            if (p.y + y >= worldLength) {
                return true;
            }
        }
        return false;
    }

    public void saveToFile(String name) {
        File currentRecord = join(RoomGenerator.CWD, name);
        if (currentRecord.exists()) {
            currentRecord.delete();
        }
        writeObject(join(RoomGenerator.CWD, name), this);
    }

    static void writeObject(File file, Serializable obj) {
        writeContents(file, serialize(obj));
    }

    public void addToRoomRecord(ROH newRoom) {
        this.roomObjects.addLast(newRoom);
    }

    static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    static String readContentsAsString(File file) {
        return new String(readContents(file), StandardCharsets.UTF_8);
    }

    static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(obj);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException excp) {
            throw new IllegalArgumentException("Serializing didn't work lol");
        }
    }
    static <T extends Serializable> T readObject(File file,
                                                 Class<T> expectedClass) {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(file));
            T result = expectedClass.cast(in.readObject());
            in.close();
            return result;
        } catch (IOException | ClassCastException
                | ClassNotFoundException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }
}
