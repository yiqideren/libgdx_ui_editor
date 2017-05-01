package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.ActorUtils;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;


import sun.applet.Main;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.actors.SelectGroup;
import xyz.white.editor.events.assets.LoadSceneEvent;
import xyz.white.editor.events.attrs.*;
import xyz.white.editor.events.editor.ActorAddEvent;
import xyz.white.editor.events.editor.SureActorEvent;
import xyz.white.editor.events.editor.AttrEvent;
import xyz.white.editor.events.listener.AssetEventListener;
import xyz.white.editor.events.listener.ChangeActorAttrListener;
import xyz.white.editor.events.listener.TreeEventListener;
import xyz.white.editor.events.tree.TreeActorMoveEvent;
import xyz.white.editor.events.tree.TreeCancelEvent;
import xyz.white.editor.events.tree.TreeSelectedActroEvent;

import java.io.IOException;

/**
 * Created by 10037 on 2017/4/15 0015.
 */

public class MainWindow extends VisWindow implements ChangeActorAttrListener, TreeEventListener,AssetEventListener {
    private NativeFont font;
    private SelectGroup selectedGroup = new SelectGroup();

    public MainWindow(String title) {
        super(title,false);
        setSize(480, 800);
        EditorManager.getInstance().getEventBus().register(this);
        setBackGround(Color.RED);
        font = new NativeFont();
        this.addListener(clickListener);
        this.addActor(selectedGroup);
    }



    public void addActorIn(Actor actor, float x, float y) {
        Actor cloneActor = null;
        switch ((int) actor.getUserObject()) {
            case Config.LABEL:
                cloneActor = new NativeLabel("Label", font);
                break;
            case Config.BUTTON:
                cloneActor = new VisImageButton(VisUI.getSkin().get(VisImageButton.VisImageButtonStyle.class));
                cloneActor.setSize(100, 60);
                cloneActor.setHeight(60);
                break;
            case Config.CHECKBOX:
                cloneActor = new VisCheckBox("CheckButton");
                break;
            case Config.IMAGE:
                cloneActor = new VisImage(new Texture(Gdx.files.internal("badlogic.jpg")));
                break;
            case Config.TEXTFIELD:
                cloneActor = new TextField("Hello", VisUI.getSkin());
                break;
            case Config.GROUP:
                cloneActor = new Group();
                cloneActor.setSize(100, 100);
                break;
        }
        if (null != cloneActor) {
            if (selectedGroup.getSelectedActorSize() > 0) {
                Actor curActor = selectedGroup.getLastSelectActor();
                if (EditorManager.getInstance().getActorType(curActor) == Group.class) {
                    ((Group) curActor).addActor(cloneActor);
                } else {
                    Group group = curActor.getParent();
                    if (group != null && !group.equals(MainWindow.this)) {
                        group.addActor(cloneActor);
                    } else {
                        cloneActor.setPosition(x, y, Align.center);
                        this.addActor(cloneActor);
                    }
                }
            } else {
                cloneActor.setPosition(x, y, Align.center);
                this.addActor(cloneActor);
            }
            selectedGroup.clearAllActor();
            selectedGroup.addSelectActor(cloneActor);
            cloneActor.debug();
            cloneActor.addListener(clickListener);
            EditorManager.getInstance().getEventBus().post(new ActorAddEvent(cloneActor));
        }
    }


    private void setBackGround(Color color) {
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGB888);
        pixmap.setColor(color);
        pixmap.fill();
        setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));
    }

    private ClickListener clickListener = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Actor actor = event.getListenerActor();
            if (actor != null) {
                if (actor.equals(MainWindow.this)) {
                    if (!MainWindow.this.hit(x, y, true).equals(selectedGroup)) {
                        getStage().setScrollFocus(MainWindow.this);
                        selectedGroup.clearAllActor();
                    }
                } else {
                    selectedGroup.clearAllActor();
                    actor.setDebug(true);
                    selectedGroup.addSelectActor(actor);
                    EditorManager.getInstance().getEventBus().post(new SureActorEvent(actor));
                }

            }
        }
    };


    @Override
    public void changeColor(Color color) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (curActor.equals(MainWindow.this))
                setBackGround(color);
            else
                curActor.setColor(color);
        }
    }

    @Override
    public void changePos(ActorPosChangeEvent event) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            curActor.setX(event.x);
            curActor.setY(event.y);
        }
        selectedGroup.initLayout();
    }

    @Override
    public void changeSize(ActorSizeEvent actorSizeEvent) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (curActor.equals(MainWindow.this)) {
                this.setOrigin(Align.center);
            }
            curActor.setSize(actorSizeEvent.width, actorSizeEvent.height);
        }
        selectedGroup.initLayout();
    }

    @Override
    public void changeOrigin(ActorOriginEvent actorOriginEvent) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (actorOriginEvent.align == -1){
                curActor.setOrigin(actorOriginEvent.originX, actorOriginEvent.originY);
            }else {
                curActor.setOrigin(actorOriginEvent.align);
            }

        }
    }

    @Override
    public void changeTextAttr(ActorTextEvent actorTextEvent) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (curActor instanceof Label) {
                ((Label) curActor).setText(actorTextEvent.msg);
            }
        }
    }

    @Override
    public void setLabelWrap(LabelWrapEvent labelWrapEvent) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (curActor instanceof Label) {
                ((Label) curActor).setWrap(labelWrapEvent.isWrap);
            }
        }
    }

    @Override
    public void setAlign(ActorAlignEvent alignEvent) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (curActor instanceof Label) {
                ((Label) curActor).setAlignment(alignEvent.align);
            }
        }
    }

    @Override
    public void changeRotate(ActorRotationEvent event) {
        float rotation = event.rotation;
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            curActor.setRotation(rotation);
        }
    }


    @Override
    public void selecedActor(TreeSelectedActroEvent event) {
        selectedGroup.clearAllActor();
        Array<Actor> array = event.actors;
        selectedGroup.addAllSelectActor(array);
        EditorManager.getInstance().getEventBus().post(new SureActorEvent(selectedGroup.getLastSelectActor()));
    }

    @Override
    public void cancelActor(TreeCancelEvent event) {
        selectedGroup.clearAllActor();
    }

    @Override
    public void moveActor(TreeActorMoveEvent event) {
        Group parentGroup = event.group;
        Actor actor = event.actor;
        if (actor.hasParent()&&parentGroup.hasParent()){
            Vector2 vector2 = new Vector2();
            actor.localToStageCoordinates(vector2);
            actor.remove();
            parentGroup.stageToLocalCoordinates(vector2);
            parentGroup.addActor(actor);
            actor.setPosition(vector2.x,vector2.y);
        }
    }

    @Override
    public void loadScene(LoadSceneEvent event) {
        XmlReader xmlReader = new XmlReader();
        try {
            XmlReader.Element stageElement = xmlReader.parse(event.sceneFile);
            float width = stageElement.getFloat("width",480);
            float height = stageElement.getFloat("height",800);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
