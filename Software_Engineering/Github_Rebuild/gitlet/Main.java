package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Claire McCallick
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        if (checkIfRealCase(firstArg)) {
            switch (firstArg) {
                case "init":
                    if (validateNumArgs(args, 1)) {
                        Repository.initCommand();
                        break;
                    }
                    break;
                case "add":
                    if (validateNumArgs(args, 2)) {
                        if (checkIfInitialized()) {
                            String fileName = args[1];
                            Repository.add(fileName);
                            break;
                        }
                    }
                    break;
                case "commit":
                    if (validateNumArgs(args, 2)) {
                        if (checkIfInitialized()) {
                            String commitMessage = args[1];
                            Repository.commit(commitMessage);
                            break;
                        }
                    }
                    break;
                case "rm":
                    if (validateNumArgs(args, 2)) {
                        if (checkIfInitialized()) {
                            String fileName = args[1];
                            Repository.remove(fileName);
                            break;
                        }
                    }
                    break;
                case "log":
                    if (validateNumArgs(args,1)) {
                        if (checkIfInitialized()) {
                            Repository.returnLog();
                            break;
                        }
                    }
                    break;
                case "global-log":
                    if (validateNumArgs(args, 1)) {
                        if (checkIfInitialized()) {
                            Repository.returnGlobalLog();
                            break;
                        }
                    }
                    break;
                case "find":
                    if (validateNumArgs(args, 2)) {
                        if (checkIfInitialized()) {
                            String commitMessage = args[1];
                            Repository.find(commitMessage);
                            break;
                        }
                    }
                    break;
                case "checkout":
                    if (validateNumArgsCheckout(args)) {
                        if (checkIfInitialized()) {
                            if (args.length == 2) {
                                String branchName = args[1];
                                Repository.checkOutBranch(branchName);
                                break;
                            }
                            if (args.length == 3) {
                                String fileName = args[2];
                                Repository.checkOut(fileName);
                            }
                            if (args.length == 4) {
                                String fileName = args[3];
                                String commitHash = args[1];
                                if (args[2].equals("--")) {
                                    Repository.checkOut(fileName, commitHash);
                                } else {
                                    System.out.println("Incorrect operands.");
                                }
                            }
                        break;
                        }
                    }
                    break;
                case "status":
                    if (validateNumArgs(args, 1)) {
                        if (checkIfInitialized()) {
                            Repository.printStatusObject();
                            break;
                        }
                    }
                    break;
                case "branch":
                    if (validateNumArgs(args, 2)) {
                        if (checkIfInitialized()) {
                            String branchName = args[1];
                            Repository.createBranch(branchName);
                            break;
                        }
                    }
                    break;
                case "rm-branch":
                    if (validateNumArgs(args, 2)) {
                        if (checkIfInitialized()) {
                            String branchName = args[1];
                            Repository.removeBranch(branchName);
                            break;
                        }
                    }
                    break;
                case "reset":
                    if (validateNumArgs(args, 2)) {
                        if (checkIfInitialized()) {
                            String commitHash = args[1];
                            Repository.reset(commitHash);
                            break;
                        }
                    }
                    break;
                case "merge":
                    if (validateNumArgs(args, 2)) {
                        if (checkIfInitialized()) {
                            String branchName = args[1];
                            Repository.merge(branchName);
                            break;
                        }
                    }
                    break;
            }
        }
        return;
    }

    public static boolean checkIfInitialized() {
        if (!Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return false;
        }
        return true;
    }

    public static boolean checkIfRealCase(String cmd) {
        String[] cmds = {"init", "add", "commit", "rm", "log", "checkout", "global-log", "find", "branch", "status", "reset", "rm-branch", "merge"};
        for (int i = 0; i < cmds.length; i ++) {
            if (cmds[i].equals(cmd)) {
                return true;
            }
        }
        System.out.println("No command with that name exists.");
        return false;
    }

    public static boolean validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            return false;
        }
        return true;
    }

    public static boolean validateNumArgsCheckout(String[] args) {
        if (args.length != 2) {
            if (args.length != 3) {
                if (args.length != 4) {
                    System.out.println("Incorrect operands.");
                    return false;
                }
            }
        }
        return true;
    }
}