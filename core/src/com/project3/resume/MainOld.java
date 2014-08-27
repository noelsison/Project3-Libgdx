package com.project3.resume;

import java.awt.FileDialog;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainOld extends GdxTest {
	Stage stage;

	@Override
	public void create() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		// INITIALIZE UI ELEMENTS
		final TextArea output = new TextArea("", skin);
		TextButton button = new TextButton("Open Word Processor", skin);
		TextButton button2 = new TextButton("Upload Letter", skin);

		// SET UI ELEMENTS PROPERTIES
		output.setDisabled(true);

		button.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				try {
					System.out.println("Opening Word Processor...");
					Runtime rt = Runtime.getRuntime();
					Process p = rt
							.exec("C:\\Program Files (x86)\\LibreOffice 4\\program\\swriter.exe");
					System.out.println("Opened Word Processor.");
				} catch (IOException ex) {
					Logger.getLogger(MainOld.class.getName()).log(Level.SEVERE,
							null, ex);
				}
				return false;
			}
		});

		button2.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				System.out.println("Choosing file to upload...");
				FileDialog chooser = new java.awt.FileDialog(
						(java.awt.Frame) null);
				chooser.setTitle("Choose file to upload");
				chooser.setVisible(true);
				String fileChosen = chooser.getDirectory() + chooser.getFile();
				if (chooser.getFile() != null) {
					System.out.println("File chosen: " + fileChosen);
					output.setText("File chosen: " + fileChosen);
				} else {
					System.out.println("File choosing cancelled.");
				}
				return false;
			}
		});

//		 ScrollPane sp = new ScrollPane(output, skin);
//		 stage.addActor(sp);

		// ADD UI ELEMENTS
		stage.addActor(button);
		stage.addActor(button2);
		stage.addActor(output);

		// SET UI ELEMENTS' POSITIONS
		button.setPosition(10, 430);
		button2.setPosition(10, 400);
		output.setBounds(10, 10, 620, 300);

		output.setText("Ready.");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}