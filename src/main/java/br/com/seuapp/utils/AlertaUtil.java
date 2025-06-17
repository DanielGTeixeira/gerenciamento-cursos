package br.com.seuapp.utils;
import javafx.scene.control.Alert;
public class AlertaUtil {
    public static void mostrarInfo(String msg) { new Alert(Alert.AlertType.INFORMATION, msg).showAndWait(); }
    public static void mostrarErro(String msg) { new Alert(Alert.AlertType.ERROR, msg).showAndWait(); }
}
