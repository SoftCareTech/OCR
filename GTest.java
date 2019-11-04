package imageTotext; 

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
 
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GTest  extends Application{
	
	private Mat matrix= null;
	Stage stage;
    public static void main(String[] args) throws Exception{ 
        launch(args);              
}
    
    WritableImage writableImage= null;
    Group root;
    BorderPane bp;
    ImageView imageView;
	@Override
	public void start(Stage stage) throws Exception {
		this.stage=stage;
		// AddingTextToImage obj = new AddingTextToImage();
		//WritableImage writableImage = obj.LoadImage(); 
		 //loadImage();
		  // writableImage =draw();
		writableImage=	loadAndConvert();
				// loadImage();
		 // WritableImage writableImage = capureFrame();
		//WritableImage writableImage = capureSnapShot();
		//Setting the image view
		  
	imageView = new ImageView(writableImage);
		//Setting the position of the image
		//imageView.setX(50);
		//imageView.setY(25);
		//setting the fit height and width of the image view
		 //imageView.setFitHeight(400);
		 //imageView.setFitWidth(500); 
		 
		//Setting the preserve ratio of the image view
		imageView.setPreserveRatio(true);
		//Creating a Group object 
		root = new Group(imageView);
		bp = new BorderPane();
		bp.setCenter(root);
		//Creating a scene object
		
		Scene scene = new Scene(bp, 800, 600);
		//Setting title to the Stage
		stage.setTitle("Loading an image");
		//Adding scene to the stage
		stage.setScene(scene);
	/*	
	 	 
		pd.getCd().draw.addListener((ChangeListener<? super Boolean>) new ChangeListener() {

			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				try {
					writableImage =draw();
					imageView = new ImageView(writableImage);
					//Setting the position of the image
					imageView.setX(50);
					imageView.setY(25);
					//setting the fit height and width of the image view
					imageView.setFitHeight(400);
					imageView.setFitWidth(500); 
					 
					//Setting the preserve ratio of the image view
					imageView.setPreserveRatio(true);
					//Creating a Group object 
					root = new Group(imageView);
					bp.setCenter(root);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		*/
		//Displaying the contents of the stage
		stage.show();
		//System.exit(0);
	//	 String fs=  pd.proces(stage,100);
		// System.out.println(fs);
		
		
	}
	Page pd = null;
public WritableImage draw() throws IOException{
		//Loading the OpenCV core library 
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		//Reading the Image from the file and storing it in to a Matrix object
		String file ="C:/Users/GBENGE AONDOAKULA/Pictures/Screenshots/page0.png";
		Mat image = Imgcodecs.imread(file);
		 
		Imgproc.rectangle(image,    // where to draw the box
				new Point(pd.getIx()-1, pd.getIy()-1), // bottom left
				new Point( pd.getIdx()+1,  pd.getIdy()+1), //  top right 
				new Scalar(0, 234, 0),
				1);   
		// System.out.println(pd.getIx()+"  "+pd.getIy()+"  "+pd.getIdx()+"  "+pd.getIdy());
		//Encoding the image
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		 
		//Displaying the image
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = ImageIO.read(in);
		System.out.println("Image Loaded");
		WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null); 
		return writableImage;
		}
	
	
	
	
public WritableImage loadImage() throws IOException{
	//Loading the OpenCV core library 
	System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	//Reading the Image from the file and storing it in to a Matrix object
	String file ="C:/Users/GBENGE AONDOAKULA/Pictures/Screenshots/page0.png";
	Mat image = Imgcodecs.imread(file);
	matrix =image;
	
                   pd = new Page(image);
	
	//Encoding the image
	MatOfByte matOfByte = new MatOfByte();
	Imgcodecs.imencode(".jpg", image, matOfByte);
	byte[] byteArray = matOfByte.toArray();
	 
	//Displaying the image
	InputStream in = new ByteArrayInputStream(byteArray);
	BufferedImage bufImage = ImageIO.read(in);
	System.out.println("Image Loaded");
	WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null); 
	return writableImage;
	}

int whiteContantUp =250;

