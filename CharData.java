package imageTotext; 
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.stage.Window;
public class CharData extends CharGraphics{
	String chs;
	String ch="";
	 
	ObservableMap<String,List<Mat>> dat;
	List<String> data;
	int whiteContantUp =250;
	 CharData(){
		  
		dat=FXCollections.<String,List<Mat>>observableHashMap();
		
		 dat=openData("second"); 
	 }
	
	
		
		 public boolean saveData(String fo,String cha ,Mat m) throws FileNotFoundException{
		   String ch="";
			 
			 if(!new File(fo).exists()) {
				 new File(fo).mkdirs();
			 }else {
				 File x= new File(new File("").getAbsolutePath()+"/"+fo+"/data.SoftCare");
				
				try {
					if(x.exists()) {
					 FileInputStream in = new FileInputStream(x);
					 int i = in.read();
						while (i != -1){
						ch=ch+(char)i;
							i = in.read();
						} ;
if(in!=null)in.close();
}
				} catch (IOException e) {
					e.printStackTrace();
				}
				 
			 }
			
		  ch=ch+Long.toString(new File(fo).listFiles(new FileFilter() {

					@Override
					public boolean accept(File f) {
						if(f.getName().endsWith(".bmp"))
							return true;
						return false;
					}
					   
				   }).length )
						+ "<equal>"+cha+"\n" ;
		  
						Imgcodecs.imwrite( ((new File(fo).getAbsolutePath()+"/mat"+
					Long.toString(new File(fo).listFiles(new FileFilter() {

						@Override
						public boolean accept(File f) {
							if(f.getName().endsWith(".bmp"))
								return true;
							return false;
						}
						   
					   }).length )+".bmp" )), m);
					 
				
				 

				
				try {
					FileOutputStream out= new FileOutputStream(new File(fo).getAbsolutePath()+"/data.SoftCare");
				out.write(ch.getBytes());
				} catch (FileNotFoundException e) {
					 
					e.printStackTrace();
				} catch (IOException e) {
					 
					e.printStackTrace();
				} 
				return true
						;}


