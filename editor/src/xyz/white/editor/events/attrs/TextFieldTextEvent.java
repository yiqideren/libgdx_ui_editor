package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class TextFieldTextEvent extends SelectedActorEvent {
    public String text;
    public String message;
    public String orginText,orginMessage;
    public TextField textField;

    public TextFieldTextEvent(String text,String message){
        this.text = text;
        this.message = message;
    }


    @Override
    public Event redo() {
        if (textField == null && selectGroup.getLastSelectActor() instanceof TextField){
            textField = (TextField) selectGroup.getLastSelectActor();
            orginText = textField.getText();
            orginMessage = textField.getMessageText();
            textField.setText(text);
            textField.setMessageText(message);
            isUndo = true;
        }else {
            isUndo = false;
        }
        return this;
    }

    @Override
    public Event undo() {
        if (textField!=null){
            textField.setMessageText(orginMessage);
            textField.setMessageText(orginText);
        }
        return this;
    }
}
