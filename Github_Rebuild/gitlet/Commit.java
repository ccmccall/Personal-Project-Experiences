package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;


/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author Claire McCallick
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /**The timestamp for this commit */
    private String timestamp;
    /**parent commit's hash, at worst case will have 2*/
    private String[] parentHashes;
    /*Array to store all fileIds in that commit version */
    private HashMap<String, String> fileRecord;

    /** All commit structure Classes **/

    /*CommitNodes Structure so that each Commit is it's own Node on the Commit Tree
    * Each commit node also contains a TreeMap that holds fileNodes for each file's address
    * In the commit tree folder system directory*/

    /*Commit constructor for calling initial commit only*/
    public Commit() {
        this.message = "initial commit";   /*the string name for every fist commit should be this */
        this.timestamp = (new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z")).format(new Date(0));
        this.parentHashes = new String[] {"null", "null"};
        this.fileRecord = new HashMap<>();
    }

    /**Commit constructor for copying off of last commit
     * Fits all versions that is not initial commit*/
    public static Commit createCommit(String message) {
        String hash = Repository.retrieveBranchHash(Repository.getHeadPointer());
        Commit copiedCommitObject = commitFromFile(hash);
        copiedCommitObject.updateFromStagingArea(message);
        return copiedCommitObject;
    }

    public void updateFromStagingArea(String commitMessage) {
        this.message = commitMessage;
        this.timestamp = (new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z")).format(new Date());
        if (mergeCheck(commitMessage)) {
            String[] mergeMessage = commitMessage.split("\\W+");
            String branchName = mergeMessage[1];
            this.parentHashes[1] = Repository.retrieveBranchHash(branchName);
        }
        this.parentHashes[0] = Repository.retrieveBranchHash(Repository.getHeadPointer());
        this.fileRecord = updateFileRecord(this.fileRecord);
    }


    public static boolean mergeCheck(String commitMessage) {
        if (commitMessage.contains("Merged")) {
            return true;
        }
        return false;
    }

    public String retrieveFile(String fileName) {
        return this.fileRecord.get(fileName);
    }
    public boolean containsKey(String fileName) {
        return this.fileRecord.containsKey(fileName);
    }


    public HashMap<String, String> updateFileRecord(HashMap<String, String> previousRec) {
        String[] additionArray = Utils.allFilesArray(Repository.ADDITION_DIR);
        String[] removalArray = Utils.allFilesArray(Repository.REMOVAL_DIR);
        if (additionArray.length != 0) {
            for (int i = 0; i < additionArray.length; i++) {
                String fileName = additionArray[i];
                File currentFile = Utils.join(Repository.ADDITION_DIR, fileName);
                String fileHash = createHashFile(currentFile);
                addFileInstanceToFilesFolder(currentFile, fileHash);
                previousRec.put(fileName, fileHash);
            }
        }
        if (removalArray.length != 0) {
            for (int i = 0; i < removalArray.length; i++) {
                String fileName = removalArray[i];
                previousRec.remove(fileName);
            }
        }
        clearDirectory(Repository.ADDITION_DIR);
        clearDirectory(Repository.REMOVAL_DIR);
        return previousRec;
    }

    public static void addFileInstanceToFilesFolder(File currentFile, String hashName) {
        try {
            File sourceFile = Utils.join(Repository.FILE_DIR, hashName);
            Files.copy(currentFile.toPath(), sourceFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearDirectory(File directory) {
        String[] allFilesInDirectory = Utils.allFilesArray(directory);
        for (int i = 0; i < allFilesInDirectory.length; i++) {
            File currentFile = Utils.join(directory, allFilesInDirectory[i]);
            currentFile.delete();
        }
    }

    /**def might have issues here with returning null values if not present in hashMap*/
    public String findFileRecord(String fileName) {
        String fileHash = String.valueOf(this.fileRecord.get(fileName));
        return fileHash;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String[] getParentHashes() {
        return this.parentHashes;
    }

    public HashMap<String, String> getFileRecord() {
        return this.fileRecord;
    }

    /*Blob/File Node Structure to store in CommitNode TreeMap
    * Each fileNode has the name of the file, and what commit it was stored in
    * Each commit hash corresponds to a folder in the .gitlet commit folder
    * Which holds all compressed files that were in the CWD at the time of commmit */

    /*Writing the commit object to the commits_folder, persistence storage */
    public void saveCommit() {
        String commitHash = this.createHashCommit();
        Repository.writeActiveBranch(commitHash);
        this.commitToFile(commitHash);
    }

    public void commitToFile(String name) {
        Utils.writeObject(Utils.join(Repository.COMMIT_FOLDER, name), this);
    }

    public static Commit commitFromFile(String hash) {
        if (hash.length() < 40 && hash.length() >= 6) {
            String[] allCommitsArray = allFilesArray(Repository.COMMIT_FOLDER);
            int length = hash.length();
            for (int i = 0; i < allCommitsArray.length; i++) {
                if (hash.equals(allCommitsArray[i].substring(0, length))) {
                    File commitObject = Utils.join(Repository.COMMIT_FOLDER, allCommitsArray[i]);
                    return Utils.readObject(commitObject, Commit.class);
                }
            }
        }
        return Utils.readObject(Utils.join(Repository.COMMIT_FOLDER, hash), Commit.class);
    }

    public String createHashCommit() {
        return Utils.sha1(serialize(this));
    }

    public static String createHashFile(File currentFile) {
        return Utils.sha1(readContentsAsString(currentFile));
    }
}
