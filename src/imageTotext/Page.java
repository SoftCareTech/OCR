package imageTotext;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.stage.Window;

public class Page {
	
	boolean endPage;
	int endPageY = -1;
	int endPageX = -1;
	int fh = 10;
	int fw = 10;
	int whiteContant = 250;
	int whiteContantUp = 250;
	double bg[] = { 1.0, 1.0, 1.0 };
	int whiteHexa;
	public Mat page = null;
	private String text = "";
	String chara = "";
	private CharDataCompress cd; 
	public Page(Mat page) {
		this.page = page;
		cd = new CharDataCompress();
		if (this.page == null) {
			System.out.println("page.java, Error : The Mat(page) is null");
			System.exit(0);
		}
		
	}
	
	CharDataCompress getCd() {
		return cd;
	}
	
	public String getText() {
		return this.text;
	}
	
	IntegerProperty ix = new SimpleIntegerProperty(0);
	IntegerProperty iy = new SimpleIntegerProperty(0);
	IntegerProperty idx = new SimpleIntegerProperty(0);
	IntegerProperty idy = new SimpleIntegerProperty(0);
	
	/* ix property */
	public final int getIx() {
		return ix.get();
	}
	
	public final void setIx(int position) {
		ixProperty().set(position);
	}
	
	public final IntegerProperty ixProperty() {
		return ix;
	}
	
	/* iy property */
	public final int getIy() {
		return iy.get();
	}
	
	public final void setIy(int position) {
		iyProperty().set(position);
	}
	
	public final IntegerProperty iyProperty() {
		return iy;
	}
	
	/* idx property */
	public final int getIdx() {
		return idx.get();
	}
	
	public final void setIdx(int position) {
		idxProperty().set(position);
	}
	
	public final IntegerProperty idxProperty() {
		return idx;
	}
	
	/* idy property */
	public final int getIdy() {
		return idy.get();
	}
	
	public final void setIdy(int position) {
		idyProperty().set(position);
	}
	
	public final IntegerProperty idyProperty() {
		return idy;
	}
	
	public String proces(Window owner, int accuracy) {
		String st = "";
		List<List<Rect>> lines = FXCollections.<List<Rect>>observableArrayList();
		Mat image = this.page;
		
		List<Rect> abtract_form_lines = new ArrayList<Rect>();// created because recursive form of loadline();
		loadLine(image, abtract_form_lines, 0);// enter with emptylist
		// adding rect chars
		for (Rect rect : abtract_form_lines) {
			List<Rect> form_line = new ArrayList<Rect>();
			nextChar(form_line, image, rect.y, rect.height, 0);
			lines.add(form_line);
		}
		// end getting lines
		
		// getting space in between characters images, to get minimum for space for
		// word.
		int[] n = new int[50];
		if(lines.size()<1) {
			System.err.println("The page contain border");
			System.exit(0);
		}
		for (int No = 1; No < lines.get(0).size() - 1; No++) {
			// spaces count from 1 to 20
			 int limit =lines.get(0).get(No).x
						- lines.get(0).get(No - 1).width;
			 if(limit<50)
			n[limit] = n[limit] + 1;
			
		}
		int sum = 0;
		List<Integer> l = new ArrayList<Integer>();
		int last = 0;
		
		for (int g = 0; g < n.length; g++) {
			// n.lenght ==20
			// System.out.println(g+" "+n[g]);
			sum = sum + n[g];
			if (n[g] > 0) {
				l.add(n[g]);
				last = g;
			}
		}
		//int ign = l.size() / 2;
		 
		 
		 
		int comSp = last / 2;
		
		// System.out.println(rr.size()+"size"+r.get(0).y+" "+r.get(0).height);
		for (int lineNo = 0; lineNo < lines.size(); lineNo++) {
			List<Rect> line = lines.get(lineNo);
			for (int charNo = 0; charNo < line.size(); charNo++) {
				Rect c = line.get(charNo);
				String t = "";
				// x,y 9 11 w,h 11 27
				// subMate(startRow,endRow,Statcol,EndCol)11,27,9,11
				setIx(c.x);
				setIdx(c.width);
				setIy(c.y);
				setIdy(c.height);
				System.out.println(c.x + "  " + c.y + "  " + c.width + "  " + c.height);
				t = cd.proces(trimMat(image.submat(c.y, c.height, c.x, c.width)),
						image.submat(c.y, c.height, c.x, c.width), owner, accuracy);
				
				if (t != null) {
					st = st + t;
				} else {
					
					if (!cd.notify(owner, "Do you want to stop ")) {
						lineNo = lines.size();
						break;
						
					} else {
						charNo--;
					}
				}
				if (charNo > 0) {
					
					if ((comSp <= (c.x - line.get(charNo - 1).width))) {
						st = st.substring(0, st.length() - 1) + "  " + st.charAt(st.length() - 1);
					} else {
						// System.out.println( "cx-1 "+line.get(charNo-1).x+" "+
						// line.get(charNo-1).width+" "+
						// "cx "+c.x+" "+c.width+" charNo "+charNo
						// +" "+st.charAt(charNo-1)+" "+st.charAt(charNo)+" "
						// +(+c.x-line.get(charNo-1).width)
						// );
					}
					
				}
			}
			if (st.length() > 0)
				if ((st.charAt(st.length() - 1) == '-')) {
					st += " ";
				}
			
			if (st.length() > 0 && st != null)
				if ((st.charAt(st.length() - 1) == '.' || st.charAt(st.length() - 1) == ':')) {
					st = st + "\n";
				} else {
					System.out.println("last char " + st.charAt(st.length() - 1));
					st = st + " ";
				}
		}
		System.out.println("Str == " + st);
		// System.exit(0);
		return st;
	}
	
