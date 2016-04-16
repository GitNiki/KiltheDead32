import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;


public class Main2 extends Application implements Measures{
    public static Scene sceneNewGame;
    private static Group root;
	
	public int record = 0;
	Bullet bulletc;
    Pane pane = new Pane();
    Image backV = new Image("back.jpg");
    ImageView back = new ImageView(backV);
    Image shipV = new Image("ship.png");
    ImageView ship = new ImageView(shipV);
    Image enemiesV = new Image("zombie1.png");
    Image enemiesVI = new Image("zombie2.png");
    private Timeline timelineRightMove,timelineLeftMove;

    static Rectangle pointer = new Rectangle();
    String statusMP;
    ImageView[] enemies = new ImageView[ENEMY_COLUMN * ENEMY_ROW];

    public int MOV = 0;

    boolean rightEnemy = true;
    boolean bulletIsAlive = false;
    boolean newLevel = true;

    int score = 0;
    int updateTime = 28;
    Text punt = new Text("Score: " + score);

    String sRecord = "";
    String n;

    public void again() {
        for (int j = 0; j < ENEMY_ROW; j++) {
            for (int i = 0; i < ENEMY_COLUMN; i++) {
                enemies[j * ENEMY_COLUMN + i] = new ImageView(enemiesV);
                enemies[j * ENEMY_COLUMN + i].setPreserveRatio(true);
                enemies[j * ENEMY_COLUMN + i].setX(i * 50);
                enemies[j * ENEMY_COLUMN + i].setY(j * 50);
                enemies[j * ENEMY_COLUMN + i].setFitWidth(ENEMY_EDGE);
                pane.getChildren().add(enemies[j * ENEMY_COLUMN + i]);
                if (i == ENEMY_COLUMN - 1 && j == 0) {
                    pointer.setWidth(ENEMY_EDGE);
                    pointer.setHeight(ENEMY_EDGE);
                    pointer.setFill(Color.TRANSPARENT);
                    pointer.setX(enemies[i].getX() + ENEMY_EDGE);
                }
            }
        }
        updateTime-=3;
    }

    @Override
    public void start(Stage primaryStage) {

        ship.setPreserveRatio(true);
        ship.setFitWidth(80);
        ship.setX(100);
        ship.setY(550);

        pane.getChildren().add(back);
        pane.getChildren().add(ship);
        pane.getChildren().add(punt);

        punt.setFont(Font.font("Verdana", 20));
        punt.setFill(Color.YELLOW);

        again();

        back.setX(0);
        back.setY(0);

        punt.setX(10);
        punt.setY(20);

        Duration dI = new Duration(updateTime);
        KeyFrame f = new KeyFrame(dI, e -> movementCore());
        Timeline tl = new Timeline(f);
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
        Scene scene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.setOnKeyPressed(e -> {
            try {
                keyboardManage(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });


        primaryStage.setTitle("KillTheDead");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        String k = System.getProperty("user.home");
        n = k + File.separator + "si.txt";
        System.out.println(n);

        try {
            FileReader fr = new FileReader(n);
            BufferedReader br = new BufferedReader(fr);
            sRecord = br.readLine();
            record = Integer.parseInt(sRecord);
            System.out.println("" + record);
            br.close();
            fr.close();
        } catch (Exception e) {
            record = 0;
        }
    }

    public void keyboardManage(KeyEvent ke) throws Exception {// движение исправить
        if (ke.getCode() == KeyCode.D) {
            double x = ship.getX();
            x += 10;
            ship.setX(x);
        } else if (ke.getCode() == KeyCode.A) {
            double x = ship.getX();
            x -= 10;
            ship.setX(x);
        } else if (ke.getCode() == KeyCode.SPACE) {
            bulletc = new Bullet(10, 50, ship.getX(), enemies, pane);
        } else if (ke.getCode() == KeyCode.ESCAPE){
            new Main().start(Main.parentStage);
            System.out.println("exit");
        }
    }

    public void movementCore() {
        if (rightEnemy) {
            if (pointer.getX() + ENEMY_EDGE >= SCREEN_WIDTH) {
                rightEnemy = false;
                for (int i = 0; i < enemies.length; i++) {
                    if (enemies[i] != null) {
                        enemies[i].setY(enemies[i].getY() + 50);
                    }
                }
            }
            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i] != null) {
                    enemies[i].setX(enemies[i].getX() + SPEED);
                }
            }
            pointer.setX(pointer.getX() + SPEED);
        } else {
            if (pointer.getX() - ((ENEMY_EDGE * (ENEMY_COLUMN + 2))) <= 0) {
                rightEnemy = true;
                for (int i = 0; i < enemies.length; i++) {
                    if (enemies[i] != null) {
                        enemies[i].setY(enemies[i].getY() + 50);
                    }
                }
            }
            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i] != null) {
                    enemies[i].setX(enemies[i].getX() - SPEED);
                }
            }
            pointer.setX(pointer.getX() - SPEED);
        }
        MOV++;
        if (MOV == 20) {
            for (int j = 0; j < enemies.length; j++) {
                if (enemies[j] != null) {
                    enemies[j].setImage(enemiesVI);
                }
            }
        } else if (MOV == 40) {
            for (int j = 0; j < enemies.length; j++) {
                if (enemies[j] != null) {
                    enemies[j].setImage(enemiesV);
                }
            }
            MOV = 0;
        }
        if (bulletc != null) {
            score += bulletc.getScore();
            punt.setText("Score: " + score);
        }
        if (score % 50 * 49 == 0 && score >= 50 * 49) {
            if (newLevel) {
                again();
                newLevel = false;
            }
        }
        if (score % 50 * 49 > 0 && score > 50 * 49) {
            newLevel = true;
        }
    }

    
    @Override
    public void stop(){
    	 try {
             FileWriter fw = new FileWriter(n);
             BufferedWriter bw = new BufferedWriter(fw);
             bw.write(""+score);
             bw.close();
             fw.close();
         } catch (Exception e) {
             record = 0;
         }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}