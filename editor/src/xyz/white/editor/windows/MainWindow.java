package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;


import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.actors.SelectGroup;
import xyz.white.editor.events.*;
import xyz.white.editor.events.Event;
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
import xyz.white.editor.utils.DrawableUtil;
import xyz.white.editor.utils.FileUtils;

import java.io.IOException;

/**
 * Created by 10037 on 2017/4/15 0015.
 */

public class MainWindow extends Group implements ChangeActorAttrListener, TreeEventListener, AssetEventListener, ShorcutEventListener {
    private NativeFont font;
    private SelectGroup selectedGroup = new SelectGroup();
    private FileHandle curSceneFile;
    private EditorLister editorLister;
    private Drawable cubDrawable;//用于显示场景大小
    private ShapeRenderer shapeRenderer;

    public void setEditorLister(EditorLister editorLister) {
        this.editorLister = editorLister;
    }

    public EditorLister getEditorLister() {
        return editorLister;
    }

    public MainWindow(String title) {
        setSize(480, 800);
        cubDrawable = DrawableUtil.getRectLineDrawable("icon/select.9.png");
        debug();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        EditorManager.getInstance().getEventBus().register(this);
        font = EditorManager.getInstance().getMainFont();
        this.addListener(clickListener);
        this.addCaptureListener(selectListener);
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
                cloneActor = new CheckBox("CheckBox", VisUI.getSkin());
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
            float orginX = Math.min(x, startX);
            float oaginY = Math.min(y, startY);
            float width = Math.abs(startX - x);
            float hight = Math.abs(startY - y);
            selectedGroup.setDragBounds(orginX, oaginY, width, hight);
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
    public void setImagePath(ImagePathEvent event) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor instanceof Image) {
            Texture source = new Texture(Config.getImageFilePath(event.imagePath));
            Drawable sourceDra;
            if (event.isNine) {
                int left = event.nines[0];
                int right = event.nines[1];
                int top = event.nines[2];
                int bottom = event.nines[3];
                sourceDra = new NinePatchDrawable(new NinePatch(source, left, right, top, bottom));
            } else {
                sourceDra = new TextureRegionDrawable(new TextureRegion(
                        source)
                );
            }
            ((Image) curActor).setDrawable(sourceDra);
            curActor.setSize(((Image) curActor).getPrefWidth(), ((Image) curActor).getPrefHeight());
            selectedGroup.initLayout();
            if (editorLister != null) editorLister.change();
        }
    }

    @Override
    public void setNineDrawable(NineDrawableEvent event) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor instanceof Image) {
            Image image = (Image) curActor;
            if (image.getDrawable() instanceof NinePatchDrawable) {
                Gdx.app.log("app", "set nine-----------------111");
                NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) image.getDrawable();
                ninePatchDrawable.setPatch(new NinePatch(ninePatchDrawable.getPatch().getTexture()
                        , event.nines[0], event.nines[1], event.nines[2], event.nines[3]));
            }
        }
    }

    @Override
    public void setButtonPath(ButtonPathEvent event) {
        Actor curActor = selectedGroup.getLastSelectActor();
        if (curActor instanceof Button) {

            String up = event.up;
            String down = event.down;
            String check = event.check;
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
    public void selecedActorEvent(SelectedActorEvent event) {
        event.setSelectGroup(selectedGroup);
        event.redo();
        if (editorLister != null) editorLister.change();
        if (event.isUndo){
            EditorManager.getInstance().addEvent(event);
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
            FileUtils.readFile(MainWindow.this, event.sceneFile, clickListener);
            EditorManager.getInstance().getEventBus().post(new RefreshWindowEvent(MainWindow.this));
            if (editorLister != null) editorLister.loadScene();
            EditorManager.getInstance().clearEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveScene() {
        if (this.curSceneFile != null) {
            try {
                FileUtils.writeFile(MainWindow.this, this.curSceneFile);
                if (editorLister != null) editorLister.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void align(AlignEvent alignEvent) {
        if (selectedGroup.getAllActor().size > 1) {
            Actor tmp = selectedGroup.getAllActor().first();
            Vector2 tmpVec = new Vector2();
            tmp.localToStageCoordinates(tmpVec);
            for (Actor actor : selectedGroup.getAllActor()) {
                if (actor.equals(tmp)) continue;
                Vector2 vector2 = new Vector2(tmpVec);
                actor.getParent().stageToLocalCoordinates(vector2);
                switch (alignEvent.align) {
                    case Align.left:
                        actor.setPosition(vector2.x, actor.getY());
                        break;
                    case Align.right:
                        actor.setPosition(vector2.x + tmp.getWidth(), actor.getY() + actor.getHeight() / 2, Align.right);
                        break;
                    case Align.center:
                        actor.setPosition(vector2.x + tmp.getWidth() / 2, actor.getY(Align.center), Align.center);
                        break;
                    case Config.centerH:
                        actor.setPosition(actor.getX(Align.center), vector2.y + tmp.getHeight() / 2, Align.center);
                        break;
                    case Align.bottom:
                        actor.setPosition(actor.getX(), vector2.y);
                        break;
                    case Align.top:
                        actor.setPosition(actor.getX(), vector2.y + tmp.getHeight(), Align.topLeft);
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
