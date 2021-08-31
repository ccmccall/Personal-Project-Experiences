package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *
 *  @author Claire McCallick
 */
public class Repository implements Serializable {
    /** List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_FOLDER = Utils.join(GITLET_DIR, "Commits");
    public static final File FILE_DIR = Utils.join(GITLET_DIR, "Files");
    public static final File POINTERS_DIR = Utils.join(GITLET_DIR, "Pointers");
    public static final File STAGING_DIR = Utils.join(GITLET_DIR, "StagingArea");
    public static final File ADDITION_DIR = Utils.join(STAGING_DIR, "Addition");
    public static final File REMOVAL_DIR = Utils.join(STAGING_DIR, "Removal");
    public static final File BRANCHES_DIR = Utils.join(GITLET_DIR, "Branches");

    /*This method sets up everything you need for initializing a repo structure*/
    public static void setUpPersistence() {
        GITLET_DIR.mkdir();
        COMMIT_FOLDER.mkdir();
        FILE_DIR.mkdir();
        POINTERS_DIR.mkdir();
        STAGING_DIR.mkdir();
        ADDITION_DIR.mkdir();
        REMOVAL_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        try {
            File headPointer = Utils.join(POINTERS_DIR, "Head");
            headPointer.createNewFile();
            File masterBranch = Utils.join(BRANCHES_DIR, "master");
            masterBranch.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initCommand() {
        if (!GITLET_DIR.exists()) {
            setUpPersistence();
            Commit initialCommit = new Commit();
            setActiveBranch("master");
            initialCommit.saveCommit();
        } else {
            System.out.print("A Gitlet version-control system "
                    + "already exists in the current directory.");
        }
    }

    //*how you set the Head pointer to the current active branch */
    public static void setActiveBranch(String branchName) {
        Repository.writeHeadPointer(branchName);
    }

    public static void createBranch(String branchName) {
        if (branchExists(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        try {
            File newBranch = Utils.join(BRANCHES_DIR, branchName);
            newBranch.createNewFile();
            writeBranch(branchName, retrieveBranchHash(getHeadPointer()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void add(String file) {
        if (fileDoesntExist(file)) {
            System.out.println("File does not exist.");
            return;
        }
        if (changedSinceLastCommit(file)) {
            stageForAddition(file);
        } else {
            if (checkIfInStagingArea(file)) {
                removeFromStagingArea(file);
            }
        }
    }

    public static void remove(String fileName) {
        if (failsStagedAndTracked(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        if (stagedForAddition(fileName)) {
            removeFromAddition(fileName);
        }
        if (trackedInMostRecentCommit(fileName)) {
            stageForRemoval(fileName);
            removefromCWD(fileName);
        }
    }

    public static void checkOut(String fileName) {
        String headHash = retrieveBranchHash(getHeadPointer());
        checkOut(fileName, headHash);
    }

    public static void checkOut(String fileName, String currentHash) {
        if (commitExists(currentHash)) {
            Commit currentCommit = Commit.commitFromFile(currentHash);
            if (currentCommit.containsKey(fileName)) {
                String fileHash = currentCommit.retrieveFile(fileName);
                try {
                    File source = Utils.join(FILE_DIR, fileHash);
                    File destination = Utils.join(CWD, fileName);
                    Files.copy(source.toPath(), destination.toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File does not exist in that commit.");
            }
        } else {
            System.out.println("No commit with that id exists.");
        }
    }

    public static void checkOutBranch(String branchName) {
        if (branchExists(branchName)) {
            if (!alreadyCurrentBranch(branchName)) {
                if (failureCaseThree(branchName)) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                } else {
                    Commit.clearDirectory(CWD);
                    Commit.clearDirectory(ADDITION_DIR);
                    Commit.clearDirectory(REMOVAL_DIR);
                    repopulateCWD(retrieveBranchHash(branchName));
                    setActiveBranch(branchName);
                }
            } else {
                System.out.println("No need to checkout the current branch.");
            }
        } else {
            System.out.println("No such branch exists.");
        }
    }

    public static void reset(String commitHash) {
        if (failureCaseThree(commitHash)) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            return;
        }
        if (!commitExists(commitHash)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit.clearDirectory(CWD);
        Commit.clearDirectory(ADDITION_DIR);
        Commit.clearDirectory(REMOVAL_DIR);
        repopulateCWD(commitHash);
        writeActiveBranch(commitHash);
    }

    public static void removeBranch(String branchName) {
        if (isCurrentBranch(branchName)) {
            System.out.println("Cannot remove the current branch.");
        }
        if (noBranchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
        }
        File currentFile = Utils.join(BRANCHES_DIR, branchName);
        currentFile.delete();
    }
    //*Merge merges the current branch and the input branch.
    // First check (failure case) is if head commit for both branches is the same commit,
    // Then merge is complete and no need to combine
    //
    // Failure case 2: Honestly return to*/

    //*Structure:
    //
    // 0.Have to create a helper method that will idenity the commit hash
    // of the LCA commit.Might have to use BFS? Written alot on Ed.com
    // either that or can attempt to use own finder system. Doesn't necessarily have to be
    //shortest path, can instead be pull parent hashes until match found.
    //^Investigate this now.
    //
    // 1. Main body of merge creates 4 arrays, 1 for file names
    // 3 for corresponding file hash for each file.
    // (3 sets are LCA, Active Branch, Input Branch) <-- most recent commit for each
    // If file is not present in above three cases commit,
    // input the corresponding entry in that set to "null"
    //
    // 2. Create a for loop structure that cycles through the arrays
    // (all are ofc the same length), and inputs one value from each into
    // a merge action helper method that will apply the CWD changes if needed.
    // Will work with all 4 inputs to apply changes and determine the version case.
    //
    //3. Final helper methods should be stage for addition case or stage for removal case.
    // These are the version action helper methods that will modify the CWD and staging area
    // as needed depending on the filtering of the 4 arg method.
    // Creating a way to reuse this kind of code is best.
    //
    // That's really it.*/
    //

    public static void merge(String branchName) {
        //*Step 1 */
        if (!branchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        String activeHash = retrieveBranchHash(getHeadPointer());
        String inputHash = retrieveBranchHash(branchName);
        if (!stagingAreaEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (inputHash.equals(activeHash)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        if (failureCaseThree(branchName)) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            return;
        }
        BFS activeBFS = new BFS();
        activeBFS.bfsProcess(activeHash);
        BFS inputBFS = new BFS();
        inputBFS.bfsProcess(inputHash);
        String latestCommonAncestor = BFS.latestCommonAncestor(activeBFS, inputBFS);
        /* step 1 completed by this point!*/
        if (latestCommonAncestor.equals("failure case 1")) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (latestCommonAncestor.equals("failure case 2")) {
            System.out.println("Current branch fast-forwarded.");
            Commit.clearDirectory(CWD);
            Commit.clearDirectory(ADDITION_DIR);
            Commit.clearDirectory(REMOVAL_DIR);
            repopulateCWD(retrieveBranchHash(branchName));
            writeActiveBranch(inputHash);
            return;
        }
        String[] allFilesArray  = createAllFilesArray(latestCommonAncestor,
                activeHash, inputHash);
        boolean toConflictOrNotToConflict = false;
        for (int i = 0; i < allFilesArray.length; i++) {
            String merged = mergeDoEverythingFunction(allFilesArray[i],
                    latestCommonAncestor, activeHash, inputHash);
            if (merged.equals("yes")) {
                toConflictOrNotToConflict = true;
            }
        }
        if (toConflictOrNotToConflict) {
            System.out.println("Encountered a merge conflict.");
        }
        //*last step is to call a commit with the specific merge message*/
        Repository.commit("Merged " + branchName + " into " + getHeadPointer() + ".");
    }

    public static String mergeDoEverythingFunction(String fN, String lH, String aH, String iH) {
        String merged = "no";
        HashMap<String, String> activeFileMap = Commit.commitFromFile(aH).getFileRecord();
        HashMap<String, String> inputFileMap = Commit.commitFromFile(iH).getFileRecord();
        HashMap<String, String> L = Commit.commitFromFile(lH).getFileRecord();
        //*these are the three blob hashes for this file call */
        String active = retrieveBlobHash(fN, activeFileMap);
        String input = retrieveBlobHash(fN, inputFileMap);
        String lca = retrieveBlobHash(fN, L);
        if (allHashesPresent(active, input, lca)) {
            if (sameHash(lca, active) && !sameHash(lca, input)) {
                Repository.checkOut(fN, iH);
                Repository.stageForAddition(fN);
                return merged;
            }
            if (!sameHash(lca, active) && sameHash(lca, input)) {
                return merged;
            }
            if (!sameHash(lca, active) && !sameHash(lca, input)) {
                if (sameHash(active, input)) {
                    /* CASE 3 modified Same
                    /*modified same means do nothing */
                    return merged;
                } else {
                    /* CASE 3 modified different */
                    handleConflict(fN, active, input);
                    merged = "yes";
                    return merged;
                }
            }
        }
        if (!lca.equals("not present") && input.equals("not present")
                && active.equals("not present")) {
            return merged;
        }
        if (!lca.equals("not present")) {
            if (input.equals("not present") && !sameHash(active, lca)) {
                /* CASE 3 modified different*/
                handleConflict(fN, active, input);
                merged = "yes";
                return merged;
            }
            if (!sameHash(input, lca) && active.equals("not present")) {
                /* CASE 3 deleted different*/
                handleConflict(fN, active, input);
                merged = "yes";
                return merged;
            }
        } else {
            if (!input.equals("not present") && !active.equals("not present")
                    && !sameHash(active, input)) {
                /* CASE 3 modified different*/
                handleConflict(fN, active, input);
                merged = "yes";
                return merged;
            }
        }
        if (lca.equals("not present") && input.equals("not present")
                && !active.equals("not present")) {
            return merged;
        }
        if (lca.equals("not present") && !input.equals("not present")
                && active.equals("not present")) {
            Repository.checkOut(fN, iH);
            Repository.stageForAddition(fN);
            return merged;
        }
        if (sameHash(lca, active) && input.equals("not present")) {
            Repository.remove(fN);
            Repository.stageForRemoval(fN);
            return merged;
        }
        if (sameHash(lca, input) && active.equals("not present")) {
            return merged;
        }
        return merged;
    }

    public static void handleConflict(String fileName, String active, String input) {
        File mergeFile = Utils.join(CWD, fileName);
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        try {
            mergeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!active.equals("not present") && !input.equals("not present")) {
            /*case 3 2 a and case 3 2 c*/
            File activeFile = retrieveBlob(active);
            File inputFile = retrieveBlob(input);
            String activeContents = readContentsAsString(activeFile);
            String inputContents = readContentsAsString(inputFile);
            writeContents(mergeFile, "<<<<<<< HEAD" + '\n' + activeContents
                    + "=======" + '\n' + inputContents + ">>>>>>>" + '\n');
        } else if (!active.equals("not present")) {
            /*case 3 2 b */
            File activeFile = retrieveBlob(active);
            String activeContents = readContentsAsString(activeFile);
            writeContents(mergeFile, "<<<<<<< HEAD" + '\n' + activeContents
                    + "=======" + '\n' + ">>>>>>>" + '\n');
        } else if (!input.equals("not present")) {
            /*case 3 2 b */
            File inputFile = retrieveBlob(input);
            String inputContents = readContentsAsString(inputFile);
            writeContents(mergeFile, "<<<<<<< HEAD" + '\n'
                    + "=======" + '\n' + inputContents + ">>>>>>>" + '\n');
        }
        stageForAddition(fileName);
    }

    public static File retrieveBlob(String fileHash) {
        return Utils.join(FILE_DIR, fileHash);
    }

    public static boolean allHashesPresent(String active, String input, String lca) {
        if (!active.equals("not present")) {
            if (!input.equals("not present")) {
                return !lca.equals("not present");
            }
        }
        return false;
    }

    public static boolean sameHash(String input1, String input2) {
        return input1.equals(input2);
    }

    public static String retrieveBlobHash(String fileName, HashMap fileRecord) {
        if (fileRecord.containsKey(fileName)) {
            return (String) fileRecord.get(fileName);
        }
        return "not present";
    }

    public static String[] createAllFilesArray(String lcaHash,
                                               String activeHash, String inputHash) {
        String[] lcaFiles = retrieveAllFiles(lcaHash);
        String[] activeFiles = retrieveAllFiles(activeHash);
        String[] inputFiles = retrieveAllFiles(inputHash);
        HashSet<String> allFiles = new HashSet<>();
        for (int i = 0; i < lcaFiles.length; i++) {
            allFiles.add(lcaFiles[i]);
        }
        for (int i = 0; i < activeFiles.length; i++) {
            allFiles.add(activeFiles[i]);
        }
        for (int i = 0; i < inputFiles.length; i++) {
            allFiles.add(inputFiles[i]);
        }
        return allFiles.toArray(new String[0]);
    }

    public static String[] retrieveAllFiles(String commitHash) {
        Commit currentCommit = Commit.commitFromFile(commitHash);
        return currentCommit.getFileRecord().keySet().toArray(new String[0]);
    }




    public static boolean isCurrentBranch(String branchName) {
        String activeBranch = getHeadPointer();
        return activeBranch.equals(branchName);
    }

    public static boolean noBranchExists(String branchName) {
        String[] allBranches = allFilesArray(BRANCHES_DIR);
        for (int i = 0; i < allBranches.length; i++) {
            if (allBranches[i].equals(branchName)) {
                return false;
            }
        }
        return true;
    }

    public static void repopulateCWD(String commitHash) {
        Commit commitObject = Commit.commitFromFile(commitHash);
        HashMap<String, String> fileRecord = commitObject.getFileRecord();
        Object[] keysInFileRecord = fileRecord.keySet().toArray();
        for (int i = 0; i < keysInFileRecord.length; i++) {
            String fileHash = fileRecord.get(keysInFileRecord[i]);
            try {
                File source = Utils.join(FILE_DIR, fileHash);
                File dest = Utils.join(CWD, (String) keysInFileRecord[i]);
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean failureCaseThree(String branchName) {
        String[] cwdArray = allFilesArray(CWD);
        LinkedList<String> untrackedFiles = new LinkedList<>();
        for (int i = 0; i < cwdArray.length; i++) {
            if (!presentInLastCommit(cwdArray[i])) {
                untrackedFiles.add(cwdArray[i]);
            }
        }
        if (untrackedFiles.size() == 0) {
            return false;
        }
        if (!noBranchExists(branchName)) {
            return willBeOverwritten(branchName, untrackedFiles);
        } else if (commitExists(branchName)) {
            return willOverwriteRM(branchName, untrackedFiles);
        }
        return false;
    }

    public static boolean willOverwriteRM(String commitHash, LinkedList<String> untrackedFiles) {
        Commit theCommit = Commit.commitFromFile(commitHash);
        for (int i = 0; i < untrackedFiles.size(); i++) {
            if (theCommit.getFileRecord().containsKey(untrackedFiles.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean willBeOverwritten(String branchName, LinkedList<String> untrackedFiles) {
        Commit mostRecentCommitFromBranch = Commit.commitFromFile(retrieveBranchHash(branchName));
        for (int i = 0; i < untrackedFiles.size(); i++) {
            if (mostRecentCommitFromBranch.getFileRecord().containsKey(untrackedFiles.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean presentInLastCommit(String fileName) {
        Commit mostRecentCommit = Commit.commitFromFile(retrieveBranchHash(getHeadPointer()));
        HashMap<String, String> fileRecord = mostRecentCommit.getFileRecord();
        if (fileRecord.containsKey(fileName)) {
            return true;
        }
        return false;
    }

    public static boolean alreadyCurrentBranch(String branchName) {
        return getHeadPointer().equals(branchName);
    }

    public static boolean commitExists(String currentHash) {
        String[] allCommitsArray = allFilesArray(COMMIT_FOLDER);
        if (currentHash.length() == 40) { /* aka unabbreviated hash*/
            for (int i = 0; i < allCommitsArray.length; i++) {
                if (allCommitsArray[i].equals(currentHash)) {
                    return true;
                }
            }
            return false;
        }
        if (currentHash.length() < 40 && currentHash.length() >= 6) {
            int length = currentHash.length();
            for (int i = 0; i < allCommitsArray.length; i++) {
                if (allCommitsArray[i].substring(0, length).equals(currentHash)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static void returnLog() {
        String headHash = retrieveBranchHash(getHeadPointer());
        displayLog(headHash);
    }

    public static void returnGlobalLog() {
        String[] allCommitsArray = allFilesArray(COMMIT_FOLDER);
        for (int i = 0; i < allCommitsArray.length; i++) {
            printLogObject(allCommitsArray[i]);
        }
    }

    public static void displayLog(String currentHash) {
        if (currentCommitMessage(currentHash).equals("initial commit")) {
            printLogObject(currentHash);
            return;
        } else {
            printLogObject(currentHash);
            displayLog(parentCommitHash1(currentHash));
        }
    }

    public static void printLogObject(String currentCommitHash) {
        System.out.println("===");
        System.out.println("commit " + currentCommitHash);
        System.out.println("Date: " + currentCommitTimestamp(currentCommitHash));
        System.out.println(currentCommitMessage(currentCommitHash) + "\n");
    }


    public static void find(String commitMessage) {
        boolean foundMatch = false;
        String[] commitsArray = Utils.allFilesArray(Repository.COMMIT_FOLDER);
        for (int i = 0; i < commitsArray.length; i++) {
            String currentCommitMessage = currentCommitMessage(commitsArray[i]);
            if (commitMessage.equals(currentCommitMessage)) {
                System.out.println(commitsArray[i]);
                foundMatch = true;
            }
        }
        if (!foundMatch) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void printStatusObject() {
        System.out.println("=== Branches ===");
        printLexigraphicalOrder(BRANCHES_DIR);
        System.out.println("=== Staged Files ===");
        if (ADDITION_DIR.list().length != 0) {
            printLexigraphicalOrder(ADDITION_DIR);
        } else {
            System.out.println();
        }
        System.out.println("=== Removed Files ===");
        if (REMOVAL_DIR.list().length != 0) {
            printLexigraphicalOrder(REMOVAL_DIR);
        } else {
            System.out.println();
        }
        System.out.println("=== Modifications Not Staged For Commit ===" + "\n");
        System.out.println("=== Untracked Files ===" + "\n");
    }

    public static void printLexigraphicalOrder(File directory) { //* here*/
        String[] allFiles = directory.list();
        printSmallest(allFiles);
    }

    public static void printSmallest(String[] files) {
        if (files.length == 1) {
            if (files[0].equals(getHeadPointer())) {
                System.out.print("*");
            }
            System.out.println(files[0] + "\n");
        } else {
            int smallestindex = 0;
            for (int i = 0; i < files.length; i++) {
                if (files[smallestindex].compareTo(files[i]) > 0) {
                    smallestindex = i;
                }
            }
            if (files[smallestindex].equals(getHeadPointer())) {
                System.out.print("*");
            }
            System.out.println(files[smallestindex]);
            String[] newArray = new String[files.length - 1];
            for (int i = 0, j = 0; i < files.length; i++) {
                if (i != smallestindex) {
                    newArray[j++] = files[i];
                }
            }
            printSmallest(newArray);
        }
    }

    public static boolean branchExists(String branchName) {
        String[] branchesArray = Utils.allFilesArray(BRANCHES_DIR);
        for (int i = 0; i < branchesArray.length; i++) {
            if (branchesArray[i].equals(branchName)) {
                return true;
            }
        }
        return false;
    }

    public static String currentCommitTimestamp(String currentHash) {
        return Commit.commitFromFile(currentHash).getTimestamp();
    }

    public static String parentCommitHash1(String currentHash) {
        return Commit.commitFromFile(currentHash).getParentHashes()[0];
    }

    public static String currentCommitMessage(String currentHash) {
        return Commit.commitFromFile(currentHash).getMessage();
    }

    public static void removefromCWD(String fileName) {
        File currentFile = Utils.join(CWD, fileName);
        if (currentFile.exists()) {
            currentFile.delete();
        }
    }

    public static boolean trackedInMostRecentCommit(String fileName) {
        String mostRecentHash = mostRecentCommittedVersion(fileName);
        return !mostRecentHash.equals("null");
    }

    public static void stageForRemoval(String fileName) {
        if (presentInCWD(fileName)) {
            try {
                File source = Utils.join(CWD, fileName);
                File dest = Utils.join(REMOVAL_DIR, fileName);
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File makeFile = Utils.join(REMOVAL_DIR, fileName);
                makeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean presentInCWD(String fileName) {
        File currentFile = Utils.join(CWD, fileName);
        if (currentFile.exists()) {
            return true;
        }
        return false;
    }

    public static boolean stagedForAddition(String fileName) {
        String[] additionArray = Utils.allFilesArray(Repository.ADDITION_DIR);
        for (int i = 0; i < additionArray.length; i++) {
            if (additionArray[i].equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean failsStagedAndTracked(String fileName) {
        return !stagedForAddition(fileName) && !trackedInMostRecentCommit(fileName);
    }

    public static boolean fileDoesntExist(String fileName) {
        File currentFile = Utils.join(CWD, fileName);
        if (!currentFile.exists()) {
            return true;
        }
        return false;
    }
    public static boolean checkIfInStagingArea(String fileName) {
        String[] additionArray = Utils.allFilesArray(Repository.ADDITION_DIR);
        for (int i = 0; i < additionArray.length; i++) {
            if (additionArray[i].equals(fileName)) {
                return true;
            }
        }
        String[] removalArray = Utils.allFilesArray(Repository.REMOVAL_DIR);
        for (int i = 0; i < removalArray.length; i++) {
            if (removalArray[i].equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public static void removeFromAddition(String fileName) {
        File currentFile = Utils.join(ADDITION_DIR, fileName);
        currentFile.delete();
    }

    public static void removeFromStagingArea(String fileName) {
        File currentFile = Utils.join(ADDITION_DIR, fileName);
        File potentialFile = Utils.join(REMOVAL_DIR, fileName);
        currentFile.delete();
        potentialFile.delete();
    }


    public static void commit(String s) {
        if (stringEmpty(s)) {
            System.out.println("Please enter a commit message.");
            return;
        }
        if (!stagingAreaEmpty()) {
            Commit currentCommit = Commit.createCommit(s);
            currentCommit.saveCommit();
        } else {
            System.out.println("No changes added to the commit.");
        }
    }

    public static boolean stringEmpty(String s) {
        if (s.trim().length() == 0) {
            return true;
        }
        return false;
    }
    public static void writeHeadPointer(String currentHeadBranch) {
        File headPointer = Utils.join(POINTERS_DIR, "Head");
        Utils.writeContents(headPointer, currentHeadBranch);
    }

    public static void writeActiveBranch(String commitHash) {
        if (commitHash.length() < 40 && commitHash.length() >= 6) {
            String[] allCommitsArray = allFilesArray(Repository.COMMIT_FOLDER);
            int length = commitHash.length();
            for (int i = 0; i < allCommitsArray.length; i++) {
                if (commitHash.equals(allCommitsArray[i].substring(0, length))) {
                    commitHash = allCommitsArray[i];
                }
            }
        }
        String activeBranch = getHeadPointer();
        writeBranch(activeBranch, commitHash);
    }

    public static void writeBranch(String branchName, String commitHash) {
        File currentBranch = Utils.join(BRANCHES_DIR, branchName);
        Utils.writeContents(currentBranch, commitHash);
    }

    public static String getHeadPointer() {
        File headPointer = Utils.join(POINTERS_DIR, "Head");
        return Utils.readContentsAsString(headPointer);
    }

    public static boolean changedSinceLastCommit(String file) {
        File currentFile = Utils.join(CWD, file);
        String currentHash = Commit.createHashFile(currentFile);
        String mostRecentHash = mostRecentCommittedVersion(file);
        return !currentHash.equals(mostRecentHash);
    }

    public static String mostRecentCommittedVersion(String fileName) {
        String activeBranchHash = retrieveBranchHash(getHeadPointer());
        Commit mostRecentCommit = Commit.commitFromFile(activeBranchHash);
        String fileHash = mostRecentCommit.findFileRecord(fileName);
        return fileHash;
    }

    public static String retrieveBranchHash(String branchName) {
        File activeBranchObject = Utils.join(BRANCHES_DIR, branchName);
        return readContentsAsString(activeBranchObject);
    }


    public static void stageForAddition(String file) {
        try {
            File source = Utils.join(CWD, file);
            File dest = Utils.join(ADDITION_DIR, file);
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean stagingAreaEmpty() {
        String[] filesInAddition = allFilesArray(ADDITION_DIR);
        String[] filesInRemoval = allFilesArray(REMOVAL_DIR);
        if (filesInRemoval.length == 0 && filesInAddition.length == 0) {
            return true;
        }
        return false;
    }
}
