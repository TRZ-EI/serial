package trzpoc.utils;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 06/05/17
 * Time: 11.06
 */
public class FontAndColorSelector {

    private final char NERO_PICCOLO = 'P';     //0x31
    private final char ROSSO_PICCOLO = 'Q';
    private final char VERDE_PICCOLO = 'R';
    private final char BLU_PICCOLO = 'S';


    private final char NERO_GRANDE = '9';  //0x39
    private final char ROSSO_GRANDE = 'A';
    private final char VERDE_GRANDE = 'G';
    private final char BLU_GRANDE = 'C';

    private Font bigFont = Font.font("Arial", FontWeight.NORMAL, 20);
    private Font smallFont = Font.font("Arial", FontWeight.NORMAL, 16);
    
    private Set<Character> smallFontsChars;
    private Map<Character, Color> colorMap; 

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
        this.smallFontsChars = this.fillDataForFonts();
        this.colorMap = this.fillDataForColors();
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


    
    
}
