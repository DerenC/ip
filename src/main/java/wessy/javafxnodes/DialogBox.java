package wessy.javafxnodes;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * An example of a custom control using FXML.
 * This control represents a dialog box consisting of an ImageView to represent the speaker's face and a label
 * containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private Pane displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(wessy.javafxnodes.MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        dialog.setPadding(new Insets(10, 10, 10, 10));
        ImageView iv = (ImageView) displayPicture.lookup("#iv");
        iv.setImage(img);
        displayPicture.setClip(new Circle(50, 50, 50));
        this.setBorder(new Border(new BorderStroke(Color.WHITE,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(15),
                new Insets(-7.5, 0, -7.5, 0)
        )));
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    public static DialogBox getUserDialogBox(String text, Image img) {
        return new DialogBox(text, img);
    }

    public static DialogBox getWessyDialogBox(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}