	private int nextChar(List<Rect> rect, Mat page, int y1, int y2, int x0) {
		
		if (page != null) {
			for (int s = x0; s < page.cols(); s++) {
				if (notWhite(page.col(s), y1, y2)) {
					
					for (int e = s + 1; e < page.cols(); e++) {
						if (!notWhite(page.col(e), y1, y2)) {
							rect.add(new Rect(s, y1, e, y2));
							nextChar(rect, page, y1, y2, e + 1);
							break;
						}
					}
					
					break;
				}
				
			}
			
		}
		
		return 0;
	}
	
	int h = 0;
	
	private int loadLine(Mat page, List<Rect> rect, int y0) {
		
		if (page != null) {
			for (int s = y0; s < page.rows(); s++) {
				if (notWhite(page.row(s))) {
					
					for (int e = s + 1; e < page.rows(); e++) {
						if (e - s >= h)
							if (!notWhite(page.row(e))) {
								h = e - s + 1;
								
								rect.add(new Rect(0, s, page.cols(), e));
								loadLine(page, rect, e + 1);
								break;
							}
					}
					
					break;
				}
				
			}
			
		}
		
		return 0;
	}
	
	private Mat trimMat(Mat p) {
		
		int y1 = 0, y2 = p.rows();
		;
		for (int r = 0; r < p.rows(); r++) {
			if (notWhiteH(p.row(r), 0)) {
				y1 = r;
				break;
			}
		}
		y2 = endPageY(p) + 1;
		// System.out.println("rc "+p.rows()+" "+p.cols()+" y1 y2 "+y1+" "+y2);
		Mat p1 = p.submat(y1, y2, 0, p.cols());
		
		return p1;
	}
	
	private boolean notWhiteH(Mat hImage, int startY) {
		/// put starty for line not equal first line
		
		for (int col = startY; col < hImage.cols(); col++) {
			
			double d[] = hImage.get(0, col);
			// System.out.println("maxi CR "+vImage.cols()+" "+vImage.rows());
			
			if (fc(d)) {
				return true;
			}
		}
		
		return false;
	}
	
	int endPageY(Mat page) {
		
		for (int x = page.rows() - 1; x > 0; x--) {
			if (notWhiteH(page.row(x), 0)) {
				return x;
				
			}
			;
		}
		
		return -1;
		
	}
	
	private boolean notWhite(Mat image) {
		for (int i = 0; i < image.rows(); i = i + 1) {
			for (int j = 0; j < image.cols(); j = j + 1) {
				
				double d[] = image.get(i, j);
				
				if (fc(d)) {
					return true;
				}
				
			}
		}
		
		return false;
	}
	
	private boolean notWhite(Mat vImage, int starty, int endy) {
		/// put starty for line not equal first line
		
		for (int col = starty; col < endy; col++) {
			
			double d[] = vImage.get(col, 0);
			// System.out.println("maxi CR "+vImage.cols()+" "+vImage.rows());
			
			if (fc(d)) {
				return true;
			}
		}
		
		return false;
	}
	
	boolean fc(double[] d) {
		whiteContant = 90;
		if (d[0] < whiteContant) {
			// System.out.println(" here "+d[0]);
			return true;
		}if (d[1] < whiteContant) {
			// System.out.println(" here "+d[0]);
			return true;
		}if (d[2] < whiteContant) {
			// System.out.println(" here "+d[0]);
			return true;
		}
		 
		
		return false;
		
	}
	//// After
	
	
}
