package dsstorebegone;


import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * This class contains all information and methods relevant to deleting files from a drive.
 * The letter of the drive to be used for these operations is variable, though what specific files will be deleted
 * will not be. A list of files deleted in an operation will be printed to 'deletedfiles.txt.'
 */
public class DriveOperations {
    FileUtils utils = new FileUtils();
    int numOfFiles = 0; //used to count the number of files successfully deleted.
    File drive; //File object containing the drive selected by the user
    File deletedFiles = new File("deletedfiles.txt");
    String filesListString = ""; //string will be used to display all files deleted in the current operation
    BufferedWriter writer = new BufferedWriter(new FileWriter("deletedfiles.txt", true));

    //DriveOperations requires either a drive object in the constructor, or a driveLetter string in initialise().
    public DriveOperations() throws IOException { }
    //optional drive file can be supplied in constructor, in case of use in other libraries.
    public DriveOperations(File drive) throws IOException {
        this.drive = drive;
    }

    //getters and setters
    public String getFilesListString() {
        return filesListString;
    }
    public void setFilesListString(String filesListString) {
        this.filesListString = filesListString;
    }
    public int getNumOfFiles() {
        return numOfFiles;
    }
    public void setNumOfFiles(int numOfFiles) {
        this.numOfFiles = numOfFiles;
    }
    public File getDrive() {
        return drive;
    }
    public void setDrive(File drive) {
        this.drive = drive;
    }

    //Introductory message to the program, only used for non-GUI use.
    public void printStartMessage() {
        System.out.println("You are about to delete all files starting in '._' and .DS_Store files on the selected drive");
        System.out.println("this also includes the folders '.Trashes', '.Spotlight-V100, and '.fseventsd'.");
        System.out.println("Please select your chosen drive with the letter associated with it");
        System.out.println("Operations will begin as soon as drive is selected");
    }
    //creates a path to the drive using the string argument, and creates the 'deletedfiles.txt' for use with other methods.
    //TODO: check to see if drive exists, if not, cease operations.
    public void initialise(String driveLetter) throws IOException {
        try {
            deletedFiles.createNewFile();
        } catch (Exception e) {
            System.out.println("Could not create deletedfiles.txt");
            e.printStackTrace();
        }

        if (!driveLetter.matches("[a-zA-Z]")) {
            System.out.println("Error: Incorrect drive letter input.");
        } else {
            File drive = new File(driveLetter + ":/");
            setDrive(drive);
        }
    }


    //Lists and deletes files that start with ._ in the selected drive.
    //does not search folders starting with $ as these are usually protected (eg. recycle bin).
    public void deleteFiles(File drive) throws IOException {
        File[] files = drive.listFiles();
        for (File file : files) {
            if (file.isDirectory() && !file.getName().startsWith("$")) {
                deleteFiles(file);
            } else if (file.getName().startsWith("._") || file.getName().startsWith(".DS_Store")) {
                System.out.println(file);
                file.delete();
                setNumOfFiles(getNumOfFiles() + 1);
                writer.write(file.toString() + "\r \n");
                setFilesListString(getFilesListString() + file.toString() + "\r \n");
            }
        }
    }
    //lists and deletes the .fseventsd folder, and everything in it.
    //Returns true if the file was successfully deleted, false otherwise.
    public Boolean deleteFseventsd(File drive) throws IOException {
        File fileF = new File(drive.toString() + "\\.fseventsd");
        if (fileF.exists()) {
            try {
                utils.deleteDirectory(fileF);
                System.out.println(".fseventsd Folder Deleted");
                writer.write(".fseventsd Folder Deleted\n");
                return true;
            } catch (IOException e) {
                System.out.println(".fseventsd folder could not be deleted");
                e.printStackTrace();
                System.out.println("");
                return false;
            }
        } else {
            System.out.println("Extra false statement reached");

            return false;
        }
    }
    //lists and deletes the .Trashes folder, and everything in it.
    //Returns true if the file was successfully deleted, false otherwise.
    public Boolean deleteTrashes(File drive) throws IOException {
        File fileT = new File(drive.toString() + "\\.Trashes");
        if (fileT.exists()) {
            try {
                utils.deleteDirectory(fileT);
                System.out.println(".Trashes Folder Deleted");
                writer.write(".Trashes Folder Deleted\n");
                return true;
            } catch (IOException e) {
                System.out.println(".Trashes folder could not be deleted");
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    //lists and deletes the .Spotlight-V100 folder, and everything in it.
    //Returns true if the file was successfully deleted, false otherwise.
    public Boolean deleteSpotlightV100(File drive) throws IOException {
        File fileS = new File(drive.toString() + "\\.Spotlight-V100");
        if (fileS.exists()) {
            try {
                utils.deleteDirectory(fileS);
                System.out.println(".Spotlight-V100 Folder Deleted");
                writer.write(".Spotlight-V100 Folder Deleted\n");
                return true;
            } catch (IOException e) {
                System.out.println(".Spotlight-V100 folder could not be deleted");
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    //Message stating how many files were deleted, if at all.
    //Returns False if no individual files were deleted, true otherwise.
    public Boolean Finish() throws IOException {
        if (numOfFiles > 0) {
            System.out.println("Operation successful! " + getNumOfFiles() + " file(s) deleted. A list of deleted files can be found in deletedfiles.txt");
            writer.close();
            return true;
        } else {
            System.out.println("No files were deleted.");
            writer.close();
            return false;
        }
    }
}
