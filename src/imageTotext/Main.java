package imageTotext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class Main extends Application {
	BorderPane main;
	BorderPane imageDisplay;
	BorderPane textDisplay;
	String textValue = "";
	BorderPane n;
	Stage stage;
	Page pd = null;
	Scene scene;
	VBox logs;
	ScrollPane logsScrol;
	List<File> file;
	VBox settings;
	private TextField accuracyView;
	FileChooser fc;
	Button loadFile;
	Button convert;
	Mat currentPage;
	String currentFile = "";
	Label msg=new Label();
	String previous = "";
	Spinner<String> sp;
	WritableImage writableImage;
	private ImageView imageView;
	TextArea text = new TextArea();
	Button save = new Button("save text");
	int accuracy = 100;
	boolean auto=false;
	@Override
	public void start(Stage stage) {
		this.stage = stage;
		sp = new Spinner<String>();
		ComboBox<String> recogniseTrain = new ComboBox<>(
				FXCollections.<String>observableArrayList(
						"recognise and train",
						"recognise only", 
						"train only"));
		recogniseTrain.setEditable(false);
		recogniseTrain.getSelectionModel().select(0);
		main = new BorderPane();
		textDisplay = new BorderPane();
		imageDisplay = new BorderPane();
		logsScrol = new ScrollPane(logs);
		main.setBottom(logsScrol);
		accuracyView = new TextField();
		convert = new Button("convert ");
		msg.setVisible(true);
	 
		accuracyView.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent k) {
			IntegerStringConverter c= new IntegerStringConverter();	
			try {
				if(accuracyView.getText().isEmpty()) {
					msg.setTextFill(Color.RED);
					msg.setText("empty number");
					msg.setVisible(true);
					convert.setDisable(true);
				}else {
			int ck=	c.fromString(accuracyView.getText());
				msg.setTextFill(Color.GREEN);
				msg.setText("a number");
				msg.setVisible(false);
				convert.setDisable(false);
				if(writableImage==null) {
					convert.setDisable(true);
					msg.setText("select an image ");
					msg.setVisible(true);
				}
				if(ck==0) {
					recogniseTrain.getSelectionModel().select(1);
					auto=true;
				}else if(ck==100) {
					recogniseTrain.getSelectionModel().select(2);
					auto=true;
				}else {
					recogniseTrain.getSelectionModel().select(0);
					auto=true;
				}
				
				if(k.getCode().equals(KeyCode.ENTER)&&writableImage!=null) {
					convert.fire();
				}
				}
			}catch(Exception e) {
				msg.setTextFill(Color.RED);
				msg.setText("not a number");
				convert.setDisable(true);
				msg.setVisible(true);
			}
			}
			
		});
		fc = new FileChooser();
		loadFile = new Button("Load image");
		ListView<String> lv = new ListView<String>();
		
		accuracyView.setText("100");
		
		loadFile.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				file = fc.showOpenMultipleDialog(stage);
				lv.setPrefHeight(300);
				lv.setPrefWidth(200);
				if (file != null) {
					if (file.size() > 0) {
						try {
							S.waiting(stage, "Laoding Files ","Please Wait\n  Loading "
						+new Integer(file.size()).toString()+" page(s)");
							 
							for (File fil : file)
								lv.getItems().addAll(fil.getName());
							
							lv.getSelectionModel().selectedIndexProperty().addListener(
									new ChangeListener() {
								
								@Override
								public void changed(ObservableValue observable, Object oldValue, Object newValue) {
									ScrollPane sp;
									try {
										lv.setVisible(false);
										sp = getCurrentImage(new Integer(newValue.toString()).intValue());
										System.out.print("image " + new Integer(newValue.toString()).intValue());
										imageDisplay.setCenter(sp);
										lv.setVisible(true);
									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
								
							});
							ScrollPane sp = getCurrentImage(0);
							imageDisplay.setCenter(sp);
							lv.setVisible(true);
							// .setCenter(sp);
							imageDisplay.setCenter(sp);
							textDisplay.setLeft(lv);
							main.setCenter(imageDisplay);
							if(S.sta!=null)
								S.sta.close(); 
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(S.sta!=null)
								S.sta.close();
						}
						
					}
				}
				 
			}
			
			
			
	private ScrollPane getCurrentImage(int i) throws IOException {
				// TODO Auto-generated method stub
				writableImage = loadImage(file.get(i));
				imageView = new ImageView(writableImage);
				imageView.setPreserveRatio(true);
				
				// Creating a Group object
				Group root = new Group(imageView);
				ScrollPane sp = new ScrollPane(root);
				sp.minHeight(400);
				sp.minWidth(500);
				return sp;
			}
	
		});
