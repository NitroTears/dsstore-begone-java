package dsstorebegone;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;

public class Controller {
    DriveOperations op = new DriveOperations();

    @FXML
    private TextField driveInput;
    @FXML
    private Label driveInfo;
    @FXML
    private TextArea deletedFilesList = new TextArea("");
    @FXML
    private Button startButton;

    public Controller() throws IOException {
    }

    public void initialize() {
        deletedFilesList.clear();
        startButton.setOnAction((event) -> {
            if (driveInput.getText().length() != 1 || !driveInput.getText().chars().allMatch(Character::isLetter)) {
                driveInfo.setText("(Incorrect Drive Letter Input)");

            } else {
                driveInfo.setText("(Please enter a single letter to represent the drive.)");
                try {
                    startDelete(driveInput.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                    driveInfo.setText("(An Error has occurred, check console.)");
                }
            }

        });

    }
    //Manages the deletion of files and folders
    public void startDelete(String driveLetter) throws IOException {
        op.initialise(driveLetter);
        File drive = op.getDrive();
        //Folder Deletion Segment
        if (op.deleteFseventsd(drive)) {
            op.setFilesListString(op.getFilesListString() + ".fseventsd Folder Deleted. \r \n");
            deletedFilesList.setText(op.getFilesListString());
        }
        if (op.deleteTrashes(drive)) {
            op.setFilesListString(op.getFilesListString() + ".Trashes Folder Deleted. \r \n");
            deletedFilesList.setText(op.getFilesListString());
        }
        if (op.deleteSpotlightV100(drive)) {
            op.setFilesListString(op.getFilesListString() + ".Spotlight-V100 Folder Deleted. \r \n");
            deletedFilesList.setText(op.getFilesListString());
        }
        //File Deletion Segment
        deleteFiles(drive);

        //Finishing Segment
        if (!op.Finish()) {
            op.setFilesListString(op.getFilesListString() + "No files were deleted. \r \n");
        } else {
            op.setFilesListString(op.getFilesListString() + "Operation successful! " + op.getNumOfFiles() + " file(s) deleted.\r \nA list of deleted files can be found in deletedfiles.txt. \r \n");
        }
        deletedFilesList.setText(op.getFilesListString());
    }

    public void deleteFiles(File drive) throws IOException {
        File[] files = drive.listFiles();
        for (File file : files) {
            if (file.isDirectory() && !file.getName().startsWith("$")) {
                deleteFiles(file);
            } else if (file.getName().startsWith("._") || file.getName().startsWith(".DS_Store")) {
                file.delete();
                op.setNumOfFiles(op.getNumOfFiles() + 1);
                op.writer.write(file.toString() + "\r \n");
                op.setFilesListString(op.getFilesListString() + file.toString() + "\r \n");
            }
        }
    }
}
