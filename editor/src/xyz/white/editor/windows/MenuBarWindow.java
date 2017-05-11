package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisTable;
import xyz.white.editor.Config;
import xyz.white.editor.windows.dialogs.NewProjectDialog;

/**
 * Created by 10037 on 2017/4/27 0027.
 */
public class MenuBarWindow extends VisTable {

    public MenuBarWindow() {
        super();
        setBackground(VisUI.getSkin().getDrawable("menu-bg"));
        setSize(Config.width,Config.height*0.05f);
        init();
    }

    private void init(){
        MenuBar menuBar = new MenuBar();
        initFileMenu(menuBar);

        add(menuBar.getTable());
        add().expandX().fillX();
    }

    private void initFileMenu(MenuBar menuBar){
        Menu fileMenu = new Menu("File");

        MenuItem openItem = new MenuItem("Open Project", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(new NewProjectDialog());
            }
        });


        fileMenu.addItem(openItem);
        fileMenu.addItem(new MenuItem("Exit", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        }));

        menuBar.addMenu(fileMenu);
    }
}
