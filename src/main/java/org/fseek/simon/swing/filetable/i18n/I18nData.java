package org.fseek.simon.swing.filetable.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Simon Wimmesberger
 */
public class I18nData {
    private static final String[] INIT_BUNDLES = {"org/fseek/simon/swing/filetable/i18n/filetable"};
    
    private static List<ResourceBundle> bundles;
    
    static{
        loadBundles();
    }
    
    public static String get(String name){
        if(bundles == null)throw new IllegalStateException("Bundles not initialized !");
        String value = null;
        for(ResourceBundle rb : bundles){
            try{
                value = rb.getString(name);
                break;
            }catch(MissingResourceException ex){
                
            }
        }
        return value;
    }
    
    
    private static void loadBundles(){
        if(bundles == null){
            bundles = new ArrayList<>();
        }else{
            bundles.clear();
        }
        for(String s : INIT_BUNDLES){
            bundles.add(ResourceBundle.getBundle(s));
        }
    }
}
