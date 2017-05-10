package xyz.white.editor.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.utils.Array;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.editor.AttrEvent;
import xyz.white.editor.windows.MainWindow;

/**
 * 选择框
 * Created by 10037 on 2017/4/22 0022.
 */
public class SelectGroup extends Group {
    private Selection<Actor> selection;
    private Drawable cubDra;
    private Rectangle cubRect,childRect;

    public SelectGroup(){
        cubRect = new Rectangle();
        childRect = new Rectangle();
        setCullingArea(cubRect);
        cubDra = getRectLineDrawable();
        selection = new Selection<Actor>();
        addListener(dragListener);
    }

    private DragListener dragListener = new DragListener(){
        @Override
        public void drag(InputEvent event, float x, float y, int pointer) {
            if (pointer == -1) return;
            Actor actor = event.getListenerActor();
            actor.moveBy(x - getTouchDownX(), y - getTouchDownY());
            if (getAllActor().contains(SelectGroup.this.getParent(),false)){
                return;
            }
            for (Actor child : getAllActor()){
                if (child instanceof MainWindow){
                    break;
                }else {
                    child.moveBy(x-getTouchDownX(),y-getTouchDownY());
                }
            }
            Actor lastActor = selection.getLastSelected();
            if (lastActor!=null){
                EditorManager.getInstance().getEventBus().post(new AttrEvent(lastActor));
                if (getParent() instanceof MainWindow){
                    MainWindow mainWindow = (MainWindow) getParent();
                    if (mainWindow.getEditorLister()!=null) mainWindow.getEditorLister().change();
                }
            }
        }
    };

    public void setDragBounds(float x, float y, float width, float height) {
        setBounds(x, y, width, height);
        Vector2 vector2 = new Vector2(x,y);
        getParent().localToStageCoordinates(vector2);
        getCullingArea().set(vector2.x,vector2.y,width,height);
        MainWindow mainWindow = (MainWindow) this.getParent();
        isInsideCub(mainWindow);
        setZIndex(100);
    }

    //判断是否在cub中
    private void isInsideCub(Group group){
        for (Actor child:group.getChildren()){
            if (EditorManager.getInstance().getActorType(child).equals(Group.class)){
                isInsideCub((Group) child);
            }
            if (child instanceof SelectGroup) continue;
            Vector2 childVect2 = child.getParent().localToStageCoordinates(new Vector2(child.getX(),child.getY()));
            childRect.set(childVect2.x,childVect2.y,child.getWidth(),child.getHeight());
            if (getCullingArea().overlaps(childRect)){
                if (!selection.contains(child)){
                    child.debug();
                    selection.add(child);
                }
            }else if (selection.contains(child)){
                selection.remove(child);
                child.setDebug(false);
            }
        }
    }

    public void clearAllActor(){
        for (Actor actor:getAllActor()){
            if (actor instanceof MainWindow) continue;
            actor.setDebug(false);
        }
        selection.clear();
        setVisible(false);
        initLayout();
    }

    public void addSelectActor(Actor actor){
        selection.add(actor);
        setZIndex(100);
        initLayout();
    }

    public void addAllSelectActor(Array<Actor> actors){
        selection.addAll(actors);
        initLayout();
    }

    public Actor getLastSelectActor(){
        return selection.getLastSelected();
    }

    public Array<Actor> getAllActor(){
        return selection.toArray();
    }

//    重新设置大小
    public void initLayout(){
        if (selection.size()<=0){
            setVisible(false);
        }else {
            setVisible(true);
            Array<Actor> actors = getAllActor();
            Actor parentWindow = SelectGroup.this.getParent();
            float x1=0,x2 = 0,y1=0,y2=0;
            for (int i = 0; i <actors.size; i++) {

                Actor actor = actors.get(i);

                if (actor instanceof MainWindow){
                    x1 = 0;
                    x2 = actor.getWidth();
                    y1 = 0;
                    y2 = actor.getHeight();
                    break;
                }
                Vector2 posV = new Vector2(0,0);
                Vector2 posV2 = new Vector2(actor.getWidth(),actor.getHeight());
                posV =  actor.localToStageCoordinates(posV);
                posV2 = actor.localToStageCoordinates(posV2);

                posV = parentWindow.stageToLocalCoordinates(posV);
                posV2 = parentWindow.stageToLocalCoordinates(posV2);
                float minX = Math.min(posV.x,posV2.x);
                float maxX = Math.max(posV.x,posV2.x);
                float minY = Math.min(posV.y,posV2.y);
                float maxY = Math.max(posV.y,posV2.y);
                if (i == 0){
                    x1 = minX;
                    x2 = maxX;
                    y1 = minY;
                    y2 = maxY;
                }else {

                    if (minX<x1) x1 = minX;
                    if (maxX>x2) x2 = maxX;
                    if (minY<y1) y1 = minY;
                    if (maxY>y2) y2 = maxY;
                }

            }
            SelectGroup.this.setBounds(x1,y1,x2 - x1,y2 - y1);

        }

    }

    @Override
    public void act(float delta) {

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        cubDra.draw(batch,getX(),getY(),getWidth(),getHeight());
        super.draw(batch, parentAlpha);
    }


    // 创建钜形线条Drawable
    public Drawable getRectLineDrawable() {
        NinePatchDrawable nine = new NinePatchDrawable(new NinePatch(
                EditorManager.getInstance().assetManager.get("icon/select.9.png",Texture.class),
                2,2, 2, 2));
        return nine;
    }

    public int getSelectedActorSize(){
        return selection.size();
    }

    public Selection<Actor> getSelection() {
        return selection;
    }
}
