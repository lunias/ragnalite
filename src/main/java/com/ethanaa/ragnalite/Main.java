package com.ethanaa.ragnalite;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
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

    private final PerspectiveCamera camera = new PerspectiveCamera(true);

    private final WorldRegion worldRegion = new WorldRegion();

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

        SubScene subScene = setupSubScene(main);
        subScene.heightProperty().bind(stackPane.heightProperty());
        subScene.widthProperty().bind(stackPane.widthProperty());
        stackPane.getChildren().addAll(subScene);

        main.setCenter(stackPane);
        Scene scene = new Scene(main);

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

    private SubScene setupSubScene(Pane parent) {

        Group worldGroup = new Group(createAxes(), worldRegion);

        SubScene subScene = new SubScene(worldGroup, -1, -1, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setPickOnBounds(true);
        camera.setFieldOfView(60);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-300);
        camera.setTranslateX(1250);
        camera.setTranslateY(1400);
        Rotate cameraRx = new Rotate(26.565, Rotate.X_AXIS);
        camera.getTransforms().add(cameraRx);

        subScene.setOnMousePressed(me -> {
            mousePosX = me.getX();
            mousePosY = me.getY();
        });

        subScene.setOnMouseDragged(me -> {
            if (me.isPrimaryButtonDown()) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getX();
                mousePosY = me.getY();
                double mouseDeltaX = (mousePosX - mouseOldX);
                double mouseDeltaY = (mousePosY - mouseOldY);
                worldRegion.rx(mouseDeltaY * 180.0 / subScene.getHeight());
                worldRegion.ry(-mouseDeltaX * 180.0 / subScene.getWidth());
            }
        });

        subScene.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.SECONDARY && me.isStillSincePress()) {
                System.out.println("Right clicked: " + me.getX() + ", " + me.getY());
            }
        });

        subScene.setOnScroll(me -> {
            double scrollAmount = me.getDeltaY();
            //worldGroup.translateZProperty().set(worldGroup.getTranslateZ() + scrollAmount);
            camera.setTranslateZ(camera.getTranslateZ() + scrollAmount);
        });

        return subScene;
    }

    public void lookAt(Point3D cameraPosition, Point3D lookAtPos) {
        //Create direction vector
        Point3D camDirection = lookAtPos.subtract(cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ());
        camDirection = camDirection.normalize();

        double xRotation = Math.toDegrees(Math.asin(-camDirection.getY()));
        double yRotation =  Math.toDegrees(Math.atan2( camDirection.getX(), camDirection.getZ()));

        Rotate rx = new Rotate(xRotation, cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ(), Rotate.X_AXIS);
        Rotate ry = new Rotate(yRotation, cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ(),  Rotate.Y_AXIS);

        camera.getTransforms().addAll( ry, rx,
                new Translate(
                        cameraPosition.getX(),
                        cameraPosition.getY(),
                        cameraPosition.getZ()));
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

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(5, 1, 1);
        final Box yAxis = new Box(1, 5, 1);
        final Box zAxis = new Box(1, 1, 5);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        return new Group(xAxis, yAxis, zAxis);
    }

    private Group createDebugPanel() {

        VBox debugPanel = new VBox();

        debugPanel.getChildren().add(new Label("Debug: "));
        debugPanel.setStyle("-fx-background-color: yellow");

        return new Group(debugPanel);
    }
}
