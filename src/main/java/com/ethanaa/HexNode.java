package com.ethanaa;


import com.ethanaa.ragnalite.Action;
import com.ethanaa.ragnalite.Orientation;
import com.ethanaa.ragnalite.Sprite;
import com.ethanaa.ragnalite.SpriteBuilder;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HexNode extends StackPane {

    private Hexagon<GridData> hexagon;
    private int nodeHeight;
    private Color color;

    private Polygon outline = new Polygon();
    private Polygon top = new Polygon();
    private Polygon polyUnder = new Polygon();

    private PolyLine3D cliff;
    private PolyLine3D rocks;

    private Group all;

    private Sprite sprite;

    private static final Image IMAGE = new Image("sprites2.png");
    private static final Image IMAGE_BACK = new Image("sprites2_back.png");

    public HexNode(Hexagon<GridData> hexagon, int height, Rotate mapRotate) {

        this.hexagon = hexagon;
        this.nodeHeight = height;

        List<Point3D> points = new ArrayList<>();
        for (Point p : hexagon.getPoints()) {

            outline.getPoints().addAll(p.getCoordinateX(), p.getCoordinateY());
            top.getPoints().addAll(p.getCoordinateX(), p.getCoordinateY());
            polyUnder.getPoints().addAll(p.getCoordinateX(), p.getCoordinateY());

            points.add(new Point3D(p.getCoordinateX(), p.getCoordinateY(), 0));
        }
        points.add(new Point3D(hexagon.getPoints().get(0).getCoordinateX(), hexagon.getPoints().get(0).getCoordinateY(), 0));

        if (height / 20 < 2) {
            this.color = Color.BURLYWOOD;
            top.setFill(Color.KHAKI);
        } else {
            this.color = Color.FORESTGREEN;
            top.setFill(Color.OLIVEDRAB);
        }

        Color cliffColor = Color.SADDLEBROWN;
        if (height / 20 > 4) {
            cliffColor = Color.SLATEGRAY;
        }

        cliff = new PolyLine3D(points, height, cliffColor, PolyLine3D.LineType.RIBBON);
        rocks = new PolyLine3D(points, 10, Color.DARKSLATEGRAY, PolyLine3D.LineType.RIBBON);

        outline.setFill(color);
        top.setScaleX(.80);
        top.setScaleY(.80);

        polyUnder.setScaleX(1.4);
        polyUnder.setScaleY(1.4);
        polyUnder.setFill(Color.DARKSLATEGRAY);
        rocks.setScaleX(1.4);
        rocks.setScaleY(1.4);

        top.setOnMouseEntered(e -> {
            outline.setFill(Color.ANTIQUEWHITE);
        });

        top.setOnMouseExited(e -> {
            outline.setFill(color);
        });

        top.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                color = Color.FORESTGREEN;
            } else {
                color = Color.FIREBRICK;
            }
            outline.setFill(color);
            System.out.println(hexagon.getCubeCoordinate().getGridX() + ", " + hexagon.getCubeCoordinate().getGridY());
            System.out.println(hexagon.getCenterX() + ", " + hexagon.getCenterY());
        });

        setTranslateX(hexagon.getCenterX());
        setTranslateY(hexagon.getCenterY());
        setTranslateZ(-20 - height);

        outline.setTranslateZ(0);
        top.setTranslateZ(-1);
        cliff.setTranslateZ(0);
        rocks.setTranslateZ(height);
        polyUnder.setTranslateZ(height + 1);
        setPickOnBounds(false);

        all = new Group(outline, top, cliff, rocks, polyUnder);
        setVisible(new Random().nextDouble() > .3);

        /*
        if (new Random().nextDouble() > .93) {
            sprite = new SpriteBuilder()
                    .setHexagon(hexagon)
                    .setMapRotate(mapRotate)
                    .setIdleAnimation(IMAGE, 0, 0,
                            1600 / 10, 1097 / 4, 39,
                            10, 2_000, -60)
                    .setIdleAnimationBehind(IMAGE_BACK, 0, 0,
                            1600 / 10, 1102 / 4, 40,
                            10, 2_000, -60)
                    .setOrientation(new Random().nextBoolean() ? Orientation.FORWARD : Orientation.BACKWARD)
                    .setAction(Action.IDLE)
                    .build();

            all.getChildren().add(sprite);
        }
         */

        getChildren().add(all);
    }

    public void placeSprite(Sprite sprite) {

        all.getChildren().add(sprite);

        sprite.setOnMouseClicked(me -> {

        });
    }

    public int getNodeHeight() {
        return nodeHeight;
    }
}
