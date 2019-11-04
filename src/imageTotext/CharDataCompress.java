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
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.stage.Window;

public class CharDataCompress extends CharGraphics {
	String chs;
	String ch = "";
	
	ObservableMap<String, List<Img>> dat;
	List<String> data;
	int whiteContantUp = 250;
	
	CharDataCompress() {
		
		dat = FXCollections.<String, List<Img>>observableHashMap();
		
		dat = openData("CharDataCompress");
	}
	
	void show(boolean[] a, int cols) {
		int count = 0;
		for (boolean k : a) {
			count++;
			if (k) {
				System.out.print("+");
			} else {
				System.out.print("_");
			}
			if (count == cols) {
				System.out.println();
				count = 0;
			}
		}
	}
	
	private int[] store(Mat m, int r, int c) {
		int res[] = new int[3];
		int po = 0, x, y;
		boolean end = false;
		
		if (r + 1 == m.rows() && c + 1 == m.cols())
			end = true;
		x = r;
		y = c;
		// System.out.println(r+" XXXXXXX CALL YYYYYYYY "+c+" ");
		
		double[] d = m.get(r, c);
		
		if (d[0] >= whiteContantUp && d[1] >= whiteContantUp && d[2] >= whiteContantUp) {
			
			for (int i = r; i < m.rows(); i++) {
				
				for (int j = c; j < m.cols(); j++) {
					x = i;
					y = j;
					d = m.get(i, j);
					
					if (d[0] >= whiteContantUp && d[1] >= whiteContantUp && d[2] >= whiteContantUp) {
						po++;
						/// color detected as white
						// System.out.println(i+" -----negative--- "+j+" == "+po);
					} else {
						// System.exit(0);
						// System.out.println(i+" returning from ----negative "+j+" == "+po);
						res[0] = i;
						res[1] = j;
						res[2] = po;
						
						return res;
					}
					c = 0;
				}
				
			}
		} else {
			for (int i = r; i < m.rows(); i++) {
				for (int j = c; j < m.cols(); j++) {
					d = m.get(i, j);
					x = i;
					y = j;
					if (d[0] >= whiteContantUp && d[1] >= whiteContantUp && d[2] >= whiteContantUp) {
						// color detected as black or any other but not white
						res[0] = i;
						res[1] = j;
						res[2] = po;
						// System.out.println(i+" returning from+++++positive++++++ "+j+" == "+po);
						return res;
					} else {
						po++;
						// System.out.println(i+" +++++positive++++ "+j+" == "+po);
					}
					
				}
				c = 0;
			}
		}
		
		// System.out.println(x+" XXXXXXX LAST YYYYYYYY "+y+" "+po);
		res[0] = x;
		res[1] = y;
		res[2] = po;
		if (end)
			res[2] = 0;
		return res;
	}
	
	private boolean save(Mat src, FileOutputStream out) throws IOException {
		int c = 0;
		double[] ds = src.get(0, 0); /// getting the the first color(black or white ?)
		String sign = "";
		boolean ar[] = new boolean[src.rows() * src.cols()];
		out.write(src.rows());/// write rows
		out.write(src.cols());/// write cols
		if (ds[0] >= whiteContantUp && ds[1] >= whiteContantUp && ds[2] >= whiteContantUp) {
			sign = "-";
			out.write(1);/// write first sign
			
		} else {
			sign = "+";
			out.write(2);/// write first sign
		}
		
		int res[] = store(src, 0, 0);
		while (res[2] != 0) {
			if (sign == "+") {
				out.write(res[2]);
				// System.out.print(sign+res[2]+"| ");
				sign = "-";
				for (int i = 0; i < res[2]; i++) {
					ar[c] = true;
					c++;
				}
			} else if (sign == "-") {
				out.write(res[2]);
				// System.out.print(sign+res[2]+"| ");
				sign = "+";
				for (int i = 0; i < res[2]; i++) {
					ar[c] = false;
					c++;
				}
			} else {
				System.out.println("Error Sign issue ");
			}
			
			// call back
			res = store(src, res[0], res[1]);
		}
		out.close();
		/// change this at later
		return true;
		
	}
	
