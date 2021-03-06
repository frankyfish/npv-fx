package npv.fx.notifications;

import javafx.scene.control.Alert;

/**
 * Created by atsus on 1/31/2017.
 */
public class UINotification {

    public enum Type{
        INFO, WARN, ERROR;
    }

    public UINotification(Type type, String header, String cause) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        String h = null;
        switch (type) {
            case INFO:
                alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Информационное окно");
                alert.setTitle("Information Window");
                h = header.isEmpty() ? type.toString()
                        : header;
                alert.setHeaderText(h);
                break;
            case WARN:
                alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Предупреждение");
                alert.setTitle("Warning");
                h = header.isEmpty() ? type.toString()
                        : header;
                alert.setHeaderText(h);
                break;
            case ERROR:
                alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Ошибка");
                alert.setTitle("Error");
                h = header.isEmpty() ? type.toString()
                        : header;
                alert.setHeaderText(h);
                break;
        }
//        alert.setContentText("Причина:\n недопустимое значение поля " + cause);
        alert.setContentText("Caused by:\n Unacceptable value of " + cause);
        alert.showAndWait();
    }
}
