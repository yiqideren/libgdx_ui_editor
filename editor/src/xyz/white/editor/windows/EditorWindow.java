package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import org.lwjgl.openal.AL;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.shortcut.AlignEvent;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class EditorWindow extends VisWindow {
    private Group group;
    private boolean isChange = false;

    public EditorWindow(final MainWindow mainWindow) {
        super("Editor",false);
        setSize(Config.width * 0.6f, Config.height * 0.7f);
        group = new Group();
        group.setSize(mainWindow.getWidth(),mainWindow.getHeight());
        group.setPosition(getWidth()/2,getHeight()/2,Align.center);
        group.addActor(mainWindow);

        initShortCutTable();


        addActor(group);
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

        mainWindow.setEditorLister(new MainWindow.EditorLister() {
            @Override
            public void loadScene() {
                group.setPosition(EditorWindow.this.getWidth()/2,EditorWindow.this.getHeight()/2,Align.center);
            }

            @Override
            public void change() {
                if (!isChange){
                    isChange = true;
                    getTitleLabel().setText("*Editor");
                }
            }

            @Override
            public void save() {
                if (isChange){
                    isChange = false;
                    getTitleLabel().setText("Editor");
                }
            }
        });
    }


    private void initShortCutTable(){
        VisTable shortcutTable = new VisTable();
        VisImageButton alignLeftBtn = new VisImageButton(new TextureRegionDrawable(new TextureRegion(
                EditorManager.getInstance().assetManager.get("icon/align_left.png",Texture.class)
        )));
        alignLeftBtn.setUserObject(Align.left);
        VisImageButton alignRightBtn = new VisImageButton(new TextureRegionDrawable(new TextureRegion(
                EditorManager.getInstance().assetManager.get("icon/align_right.png",Texture.class)
        )));
        alignRightBtn.setUserObject(Align.right);
        VisImageButton alignCenterBtn = new VisImageButton(new TextureRegionDrawable(new TextureRegion(
                EditorManager.getInstance().assetManager.get("icon/align_center.png",Texture.class)
        )));
        alignCenterBtn.setUserObject(Align.center);

        VisImageButton alignHCenterBtn = new VisImageButton(new TextureRegionDrawable(new TextureRegion(
                EditorManager.getInstance().assetManager.get("icon/align_h_center.png",Texture.class)
        )));
        alignHCenterBtn.setUserObject(Config.centerH);
        VisImageButton alignTopButton = new VisImageButton(new TextureRegionDrawable(new TextureRegion(
                EditorManager.getInstance().assetManager.get("icon/align_top.png",Texture.class)
        )));
        alignTopButton.setUserObject(Align.top);
        VisImageButton alignBottomButton = new VisImageButton(new TextureRegionDrawable(new TextureRegion(
                EditorManager.getInstance().assetManager.get("icon/align_bottom.png",Texture.class)
        )));
        alignBottomButton.setUserObject(Align.bottom);

        alignLeftBtn.addListener(alignClickListener);
        alignRightBtn.addListener(alignClickListener);
        alignCenterBtn.addListener(alignClickListener);
        alignHCenterBtn.addListener(alignClickListener);
        alignBottomButton.addListener(alignClickListener);
        alignTopButton.addListener(alignClickListener);

        shortcutTable.add(alignLeftBtn);
        shortcutTable.add(alignRightBtn).spaceLeft(5);
        shortcutTable.row();
        shortcutTable.add(alignCenterBtn).spaceTop(5);
        shortcutTable.add(alignHCenterBtn).spaceLeft(5).spaceTop(5);
        shortcutTable.row();
        shortcutTable.add(alignTopButton).spaceTop(5);
        shortcutTable.add(alignBottomButton).spaceLeft(5).spaceTop(5);

        this.add(shortcutTable).expand().left().padLeft(10).top().padTop(10);
    }

    private ClickListener alignClickListener = new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y) {
            int align = (int) event.getListenerActor().getUserObject();
            EditorManager.getInstance().getEventBus().post(new AlignEvent(align));
            super.clicked(event, x, y);
        }
    };

}
