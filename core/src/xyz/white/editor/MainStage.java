package xyz.white.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisSplitPane;
import xyz.white.editor.events.keyboard.KeyDelEvent;
import xyz.white.editor.windows.*;
import xyz.white.editor.windows.attrs.AttrWindow;

/**
 * Created by 10037 on 2017/4/18 0018.
 */
public class MainStage extends Stage {
    final Pixmap pm = new Pixmap(Gdx.files.internal("icon/cross.png"));
    private MainWindow mainWindow;

    public MainStage(Viewport viewport){
        super(viewport);
        init();
    }

    private void init(){

        MenuBarWindow menuBarWindow = new MenuBarWindow();
        menuBarWindow.setPosition(0,Config.height,Align.topLeft);

        AttrWindow attrWindow = new AttrWindow("Attrs");
        attrWindow.setOrigin(Align.center);
        attrWindow.setPosition(Config.width,Config.height,Align.topRight);

        mainWindow = new MainWindow("PreWindow");

        EditorWindow editorWindow = new EditorWindow(mainWindow);
        editorWindow.setOrigin(Align.center);
        editorWindow.setPosition(getWidth()/2,getHeight(),Align.top);

        ActorsWindow actorsWindow = new ActorsWindow("Actors",mainWindow);
        actorsWindow.setOrigin(Align.center);
        actorsWindow.setPosition(0,getHeight(),Align.topLeft);

        TreeWindow treeWindow = new TreeWindow(mainWindow);
        treeWindow.setOrigin(Align.center);
        treeWindow.setPosition(0,0,Align.bottomLeft);

        AssetWindow assetWindow = new AssetWindow();
        assetWindow.setPosition(treeWindow.getWidth(),0,Align.bottomLeft);

        addActor(editorWindow);

        actorsWindow.setMovable(false);
        treeWindow.setMovable(false);
        VisSplitPane splitPane1 = new VisSplitPane(actorsWindow,treeWindow,true);
        splitPane1.setSize(Config.width*0.2f,Config.height-menuBarWindow.getHeight());
        splitPane1.setPosition(0,0,Align.bottomLeft);

        VisSplitPane splitPane2 = new VisSplitPane(editorWindow,assetWindow,true);
        splitPane2.setSize(Config.width*0.6f,Config.height-menuBarWindow.getHeight());
        splitPane2.setSplitAmount(0.7f);

        VisSplitPane splitPane3 = new VisSplitPane(splitPane1,splitPane2,false);
        splitPane3.setSize(Config.width*0.8f,Config.height-menuBarWindow.getHeight());
        splitPane3.setSplitAmount(0.3f);
        splitPane3.setMaxSplitAmount(0.3f);

        VisSplitPane splitPane4 = new VisSplitPane(splitPane3,attrWindow,false);
        splitPane4.setSize(Config.width,Config.height-menuBarWindow.getHeight());
        splitPane4.setSplitAmount(0.8f);
        splitPane4.setMinSplitAmount(0.4f);

        addActor(splitPane4);
        addActor(menuBarWindow);
    }

    @Override
    public void dispose() {
        pm.dispose();
        EditorManager.getInstance().assetManager.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keyCode) {
        Gdx.app.log("app",""+keyCode);
        if (keyCode == Input.Keys.FORWARD_DEL){
            EditorManager.getInstance().getEventBus().post(new KeyDelEvent());
        }else if (keyCode == Input.Keys.SPACE){
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm,0,0));
        }else if (keyCode == Input.Keys.S && (UIUtils.ctrl())){
            mainWindow.saveScene();
        }
        return super.keyDown(keyCode);
    }



}