	private Img matToImg(Mat src) {
		int c = 0;
		int count = 0;
		int color = 0;
		double[] ds = src.get(0, 0); /// getting the the first color(black or white ?)
		String sign = "";
		boolean ar[] = new boolean[src.rows() * src.cols()];
		
		if (ds[0] >= whiteContantUp && ds[1] >= whiteContantUp && ds[2] >= whiteContantUp) {
			sign = "-";
			color = 1;
		} else {
			sign = "+";
			color = 2;
		}
		
		int res[] = store(src, 0, 0);
		while (res[2] != 0) {
			count++;
			if (sign == "+") {
				
				// System.out.print(sign+res[2]+"| ");
				sign = "-";
				for (int i = 0; i < res[2]; i++) {
					ar[c] = true;
					c++;
				}
			} else if (sign == "-") {
				
				// System.out.print(sign+res[2]+"| ");
				sign = "+";
				for (int i = 0; i < res[2]; i++) {
					ar[c] = false;
					c++;
				}
			} else {
				System.out.println("Error Sign issue ");
			}
			
			// call back
			res = store(src, res[0], res[1]);
		}
		Img img = new Img(src.rows(), src.cols(), color, ar, count);
		/// change this at later
		return img;
		
	}
	
	public boolean saveData(String fo, String cha, Mat m) throws IOException {
		String ch = "";
		
		if (!new File(fo).exists()) {
			new File(fo).mkdirs();
		} else {
			File x = new File(new File("").getAbsolutePath() + "/" + fo + "/data.SoftCare");
			
			try {
				if (x.exists()) {
					FileInputStream in = new FileInputStream(x);
					int i = in.read();
					while (i != -1) {
						ch = ch + (char) i;
						i = in.read();
					}
					;
					if (in != null)
						in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		ch = ch + Long.toString(new File(fo).listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".bw"))
					return true;
				return false;
			}
			
		}).length) + "<equal>" + cha + "\n";
		FileOutputStream out1 = new FileOutputStream(
				((new File(fo).getAbsolutePath() + "/img" + Long.toString(new File(fo).listFiles(new FileFilter() {
					@Override
					public boolean accept(File f) {
						if (f.getName().endsWith(".bw"))
							return true;
						return false;
					}
				}).length) + ".bw")));
		save(m, out1);
		
		try {
			FileOutputStream out = new FileOutputStream(new File(fo).getAbsolutePath() + "/data.SoftCare");
			out.write(ch.getBytes());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return true;
	}
	
	public ObservableMap<String, List<Img>> openData(String folder) {
		String ch = "";
		ObservableMap<String, String> key = FXCollections.<String, String>observableHashMap();
		ObservableMap<String, List<Img>> da = FXCollections.<String, List<Img>>observableHashMap();
		;
		File fs = new File(new File("").getAbsolutePath() + "/" + folder);
		if (fs.exists()) {
			try {
				File x = new File(new File("").getAbsolutePath() + "/" + folder + "/data.SoftCare");
				if (x.exists()) {
					FileInputStream in = new FileInputStream(x);
					
					int i = in.read();
					while (i != -1) {
						ch = ch + (char) i;
						i = in.read();
					}
					;
					in.close();
					String st[] = ch.split("\n");
					for (String sd : st) {
						String sf[] = sd.split("<equal>");
						key.put("img" + sf[0], sf[1]);
						// System.out.println(key.size()+" Values "+sf[0]+" "+ sf[1]);
					}
					
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return da;
			} catch (IOException e) {
				e.printStackTrace();
				return da;
			}
			
			for (File cm : fs.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File f) {
					if (f.getName().endsWith(".bw"))
						return true;
					return false;
				}
			})) {
				
				try {
					Img m = read(cm.getAbsolutePath());
					if (key.get(cm.getName().replaceAll(".bw", "")) != null) {
						List<Img> lm = da.get(key.get(cm.getName().replaceAll(".bw", "")));
						
						if (lm != null) {
							lm.add(m);
						} else {
							lm = new ArrayList();
							lm.add(m);
							da.put(key.get(cm.getName().replaceAll(".bw", "")), lm);
						}
					} else {
						
						System.out.println(cm.getName().replaceAll(".bw", "") + "   Null  = " + da.size());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// System.out.println("size = "+da.size());
		// System.exit(0);
		return da;
		
	}
	
	// read a file in Img form and return IMG class { boolean [] etct}
	private Img read(String absolutePath) throws IOException {
		FileInputStream in = new FileInputStream(absolutePath);
		String sign = "";
		int rows = 0;
		int cols = 0;
		int v = 0;
		rows = in.read();
		cols = in.read();
		int s = in.read();
		if (s == 2) {
			sign = "+";
			
		} else if (s == 1) {
			sign = "-";
			
		} else {
			
			System.out.println("Unable decode the first color: the File corrupt ");
			return null;
		}
		boolean a[] = new boolean[rows * cols];
		int count = 0;
		int y = 0;
		if (rows > 0 && cols > 0) {
			int d = in.read();
			while (d > 0) {
				y++;
				if (sign == "+") {
					for (int i = 0; i < d; i++) {
						a[count] = true;
						count++;
					}
					// System.out.print(sign+d+"| ");
					sign = "-";
				} else if (sign == "-") {
					for (int i = 0; i < d; i++) {
						a[count] = false;
						count++;
					}
					// System.out.print(sign+d+"| ");
					sign = "+";
				} else {
					System.out.println("Error Sign issue ");
				}
				d = in.read();
			}
		}
		Img img = new Img(rows, cols, s, a, y);
		return img;
	}
	
	private int equal(boolean[] a, boolean[] ar) {
		int r = 0;
		if (a.length == ar.length) 
			for (int i = 0; i < a.length; i++) {
				if (a[i] == ar[i]) {
					r++;
					 
				}else {
					 
				}
			}
		else {
			System.err.println("size problem in equal function");
		}
		return (int) (((double) r / a.length) * 100);
	}
	
	int cv = 1;
	
	public String proces(Mat cha, Mat view, Window owner, int accuracy) {
		// System.out.println(); System.out.println("next char"); System.out.println();
		// System.out.println();
		try {
			Set<String> st = dat.keySet();
			String highestch = "";
			int freq = 0;
			int test = 0;
			for (String ss : st) {
				
				List<Img> m = dat.get(ss);
				if (m != null) {
					
					for (int k = 0; k < m.size(); k++) {
						Img mp = m.get(k);
						// System.out.println( "char " +ss+" stored "+mp.rows() +" "
						// +mp.cols()+" "+" char "+cha.rows()+" "+cha.cols());
						int e = 0;
						double diff = (double) ((double)mp.rows  / (double) mp.cols)
								- (double) ((double) cha.rows() / (double)cha.cols() );
						
						if ( diff <= 0.1 && diff >= -0.1
							 	) {
							int changeX = 1;
							int changeY = 1;
							int resizeY;
							int resizeX;
							if (cha.cols() > mp.cols) {
								changeX = (cha.cols() / mp.cols) + 1;
								resizeX = mp.cols * changeX;
								
							} else {
								// resize
								resizeX = mp.cols;
							}
							
							if (cha.rows() > mp.rows) {
								changeY = (cha.rows() / mp.rows) + 1;
								resizeY = mp.rows * changeY;
							} else {
								// resize
								resizeY = mp.rows;
							}
							System.out.println("chang x " + changeX + "  change y " + changeY);
							System.out.println("resize x " + resizeX + "  resize y " + resizeY + "");
							// System.exit(0);
							
							Img in = matToImg(cha, resizeY, resizeX);
							Img store = toMultiSize(mp, changeX, changeY);
							
							e = equal(store.value, in.value);
							if(ss.equals("i")){
								System.out.println(ss + "====i====" + cv);
								show(mp.value, mp.cols);
								System.out.println(ss + "====equal====" + cv);
								
							System.out.println(ss + "====store====" + cv);
								 show(store.value,store.cols);
								 System.out.println( "====store====" + store.cols+"  "+store.rows );
								 System.out.println( "====in====" + in.cols+"  "+in.rows );
							}
							
						}
						if (e >= 100) {
							//System.out.println(ss + "====Head====" + test);
							//show(mp.value, mp.cols);
							//System.out.println(ss + "====equal====" + cv);
							
						//	System.out.println(ss + "====store====" + cv);
							// show(store.value,store.cols);
							cv++;
							System.out.println();
							System.out.println("====NEXT image char====");
							
							/*
							 * if(ss.equals( "I")||ss.equals( ".")) { if(completeBlack(view,98)) {
							 * return"I"; }else { return ","; } }else {
							 * 
							 * }
							 */
							return ss;
							
						} else {
							if (freq < e) {
								highestch = ss;
								freq = e;
							}
							// System.out.println(cha.rows()+" bb "+mp.rows());
						}
						
					}
				}
				
				// System.out.println(); System.out.println();
			}
			
			// System.exit(0);;
			System.out.println(highestch+"====character===="+freq);
			if (freq >= accuracy) {
				return highestch;
			}
			 setDraw(true);
			String ch = "";
			ch = super.whatChar(owner, cha, view);
			 setDraw(false);
			if (ch == null) {
				
			} else if (ch.length() > 0) {
				System.out.println("ch ==== " + ch);
				List<Img> lm = dat.get(ch);
				if (lm != null) {
					lm.add(matToImg(cha));
				} else {
					lm = new ArrayList();
					lm.add(matToImg(cha));
					dat.put(ch, lm);
				}
				try {
					saveData("CharDataCompress", ch, cha);
				} catch (FileNotFoundException e) {
					System.out.println("Unable to save" + e.getMessage());
					
				}
				
				return ch;
			}
			
			return ch;
			// return"";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Img matToImg(Mat cha, int rows, int cols) {
		Mat dst = new Mat();
		Imgproc.resize(cha, dst, new Size(cols, rows));
		// System.out.println(cols+"+new coll ="+dst.cols()+" ||| "+rows+" new rows
		// "+dst.rows());
		return matToImg(dst);
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
	
	private boolean multiChar = false;
	
	boolean equa(Mat m1, Mat m2, String e) {
		// System.out.println("m1,m2 x "+m1.cols()+" "+m2.cols()+" y ,y "+m1.cols()+"
		// "+m2.cols());
		Mat c11 = new Mat();
		boolean[][] b = new boolean[20][20];
		double change = 50.0;
		Mat c22 = new Mat();
		double whiteContant = 230;
		
		try {
			int dx = m1.cols(), dy = m1.rows();
			if (dx < m2.cols())
				dx = m2.cols();
			if (dy < m2.rows())
				dy = m2.rows();
				
			// Imgproc.resize(m1, c11, new Size(dx, dy));
			// Imgproc.resize(m2, c22, new Size(dx, dy));
			c11 = m1;
			c22 = m2;
			if (c22.rows() != c11.rows()) {
				return false;
			}
			if (c22.cols() != c11.cols()) {
				return false;
			}
			
			/// System.out.println("what "+c22.rows()+" "+c22.cols()+" wg "+c11.rows()+"
			/// "+c11.cols());
			for (int i = 1; i < c22.rows(); i = i + 1) {
				for (int j = 1; j < c22.cols(); j = j + 1) {
					
					double d[] = c11.get(i, j);
					double d2[] = c22.get(i, j);
					// System.out.print("ddd0 "+d[0]+" "+ "ddd1 "+d[1]+ " "+d2[1] +" "+"ddd2
					// "+d[2]+" " +d2[2]+"ddd2 "+d[2]+" " +d2[2]);
					// System.out.println(" cr =="+j+ " "+i) ;
					if (!(d[0] - d2[0] > -change && d[0] - d2[0] < change)) {
						// System.out.print("ddd0 "+d[0]+" " +d2[0]
						// );
						// System.out.println(" cr =="+j+ " "+i) ;
						
						b[i][j] = true;
					}
					if (!(d[1] - d2[1] > -change && d[1] - d2[1] < change)) {
						// System.out.print("ddd0 "+d[0]+" " +d2[1]
						// );
						// System.out.println(" cr =="+j+ " "+i) ;
						
						b[i][j] = true;
					}
					if (!(d[2] - d2[2] > -change && d[2] - d2[2] < change)) {
						// System.out.print("ddd0 "+d[2]+" "
						// );
						// System.out.println(" cr =="+j+ " "+i) ;
						
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
		// if((double)((double)(sum/40)*100)<98)
		// return false;
		return true;
		
	}
	
	private Img toMultiSize(Img img, int multiCol, int multiRow) {
		Img res = new Img();
		if (multiRow < 1) {
			System.out.println("MultiRow cannot be less than 1");
			multiRow = 1;
		}
		if (multiCol < 1) {
			System.out.println("MultiRow cannot be less than 1");
			multiCol = 1;
		}
		
		res.color = img.color;
		res.rows = img.rows * multiRow;
		res.cols = img.cols * multiCol;
		int index = 0;
		int oldIndex = 0;
		res.value = new boolean[res.cols * res.rows];
		
		System.out.println(" old " + img.value.length + "   new " + res.value.length);
		boolean b = true;
		for (int y = 0; y < multiRow; y++) {
			for (int r = 0; r < img.rows; r++) {
				for (int c = 0; c < img.cols; c++) {
					
					for (int i = 0; i < multiCol; i++) {
						// System.out.println( " oldIndex == "+oldIndex+" row = |"+r+"| col = |"+c+"|");
						boolean a;
						if (img.value.length > oldIndex)
							a = img.value[oldIndex];
						else
							a = img.value[oldIndex - 1];
						res.value[index] = a;
						index++;
					}
					// System.out.println("yyy == "+y+" sum bbbb == "+oldIndex+" rrrr "+r+"
					// rows"+img.rows);
					
					if (y < 1) {
						oldIndex++;
						b = false;
					}
				}
			}
		}
		
		return res;
	}
}
