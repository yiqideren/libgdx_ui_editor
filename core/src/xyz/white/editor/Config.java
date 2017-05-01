package xyz.white.editor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;

/**
 * Created by 10037 on 2017/4/15 0015.
 */

public  class Config {

    public final static int width = 1366;
    public final static int height = 720;
    public final static String sceneExtension = "vwx";
    public final static int LABEL     = 1;
    public final static int IMAGE     = 2;
    public final static int BUTTON    = 3;
    public final static int CHECKBOX  = 4;
    public final static int TEXTFIELD = 5;
    public final static int GROUP     = 6;

    public final static String[] alignstrs = {"center","top","bottom","left","right","topLeft","topRight","bottomLeft","bottomRight"
    };
    public final static Integer[] alignsIns = {Align.center,Align.top,Align.bottom,Align.left,Align.right,Align.topLeft,Align.topRight,Align.bottomLeft,Align.bottomRight};
    public final static HashMap<String,Integer> aligns  = new HashMap<String, Integer>(){

    };

    static {
        for (int i = 0; i < alignstrs.length; i++) {
            aligns.put(alignstrs[i],alignsIns[i]);
        }
    }


    public static String getProjectPath(){
        Preferences preferences = Gdx.app.getPreferences("white-projectPath");
        return preferences.getString("project","");
    }

    public static void setProjectPath(String path){
        Preferences preferences = Gdx.app.getPreferences("white-projectPath");
        preferences.putString("project",path);
        preferences.flush();
    }
}