		public ObservableMap<String,List<Mat>> openData(String folder){
				String ch="";
				ObservableMap<String ,String> key =FXCollections.<String,String>observableHashMap();
				ObservableMap<String,List<Mat>> da=FXCollections.<String,List<Mat>>observableHashMap();;
				File fs = new File(new File("").getAbsolutePath()+"/"+folder);
				 	if(fs.exists()) {
				 try {
					 File x= new File(new File("").getAbsolutePath()+"/"+folder+"/data.SoftCare");
					 if(x.exists()) {
					FileInputStream in = new FileInputStream(x);
					 
					int i = in.read();
					while (i != -1){
					ch=ch+(char)i;
						i = in.read();
					} ;
				
					
					String st[]= ch.split("\n");
					 for(String sd:st) {
						String sf[]= sd.split("<equal>");
						key.put("mat"+sf[0], sf[1]);
						//System.out.println(key.size()+"  Values "+sf[0]+"  "+ sf[1]);			
					 }
						
					
					 }
				 } catch (FileNotFoundException e1) {
					e1.printStackTrace();
					 return da;
				} catch (IOException e) { 
					e.printStackTrace();
					return da;
				}
				 
				for(File cm:fs.listFiles(new FileFilter() {

					@Override
					public boolean accept(File f) {
						if(f.getName().endsWith(".bmp"))
							return true;
						return false;
					}
					   
				   })) { 
					try {
						 Mat m=Imgcodecs.imread(cm.getAbsolutePath());
						 if(key.get(cm.getName().replaceAll(".bmp", ""))!=null) {
							 
						List<Mat> lm= da.get(key.get(cm.getName().replaceAll(".bmp", "")));
						 if(lm!=null) {
							lm.add(m);
							}else {
								 
							lm= new ArrayList();
							lm.add(m);
						da.put(key.get(cm.getName().replaceAll(".bmp", "")), lm);
						}	}					
	           					 
	 
					
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
				}
				return da;
				
						
						}

		
		
	public String proces(Mat cha,Mat view,Window owner ) {
		try {
			Set<String> st= dat.keySet();
		for(String ss:st) {
		List<Mat> m= dat.get(ss);
			if(m!=null) {
				 
			 for(int k=0;k<m.size();k++) {
			  Mat mp= m.get(k); 
		//	System.out.println( "char " +ss+"  stored  "+mp.rows() +"     "
			//  +mp.cols()+"  "+"       char   "+cha.rows()+"  "+cha.cols());
				if(equa(mp,cha,ss)) {
					// System.out.println(" Equal "+ss);
					 // whatChar(owner,cha,view);
					if(ss=="I"||ss==".") {
						if(!completeBlack(view,98)) {
							return".";
						}else {
							return "I";
						}
					}else {
						return ss;
					}
				 }else {
					 
					 //System.out.println(cha.rows()+"  bb   "+mp.rows());
				 }
			}
		}}
		// System.exit(0);;
	         	setDraw(true);
	         	String ch="";
	         	
		ch=	super.whatChar(owner,cha,view);
		setDraw(false);	
		if(ch.length()>0) {
			 System.out.println("ch ==== "+ch);
			List<Mat> lm=dat.get(ch);
			if(lm!=null) {
				 lm.add(cha);
			}else {
				lm= new ArrayList();
				 lm.add(cha);
				 dat.put(ch, lm);
			}
			try {
				saveData("second",ch,cha);
			} catch (FileNotFoundException e) {
				 System.out.println("Unable to save"+e.getMessage());
				  
			}  
			
		return ch;
		}
		return ch;
		 //return"";
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	BooleanProperty draw = new SimpleBooleanProperty(false);
	public final boolean getDraw() {
		return draw.get();
		}
		public final void setDraw(boolean position) {
			 drawProperty().set(position);
		}
		public final BooleanProperty drawProperty() {
		return draw;
		}
	private boolean completeBlack(Mat m ,double percent){  
		int  s=m.cols()*m.rows();
		int c=0;
			for (int A = 0; A < m.rows(); A++) {
				for (int B = 0; B < m.cols(); B++) {
					double[] d = m.get(A,B);
					if (d[0] >= whiteContantUp && d[1] >= whiteContantUp && d[2] >= whiteContantUp) {
						c++;
						}
				}}
			if(((double)(s-c)/s)*100>=percent)
			return true;
			return false;
			}
 
private boolean multiChar=false;

private String loadChar(File file) {
	String c="";
	try {
		FileInputStream in= new FileInputStream(file);
		int i;
		try {
			while ((i=in.read())!=-1){
			c=c+(char)i;	
			}
			return c;
		} catch (IOException e) {
			
			e.printStackTrace();
			return c;
		}
		
		
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
		return  c;
	}
	 
}
 
boolean equa(Mat m1, Mat m2, String e) {
	//System.out.println("m1,m2 x "+m1.cols()+"  "+m2.cols()+"        y ,y "+m1.cols()+"  "+m2.cols());
	Mat c11 = new Mat();
	boolean[][] b = new boolean[20][20];
	double change = 50.0;
	Mat c22 = new Mat();
	double whiteContant = 230;

	
	try {
		int dx=m1.cols(),dy=m1.rows();
		if(dx<m2.cols())
		dx=m2.cols();
		if (dy<m2.rows())
			dy=m2.rows();
		
		// Imgproc.resize(m1, c11, new Size(dx, dy));
		//  Imgproc.resize(m2, c22, new Size(dx, dy));		 
		  c11=m1;
		  c22=m2;
		 if(c22.rows()!=c11.rows()) {
			 return false;
		 }
		 if(c22.cols()!=c11.cols()) { 
			 return false;
		 }
		 
		///System.out.println("what "+c22.rows()+"  "+c22.cols()+"    wg "+c11.rows()+"  "+c11.cols());
		for (int i = 1; i < c22.rows(); i = i + 1) {
			for (int j = 1; j < c22.cols(); j = j + 1) {

				double d[] = c11.get(i, j);
				double d2[] = c22.get(i, j);
				//System.out.print("ddd0 "+d[0]+"   "+ "ddd1 "+d[1]+ "  "+d2[1] +"  "+"ddd2 "+d[2]+"   " +d2[2]+"ddd2 "+d[2]+"   " +d2[2]);
				// System.out.println(" cr =="+j+ " "+i) ;
				if (!(d[0] - d2[0] > -change && d[0] - d2[0] < change)) {
					 // System.out.print("ddd0 "+d[0]+"   " +d2[0]
					//		  );
					 //System.out.println(" cr =="+j+ " "+i) ;
					 
					b[i][j] = true;
				}
				if (!(d[1] - d2[1] > -change && d[1] - d2[1] < change)) {
					 // System.out.print("ddd0 "+d[0]+"   " +d2[1]
					//		  );
					// System.out.println(" cr =="+j+ " "+i) ;
					 
					b[i][j] = true;
				}
				if (!(d[2] - d2[2] > -change && d[2] - d2[2] < change)) {
					//  System.out.print("ddd0 "+d[2]+"   "
					 //   );
					 //System.out.println(" cr =="+j+ " "+i) ;
					 
					b[i][j] = true;
				}
				
				 
			}
		}
	} catch (Exception h) {
		System.out.println(" Exception returning false not equal ");
		return false;
	}
	for (int f = 0; f < 20; f++) {
		for (int h = 0; h < 20; h++) {
			if (b[f][h]) {
				System.out.println(" SoftCare ");
				return false;

			}
		}
	}
//if((double)((double)(sum/40)*100)<98)
//return false;
	return true;

}

}