private int[] store(Mat m,int r,int c){
	int res[] = new int[3];
	 int po=0,x,y;
 boolean end=false;
 
	 if(r+1==m.rows()&&c+1==m.cols())
		 end=true;
x=r;y=c;
	// System.out.println(r+" XXXXXXX CALL YYYYYYYY "+c+"  ");
	
		double[] d = m.get(r, c);
		
	if (d[0] >= whiteContantUp && d[1] >= whiteContantUp && d[2] >= whiteContantUp) {
		 
			for (int i = r; i < m.rows(); i++) {
				
				for (int j = c; j < m.cols(); j++) {
					x=i;y=j; d = m.get(i, j);
					
					if (d[0] >= whiteContantUp && d[1] >= whiteContantUp && d[2] >= whiteContantUp) {
						po++;
						///color detected as white
						// System.out.println(i+"  -----negative--- "+j+" == "+po);
					  }else {
						//System.exit(0);
						 //System.out.println(i+"  returning from  ----negative "+j+" == "+po);
						res[0] = i;
						res[1] = j;
						res[2] = po;
						
						return res;
					}
					c=0;
				}

			}
		} else {
			for (int i = r; i< m.rows();i++) { 
				for (int j = c; j < m.cols(); j++) {	 
					d = m.get(i, j);
					x=i;
					y=j;
					if (d[0] >= whiteContantUp && d[1] >= whiteContantUp && d[2] >= whiteContantUp) {
						//color detected as black  or any other but not white
						res[0] = i;
						res[1] = j;
						res[2] = po;
						// System.out.println(i+"   returning from+++++positive++++++  "+j+" == "+po);
						return res;
					}else { 
						po++;
						// System.out.println(i+"  +++++positive++++ "+j+" == "+po);
						}
						
				}c=0;}
		}
		
		//System.out.println(x+" XXXXXXX LAST YYYYYYYY "+y+"  "+po);
		res[0] = x;
		res[1] = y;
		res[2] = po;
		if(end)res[2] =0;
		return res;
}
	
	////extra methods
public WritableImage loadAndConvert() throws Exception{
	// Loading the OpenCV core library 
	System.loadLibrary( Core.NATIVE_LIBRARY_NAME ); 
	// Instantiating the imagecodecs class
	Imgcodecs imageCodecs = new Imgcodecs();
	String input = "C:\\Users\\GBENGE AONDOAKULA\\eclipse-workspace\\Picture to texts\\second\\mat1.bmp";
	input="C:\\Users\\GBENGE AONDOAKULA\\Pictures\\main\\back.png";
	// Reading the image
	Mat src = imageCodecs.imread(input);
	// Creating the destination matrix 
	 
	Mat dst = new Mat(); 
	// Converting to binary image.../
	  
	// Extracting data from the transformed image (dst)

	
	 Imgproc.resize(src, dst, new Size(20, 50));
	 
	// Imgproc.threshold(src, dst, 0, 5000, Imgproc.THRESH_BINARY);
	byte[] data1 = new byte[dst.rows() * dst.cols() * (int)(dst.elemSize())];
dst.get(0, 0, data1);

//Encoding the image
	MatOfByte matOfByte = new MatOfByte();
	Imgcodecs.imencode(".jpg", dst, matOfByte);
	byte[] byteArray = matOfByte.toArray();
	 
	//Displaying the image
	InputStream in = new ByteArrayInputStream(byteArray);
	BufferedImage bufImage = ImageIO.read(in);
	System.out.println("Image Loaded");
	WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null); 
	
// Creating Buffered image using the data
	//BufferedImage bufImage = new BufferedImage(dst.cols(),dst.rows(), 
//	BufferedImage.TYPE_BYTE_BINARY);
	// Setting the data elements to the image
