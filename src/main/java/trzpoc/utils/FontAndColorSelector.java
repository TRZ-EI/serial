package trzpoc.utils;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import trzpoc.structure.TextMetricCalculator;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 06/05/17
 * Time: 11.06
 */
public class FontAndColorSelector {

    private final String RESOURCE = "fonts.properties";

    private final char NERO_PICCOLO = 'P';     //0x31
    private final char ROSSO_PICCOLO = 'Q';
    private final char VERDE_PICCOLO = 'R';
    private final char BLU_PICCOLO = 'S';


    private final char NERO_GRANDE = '9';  //0x39
    private final char ROSSO_GRANDE = 'A';
    private final char VERDE_GRANDE = 'G';
    private final char BLU_GRANDE = 'C';

    private final char CHAR_FOR_METRIC = 'W'; // char to measure tipical dimensions


    private Font bigFont,smallFont;

    private Set<Character> smallFontsChars;
    private Map<Character, Color> colorMap;
    private Properties properties;

    public static FontAndColorSelector getNewInstance(){

        return new FontAndColorSelector();
    }
    public Font selectFont(char selector){
        return smallFontsChars.contains(selector)? smallFont: bigFont;
    }
    public Color selectColor(char selector){
        Color c = colorMap.get(selector);
        return (c!= null)? c: Color.BEIGE;
    }
    private FontAndColorSelector(){
        try {
            this.smallFontsChars = this.fillDataForFonts();
            this.colorMap = this.fillDataForColors();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(RESOURCE);
            this.properties = new Properties();
            this.properties.load(is);
            is.close();
            this.smallFont = this.loadSmallFont();
            this.bigFont = this.loadBigFont();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Map<Character,Color> fillDataForColors() {
        Map<Character, Color> retValue = new HashMap<Character, Color>();
        retValue.put(NERO_PICCOLO, Color.BLACK);
        retValue.put(NERO_GRANDE, Color.BLACK);

        retValue.put(ROSSO_PICCOLO, Color.RED);
        retValue.put(ROSSO_GRANDE, Color.RED);

        retValue.put(BLU_PICCOLO, Color.BLUE);
        retValue.put(BLU_GRANDE, Color.BLUE);

        retValue.put(VERDE_PICCOLO, Color.GREEN);
        retValue.put(VERDE_GRANDE, Color.GREEN);
        return retValue;
    }

    private Set<Character> fillDataForFonts() {
        Set<Character> retValue = new HashSet<Character>();
        retValue.add(NERO_PICCOLO);
        retValue.add(ROSSO_PICCOLO);
        retValue.add(VERDE_PICCOLO);
        retValue.add(BLU_PICCOLO);
        return retValue;
    }
    private Font loadSmallFont(){
        String font = this.properties.getProperty(FontProperties.SMALL_FONT.name());
        String size = this.properties.getProperty(FontProperties.SMALL_SIZE.name());
        String weight = this.properties.getProperty(FontProperties.SMALL_FONT_WEIGHT.name());
        return Font.font(font, FontWeight.findByName(weight), Integer.parseInt(size));
    }
    private Font loadBigFont(){
        String font = this.properties.getProperty(FontProperties.BIG_FONT.name());
        String size = this.properties.getProperty(FontProperties.BIG_SIZE.name());
        String weight = this.properties.getProperty(FontProperties.BIG_FONT_WEIGHT.name());
        return Font.font(font, FontWeight.findByName(weight), Integer.parseInt(size));
    }

    public int getWidthForSmallFont(String w) {
        return TextMetricCalculator.getInstance().calculateWidth(w, this.smallFont);
   }

    public int getWidthForBigFont(String w) {
        return TextMetricCalculator.getInstance().calculateWidth(w, this.bigFont);
    }

    public int getHeightForSmallFont(String w) {
        return TextMetricCalculator.getInstance().calculateHeight(w, this.smallFont);
    }

    public int getHeightForBigFont(String w) {
        return TextMetricCalculator.getInstance().calculateHeight(w, this.bigFont);
    }
    public int getWidthForFont(Font font, String w) {
        return TextMetricCalculator.getInstance().calculateWidth(w, font);
    }

    public int getHeightForFont(Font font, String w) {
        return TextMetricCalculator.getInstance().calculateHeight(w, font);
    }

    private enum FontProperties{
        SMALL_FONT,SMALL_FONT_WEIGHT,SMALL_SIZE,BIG_FONT,BIG_FONT_WEIGHT,BIG_SIZE;
    }


    
    
}
