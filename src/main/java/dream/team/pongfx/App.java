package dream.team.pongfx;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    Pane root = new Pane();

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

    int score;
    int highScore;
    Text textScore;

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, SCENE_TAM_X, SCENE_TAM_Y, Color.BLACK);
        primaryStage.setTitle("PongFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        Circle circleBall = new Circle(ballCenterX, ballCenterY, 7, Color.WHITE);
        root.getChildren().add(circleBall);

        Rectangle rectStick = new Rectangle(SCENE_TAM_X * 0.9, stickPosY, STICK_WIDTH, STICK_HEIGHT);
        rectStick.setFill(Color.WHITE);
        root.getChildren().add(rectStick);
        // LAYOUTS PARA MOSTRAR PUNTUACIONES
        // Layout principal
        HBox paneScores = new HBox();
        paneScores.setTranslateY(20);
        paneScores.setMinWidth(SCENE_TAM_X);
        paneScores.setAlignment(Pos.CENTER);
        paneScores.setSpacing(100);
        root.getChildren().add(paneScores);

        // Layout para puntuación actual
        HBox paneCurrentScore = new HBox();
        paneCurrentScore.setSpacing(10);
        paneScores.getChildren().add(paneCurrentScore);

        // Layout para puntuación máxima
        HBox paneHighScore = new HBox();
        paneHighScore.setSpacing(10);
        paneScores.getChildren().add(paneHighScore);
        final int TEXT_SIZE = 20;  // Define un tamaño de fuente, por ejemplo 20

        // Texto de etiqueta para la puntuación
        Text textTitleScore = new Text("Score:");
        textTitleScore.setFont(Font.font(TEXT_SIZE));
        textTitleScore.setFill(Color.WHITE);

        // Texto para la puntuación
        textScore = new Text("0");
        textScore.setFont(Font.font(TEXT_SIZE));
        textScore.setFill(Color.WHITE);

        // Texto de etiqueta para la puntuación máxima
        Text textTitleHighScore = new Text("Max Score:");
        textTitleHighScore.setFont(Font.font(TEXT_SIZE));
        textTitleHighScore.setFill(Color.WHITE);

        // Texto para la puntuación máxima
        Text textHighScore = new Text("0");
        textHighScore.setFont(Font.font(TEXT_SIZE));
        textHighScore.setFill(Color.WHITE);

        // Dibujo de la red con los nuevos parámetros
        drawNet(10, 4, 30);
        // Añadiendo textos a los layouts reservados para ellos
        paneCurrentScore.getChildren().add(textTitleScore);
        paneCurrentScore.getChildren().add(textScore);
        paneHighScore.getChildren().add(textTitleHighScore);
        paneHighScore.getChildren().add(textHighScore);

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
                    //Comprobar si la boila ha tocado el lado derecho
                    if (ballCenterX >= SCENE_TAM_X) {
                        if (score > highScore) {
                            //Cambiar nueva puntuacion mas alta
                            highScore = score;
                            textHighScore.setText(String.valueOf(highScore));
                        }
                        //reiniciar partida
                        this.resetGame();

                    }
                    //Dibujo de la red
                    for (int i = 0; i < SCENE_TAM_Y; i += 30) {
                        Line line = new Line(SCENE_TAM_X / 2, i, SCENE_TAM_X / 2, i + 10);
                        line.setStroke(Color.WHITE);
                        line.setStrokeWidth(4);
                        root.getChildren().add(line);
                    }
                    //Colisión de bola en la barra
                    int collisionZone = getStickCollisionZone(circleBall, rectStick);
                    if (collisionZone != 0 && ballCurrentSpeedX > 0) {
                        // Colisión detectada, ajustar velocidad de la bola
                        calculateBallSpeed(collisionZone);
                        score++;
                        textScore.setText(String.valueOf(score));
                    }


                })
        );

        animationBall.setCycleCount(Timeline.INDEFINITE);
        animationBall.play();
    }

    private void drawNet(int portionHeight, int portionWidth, int portionSpacing) {
        for (int i = 0; i < SCENE_TAM_Y; i += portionSpacing) {
            Line line = new Line(SCENE_TAM_X / 2, i, SCENE_TAM_X / 2, i + portionHeight);
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(portionWidth);
            root.getChildren().add(line);
        }
    }

    private void resetGame() {
        score = 0;
        textScore.setText(String.valueOf(score));
        ballCenterX = 10;
        ballCurrentSpeedY = 3;
        //Posicion inicial aleatoria para la bola en el eje Y 
        Random random = new Random();
        ballCenterY = random.nextInt(SCENE_TAM_Y);
    }

    private void calculateBallSpeed(int collisionZone) {
        switch (collisionZone) {
            case 1:
                ballCurrentSpeedX = -3;
                ballCurrentSpeedY = -6;
                break;
            case 2:
                ballCurrentSpeedX = -3;
                ballCurrentSpeedY = -3;
                break;
            case 3:
                ballCurrentSpeedX = -3;
                ballCurrentSpeedY = 3;
                break;
            case 4:
                ballCurrentSpeedX = -3;
                ballCurrentSpeedY = 6;
                break;
        }
    }

    private int getStickCollisionZone(Circle ball, Rectangle stick) {
        if (Shape.intersect(ball, stick).getBoundsInLocal().isEmpty()) {
            return 0;
        } else {
            double offsetBallStick = ball.getCenterY() - stick.getY();
            if (offsetBallStick < stick.getHeight() * 0.1) {
                return 1;
            } else if (offsetBallStick < stick.getHeight() / 2) {
                return 2;
            } else if (offsetBallStick >= stick.getHeight() / 2 && offsetBallStick < stick.getHeight() * 0.9) {
                return 3;
            } else {
                return 4;
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
