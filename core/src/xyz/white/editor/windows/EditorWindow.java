package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.tree.TreeCancelEvent;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class EditorWindow extends VisWindow {
    private MainWindow mainWindow;
    private Group group;

    public EditorWindow(final MainWindow mainWindow) {
        super("Editor",false);
        this.mainWindow = mainWindow;
        setSize(Config.width * 0.6f, Config.height * 0.7f);
        group = new Group();
        group.setSize(mainWindow.getWidth(),mainWindow.getHeight());
        group.addActor(mainWindow);
        final VisTable table = new VisTable();
        table.add(group);

        table.setPosition(getWidth()/2,getHeight()/2,Align.center);
        add(group).expand().fill();
        EditorWindow.this.addListener(new InputListener() {

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                    float scaleFactor = -0.1f;

                    mainWindow.scaleBy(amount*scaleFactor);
                    group.setSize(mainWindow.getWidth()*mainWindow.getScaleX(),
                            mainWindow.getHeight()*mainWindow.getScaleY());
                    return true;

                }
                return false;
            }
        });
        group.addListener(new DragListener(){


            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                {
                    group.moveBy(x-getTouchDownX(), y-getTouchDownY());
                }
                super.drag(event, x, y, pointer);
            }
        });
    }

}
