package com.ethanaa;


import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.HexagonalGrid;
import org.codetome.hexameter.core.api.HexagonalGridBuilder;
import org.codetome.hexameter.core.api.HexagonalGridCalculator;
import rx.Observable;

import java.util.*;

public class HexMap extends StackPane {

    private Group group;
    private Map<Hexagon<GridData>, HexNode> hexagonHexNodeMap = new HashMap<>();
    private HexagonalGrid<GridData> grid;
    private HexagonalGridCalculator gridCalculator;

    public HexMap(HexagonalGridBuilder<GridData> builder, Rotate mapRotate) {

        this.grid = builder.build();
        this.gridCalculator = builder.buildCalculatorFor(grid);

        setStyle("-fx-background-color: #0077be;\n"
                + "-fx-background-image: url(\"water.jpg\");\n"
                + "-fx-border-color: blue;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-border-width: 2;\n"
                + "-fx-border-style: dashed;\n");

        setPickOnBounds(false);

        this.group = new Group();

        getHexagons().forEach(h -> {

            int height = 20;
            double bigness = new Random().nextDouble();
            height += bigness >= .9 ? new Random().nextInt(5) * 20 : new Random().nextInt(2) * 20;

            HexNode hexNode = new HexNode(h, height, mapRotate);

            hexagonHexNodeMap.put(h, hexNode);

            group.getChildren().add(hexNode);
        });

        getChildren().add(group);

    }

    public Observable<Hexagon<GridData>> getHexagons() {

        return grid.getHexagons();
    }

    public Map<Hexagon<GridData>, HexNode> getHexagonHexNodeMap() {
        return hexagonHexNodeMap;
    }

    public void setHexagonHexNodeMap(Map<Hexagon<GridData>, HexNode> hexagonHexNodeMap) {
        this.hexagonHexNodeMap = hexagonHexNodeMap;
    }
}
