package dream.team.pongfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
    int ballCenterX = 10;
    int ballCurrentSpeedX = 3; 
    int ballCenterY = 30;
    int ballCurrentSpeedY = 3; 
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 600, 400);
        scene.setFill(Color.BLACK);
        primaryStage.setTitle("PongFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        Circle circleBall = new Circle(ballCenterX, ballCenterY, 7, Color.WHITE);
        root.getChildren().add(circleBall);

        Timeline animationBall = new Timeline(
            new KeyFrame(Duration.millis(17), ae -> {
                // Actualización de la posición de la pelota
                circleBall.setCenterX(ballCenterX);
                circleBall.setCenterY(ballCenterY);

                // Movimiento en X
                ballCenterX += ballCurrentSpeedX;
                if (ballCenterX >= 600 || ballCenterX <= 0) {
                    ballCurrentSpeedX *= -1; // Cambiar dirección en X
                }

                // Movimiento en Y
                ballCenterY += ballCurrentSpeedY;
                if (ballCenterY >= 400 || ballCenterY <= 0) {
                    ballCurrentSpeedY *= -1; // Cambiar dirección en Y
                }
            })
        );

        animationBall.setCycleCount(Timeline.INDEFINITE);
        animationBall.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
