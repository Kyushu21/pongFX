package dream.team.pongfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    int ballCenterX = 10;
    int ballCurrentSpeedX = 3;
    int ballCenterY = 30;
    int ballCurrentSpeedY = 3;

    final int SCENE_TAM_X = 600;
    final int SCENE_TAM_Y = 400;
    final int STICK_WIDTH = 7;
    final int STICK_HEIGHT = 50;
    int stickPosY = (SCENE_TAM_Y - STICK_HEIGHT) / 2;
    int stickCurrentSpeed = 0;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, SCENE_TAM_X, SCENE_TAM_Y, Color.BLACK);
        primaryStage.setTitle("PongFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        Circle circleBall = new Circle(ballCenterX, ballCenterY, 7, Color.WHITE);
        root.getChildren().add(circleBall);

        Rectangle rectStick = new Rectangle(SCENE_TAM_X * 0.9, stickPosY, STICK_WIDTH, STICK_HEIGHT);
        rectStick.setFill(Color.WHITE);
        root.getChildren().add(rectStick);

        Timeline animationBall = new Timeline(
                new KeyFrame(Duration.millis(17), ae -> {
                    // Actualización de la posición de la pelota
                    circleBall.setCenterX(ballCenterX);
                    circleBall.setCenterY(ballCenterY);
                    
                    //Actualizar la posicion de la barra
                    stickPosY += stickCurrentSpeed;
                    if (stickPosY < 0) {
                        //No sobrepasar el borde superior de la ventana
                        stickPosY = 0;
                    } else {
                        //No sobrepasar el borde inferior de la ventana
                        if (stickPosY > SCENE_TAM_Y - STICK_HEIGHT) {
                            stickPosY = SCENE_TAM_Y - STICK_HEIGHT;
                        }
                    }
                    
                    //Mover la barra a la posicion actual
                    rectStick.setY(stickPosY);

                    // Movimiento en X
                    ballCenterX += ballCurrentSpeedX;
                    if (ballCenterX >= SCENE_TAM_X || ballCenterX <= 0) {
                        ballCurrentSpeedX *= -1; // Cambiar dirección en X
                    }

                    // Movimiento en Y
                    ballCenterY += ballCurrentSpeedY;
                    if (ballCenterY >= SCENE_TAM_Y || ballCenterY <= 0) {
                        ballCurrentSpeedY *= -1; // Cambiar dirección en Y
                    }
                    
                    //Movimiento barra al presionar teclas
                    scene.setOnKeyPressed((KeyEvent event) -> {
                        switch (event.getCode()) {
                            case UP:
                                stickCurrentSpeed = -6;
                                break;
                            case DOWN:
                                stickCurrentSpeed = 6;
                                break;
                        }
                    });

                    //Movimiento barra al soltar teclas
                    scene.setOnKeyReleased((KeyEvent event) -> {
                        stickCurrentSpeed = 0;
                    });
                    
                    //Colision de bola en la barra
                    Shape shapeColision = Shape.intersect(circleBall, rectStick);
                    boolean colisionVacia = shapeColision.getBoundsInLocal().isEmpty();
                    if (colisionVacia == false) {
                        //Colision detectada, rebotar bola hacia la izquierda
                        ballCurrentSpeedX = -3;
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
