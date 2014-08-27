package com.project3.screens;

import java.awt.FileDialog;
import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.project3.actors.DialogueBox;

public class ResumeScreen implements Screen {

	private Stage stage;
	private Skin skin;

	private TextButton downloadBtn;
	private TextButton uploadBtn;
	private DialogueBox dialogueBox;
	private ScrollPane scrollPane;

	private Label[] instructionLabel;
	private Label activityLabel;
	private Label activityTitleLabel;

	private BitmapFont font18;
	private BitmapFont font14;
	private BitmapFont font12;
	private Skin dialogueSkin;

	private Image resumeImage;
	private Image host;

	private String[] introText = {
			"One way of effectively promoting one's skills, experience and accomplishments is through a well presented résumé. Let's see if you can help make Ms. Martin's résumé standout by following the editing instructions provided.",
			"Refer to the side bar for instructions on how to start this activity.\nGood luck!!!" };

	private final String[] instructionText = {
			"1. Click the \"Download\" button below to start editing the résumé.",
			"2. After saving the resume using the LibreOffice software, click the \"Upload\" button to submit your work." };

	public ResumeScreen() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		initUIActors();
	}

	private void initUIActors() {
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"), new TextureAtlas(
				"ui/uiskin.atlas"));

		downloadBtn = new TextButton("Download Resume", skin);
		uploadBtn = new TextButton("Upload Resume", skin);

		resumeImage = new Image(new TextureRegion(new Texture(
				Gdx.files.internal("ui/resume-image.jpg"))));
		scrollPane = new ScrollPane(resumeImage, skin);
		host = new Image(new TextureRegion(new Texture(
				Gdx.files.internal("ui/host.png"))));

		// Add listener for openBtn
		downloadBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				try {
					Gdx.app.log("ResumeScreen", "Opening Word Processor...");
					System.out.println("Opening Word Processor...");
					Runtime rt = Runtime.getRuntime();
					Process p = rt
							.exec("C:\\Program Files (x86)\\LibreOffice 4\\program\\swriter.exe");
					Gdx.app.log("ResumeScreen", "Opened Word Proccessor.");
					
					dialogueBox.updateDisplay("Downloaded \"resume.docx\". You can now begin editing.");
					
				} catch (IOException e) {
					Gdx.app.error("ResumeScreen",
							"Cannot open word processor.", e);
				}
				return true;
			}
		});

		// Add listener for uploadBtn
		uploadBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log("ResumeScreen", "Choosing file to upload...");
				FileDialog chooser = new java.awt.FileDialog(
						(java.awt.Frame) null);
				chooser.setTitle("Choose file to upload");
				chooser.setVisible(true);
				String fileChosen = chooser.getDirectory() + chooser.getFile();
				if (chooser.getFile() != null) {
					Gdx.app.log("ResumeScreen", "File chosen: " + fileChosen);
					dialogueBox.setText("File chosen: " + fileChosen);
				} else {
					Gdx.app.log("ResumeScreen", "File choosing cancelled.");
				}
				
				dialogueBox.updateDisplay("You have uploaded \"resume.docx\". We are now checking your changes.");
				return true;
			}
		});

		generateFonts();
		initDialogue();
		initSideBar();

		Table titleTable = new Table();
		titleTable.setBackground(new NinePatchDrawable(
				getNinePatch(("ui/bg_title.9.png"))));
		titleTable.add(activityLabel);
		titleTable.row().spaceBottom(10f);
		titleTable.add(activityTitleLabel);
		titleTable.row().spaceBottom(10f);

		Table sideBarTable = new Table();
		sideBarTable.setBackground(new NinePatchDrawable(
				getNinePatch(("ui/bg_sidebar.9.png"))));
		sideBarTable.add(titleTable).spaceBottom(20f);
		sideBarTable.row().spaceBottom(50f);
		sideBarTable.add(instructionLabel[0]).width(230f).spaceBottom(20f);
		sideBarTable.row().spaceBottom(20f);
		sideBarTable.add(downloadBtn).spaceBottom(50f);
		sideBarTable.row().spaceBottom(20f);
		sideBarTable.add(instructionLabel[1]).width(230f).spaceBottom(20f);
		sideBarTable.row().spaceBottom(20f);
		sideBarTable.add(uploadBtn).spaceBottom(20f);
		sideBarTable.row().spaceBottom(30f);
		sideBarTable.setBounds(780, 0, 245, 1050);
		stage.addActor(sideBarTable);

		// Add UI actors to stage
		stage.addActor(scrollPane);
		stage.addActor(dialogueBox);
		stage.addActor(host);
		// stage.addActor(okBtn);

		// Set UI actor's position
		scrollPane.setBounds(35, 120, 720, 550);
		dialogueBox.setBounds(35, 10, 720, 100);
		host.setPosition(750, 0);
		dialogueBox.setScreen(this);
	}

	private NinePatch getNinePatch(String fname) {

		final Texture t = new Texture(Gdx.files.internal(fname));
		return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 2,
				t.getWidth() - 2), 10, 10, 10, 10);
	}

	private void initDialogue() {
		dialogueSkin = new Skin();
		dialogueSkin.add("bg",
				new Texture(Gdx.files.internal("ui/dialoguebg.png")));
		dialogueSkin.add("font", font14);
		dialogueSkin.add("ticker",
				new Texture(Gdx.files.internal("ui/dialogue-ticker.png")));

		TextFieldStyle tfstyle = new TextFieldStyle();
		tfstyle.background = dialogueSkin.getDrawable("bg");
		tfstyle.cursor = dialogueSkin.getDrawable("ticker");
		tfstyle.font = dialogueSkin.getFont("font");
		tfstyle.fontColor = Color.WHITE;
		tfstyle.background.setLeftWidth(tfstyle.background.getLeftWidth() + 20);
		tfstyle.background.setTopHeight(tfstyle.background.getTopHeight() + 20);
		tfstyle.background
				.setRightWidth(tfstyle.background.getRightWidth() + 20);

		dialogueBox = new DialogueBox("", tfstyle);
		dialogueBox.setDisabled(true);
		
		dialogueBox.addText(introText[0]);
		dialogueBox.addText(introText[1]);
		dialogueBox.updateDisplay();
	}

	private void initSideBar() {
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font14;
		labelStyle.fontColor = Color.YELLOW;
		activityLabel = new Label("ACTIVITY", labelStyle);

		labelStyle = new LabelStyle();
		labelStyle.font = font18;
		labelStyle.fontColor = Color.WHITE;
		activityTitleLabel = new Label("Creating a Résumé", labelStyle);

		instructionLabel = new Label[instructionText.length];
		labelStyle = new LabelStyle();
		labelStyle.font = font12;
		labelStyle.fontColor = Color.WHITE;

		for (int i = 0; i < instructionLabel.length; i++) {
			instructionLabel[i] = new Label(instructionText[i], labelStyle);
			// instructionLabel[i].setBounds(775, 570 - (i * 150), 225, 55);
			instructionLabel[i].setWrap(true);
		}

	}

	@SuppressWarnings("deprecation")
	private void generateFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				Gdx.files.internal("ui/Anonymous.ttf"));
		font18 = generator.generateFont(18);
		font14 = generator.generateFont(14);
		font12 = generator.generateFont(12);
		generator.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(4 / 255f, 155 / 255f, 157 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

	}

	public boolean clickDialogueBox() {
		return false;
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		dialogueSkin.dispose();
	}

}
