package seedu.todolist.ui;

import java.util.Date;
import java.util.logging.Logger;

import org.controlsfx.control.StatusBar;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import seedu.todolist.commons.core.LogsCenter;
import seedu.todolist.commons.events.model.TodoListChangedEvent;
import seedu.todolist.commons.events.storage.SaveFilePathChangedEvent;
import seedu.todolist.commons.util.FxViewUtil;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {
    private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);

    @FXML
    private StatusBar syncStatus;
    @FXML
    private StatusBar saveLocationStatus;

    private static final String FXML = "StatusBarFooter.fxml";

    public StatusBarFooter(AnchorPane placeHolder, String saveLocation) {
        super(FXML);
        addToPlaceholder(placeHolder);
        setSyncStatus("Not updated yet in this session");
        setSaveLocation("./" + saveLocation);
        registerAsAnEventHandler(this);
    }

    private void addToPlaceholder(AnchorPane placeHolder) {
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(getRoot());
    }

    private void setSaveLocation(String location) {
        this.saveLocationStatus.setText(location);
    }

    private void setSyncStatus(String status) {
        this.syncStatus.setText(status);
    }

    @Subscribe
    public void handleTodoListChangedEvent(TodoListChangedEvent abce) {
        String lastUpdated = (new Date()).toString();
        logger.info(LogsCenter.getEventHandlingLogMessage(abce, "Setting last updated status to " + lastUpdated));
        setSyncStatus("Last Updated: " + lastUpdated);
    }

    //@@author A0163720M
    @Subscribe
    public void handleSaveFileChangedEvent(SaveFilePathChangedEvent evt) {
        String lastUpdated = (new Date()).toString();
        setSaveLocation(evt.saveFilePath);
        logger.info(LogsCenter.getEventHandlingLogMessage(evt, "Setting last updated status to " + lastUpdated));
        setSyncStatus("Last Updated: " + lastUpdated);
    }
}