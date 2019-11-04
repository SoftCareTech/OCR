package imageTotext;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class CharGraphics   {

	private String ch;
	private boolean multiChar;
	TextField tf = new TextField();
	protected boolean bye;
	protected boolean bye1;
	
	
	public String  whatString(Window owner,Mat cha,Mat view )   {
		 ch=""; 
		 multiChar=false;
		 Stage stage = new Stage();
			stage.setResizable(false);
			stage.initOwner(owner);
			MatOfByte matOfByte = new MatOfByte();
			Imgcodecs.imencode(".png", view, matOfByte);	 
			byte[] byteArray = matOfByte.toArray();
			InputStream in = new ByteArrayInputStream(byteArray);
			BufferedImage bufImage= null;
			try {
				bufImage = ImageIO.read(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			 
			WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null); 
			ImageView imageView = new ImageView(writableImage);
			//Setting the position of the image
			imageView.setX(50);
			imageView.setY(25);
			
			tf.setText("");
			//setting the fit height and width of the image view
			 imageView.setFitHeight(50);
		 	imageView.setFitWidth(50);  
			//Setting the preserve ratio of the image view
			imageView.setPreserveRatio(true);
			//Creating a Group object 
			Group root = new Group(imageView);		
	       BorderPane main =new BorderPane();
	       main.setStyle("-fx-background: blue;");
	        main.setCenter(root);
	        main.setBottom(tf);
	       Scene st = new Scene(main, 300, 100);
	       
			st.setOnKeyPressed(  new EventHandler<KeyEvent>() {
				@Override
					public void handle(KeyEvent k) { 
					if(k.getCode()==KeyCode.ENTER) {
						ch=tf.getText();
						stage.close();
					}
				}
					
				});
				stage.setScene(st);
				stage.centerOnScreen();
			stage.setTitle("Type Help");
			stage.initStyle(StageStyle.UNIFIED);
			stage.initModality(Modality.APPLICATION_MODAL);
				

		stage.initOwner(owner);
				 
				stage.showAndWait();
				 if(ch.length()>0) { 
				return ch;
				}else if(notify(owner,"DO want to summit empty value "))
					return whatString(owner,cha,view );
				return null;
				
			 
	}
 
public boolean  notify(Window owner, String msg) {
	
	FlowPane vertical = new FlowPane();
	BorderPane main= new BorderPane(vertical);
	HBox controls = new HBox();
	Label text = new Label(msg);
	Label classs;
	 
	 
	 classs = new Label("processing Charaters ");
	 
	
	

	text.setFont(Font.font(15));
	classs.setFont(Font.font(15));
	vertical.setAlignment(Pos.CENTER);
controls.setAlignment(Pos.CENTER_RIGHT);
vertical.setAlignment(Pos.CENTER);
Button yes = new Button("Yes");
Button no = new Button("No");
Button cancel = new Button("Cancel");
controls.getChildren().addAll( yes,no,cancel);
vertical.getChildren().addAll(text,classs);//new ImageView(new Image("questionMark1.png")),
main.setBottom(controls);
Stage stage = new Stage();
stage.setResizable(false);
no.setPrefSize(60, 14);
yes.setPrefSize(60, 14);
cancel.setPrefSize(60, 14);
  
	Scene st = new Scene(main, 300, 100);
	stage.setScene(st);
	stage.initOwner(owner);
	stage.centerOnScreen();
stage.setTitle("Take Action");
stage.initStyle(StageStyle.UNIFIED);
stage.initModality(Modality.APPLICATION_MODAL);
	
	cancel.setOnAction(new EventHandler<ActionEvent>() {

		

		@Override
		public void handle(ActionEvent arg0) {
			bye= true;
			stage.close();
		}
		
	});
	yes.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {
			bye= false;
			bye1=true;
			stage.close();
		}
		
	});
	no.setOnAction(new EventHandler<ActionEvent>() {

		 

		@Override
		public void handle(ActionEvent arg0) {
			bye= false;
			bye1=false;
			stage.close();
		}
		
	});
	
	stage.showAndWait();;
	 
	return bye;
	
}

	public String whatChar(Window owner, Mat cha, Mat view) {
		ch = "";
		multiChar = false;
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initOwner(owner);
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".png", view, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = null;
		try {
			bufImage = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);
		ImageView imageView = new ImageView(writableImage);
		// Setting the position of the image
		imageView.setX(50);
		imageView.setY(25);
		// setting the fit height and width of the image view
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		// Setting the preserve ratio of the image view
		imageView.setPreserveRatio(true);
		// Creating a Group object
		Group root = new Group(imageView);
		BorderPane main = new BorderPane();
		main.setStyle("-fx-background: blue;");
		main.setCenter(root);
		Scene st = new Scene(main, 300, 100);
		st.setOnKeyTyped(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent k) {
				{
					
					String t = k.getCharacter();
					t = t.trim();
					if (!t.equals("")) {
						ch += t;
						
						stage.close();
					} else {
						
						// System.out.println("UNDEFIND CHAR OPERATION "+t);
					}
					
				}
			}
			
		});
		st.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent k) {
				if (k.isControlDown()) {
					multiChar = true;
					stage.close();
				}
			}
			
		});
		stage.setScene(st);
		stage.centerOnScreen();
		stage.setTitle("Type Help");
		stage.initStyle(StageStyle.UNIFIED);
		stage.initModality(Modality.APPLICATION_MODAL);
		
		stage.initOwner(owner);
		//stage.setX(0);
		stage.showAndWait();
		// setDraw(false);
		if (ch.length() > 0) {
			return ch;
		} else if (multiChar == true)
			return whatString(owner, cha, view);
		return null;
		
	}

	 	
}
