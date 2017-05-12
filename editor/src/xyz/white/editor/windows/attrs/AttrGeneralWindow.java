package xyz.white.editor.windows.attrs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.actors.DigitsFieldFilter;
import xyz.white.editor.events.attrs.*;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class AttrGeneralWindow extends VisWindow {
    private VisTable generalTable = null;
    private VisTextField xTextField = null;
    private VisTextField nameTextField = null;
    private VisTextField yTextField = null;
    private VisTextField orignXTextField,orignYTextField,widthText,heightText,rotationText,zIndexTF;
    private VisTextField scaleXTF,scaleYTF;
    private Image colorImage;
    private ColorPicker picker ;
    private VisCheckBox visibleBox;
    public AttrGeneralWindow() {
        super("General Window",false);
        setSize(Config.width*0.2f,Config.height*0.4f);
        setMovable(false);
        EditorManager.getInstance().getEventBus().register(this);
    }

    //初始化通用属性
    private void initGeneralAttr(){
        generalTable = new VisTable(true);
        generalTable.add(new VisLabel("Name"));
        nameTextField = new VisTextField("Name");
        generalTable.add(nameTextField).width(getWidth()*0.3f).left();
        generalTable.row();



        generalTable.add(new VisLabel("X"));
        DigitsFieldFilter digitsFieldFilter = DigitsFieldFilter.instance;
        xTextField = new VisTextField();
        xTextField.setTextFieldFilter(digitsFieldFilter);
        generalTable.add(xTextField).width(getWidth()*0.3f).left();
        generalTable.add(new VisLabel("Y")).expand().width(20);
        yTextField = new VisTextField();
        yTextField.setTextFieldFilter(digitsFieldFilter);
        generalTable.add(yTextField).expand().width(getWidth()*0.3f).left();
        generalTable.row();
        final VisSelectBox<String> selectPosBox = new VisSelectBox<String>();
        selectPosBox.setItems(Config.alignstrs);
        selectPosBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String alignStr = selectPosBox.getSelected();
                if (Config.aligns.containsKey(alignStr)) {
                    float x = Float.valueOf(xTextField.getText().isEmpty()?"0":xTextField.getText());
                    float y = Float.valueOf(yTextField.getText().isEmpty()?"0":yTextField.getText());
                    EditorManager.getInstance().getEventBus().post(new ActorPosChangeEvent(x,y,Config.aligns.get(alignStr)));
                }
            }
        });
        generalTable.add();
        generalTable.add(selectPosBox).width(getWidth()*0.3f).left();
        generalTable.row();

        generalTable.add(new VisLabel("ScaleX"));
        scaleXTF = new VisTextField();
        scaleXTF.setTextFieldFilter(DigitsFieldFilter.instance);
        generalTable.add(scaleXTF).width(getWidth()*0.3f).left();
        scaleYTF = new VisTextField();
        scaleYTF.setTextFieldFilter(DigitsFieldFilter.instance);
        generalTable.add(new VisLabel("ScaleY"));
        generalTable.add(scaleYTF).width(getWidth()*0.3f).left();
        generalTable.row();


        generalTable.add(new VisLabel("OrginX"));
        orignXTextField = new VisTextField();
        orignXTextField.setTextFieldFilter(digitsFieldFilter);
        generalTable.add(orignXTextField).width(getWidth()*0.3f).left();
        generalTable.add(new VisLabel("OrginY"));
        orignYTextField = new VisTextField();
        orignYTextField.setTextFieldFilter(digitsFieldFilter);
        generalTable.add(orignYTextField).width(getWidth()*0.3f).left();
        generalTable.row();

        final VisSelectBox<String> selectBox = new VisSelectBox<String>();
        selectBox.setItems(Config.alignstrs);
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String alignStr = selectBox.getSelected();
                if (Config.aligns.containsKey(alignStr)) {
                    EditorManager.getInstance().getEventBus().post(new ActorOriginEvent(Config.aligns.get(alignStr)));
                }
            }
        });
        generalTable.add();
        generalTable.add(selectBox).width(getWidth()*0.3f).left();
        generalTable.row();

        generalTable.add(new VisLabel("Width"));
        widthText = new VisTextField();
        widthText.setTextFieldFilter(digitsFieldFilter);
        generalTable.add(widthText).width(getWidth()*0.3f).left();
        generalTable.add(new VisLabel("Height"));
        heightText = new VisTextField();
        heightText.setTextFieldFilter(digitsFieldFilter);
        generalTable.add(heightText).width(getWidth()*0.3f).left();
        generalTable.row();

        generalTable.add(new VisLabel("Rotation"));
        rotationText = new VisTextField();
        rotationText.setTextFieldFilter(digitsFieldFilter);
        generalTable.add(rotationText).width(getWidth()*0.3f).left();
        generalTable.row();

        generalTable.add(new VisLabel("Color"));
        colorImage = new Image(VisUI.getSkin().getDrawable("white"));
        generalTable.add(colorImage).size(32);
        picker = new ColorPicker("color picker",colorPickerAdapter);
        colorImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                getStage().addActor(picker.fadeIn());
            }
        });

        visibleBox = new VisCheckBox("visible");
        visibleBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EditorManager.getInstance().getEventBus().post(new ActorVisibleEvent(visibleBox.isChecked()));
            }
        });

        generalTable.add(visibleBox);
        generalTable.row();

        generalTable.add(new VisLabel("Z"));
        zIndexTF = new VisTextField();
        zIndexTF.setTextFieldFilter(DigitsFieldFilter.instance);
        generalTable.add(zIndexTF).width(getWidth()*0.3f).left();

        generalTable.left().padLeft(10).top().padTop(10);
        VisScrollPane scrollPane = new VisScrollPane(generalTable);
        scrollPane.setScrollingDisabled(true,false);
        add(scrollPane).expand().top();

        nameTextField.setTextFieldListener(nameFieldListener);

        xTextField.setTextFieldListener(posChangeFieldListener);
        yTextField.setTextFieldListener(posChangeFieldListener);

        widthText.setTextFieldListener(sizeChangeFieldListener);
        heightText.setTextFieldListener(sizeChangeFieldListener);

        scaleXTF.setTextFieldListener(scaleFieldListener);
        scaleYTF.setTextFieldListener(scaleFieldListener);
        zIndexTF.setTextFieldListener(zIndexTFListener);
        orignXTextField.setTextFieldListener(orginChangeFieldListener);
        orignYTextField.setTextFieldListener(orginChangeFieldListener);

        rotationText.setTextFieldListener(rotationFieldListener);
    }

    private VisTextField.TextFieldListener nameFieldListener = new VisTextField.TextFieldListener() {
        @Override
        public void keyTyped(VisTextField textField, char c) {
            String name = nameTextField.getText();
            EditorManager.getInstance().getEventBus().post(new ActorNameEvent(name));
        }
    };

    private VisTextField.TextFieldListener zIndexTFListener = new VisTextField.TextFieldListener() {
        @Override
        public void keyTyped(VisTextField textField, char c) {
            float zIndx = Float.valueOf(zIndexTF.getText().isEmpty()?"0":zIndexTF.getText());
            EditorManager.getInstance().getEventBus().post(new ActorZIndexEvent((int) zIndx));
        }
    };

    private ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(){
        @Override
        public void finished(Color newColor) {
            super.finished(newColor);
            colorImage.setColor(newColor);
            EditorManager.getInstance().getEventBus().post(new ActorColorEvent(newColor));
        }
    };

    private VisTextField.TextFieldListener scaleFieldListener = new VisTextField.TextFieldListener() {
        @Override
        public void keyTyped(VisTextField textField, char c) {
            float scaleX = Float.valueOf(scaleXTF.getText().isEmpty()?"0":scaleXTF.getText());
            float scaleY = Float.valueOf(scaleYTF.getText().isEmpty()?"0":scaleYTF.getText());
            EditorManager.getInstance().getEventBus().post(new ActorScaleEvent(scaleX,scaleY));
        }
    };

    //监听actor x y 输入框
    private VisTextField.TextFieldListener posChangeFieldListener = new VisTextField.TextFieldListener() {
        @Override
        public void keyTyped(VisTextField textField, char c) {
            float x = Float.valueOf(xTextField.getText().isEmpty()?"0":xTextField.getText());
            float y = Float.valueOf(yTextField.getText().isEmpty()?"0":yTextField.getText());
            EditorManager.getInstance().getEventBus().post(new ActorPosChangeEvent(x,y));
        }
    };

    //监听Actor的长宽输入框
    private VisTextField.TextFieldListener sizeChangeFieldListener = new VisTextField.TextFieldListener() {
        @Override
        public void keyTyped(VisTextField textField, char c) {
            float width = Float.valueOf(widthText.getText().isEmpty()?"0":widthText.getText());
            float height = Float.valueOf(heightText.getText().isEmpty()?"0":heightText.getText());
            EditorManager.getInstance().getEventBus().post(new ActorSizeEvent(width,height));
        }
    };

    //监听Actor的Org 坐标

    private VisTextField.TextFieldListener orginChangeFieldListener = new VisTextField.TextFieldListener() {
        @Override
        public void keyTyped(VisTextField textField, char c) {
            float originX = Float.valueOf(orignXTextField.getText().isEmpty()?"0":orignXTextField.getText());
            float originY = Float.valueOf(orignYTextField.getText().isEmpty()?"0":orignYTextField.getText());
            EditorManager.getInstance().getEventBus().post(new ActorOriginEvent(originX,originY));
        }
    };

    private VisTextField.TextFieldListener rotationFieldListener  = new VisTextField.TextFieldListener() {
        @Override
        public void keyTyped(VisTextField textField, char c) {
            float rotation = Float.valueOf(textField.getText().isEmpty()?"0":textField.getText());
            EditorManager.getInstance().getEventBus().post(new ActorRotationEvent(rotation));
        }
    };

    public void refreshAttr(Actor actor){
        if (xTextField == null){
            initGeneralAttr();
        }
        xTextField.setText(String.format("%.2f",actor.getX()));
        yTextField.setText(String.format("%.2f",actor.getY()));
        nameTextField.setText(actor.getName());
        scaleXTF.setText(String.valueOf(actor.getScaleX()));
        scaleYTF.setText(String.valueOf(actor.getScaleY()));
        orignYTextField.setText(String.format("%.2f",actor.getOriginY()));
        orignXTextField.setText(String.format("%.2f",actor.getOriginX()));
        widthText.setText(String.format("%.2f",actor.getWidth()));
        heightText.setText(String.format("%.2f",actor.getHeight()));
        rotationText.setText(String.format("%.2f",actor.getRotation()));
        colorImage.setColor(actor.getColor());
        picker.setColor(actor.getColor());
        visibleBox.setChecked(actor.isVisible());
        zIndexTF.setText(String.valueOf(actor.getZIndex()));
    }


}
