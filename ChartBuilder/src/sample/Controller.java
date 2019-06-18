package sample;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller {

    public static double a;
    public static double b;
    public static double step;
    public static boolean stepSmall = false;
    public static boolean stepBig = false;
    public static double x;
    public static double y;
    public static double fiCur;
    public static double fiMin = 0;
    public static double fiMax = 4*Math.PI; //fi є [0, 4pi]

    @FXML private LineChart<Number, Number> lineChart;
    @FXML private Button buildButton;
    @FXML private TextField aArea;
    @FXML private TextField bArea;
    @FXML private TextField stepArea;
    private Stage dialogStage;

    //x = a*fi - b*sin(fi);
    //y = a - n*cos(fi);

    @FXML
    void build(ActionEvent event) {

        try{
            a = Double.valueOf(aArea.getText());
            b = Double.valueOf(bArea.getText());
            step = Double.valueOf(stepArea.getText());

            if(a != b){
                throw new IllegalArgumentException();
            }
            if(step > 3.2){
                stepBig = true;
                throw new IllegalArgumentException();
            }else if(step <= 0.005){
                stepSmall = true;
                throw new IllegalArgumentException();
            }
        }catch(IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            if(a != b){
                alert.setContentText("Для циклоїди значення a та b повинні бути однакові.");
            }else if(stepBig){
                alert.setContentText("Зменшіть крок.");
                stepBig = false;
            }else if(stepSmall){
                alert.setContentText("Збільшіть крок.");
                stepSmall = false;
            }else{
                alert.setContentText("Введіть коректні значення.");
            }
            alert.showAndWait();
            return;
        }

        lineChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (fiCur = fiMin; fiCur <= fiMax; fiCur += step){
            x = a*fiCur - b*Math.sin(fiCur);
            y = a - b*Math.cos(fiCur);
            series.getData().add(new XYChart.Data<>(x, y));
        }
        series.setName("FUNCTION");
        lineChart.getData().add(series);
    }

    @FXML
    void save(ActionEvent event) {
        WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show save file dialog
        File file = fileChooser.showSaveDialog(this.dialogStage);
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".png")) {
                file = new File(file.getPath() + ".png");
            }
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
            }
        }
    }

}
