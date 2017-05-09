package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;


import org.lwjgl.openal.AL;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.actors.SelectGroup;
import xyz.white.editor.events.assets.LoadSceneEvent;
import xyz.white.editor.events.attrs.*;
import xyz.white.editor.events.editor.ActorAddEvent;
import xyz.white.editor.events.editor.RefreshWindowEvent;
import xyz.white.editor.events.editor.SureActorEvent;
import xyz.white.editor.events.listener.AssetEventListener;
import xyz.white.editor.events.listener.ChangeActorAttrListener;
import xyz.white.editor.events.listener.ShorcutEventListener;
import xyz.white.editor.events.listener.TreeEventListener;
import xyz.white.editor.events.shortcut.AlignEvent;
import xyz.white.editor.events.tree.TreeActorMoveEvent;
import xyz.white.editor.events.tree.TreeCancelEvent;
import xyz.white.editor.events.tree.TreeSelectedActroEvent;
import xyz.white.editor.utils.FileUtils;

import java.io.IOException;

/**
 * Created by 10037 on 2017/4/15 0015.
 */

public class MainWindow extends Group implements ChangeActorAttrListener, TreeEventListener, AssetEventListener,ShorcutEventListener {
    private NativeFont font;
    private SelectGroup selectedGroup = new SelectGroup();
    private FileHandle curSceneFile;
    private EditorLister editorLister;

    public void setEditorLister(EditorLister editorLister) {
        this.editorLister = editorLister;
    }

    public EditorLister getEditorLister() {
        return editorLister;
    }

