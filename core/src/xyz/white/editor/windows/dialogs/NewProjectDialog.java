/*
 * Copyright 2014-2016 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.white.editor.windows.dialogs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import net.mwplay.nativefont.NativeTextField;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.application.LoadProjectEvent;

/**
 * New project dialog
 * @author Kotcrab
 */
public class NewProjectDialog extends VisWindow {

	private Table containerTable;
	private FileChooser chooser;


	public NewProjectDialog () {
		super("New Project");
		TableUtils.setSpacingDefaults(this);
		setModal(true);
		addCloseButton();
		closeOnEscape();
		containerTable = new Table();

		final NativeTextField projectField = new NativeTextField("", EditorManager.getInstance().getInputTextStyle());

		VisTextButton chooseButton = new VisTextButton("...");


		chooser = new FileChooser(FileChooser.Mode.OPEN);
		chooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
		chooser.setListener(new FileChooserListener() {
			@Override
			public void selected (Array<FileHandle> files) {
				if (files.size>0){
					projectField.setText(files.get(0).path());
				}

			}

			@Override
			public void canceled () {

			}
		});
		chooseButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getStage().addActor(chooser.fadeIn());
				super.clicked(event, x, y);
			}
		});

		VisTextButton okButton = new VisTextButton("OK");

		okButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final String path = projectField.getText();
				if (path.isEmpty()){

				}else {
					Config.setProjectPath(path);
					NewProjectDialog.this.close();
					EditorManager.getInstance().getEventBus().post(new LoadProjectEvent());
				}
				super.clicked(event, x, y);
			}
		});

		containerTable.add(projectField).minWidth(200).pad(10);
		containerTable.add(chooseButton).pad(10);
		containerTable.row();
		containerTable.add(okButton).expandX().right();

		containerTable.center();
		add(containerTable).expand().fill();
		pack();
		centerWindow();
	}
}
