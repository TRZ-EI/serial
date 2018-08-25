/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package trzpoc.gui.hansolo.skins;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.LedType;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.skins.GaugeSkinBase;
import eu.hansolo.medusa.tools.Helper;
import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Locale;

import static eu.hansolo.medusa.tools.Helper.clamp;


/**
 * Created by Luigi Talamona on May 30, 2017
 */
public class TRZLinearSkin extends GaugeSkinBase {
    private double                preferredWidth  = 140;
    private double                preferredHeight = 350;
    private double                aspectRatio     = 2.5;
    private double                size;
    private double                width;
    private double                height;
    private double                stepSize;
    private Pane                  pane;
    private Orientation           orientation;
    private Line                  zeroMark;
    private Rectangle             barBackground;
    private Rectangle             bar;
    private Rectangle             barHighlight;
    private Locale                locale;
    private double                minValuePosition;
    private double                maxValuePosition;
    private double                zeroPosition;
    private List<Section>         sections;
    private List<Section>         areas;
    private InvalidationListener  currentValueListener;
    private InvalidationListener  paneSizeListener;


    // ******************** Constructors **************************************
    public TRZLinearSkin(Gauge gauge) {
        super(gauge);
        if (gauge.isAutoScale()) gauge.calcAutoScale();
        orientation           = gauge.getOrientation();
        locale                = gauge.getLocale();
        sections              = gauge.getSections();
        areas                 = gauge.getAreas();
        currentValueListener  = o -> setBar(gauge.getCurrentValue());
        paneSizeListener      = o -> handleEvents("RESIZE");

        if (Orientation.VERTICAL == orientation) {
            preferredWidth  = 140;
            preferredHeight = 350;
        } else {
            preferredWidth  = 350;
            preferredHeight = 140;
        }

        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        // Set initial size
        if (Double.compare(gauge.getPrefWidth(), 0.0) <= 0 || Double.compare(gauge.getPrefHeight(), 0.0) <= 0 ||
            Double.compare(gauge.getWidth(), 0.0) <= 0 || Double.compare(gauge.getHeight(), 0.0) <= 0) {
            if (gauge.getPrefWidth() > 0 && gauge.getPrefHeight() > 0) {
                gauge.setPrefSize(gauge.getPrefWidth(), gauge.getPrefHeight());
            } else {
                gauge.setPrefSize(preferredWidth, preferredHeight);
            }
        }

        this.zeroMark = new Line();

        barBackground = new Rectangle();
        bar = new Rectangle();
        bar.setStroke(null);

        barHighlight = new Rectangle();
        barHighlight.setStroke(null);
        Helper.enableNode(barHighlight, gauge.isBarEffectEnabled());

        pane = new Pane(//barBorder1,
                        //barBorder2,
                        this.zeroMark,
                        barBackground,
                        bar,
                        barHighlight);
        pane.setBorder(new Border(new BorderStroke(gauge.getBorderPaint(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(gauge.getBorderWidth()))));
        pane.setBackground(new Background(new BackgroundFill(gauge.getBackgroundPaint(), CornerRadii.EMPTY, Insets.EMPTY)));

        getChildren().setAll(pane);
    }

    @Override protected void registerListeners() {
        super.registerListeners();
        gauge.currentValueProperty().addListener(currentValueListener);
        pane.widthProperty().addListener(paneSizeListener);
        pane.heightProperty().addListener(paneSizeListener);
    }


    // ******************** Methods *******************************************
    @Override protected void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);
        if ("FINISHED".equals(EVENT_TYPE)) {
            if ( gauge.isHighlightSections() ) {
                redraw();
            }
        } else if ("VISIBILITY".equals(EVENT_TYPE)) {
            Helper.enableNode(barHighlight, gauge.isBarEffectEnabled());
            resize();
            redraw();
        } else if ("SECTION".equals(EVENT_TYPE)) {
            sections = gauge.getSections();
            areas    = gauge.getAreas();
            resize();
            redraw();
        } else if ("RECALC".equals(EVENT_TYPE)) {
            orientation = gauge.getOrientation();
            if (Orientation.VERTICAL == orientation) {
                width    = height / aspectRatio;
            } else {
                height   = width / aspectRatio;
            }
            resize();
            redraw();
        }
    }

    @Override public void dispose() {
        gauge.currentValueProperty().removeListener(currentValueListener);
        pane.widthProperty().removeListener(paneSizeListener);
        pane.heightProperty().removeListener(paneSizeListener);
        super.dispose();
    }


    // ******************** Private Methods ***********************************


    private void setBar(final double VALUE) {

        double maxValue = ( gauge.getMaxValue() - gauge.getMinValue() ) * stepSize;

        if (Orientation.VERTICAL == orientation) {

            double valueHeight = 0;
            double layoutY = 0;

            if (gauge.isStartFromZero()) {

                double maxV = gauge.getMaxValue();
                double maxY = maxV * stepSize;
                double minV = gauge.getMinValue();
                double minY = minV * stepSize;
                double valY = VALUE * stepSize;

                if ( ( valY > minY || minY < 0 ) && ( valY < maxY || maxY > 0 ) ) {

                    valY = clamp(minY, maxY, valY);

                    if ( maxY < 0 ) {
                        layoutY = - maxY;
                        valueHeight = maxY - valY;
                    } else if ( minY > 0 ) {
                        layoutY = - valY;
                        valueHeight = valY - minY;
                    } else if ( valY < 0 ) {
                        layoutY = 0;
                        valueHeight = - valY;
                    } else {
                        layoutY = - valY;
                        valueHeight = valY;
                    }

                }

            } else {
                valueHeight = clamp(0, maxValue, ( VALUE - gauge.getMinValue() ) * stepSize);
                layoutY = -valueHeight;
            }

            bar.setLayoutY(layoutY);
            bar.setHeight(valueHeight);
            barHighlight.setLayoutY(layoutY);
            barHighlight.setHeight(valueHeight);


        } else {

            double valueWidth = 0;
            double layoutX = 0;

            if (gauge.isStartFromZero()) {

                double maxV = gauge.getMaxValue();
                double maxX = maxV * stepSize;
                double minV = gauge.getMinValue();
                double minX = minV * stepSize;
                double valX = VALUE * stepSize;

                if ( ( valX > minX || minX < 0 ) && ( valX < maxX || maxX > 0 ) ) {

                    valX = clamp(minX, maxX, valX);

                    if ( maxX < 0 ) {
                        layoutX = valX;
                        valueWidth = maxX - valX;
                    } else if ( minX > 0 ) {
                        layoutX = minX;
                        valueWidth = valX - minX;
                    } else if ( valX < 0 ) {
                        layoutX = valX;
                        valueWidth = - valX;
                    } else {
                        layoutX = 0;
                        valueWidth = valX;
                    }

                }

            } else {
                valueWidth = clamp(0, maxValue, ( VALUE - gauge.getMinValue() ) * stepSize);
            }

            bar.setLayoutX(layoutX);
            bar.setWidth(valueWidth);
            barHighlight.setLayoutX(layoutX);
            barHighlight.setWidth(valueWidth);

            //valueText.setText(formatNumber(gauge.getLocale(), gauge.getFormatString(), gauge.getDecimals(), VALUE));
            //valueText.setLayoutX(( 0.98 * width - valueText.getLayoutBounds().getWidth() ));

        }

        setBarColor(VALUE);

    }
    private void setBarColor(final double VALUE) {
        if (!gauge.getAreasVisible() && !gauge.isGradientBarEnabled()) {
            bar.setFill(gauge.getBarColor());
        } else if (gauge.isGradientBarEnabled() && gauge.getGradientBarStops().size() > 1) {
            bar.setFill(gauge.getGradientLookup().getColorAt((VALUE - gauge.getMinValue()) / gauge.getRange()));
        } else {
            bar.setFill(gauge.getBarColor());
            int listSize = areas.size();
            for (int i = 0 ; i < listSize ; i++) {
                Section area = areas.get(i);
                if (area.contains(VALUE)) {
                    bar.setFill(area.getColor());
                    break;
                }
            }
        }
    }


    private void resizeText() {
        if (Orientation.VERTICAL == orientation) {
            double maxWidth = width * 0.95;
            double fontSize = width * 0.13;

        } else {
            double maxWidth = width * 0.8;
            double fontSize = height * 0.15;

        }
    }

    @Override protected void resize() {
        width  = gauge.getWidth() - gauge.getInsets().getLeft() - gauge.getInsets().getRight();
        height = gauge.getHeight() - gauge.getInsets().getTop() - gauge.getInsets().getBottom();

        if (width > 0 && height > 0) {
            orientation = gauge.getOrientation();

            double  currentValue   = gauge.getCurrentValue();

            if (Orientation.VERTICAL == orientation) {
                width    = height / aspectRatio;
                size     = width < height ? width : height;
                stepSize = Math.abs((0.66793 * height) / gauge.getRange());

                pane.setMaxSize(width, height);
                pane.relocate((gauge.getWidth() - width) * 0.5, (gauge.getHeight() - height) * 0.5);

                width  = pane.getLayoutBounds().getWidth();
                height = pane.getLayoutBounds().getHeight();

                barBackground.setWidth(0.14286 * width);
                barBackground.setHeight(0.67143 * height);
                barBackground.relocate((width - barBackground.getWidth()) * 0.5, (height - barBackground.getHeight()) * 0.5);
                barBackground.setStroke(null);
                barBackground.setFill(new LinearGradient(0, barBackground.getLayoutBounds().getMinY(),
                                                         0, barBackground.getLayoutBounds().getMaxY(),
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, Color.rgb(255, 255, 255, 0.05)),
                                                         new Stop(0.5, Color.rgb(255, 255, 255, 0.15)),
                                                         new Stop(1.0, Color.rgb(255, 255, 255, 0.05))));

                minValuePosition = barBackground.getLayoutY() + barBackground.getLayoutBounds().getHeight();
                maxValuePosition = barBackground.getLayoutY();
                zeroPosition     = maxValuePosition - (maxValuePosition * 0.3);


                bar.setWidth(0.14286 * width);
                bar.setLayoutX(0);
                bar.setLayoutY(0);
                bar.setTranslateX((width - bar.getWidth()) * 0.5);
                bar.setTranslateY(gauge.isStartFromZero() ? zeroPosition : minValuePosition);

                barHighlight.setWidth(bar.getWidth());
                barHighlight.setLayoutX(0);
                barHighlight.setLayoutY(0);
                barHighlight.setTranslateX(bar.getTranslateX());
                barHighlight.setTranslateY(bar.getTranslateY());

                setBar(currentValue);


            } else {
                height   = width / aspectRatio;
                size     = width < height ? width : height;
                stepSize = Math.abs(0.9 * width / gauge.getRange());

                pane.setMaxSize(width, height);
                pane.relocate((gauge.getWidth() - width) * 0.5, (gauge.getHeight() - height) * 0.5);

                width  = pane.getLayoutBounds().getWidth();
                height = pane.getLayoutBounds().getHeight();

                barBackground.setWidth(0.9 * width);
                barBackground.setHeight(0.14286 * height);
                barBackground.relocate((width - barBackground.getWidth()) * 0.5, (height - barBackground.getHeight()) * 0.5);
                barBackground.setStroke(null);
                barBackground.setFill(new LinearGradient(barBackground.getLayoutBounds().getMinX(), 0,
                                                         barBackground.getLayoutBounds().getMaxX(), 0,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, Color.rgb(255, 255, 255, 0.05)),
                                                         new Stop(0.5, Color.rgb(255, 255, 255, 0.15)),
                                                         new Stop(1.0, Color.rgb(255, 255, 255, 0.05))));

                minValuePosition = barBackground.getLayoutX();
                maxValuePosition = barBackground.getLayoutX() + barBackground.getLayoutBounds().getWidth() + 80;
                // TODO: CHANGE VALUE OF maxValuePosition to change red bar max
                zeroPosition = maxValuePosition -(maxValuePosition * 0.3);




                double ypos1 = barBackground.getLayoutY() - 1;
                double ypos2 = barBackground.getLayoutY() + barBackground.getLayoutBounds().getHeight() + 1;

                this.zeroMark.setStartX(zeroPosition);
                this.zeroMark.setStartY(ypos1);
                this.zeroMark.setEndX(zeroPosition);
                this.zeroMark.setEndY(ypos2);
                this.zeroMark.setStroke(Color.BLUE);
                this.zeroMark.setStrokeWidth(3);

                bar.setHeight(0.14286 * height);
                bar.setLayoutX(0);
                bar.setLayoutY(0);
                bar.setTranslateX(gauge.isStartFromZero() ? zeroPosition : minValuePosition);
                bar.setTranslateY((height - bar.getHeight()) * 0.5);

                barHighlight.setHeight(bar.getHeight());
                barHighlight.setLayoutX(0);
                barHighlight.setLayoutY(0);
                barHighlight.setTranslateX(bar.getTranslateX());
                barHighlight.setTranslateY(bar.getTranslateY());

                setBar(currentValue);
            }

            resizeText();
        }
    }

    @Override protected void redraw() {
        locale                = gauge.getLocale();
        //tickLabelFormatString = new StringBuilder("%.").append(Integer.toString(gauge.getTickLabelDecimals())).append("f").toString();

        // Background stroke and fill
        pane.setBorder(new Border(new BorderStroke(gauge.getBorderPaint(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(Orientation.HORIZONTAL == orientation ? gauge.getBorderWidth() / preferredHeight * height : gauge.getBorderWidth() / preferredWidth * width))));
        pane.setBackground(new Background(new BackgroundFill(gauge.getBackgroundPaint(), CornerRadii.EMPTY, Insets.EMPTY)));

        if (gauge.getAreasVisible()) {
            setBarColor(gauge.getCurrentValue());
        } else {
            bar.setFill(gauge.getBarColor());
        }

        if (Orientation.VERTICAL == orientation) {
            barHighlight.setFill(new LinearGradient(barHighlight.getLayoutX(), 0, barHighlight.getLayoutX() + barHighlight.getWidth(), 0,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, Color.rgb(255, 255, 255, 0.65)),
                                                    new Stop(0.92, Color.TRANSPARENT),
                                                    new Stop(1.0, Color.rgb(0, 0, 0, 0.2))));
        } else {
            barHighlight.setFill(new LinearGradient(0, barHighlight.getLayoutY(), 0, barHighlight.getLayoutY() + barHighlight.getHeight(),
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, Color.rgb(255, 255, 255, 0.65)),
                                                    new Stop(0.92, Color.TRANSPARENT),
                                                    new Stop(1.0, Color.rgb(0, 0, 0, 0.2))));
        }

    }
}