recogniseTrain.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (oldValue != newValue) {
					if (newValue == "train only"&&!auto) {
						accuracyView.setText("100");
						
						auto=false;
					} else if (newValue == "recognise only"&&!auto) {
						accuracyView.setText("0");
					
						auto=false;
					} else if (!auto){
						accuracyView.setEditable(true);
						accuracyView.setText("95");
						auto=false;
					}
				}
			}
			
		});
		
		
		
		convert.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				try {
					accuracy = new Integer(accuracyView.getText()).intValue();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (currentPage != null) {
					pd = new Page(currentPage);
					text.setText(pd.getText());
					pd.getCd().draw.addListener( new ChangeListener() {
						@Override
						public void changed(ObservableValue arg0, Object arg1, Object arg2) {
							try {
								System.out.println(" Ok  Ok");
								writableImage = draw();
								imageView = new ImageView(writableImage);
								// Setting the position of the image
								imageView.setX(50);
								imageView.setY(25);
								// setting the fit height and width of the image view
								//imageView.setFitHeight(400);
								//imageView.setFitWidth(500);
								
								// Setting the preserve ratio of the image view
								imageView.setPreserveRatio(true);
								Group root;
								// Creating a Group object
								root = new Group(imageView);
								imageDisplay.setCenter(root);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					});
				}
				textValue = pd.proces(stage, accuracy);
				text.setText(textValue);
			}
			
		});
		
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fc.setTitle("Save your Text Document");
				File file = fc.showSaveDialog(stage);
				if (file != null) {
					try {
						// FileOutputStream out = new FileOutputStream();
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						fw.write(textValue);
						fw.flush();
						fw.close();
						// out.write(textValue);
						// out.flush();
					} catch (IOException e) {
						System.out.println("Error Unable to save");
						e.printStackTrace();
					}
					
				} else {
					System.out.println("Error Unable to save");
				}
			}
		});
			
		textDisplay.setCenter(text);
		textDisplay.setTop(new Label("copy the text or save"));
		settings = new VBox(loadFile, recogniseTrain, accuracyView,msg, convert);
		textDisplay.setRight(save);
		// settings.setAlignment(Pos.BASELINE_CENTER);
		main.setLeft(settings);
		main.setCenter(imageDisplay);
		main.setBottom(textDisplay);
		scene = new Scene(main, 1000, 600);
		this.stage.setScene(scene);
		this.stage.setTitle("IMAGE TEXT RECOGNITION");
		this.stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public WritableImage loadImage(File f) throws IOException {
		// Loading the OpenCV core library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// Reading the Image from the file and storing it in to a Matrix object
		currentFile = f.getAbsolutePath();
		Mat mat = Imgcodecs.imread(currentFile);
		/*
		 * Mat mat = new Mat(); 
		Mat ma= new Mat()
				;	
		// Converting to binary image...
		Imgproc.cvtColor(src, ma, Imgproc.COLOR_RGB2GRAY);//(src, mat, 200, 255, Imgproc.THRESH_BINARY);
		Imgproc.cvtColor(ma,src, Imgproc.COLOR_GRAY2RGB);
		 */
		currentPage = mat;
		String type = "";
		String fileName = f.getName();
		fileName = fileName.toLowerCase();
		if (fileName.endsWith(".jpg"))
			type = ".jpg";
		else if (fileName.endsWith("png"))
			type = ".png";
		else if (fileName.endsWith(".bmp"))
			type = ".bmp";
		else if (fileName.endsWith(".jpeg"))
			type = ".jpg";
		else
			System.out.println("undefined file loaded access ::||::" + f.getName());
		
		// Encoding the image
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(type, mat, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		
		// Displaying the image
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = ImageIO.read(in);
		System.out.println("Image Loaded");
		WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);
		return writableImage;
	}
	
	public WritableImage draw() throws IOException {
		// Loading the OpenCV core library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// Reading the Image from the file and storing it in to a Matrix object
		
		Mat image = Imgcodecs.imread(currentFile);
		
		Imgproc.rectangle(image, // where to draw the box
				new Point(pd.getIx() - 1, pd.getIy() - 1), // bottom left
				new Point(pd.getIdx() + 1, pd.getIdy() + 1), // top right
				new Scalar(0, 250, 0), 1);
		
		
		// System.out.println(pd.getIx()+" "+pd.getIy()+" "+pd.getIdx()+"
		// "+pd.getIdy());
		// Encoding the image
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		
		// Displaying the image
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = ImageIO.read(in);
		System.out.println("Image Loaded");
		WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);
		return writableImage;
	}
	
}
