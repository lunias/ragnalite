package com.ethanaa.ragnalite;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main extends Application implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final double DEFAULT_ZOOM_DISTANCE = 300.0;
    private static final double MIN_ZOOM_DISTANCE = 220.0;
    private static final double MAX_ZOOM_DISTANCE = 580.0;
    private static final double CAMERA_ANGLE = 26.565;

    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final DoubleProperty cameraTx = camera.translateXProperty();
    private final DoubleProperty cameraTy = camera.translateYProperty();
    private final DoubleProperty cameraTz = camera.translateZProperty();
    private final Rotate cameraRx = new Rotate(CAMERA_ANGLE, Rotate.X_AXIS);

    private final WorldRegion worldRegion = new WorldRegion();

    private final Rotate worldRotate = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    private double mousePosX, mousePosY, mouseOldX, mouseOldY;

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
        launch(args);
    }

    @Override
    public void run(String... args) {

        LOG.info("Welcome to Ragnalite!");
    }

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane main = new BorderPane();
        StackPane stackPane = new StackPane();

        Player player = new Player(worldRegion.getTileNode(50, 50), Orientation.FORWARD, new Rotate());

        SubScene subScene = setupSubScene(main, player);
        subScene.heightProperty().bind(stackPane.heightProperty());
        subScene.widthProperty().bind(stackPane.widthProperty());
        stackPane.getChildren().addAll(subScene);

        main.setCenter(stackPane);

        Scene scene = new Scene(main);

        scene.setOnKeyPressed(ke -> {
            switch (ke.getCode()) {
                case W:
                    // move north
                    System.out.println("Moving North");
                    player.moveNorth(worldRegion);
                    break;
                case A:
                    // move west
                    System.out.println("Moving West");
                    player.moveWest(worldRegion);
                    break;
                case S:
                    // move south
                    System.out.println("Moving South");
                    player.moveSouth(worldRegion);
                    break;
                case D:
                    // move east
                    System.out.println("Moving East");
                    player.moveEast(worldRegion);
                    break;
            }
        });

        stage.setTitle("Ragnalite (alpha)");
        stage.setScene(scene);

        Screen screen = Screen.getPrimary();
        Rectangle2D screenBounds = screen.getVisualBounds();

        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();
        double windowWidth = screenBounds.getWidth() * .8;
        double windowHeight = screenBounds.getHeight() * .8;

        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
        stage.setX((screenWidth - windowWidth) / 2.0);
        stage.setY((screenHeight - windowHeight) / 2.0);

        stage.show();
    }

    private SubScene setupSubScene(Pane parent, Player player) {

        Group worldGroup = new Group(worldRegion, player.getSprite());

        SubScene subScene = new SubScene(worldGroup, -1, -1, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setPickOnBounds(true);

        worldRegion.getTransforms().add(worldRotate);
        worldRotate.pivotXProperty().bind(player.centerXProperty().add(25.0));
        worldRotate.pivotYProperty().bind(player.centerYProperty().add(25.0));

        camera.setFieldOfView(60);
        camera.setFarClip(10000.0);

        cameraTz.set(-DEFAULT_ZOOM_DISTANCE);
        cameraTx.bind(player.centerXProperty());
        cameraTy.bind(player.centerYProperty()
                .add(cameraTz.multiply(-1).divide(Math.tan(Math.toRadians(90.0 - cameraRx.getAngle())))));

        camera.getTransforms().addAll(cameraRx);

        // save mouse press location for drag
        subScene.setOnMousePressed(me -> {
            mousePosX = me.getX();
            mousePosY = me.getY();
        });

        // rotate the world around the player
        subScene.setOnMouseDragged(me -> {
            if (me.isSecondaryButtonDown()) {
                mouseOldY = mousePosY;
                mousePosY = me.getY();
                double mouseDeltaY = (mousePosY - mouseOldY);
                worldRotate.angleProperty().setValue((worldRotate.getAngle() + mouseDeltaY) % 360.0);
            }
        });

        // zoom in and out on player
        subScene.setOnScroll(me -> {
            double scrollAmount = me.getDeltaY();
            double newTz = cameraTz.get() + scrollAmount;
            if (-newTz >= MIN_ZOOM_DISTANCE && -newTz <= MAX_ZOOM_DISTANCE) {
                cameraTz.set(cameraTz.get() + scrollAmount);
            }
        });

        return subScene;
    }

    private LightBase createLight() {

        Light.Distant distant = new Light.Distant();
        distant.setAzimuth(-135.0f);
        distant.setElevation(100d);

        Lighting l = new Lighting();
        l.setLight(distant);
        l.setSurfaceScale(10.0f);

        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(100);
        pointLight.setTranslateY(100);
        pointLight.setTranslateZ(-300);
        pointLight.setRotate(90.0);
        pointLight.setEffect(l);

        AmbientLight ambientLight = new AmbientLight();

        return ambientLight;
    }

    private Group createAxes() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial yellowMaterial = new PhongMaterial();
        yellowMaterial.setDiffuseColor(Color.DARKGOLDENROD);
        yellowMaterial.setSpecularColor(Color.YELLOW);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(50, 2, 2);
        final Box yAxis = new Box(2, 50, 2);
        final Box zAxis = new Box(2, 2, 50);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(yellowMaterial);
        zAxis.setMaterial(blueMaterial);

        Group axesGroup = new Group(xAxis, yAxis, zAxis);

        axesGroup.getTransforms().addAll(new Translate(1250, 1250, -100));

        return axesGroup;
    }

    private Group createDebugPanel() {

        VBox debugPanel = new VBox();

        debugPanel.getChildren().add(new Label("Debug: "));
        debugPanel.setStyle("-fx-background-color: yellow");

        return new Group(debugPanel);
    }
}
