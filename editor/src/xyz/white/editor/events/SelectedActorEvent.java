package xyz.white.editor.events;

import xyz.white.editor.actors.SelectGroup;

/**
 * Created by 10037 on 2017/5/13 0013.
 */
public class SelectedActorEvent implements Event {
    protected SelectGroup selectGroup;
    public boolean isUndo = false;

    public SelectGroup getSelectGroup() {
        return selectGroup;
    }

    public void setSelectGroup(SelectGroup selectGroup) {
        this.selectGroup = selectGroup;
    }

    @Override
    public Event redo() {
        return this;
    }

    @Override
    public Event undo() {
        return this;
    }
}
