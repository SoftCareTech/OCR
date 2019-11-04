package imageTotext;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class S {
	
	  static  void print (ObservableList<String> sub) {
		  System.out.println();
		  System.out.println("start");
		for(String s :sub) {
			System.out.println(s);
		}
		 System.out.println("end");
		  System.out.println( );
	}
	  
	  public static double  formDouble(double d) {
 return new Double(new DecimalFormat("##.00").format(d)).doubleValue();
	  }
	  public static void enterKeyPress() {
			Robot re;
			try {
				re = new Robot();
				//
				//re.keyPress(KeyCode.getCode());
				;

			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	  
	public static void  notice(Window owner,String title,String warning,String solution) {
			
			VBox vertical = new VBox();
			vertical.setAlignment(Pos.CENTER);
			FlowPane controls = new FlowPane();
			BorderPane main= new BorderPane(vertical);
		 
			Label text  ;
			Text warn;
			 
			
			 warn = new Text(warning );
			 text= new Label(solution );
			
text.setTextFill(Color.GREEN);;
			text.setFont(Font.font(15));
			warn.setFont(Font.font(15));
		 
		controls.setAlignment(Pos.CENTER_RIGHT);
		 
		Button ok = new Button("Ok");
		controls.getChildren().addAll( ok);
		vertical.getChildren().addAll(  new VBox(  new   TextFlow(
				//new ImageView(new Image("questionMark1.png")),
				warn) ,text) );
		main.setBottom(controls);
		Stage stage = new Stage();
		//stage.setResizable(false);
	 
		ok.setPrefSize(100, 14);
		 
		 ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				stage.close();
				
			}
			 
		 }); 
			Scene st = new Scene(main, 450, 250);
			stage.setScene(st);
			stage.centerOnScreen();
		stage.setTitle(title);
		stage.initStyle(StageStyle.UNIFIED);
		stage.initModality(Modality.APPLICATION_MODAL);
			

stage.initOwner(owner);
			 
			stage.showAndWait();;
			 
			 
			
		}
			
 private static String res="";
 private static String resDBE="";
  
 public static String  saveChanges(Window myStage, String name) {
	
	FlowPane vertical = new FlowPane();
	BorderPane main= new BorderPane(vertical);
	HBox controls = new HBox();
	Label text = new Label("Save Changes To  ");
	Label classs;
	 
	if( name.length()>40)
	 classs = new Label( name.substring(0,40)+" ?");
	else
	  classs = new Label( name);
	 
	classs.setFont(Font.font("Britannic Bold"));
	

	text.setFont(Font.font(15));
	classs.setFont(Font.font(15));
	vertical.setAlignment(Pos.CENTER);
controls.setAlignment(Pos.CENTER_RIGHT);
vertical.setAlignment(Pos.CENTER);
Button yes = new Button("Yes");
Button no = new Button("No");
Button cancel = new Button("Cancel");
controls.getChildren().addAll( yes,no,cancel);
vertical.getChildren().addAll(//new ImageView(new Image("questionMark1.png")),
		text,classs);
main.setBottom(controls);
Stage stage = new Stage();
stage.setResizable(false);
no.setPrefSize(60, 14);
yes.setPrefSize(60, 14);
cancel.setPrefSize(60, 14);
  
	Scene st = new Scene(main, 300, 100);
	stage.setScene(st);
	stage.centerOnScreen();
stage.setTitle("Take Action");
stage.initOwner(myStage);
stage.initStyle(StageStyle.UNIFIED);
stage.initModality(Modality.APPLICATION_MODAL);
	
	cancel.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {
			res="cancel";
			stage.close();
		}
		
	});
	yes.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {
			res="yes";
			stage.close();
		}
		
	});
	no.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {
			res="no";
			stage.close();
		}
		
	});
	
	stage.showAndWait();;
	 
	return res;
	
}







private static boolean bol=false;
 

private static boolean resREG=false;

 



public static Stage sta=null;
public static void  waiting(Window owner,String title,String msg ) {
	ProgressIndicator p= new ProgressIndicator();
		VBox vertical = new VBox();
	vertical.setAlignment(Pos.CENTER);
	FlowPane controls = new FlowPane();
	BorderPane main= new BorderPane(vertical);
 
	Label text  ; 
	 
	
	 text= new Label(msg);
	p.setMinSize(150, 150);
text.setTextFill(Color.GREEN);;
	text.setFont(Font.font(15)); 
 
 
vertical.getChildren().addAll( p, new VBox(  new   TextFlow(
		//new ImageView(new Image("questionMark1.png")),
		) ,text) );
main.setBottom(controls);
Stage stage = new Stage();
//stage.setResizable(false);

	Scene st = new Scene(main, 450, 250);
	stage.setScene(st);
	stage.centerOnScreen();
stage.setTitle(title);
stage.initStyle(StageStyle.UNDECORATED);
stage.initModality(Modality.APPLICATION_MODAL);
	

stage.initOwner(owner);
	 stage.centerOnScreen();
	 stage.initModality(Modality.APPLICATION_MODAL);
	stage.show();;
	sta=stage;
	  
	 
	 
	
}
	






static List<StringProperty> ls=null;
public static List<StringProperty>  input(Window owner,String title,String names [], String [] values ) {

	  ls = new ArrayList(); 
		VBox vertical = new VBox();
	vertical.setAlignment(Pos.CENTER);
	BorderPane main= new BorderPane(vertical);

Stage stage = new Stage();
	Button cancel= new Button("Cancel");
	Button ok= new Button("Submit");
	Button reset= new Button("Reset");
	 ok.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			stage.close();
			
		}
		 
	 });
	cancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ls=null;
				stage.close();
				
			}
			 
		 });
	FlowPane actions= new FlowPane(reset,cancel,ok);
	reset.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			if(ls!=null)
			for(StringProperty s:ls) {
				s.set("");
				 			}
			
		}
		 
	 });
		 	 
 GridPane table = new GridPane();
 int i=0;
 if(values!=null)
	 if(values.length!=names.length) {
		 
		System.err.println("Warnning Ignoring values because "
				+ "Initial values are not correspond to the Name ");
	 }

 for(String name:names) {
	 table.add(new Label(name), 0, i);
	 TextField tf= new TextField();
	 if(values!=null)
	 if(values.length==names.length) {
		tf.setText(values[i]); 
	 }
	 ls.add(tf.textProperty());
	// ls.add(new SimpleStringProperty(name));
	 //ls.get(i).bind(tf.textProperty());
	 ;
	 table.add(tf,1,i);
	 i++;
	 
 }
vertical.getChildren().addAll(table );
main.setBottom(actions);
//stage.setResizable(false);

	Scene st = new Scene(main, 450, 250);
	stage.setScene(st);
	stage.centerOnScreen();
stage.setTitle(title);
stage.initStyle(StageStyle.DECORATED);
stage.initModality(Modality.APPLICATION_MODAL);
	

stage.initOwner(owner);
	 stage.centerOnScreen();
	 stage.initModality(Modality.APPLICATION_MODAL);
	stage.showAndWait();
	
	return ls; 
	
}
	

public static boolean plot() {
	return false;
	}




}
