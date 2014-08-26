package com.project3.resume;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.NodeList;
public class Main extends GdxTest {
	Stage stage;

        @Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
                
                //INITIALIZE UI ELEMENTS
                final TextArea output = new TextArea("", skin);
		TextButton button = new TextButton("Open Word Processor", skin);
                TextButton button2 = new TextButton("Upload Letter", skin);
                
                //SET UI ELEMENTS PROPERTIES
                output.setDisabled(true);
                
		button.addListener(new InputListener() {
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        try {
                            System.out.println("Opening Word Processor...");
                            Runtime rt = Runtime.getRuntime();
                            Process p = rt.exec("C:\\Program Files (x86)\\LibreOffice 4\\program\\swriter.exe");
                            System.out.println("Opened Word Processor.");
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return false;
                    }
		});
                
		button2.addListener(new InputListener() {
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        System.out.println("Choosing file to upload...");
                        FileDialog chooser = new java.awt.FileDialog((java.awt.Frame) null);
                        chooser.setTitle("Choose file to upload");
                        chooser.setVisible(true);
                        String fileChosen = chooser.getDirectory() + chooser.getFile();
                        if(chooser.getFile() != null) {
                            System.out.println("File chosen: " + fileChosen);
                            output.setText("File chosen: " + fileChosen);
                            /*
                            Runtime rt = Runtime.getRuntime();
                            Process p1 = rt.exec("cmd.exe /c C:\\Users\\Iris\\Documents\\libGDx\\xdoc2txt.exe C:\\Users\\Iris\\Documents\\libGDx\\base.odt > C:\\Users\\Iris\\Documents\\libGDx\\1.txt");
                            Process p2 = rt.exec("cmd.exe /c C:\\Users\\Iris\\Documents\\libGDx\\xdoc2txt.exe \"" + chooser.getDirectory() + chooser.getFile() + "\" > C:\\Users\\Iris\\Documents\\libGDx\\2.txt");
                            System.out.println("Converting to text files...");
                            p1.waitFor();
                            p2.waitFor();
                            System.out.println("Opening text files...");
                            List<String> original = fileToLines("C:\\Users\\Iris\\Documents\\libGDx\\1.txt");
                            List<String> submitted  = fileToLines("C:\\Users\\Iris\\Documents\\libGDx\\2.txt");
                            */
                            /*
                            List<String> original = nodeToLines("C:\\Users\\Iris\\Documents\\libGDx\\1.odt");
                            List<String> submitted  = nodeToLines(chooser.getDirectory() + chooser.getFile());
                            System.out.println("Comparing files...");
                            output.setText("Comparing files...");
                            Patch patch = DiffUtils.diff(original, submitted);
                            System.out.println("Differences: ");
                            if(patch.getDeltas().isEmpty())
                            {
                                System.out.println("None");
                                output.setText("Uploaded document " + chooser.getFile() + " is the same.");
                            }
                            else
                            {
                                output.setText("Uploaded document " + chooser.getFile() + " is different.\n\nDifferences on lines:");
                                String d, pos, diff;
                                int trimSize = 75, buffSize = 3;
                                for (Object delta: patch.getDeltas()) {
                                    System.out.println(delta);
                                    d = delta.toString();
                                    pos = d.substring(d.indexOf("position: ") + "position: ".length(), d.indexOf(", l"));
                                    diff = d.substring(d.indexOf("lines: ")+"lines: ".length(), d.length() - 1);
                                    if (pos.length() + diff.length() > trimSize){
                                        diff = diff.substring(0, trimSize - (pos.length() + buffSize)) + "...";
                                    }
                                    output.setText(output.getText() + "\n" + pos + " : " + diff);
                                }
                            }
                            try {
                                String xml = TextDocument.loadDocument(fileChosen).getContentRoot().toString();
                                System.out.println(xml);
                                for (String s: original) {
                                    if(s.length() > 0) {
                                        if (xml.contains(s)) {
                                            System.out.println(s + " present");
                                        }
                                        else {
                                            System.out.println(s + " absent");
                                        }
                                    }
                                    
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            */
                        }
                        else
                        {
                            System.out.println("File choosing cancelled.");
                        }
                        return false;
                    }
		});
                
                //ScrollPane sp = new ScrollPane(output, skin);
                //stage.addActor(sp);
                
                //ADD UI ELEMENTS
                stage.addActor(button);
		stage.addActor(button2);
                stage.addActor(output);
                
                //SET UI ELEMENTS' POSITIONS
                button.setPosition(10, 430);
                button2.setPosition(10, 400);
                output.setBounds(10, 10, 620, 300);
                
                output.setText("Ready.");
	}

        @Override
	public void render () {
                Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

        @Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

        @Override
	public void dispose () {
		stage.dispose();
	}
}