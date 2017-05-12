package xyz.white.editor.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import xyz.white.editor.EditorManager;

/**
 * Created by 10037 on 2017/5/12 0012.
 */
public class DrawableUtil {
    // 创建钜形线条Drawable
    public static Drawable getRectLineDrawable(String ninePath) {
        NinePatchDrawable nine = new NinePatchDrawable(new NinePatch(
                EditorManager.getInstance().assetManager.get(ninePath,Texture.class),
                2,2, 2, 2));
        return nine;
    }
}
