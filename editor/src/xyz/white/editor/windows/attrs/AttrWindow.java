package xyz.white.editor.windows.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisSplitPane;
import com.kotcrab.vis.ui.widget.VisWindow;

import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.attrs.ActorNameEvent;
import xyz.white.editor.events.editor.ActorAddEvent;
import xyz.white.editor.events.editor.RefreshWindowEvent;
import xyz.white.editor.events.editor.SureActorEvent;
import xyz.white.editor.events.editor.AttrEvent;
import xyz.white.editor.events.listener.EditorEventListener;

/**
 * 属性窗口
 * Created by 10037 on 2017/4/16 0016.
 */

public class AttrWindow extends VisWindow implements EditorEventListener {
    private AttrGeneralWindow attrGeneralWindow;
    private AttrUniqueWindow attrUniqueWindow;

    public AttrWindow(String title) {
        super(title,false);
        setSize(Config.width*0.2f, Config.height);
        left();
        EditorManager.getInstance().getEventBus().register(this);
        attrGeneralWindow = new AttrGeneralWindow();
        attrUniqueWindow = new AttrUniqueWindow();

        VisSplitPane splitPane = new VisSplitPane(attrGeneralWindow,attrUniqueWindow,true);
        splitPane.setSplitAmount(0.5f);
        splitPane.setMinSplitAmount(0.2f);
        splitPane.setSize(getWidth(),getHeight());
        add(splitPane).expand().fill();

    }



    public void refreshAttr(Actor actor){
        attrGeneralWindow.refreshAttr(actor);
        attrUniqueWindow.refreshAttr(actor);
    }

    @Override
    public void changePos(AttrEvent attrEvent) {
        refreshAttr(attrEvent.actor);
    }

    @Override
    public void addActor(ActorAddEvent event) {
        refreshAttr(event.actor);
    }

    @Override
    public void sureActor(SureActorEvent event) {
        refreshAttr(event.actor);
    }

    @Override
    public void refreshWindow(RefreshWindowEvent event) {

    }

    @Override
    public void setActorName(ActorNameEvent event) {

    }

}