    public MainWindow(String title) {
        setSize(480, 800);
        debug();
        EditorManager.getInstance().getEventBus().register(this);
        font = EditorManager.getInstance().getMainFont();
        this.addListener(clickListener);
        this.addListener(selectListener);
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
                cloneActor = new VisImage(EditorManager.getInstance().assetManager.get("badlogic.jpg", Texture.class));
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
            cloneActor.addListener(clickListener);
            EditorManager.getInstance().getEventBus().post(new ActorAddEvent(cloneActor));
            if (editorLister != null) editorLister.change();
        }
    }

    private InputListener selectListener = new InputListener() {
        private float startX = 0, startY = 0;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (button == Input.Buttons.RIGHT) {
                startX = x;
                startY = y;
                selectedGroup.clearAllActor();
                selectedGroup.setVisible(true);
                return true;
            }
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            float width = startX - x;
            float hight = startY - y;
            selectedGroup.setDragBounds(startX, startY, -width, -hight);
//            super.touchDragged(event, x, y, pointer);
        }

    };

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
            if (curActor.equals(MainWindow.this)) {
                //                setBackGround(color);
            } else {
                curActor.setColor(color);
            }
            if (editorLister != null) editorLister.change();
        }
    }

    @Override
    public void changePos(ActorPosChangeEvent event) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            curActor.setPosition(event.x, event.y, event.align);
            if (editorLister != null) editorLister.change();
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
            if (editorLister != null) editorLister.change();
        }
        selectedGroup.initLayout();
    }

    @Override
    public void changeOrigin(ActorOriginEvent actorOriginEvent) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (actorOriginEvent.align == -1) {
                curActor.setOrigin(actorOriginEvent.originX, actorOriginEvent.originY);
            } else {
                curActor.setOrigin(actorOriginEvent.align);
            }
            if (editorLister != null) editorLister.change();
        }
    }

    @Override
    public void changeTextAttr(ActorTextEvent actorTextEvent) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (curActor instanceof Label) {
                ((Label) curActor).setText(actorTextEvent.msg);
                if (editorLister != null) editorLister.change();
            }
        }
    }

    @Override
    public void setLabelWrap(LabelWrapEvent labelWrapEvent) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            if (curActor instanceof Label) {
                ((Label) curActor).setWrap(labelWrapEvent.isWrap);
                if (editorLister != null) editorLister.change();
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
            if (editorLister != null) editorLister.change();
        }
    }

    @Override
    public void changeRotate(ActorRotationEvent event) {
        float rotation = event.rotation;
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor != null) {
            curActor.setRotation(rotation);
            if (editorLister != null) editorLister.change();
        }
    }

    @Override
    public void setImagePath(ImagePathEvent event) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor instanceof Image) {
            Texture source = new Texture(Config.getImageFilePath(event.imagePath));
            ((Image) curActor).setDrawable(new TextureRegionDrawable(new TextureRegion(
                    source)
            ));
            curActor.setSize(source.getWidth(), source.getHeight());
            selectedGroup.initLayout();
            if (editorLister != null) editorLister.change();
        }
    }

    @Override
    public void setButtonPath(ButtonPathEvent event) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor instanceof Button) {

            String up = event.up;
            String down = event.down;
            String check = event.check;
            Gdx.app.log("eee", "0000" + up + "------down--" + down + "-----check--" + check);
            if (up != null && !up.isEmpty()) {
                Texture upTexture = new Texture(Config.getImageFilePath(up));
                Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(
                        upTexture)
                );
                Drawable downDrawable, checkDrawable;
                if (down.isEmpty()) {
                    downDrawable = new TextureRegionDrawable(new TextureRegion(
                            upTexture)
                    );
                } else {
                    downDrawable = new TextureRegionDrawable(new TextureRegion(
                            new Texture(Config.getImageFilePath(down)))
                    );
                }

                if (check.isEmpty()) {
                    checkDrawable = new TextureRegionDrawable(new TextureRegion(
                            upTexture)
                    );
                } else {
                    checkDrawable = new TextureRegionDrawable(new TextureRegion(
                            new Texture(Config.getImageFilePath(check)))
                    );
                }
                VisImageButton.VisImageButtonStyle visImageButtonStyle = new VisImageButton.VisImageButtonStyle(
                        upDrawable, downDrawable, checkDrawable, upDrawable, downDrawable, checkDrawable
                );
                ((Button) curActor).setStyle(visImageButtonStyle);
                curActor.setSize(upTexture.getWidth(), upTexture.getHeight());
                selectedGroup.initLayout();
                if (editorLister != null) editorLister.change();
            }
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
        if (editorLister != null) editorLister.change();
    }

    @Override
    public void moveActor(TreeActorMoveEvent event) {
        Group parentGroup = event.group;
        Actor actor = event.actor;
        if (actor.hasParent() && parentGroup.hasParent()) {
            Vector2 vector2 = new Vector2();
            actor.localToStageCoordinates(vector2);
            actor.remove();
            parentGroup.stageToLocalCoordinates(vector2);
            parentGroup.addActor(actor);
            actor.setPosition(vector2.x, vector2.y);
            if (editorLister != null) editorLister.change();
        }
    }

    @Override
    public void loadScene(LoadSceneEvent event) {
        try {
            this.curSceneFile = event.sceneFile;
            MainWindow.this.clearChildren();
            selectedGroup.clearAllActor();
            MainWindow.this.addActor(selectedGroup);
            FileUtils.ReadFile(MainWindow.this, event.sceneFile, clickListener);
            EditorManager.getInstance().getEventBus().post(new RefreshWindowEvent(MainWindow.this));
            if (editorLister != null) editorLister.loadScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveScene() {
        if (this.curSceneFile != null) {
            try {
                FileUtils.WriteFile(MainWindow.this, this.curSceneFile);
                if (editorLister != null) editorLister.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void align(AlignEvent alignEvent) {
        if (selectedGroup.getAllActor().size>1){
            Actor tmp = selectedGroup.getAllActor().first();
            Vector2 tmpVec = new Vector2();
            tmp.localToStageCoordinates(tmpVec);
            for (Actor actor:selectedGroup.getAllActor()){
                if (actor.equals(tmp)) continue;
                Vector2 vector2 = new Vector2(tmpVec);
                actor.getParent().stageToLocalCoordinates(vector2);
                switch (alignEvent.align){
                    case Align.left:
                        actor.setPosition(vector2.x,actor.getY());
                        break;
                    case Align.right:
                        actor.setPosition(vector2.x+tmp.getWidth(),actor.getY()+actor.getHeight()/2,Align.right);
                        break;
                    case Align.center:
                        actor.setPosition(vector2.x+tmp.getWidth()/2,vector2.y+tmp.getHeight()/2,Align.center);
                        break;
                }
            }
        }
    }


    public interface EditorLister {
        void loadScene();

        void change();

        void save();
    }
}
