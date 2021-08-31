package byow.Core;
import byow.TileEngine.TETile;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else  if (args.length == 1) {
            System.out.println("Can only have two or 0 arguments.");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            Engine engine = new Engine();
            //interactWithInputString is bug free now
            TETile[][] board = engine.interactWithInputString(args[1]);
            engine.ter.initialize(70, 70+2);
            engine.ter.renderFrame(board);
            System.out.println(engine.toString());
        } else {
            Engine engine = new Engine();
//            Object[] results = engine.interactWithKeyboard();
            TETile[][] board = engine.interactWithKeyboard();
//            TETile[][] board = (TETile[][]) results[0];
//            String keyRecord = (String) results[1];
            if (!engine.isLoadedGame) {
                //This instance of engine has avatarPosition defined.
                engine.avatarPosition = engine.placeAvatar(board, "Keyboard");
            } else {
                engine.avatarPosition = new ROH.Pos(0,0);
            }
            engine.WInput(board, engine.avatarPosition);
            System.out.println(engine.toString());
        }
    }

}
