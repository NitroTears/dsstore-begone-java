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
    //controller initialisation.
    public void initialize() {
        deletedFilesList.clear();
        startButton.setOnAction((event) -> {
            if (!driveInput.getText().matches("[a-zA-Z]")) {
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
    //the main process of file and folder deletion, run when the execute button is pressed.
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
        //Files Deletion Segment
        op.deleteFiles(drive);

        //Finishing Segment
        if (!op.Finish()) {
            op.setFilesListString(op.getFilesListString() + "No files were deleted. \r \n");
        } else {
            op.setFilesListString(op.getFilesListString() + "Operation successful! " + op.getNumOfFiles() + " file(s) deleted.\r \nA list of deleted files can be found in deletedfiles.txt. \r \n");
        }
        deletedFilesList.setText(op.getFilesListString());
    }
}
