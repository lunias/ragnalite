package com.ethanaa;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.codetome.hexameter.core.api.HexagonOrientation;
import org.codetome.hexameter.core.api.HexagonalGridBuilder;
import org.codetome.hexameter.core.api.HexagonalGridLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.Function;

@SpringBootApplication
public class HexApplication extends Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(HexApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(HexApplication.class, args);
		launch(args);
	}

	@Override
	public void run(String... args) throws Exception {

		LOG.info("Welcome to hex");
	}

    @Override
    public void start(Stage stage) throws Exception {

	    stage.setTitle("Hex");

        Rotate mapCameraRx = new Rotate(45, Rotate.X_AXIS);
        Rotate mapCameraRy = new Rotate(0, Rotate.Y_AXIS);
        Rotate mapCameraRz = new Rotate(0, Rotate.Z_AXIS);

        Translate mapCameraPan = new Translate(0, -180, 0);

        PerspectiveCamera mapCamera = new PerspectiveCamera(false);
        mapCamera.getTransforms().addAll(mapCameraRx, mapCameraRy, mapCameraRz, mapCameraPan);

        Light.Distant distant = new Light.Distant();
        distant.setAzimuth(-135.0f);
        distant.setElevation(100d);

        Lighting l = new Lighting();
        l.setLight(distant);
        l.setSurfaceScale(10.0f);

        PointLight light = new PointLight(Color.LIGHTSKYBLUE);
        light.setEffect(l);
        light.setTranslateX(0);
        light.setTranslateY(0);
        light.setTranslateZ(-200);

        Function<Integer, Integer> f = (i) -> 2*i;

        Rotate mapRotate = new Rotate(0, 480, 480, 0, Rotate.Z_AXIS);

	    HexMap hexMap = new HexMap(new HexagonalGridBuilder<>()
                .setGridHeight(25)
                .setGridWidth(25)
                .setGridLayout(HexagonalGridLayout.HEXAGONAL)
                .setOrientation(HexagonOrientation.FLAT_TOP)
                .setRadius(30), mapRotate);

        Group rootGroup = new Group(hexMap, light);

        StackPane root = new StackPane();
        root.setPickOnBounds(false);
        root.getChildren().addAll(rootGroup);

        SubScene mapScene = new SubScene(root,500, 500, true, SceneAntialiasing.BALANCED);
        mapScene.widthProperty().bind(stage.widthProperty());
        mapScene.heightProperty().bind(stage.heightProperty());
        mapScene.setFill(Color.BLACK);
        mapScene.setCamera(mapCamera);

        root.getTransforms().addAll(mapRotate);
        mapRotate.pivotXProperty().bind(mapScene.widthProperty().divide(2.0));
        mapRotate.pivotYProperty().bind(mapScene.heightProperty().divide(2.0));

        mapScene.setOnMouseEntered(e -> {
            mapScene.requestFocus();
        });

        // TODO translate camera position rather than scale
        mapScene.setOnScroll(e -> {
            double delta = e.getDeltaY();
            if (e.getDeltaY() > 0) {
                rootGroup.setScaleX(rootGroup.getScaleX() + .3);
                rootGroup.setScaleY(rootGroup.getScaleY() + .3);
                rootGroup.setScaleZ(rootGroup.getScaleZ() + .3);

            } else {
                rootGroup.setScaleX(rootGroup.getScaleX() - .3);
                rootGroup.setScaleY(rootGroup.getScaleY() - .3);
                rootGroup.setScaleZ(rootGroup.getScaleZ() - .3);
            }
        });

        mapScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    if (mapCameraRx.angleProperty().get() > 0) {
                        mapCameraRx.angleProperty().setValue((mapCameraRx.getAngle() - 5) % 360);
                        if (mapCameraRx.getAngle() <= 25) {
                            mapCameraPan.setY(mapCameraPan.getY() + 10);
                        } else {
                            mapCameraPan.setY(mapCameraPan.getY() + 50);
                        }
                    }
                    break;
                case S:
                    if (mapCameraRx.angleProperty().get() < 90) {
                        mapCameraRx.angleProperty().setValue((mapCameraRx.getAngle() + 5) % 360);
                        if (mapCameraRx.getAngle() <= 30) {
                            mapCameraPan.setY(mapCameraPan.getY() - 10);
                        } else {
                            mapCameraPan.setY(mapCameraPan.getY() - 50);
                        }
                    }
                    break;
                case A:
                    mapRotate.angleProperty().setValue((mapRotate.getAngle() - 5) % 360);
                    System.out.println(mapRotate.getAngle());
                    break;
                case D:
                    mapRotate.angleProperty().setValue((mapRotate.getAngle() + 5) % 360);
                    System.out.println(mapRotate.getAngle());
                    break;
            }
        });

        StackPane stackPane = new StackPane(mapScene);
        stackPane.setPickOnBounds(false);

        Box handBox = new Box(800, 600, 3);
        handBox.setMaterial(new PhongMaterial(Color.SLATEGRAY));

        Rotate handRotate = new Rotate(12, Rotate.X_AXIS);
        handRotate.pivotXProperty().bind(handBox.heightProperty());
        handRotate.pivotYProperty().bind(handBox.widthProperty().divide(2));

        StackPane hand = new StackPane(handBox);
        hand.getTransforms().add(handRotate);

        PerspectiveCamera handCamera = new PerspectiveCamera(false);

        handCamera.getTransforms().add(new Rotate(10.0, Rotate.X_AXIS));

        SubScene handScene = new SubScene(hand,0, 0, false, SceneAntialiasing.BALANCED);
        handScene.widthProperty().bind(stage.widthProperty().divide(2.5));
        handScene.heightProperty().bind(stage.heightProperty().divide(5));
        handScene.setCamera(handCamera);
        handScene.setFill(Color.TRANSPARENT);
        handScene.setTranslateY(130);

        handScene.setOnMouseEntered(e -> {
            handRotate.angleProperty().set(5);
            handScene.setTranslateY(0);
        });

        handScene.setOnMouseExited(e -> {
            handRotate.angleProperty().set(12);
            handScene.setTranslateY(130);
        });

        handBox.widthProperty().bind(handScene.widthProperty().multiply(.8));
        handBox.heightProperty().bind(handScene.heightProperty().multiply(.8));

        stackPane.getChildren().add(handScene);
        StackPane.setAlignment(handScene, Pos.BOTTOM_CENTER);

        Scene scene = new Scene(stackPane,0, 0);

	    stage.setScene(scene);
	    //stage.initStyle(StageStyle.TRANSPARENT);
	    stage.show();
    }
}
