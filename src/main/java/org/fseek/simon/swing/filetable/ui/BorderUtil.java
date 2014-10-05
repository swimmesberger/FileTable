package org.fseek.simon.swing.filetable.ui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 *
 * @author Simon Wimmesberger
 */
public class BorderUtil {
        
    public static Border createLeftBorder(int size){
        return createLeftBorder(size, null);
    }
    
    public static Border createInnerBorder(int size){
        return createInnerBorder(size, null);
    }
    
    public static Border createRightBorder(int size){
        return createRightBorder(size, null);
    }
    
    public static Border createLeftBorder(int size, Color color){
        if(color != null){
            return BorderFactory.createMatteBorder(size, size, size, 0, color);
        }
        return BorderFactory.createEmptyBorder(size, size, size, 0);
    }
    
    public static Border createInnerBorder(int size, Color color){
        if(color != null){
            return BorderFactory.createMatteBorder(size, 0, size, 0, color);
        }
        return BorderFactory.createEmptyBorder(size, 0, size, 0);
    }
    
    public static Border createRightBorder(int size, Color color){
        if(color != null){
            return BorderFactory.createMatteBorder(size, 0, size, size, color);
        }
        return BorderFactory.createEmptyBorder(size, 0, size, size);
    }
}