//	bufImage.getRaster().setDataElements(0, 0, dst.cols(), dst.rows(), data1);
	// Creating a Writable image
	//WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null); 
	//System.out.println("Converted to binary");
	//Imgproc.threshold(src, dst, 200, 500, Imgproc.THRESH_BINARY);
	
	
	
	
	
	
	
	
	
	
	
	
	imageCodecs.imwrite(new File("").getAbsolutePath()+"/img.bmp", dst);
	FileOutputStream out= new FileOutputStream(new File(new File("").getAbsolutePath()+"/new.bmp"));
	 int f=0;
	 
	 /*
	 
	 ////save   char image  // read bool 
	 int c=0;
	 double[] ds = src.get(0,0);  /// getting the the first color(black or white ?)
		String sign="";
		boolean ar[] = new boolean[src.rows()*src.cols()];
		 out.write( src.rows());///write rows
		 out.write( src.cols());///write cols
		 if (ds[0] >= whiteContantUp && ds[1] >= whiteContantUp && ds[2] >= whiteContantUp) {
				sign="-";
				 out.write( 1);///write first sign
				 
			}else {
				sign="+";
				 out.write(2);///write first sign
			}
		
	int res[] = store(src,0,0);
	while(res[2]!=0) {
		if(sign=="+") {
			 out.write( res[2]);
			 System.out.print(sign+res[2]+"|      ");
			 sign="-";
			 for(int i=0; i<res[2];i++) {
				  ar[c]=true;
				  c++;
			  }
		}else if(sign=="-"){
			 out.write( res[2]);
			 System.out.print(sign+res[2]+"|      ");
			 sign="+";
			 for(int i=0; i<res[2];i++) {
				  ar[c]=false;
				  c++;
			  }
		}else {
			System.out.println("Error Sign issue ");
		}
		
		f=f+res[2];/// the the array sum to comfirm size 
		 
		//call back 
		res= store(src,res[0],res[1]);
	}
	out.close();
	
	///end of save
	
	
	System.out.println();
	
	
	//open and compare
	
	
	
	
	//reading  stored char image of the modify image format
	/*FileInputStream in =new FileInputStream(new File(new File("").getAbsolutePath()+"/new.bmp"));
	String pk="-";
	int rows=0;
	int cols=0;
	int v=0;
	rows=in.read();
	cols=in.read();
	int s=in.read();
	if (s==2) {
		sign="+";
		     
	}else if(s==1){
		sign="-";
		 
	}else{
		System.out.println("Unable decode the first color: the File corrupt ");
	}
	boolean a[] = new boolean[rows*cols];
	int count=0;
	//System.out.println(a.length+" rows "+rows+"  col "+cols);
	if(rows>0&&cols>0) {
	int d=in.read();
	while(d>0) {
		if(sign=="+") {
			  for(int i=0; i<d;i++) {
				  a[count]=true;
				  count++;
			  }
			 System.out.print(sign+d+"|    +  ");
			 sign="-";
		}else if(sign=="-"){
			for(int i=0; i<d;i++) {
				  a[count]=false;
				  count++;
			  }
			 System.out.print(sign+d+"|      ");
			 sign="+";
		}else {
			System.out.println("Error Sign issue ");
		}
		d=in.read();
	}
	
	
	}
	 
	System.out.println();
	count=0;
	for(boolean k :a) {
		 count++;
		 if(k) {
			 System.out.print("+");	 
		 }else {
			 System.out.print("_");
		 }
		 if(count==cols)
		 {
			 System.out.println();
			 count=0;
		 }
	}
	System.out.println("\nsum "+f);
	 a[45]=false;
	System.out.println(equal(a,ar));
	 
	
	
	 */
	
	
	
	
	
	return writableImage;   
	}
	
	private int equal(boolean[] a, boolean[] ar) {
		 int r=0;
		 for(int i=0;i<a.length;i++) {
			 if(a[i]==ar[i]) {
				 r++;
			 }
		 }
		return (int)(((double)r/a.length)*100);
	}




	public WritableImage capureFrame(){
		WritableImage writableImage = null; 
		 
		// Loading the OpenCV core library 
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME ); 
		// Instantiating the VideoCapture class (camera:: 0)
		VideoCapture capture = new VideoCapture(0);
		// Reading the next video frame from the camera
		Mat matrix = new Mat();
		capture.read(matrix);
		// If camera is opened 
		if(!capture.isOpened()){
		System.out.println("camera not detected");
		}else
		System.out.println("Camera detected ");
		// If there is next video frame
		if (capture.read(matrix)){ 
		/////// Detecting the face in the snap /////
		String file = "E:/OpenCV/facedetect/lbpcascade_frontalface.xml";
		CascadeClassifier classifier = new CascadeClassifier(file);
		MatOfRect faceDetections = new MatOfRect();
		classifier.detectMultiScale(matrix, faceDetections); 
		System.out.println(String.format("Detected %s faces", 
		faceDetections.toArray().length));
		// Drawing boxes 
		for (Rect rect : faceDetections.toArray()) {
		Imgproc.rectangle(matrix, //where to draw the box
		new Point(rect.x, rect.y), //bottom left
		new Point(rect.x + rect.width, rect.y + rect.height),   //top right 
		new Scalar(0, 0, 255)); //RGB colour
		}
		 
		// Creating BuffredImage from the matrix
		BufferedImage image = new BufferedImage(matrix.width(), 
		matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		matrix.get(0, 0, data);
		this.matrix = matrix; 
		// Creating the Writable Image
		writableImage = SwingFXUtils.toFXImage(image, null);
		}
		return writableImage;
		}
	
	
	public WritableImage capureSnapShot(){
		WritableImage WritableImage = null;
		// Loading the OpenCV core library 
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME ); 
		// Instantiating the VideoCapture class (camera:: 0)
		VideoCapture capture = new VideoCapture(0);
		// Reading the next video frame from the camera
		Mat matrix = new Mat();
		capture.read(matrix);
		// If camera is opened 
		if( capture.isOpened()){
		// If there is next video frame
		if (capture.read(matrix)){ 
		// Creating BuffredImage from the matrix
		BufferedImage image = new BufferedImage(matrix.width(), 
		matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();  
		 
		byte[] data = dataBuffer.getData();
		matrix.get(0, 0, data); 
		this.matrix = matrix;
		// Creating the Writable Image
		WritableImage = SwingFXUtils.toFXImage(image, null);
		}
		}
		return WritableImage;
		}
	
	

	
}
