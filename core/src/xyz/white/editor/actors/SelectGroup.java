package xyz.white.editor.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
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

    public SelectGroup(){
        selection = new Selection<Actor>();
        debugAll();
        addListener(dragListener);
    }

    private DragListener dragListener = new DragListener(){
        @Override
        public void drag(InputEvent event, float x, float y, int pointer) {
            super.drag(event, x, y, pointer);
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
            this.setBounds(x1,y1,x2 - x1,y2 - y1);

        }

    }

    public int getSelectedActorSize(){
        return selection.size();
    }

    public Selection<Actor> getSelection() {
        return selection;
    }
}
