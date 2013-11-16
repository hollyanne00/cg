package uk.ac.cam.hp343.cg.curves;

import easel.Algorithm2D;
import easel.Renderer;

public class AdaptiveBezierCubic implements Algorithm2D {

	public void midpointLine(double x0, double y0, double x1, double y1, int red, int green, int blue){
		
	// make sure that x0 < x1 so in octant 1, 2, 7, 8
	if( x0 > x1 ) {
		double xt = x0 ; x0 = x1 ; x1 = xt ;
		double yt = y0 ; y0 = y1 ; y1 = yt ;
	}
	
	double a = y1-y0;
	double b = -(x1-x0);
	double c = x1*y0 - x0*y1;

		if(y0<y1){ // Octant 1 or 2
			if( (y1-y0) <= (x1-x0) ) { //Octant 1
				
				System.out.println("Octant 1");
				
				// Calculate initial x and initial y
				int x = (int)Math.round(x0);
				int y = (int)Math.round((-a*x-c)/b);
				
				double d = a*(x+1) + b*(y+0.5) + c;
				
				// Do we need to go E or SE
				while(x<=Math.round(x1)){
					Renderer.setPixel(x, y, red, green, blue);
					if(d<0){
						d = d+a;
					} else {
						d = d+a+b;
						y = y+1;
					}
					x = x+1;
				}
				
			} else { // Octant 2
				
				System.out.println("Octant 2");
				
				// Calculate initial x and initial y
				int y = (int)Math.round(y0);
				int x = (int)Math.round((-b*y-c)/a);
		
				double d = a*(x+0.5) + b*(y+1) + c;
				
				// Do we need to go SE or S					
				while(y<=Math.round(y1)){
					Renderer.setPixel(x, y, red, green, blue);
					if(d>0){
						d = d+b; 
					} else {
						d = d+a+b;
						x = x+1;
					}
					y = y+1;
				}					
				
			}
			
		} else { // Octant 7 or 8
			if( (y0-y1) <= (x1-x0) ) { // Octant 8
				
				System.out.println("Octant 8");
				
				int x = (int)Math.round(x0);
				int y = (int)Math.round((-a*x-c)/b);					
				
				double d = a*(x+1) + b*(y-0.5) + c;
				
				// Do we need to go E or NE					
				while(x<=Math.round(x1)){
					Renderer.setPixel(x, y, red, green, blue);
					if(d>0){ 
						d = d+a;
					} else { 
						d = d+a-b;
						y = y-1;
					}
					x = x+1;
				}
				
			} else { //Octant 7
				
				System.out.println("Octant 7");
				
				int y = (int)Math.round(y0);
				int x = (int)Math.round((-b*y-c)/a);
				
				double d = a*(x+0.5) + b*(y-1) + c;
				
				// Do we need to go NE or N
				while(y>=Math.round(y1)){
					Renderer.setPixel(x, y, red, green, blue);
					if(d<0){
						d = d-b; 
					} else {
						d = d+a-b;
						x = x+1;
					}
					y = y-1;
				}					
				
			}
		}	
	}
	
	public void drawCurve(){
		
	}
	
	public void bezierCubic(double p0x, double p0y, double p1x, double p1y, double p2x, double p2y, double p3x, double p3y, double tolerance){
		
		double s;
		
		// Calculate distance of control point p1 from line
		double d1;
		s = ((p3x-p0x)*(p1x-p0x)+(p3y-p0y)*(p1y-p0y))/((p3x-p0x)*(p3x-p0x)+(p3y-p0y)*(p3y-p0y));
		if(s>0&&s<1){
			// Distance is |CP(s)|
			d1 = Math.sqrt(Math.pow(((1-s)*p0x + s*p3x-p1x), 2)+Math.pow(((1-s)*p0y + s*p3y-p1y),2));
		} else if(s<=0){
			// Distance is |AC|
			d1 = Math.sqrt(Math.pow(p1x-p0x,2) + Math.pow(p1y-p0y, 2));
		} else {
			// Distance is |BC|
			d1 = Math.sqrt(Math.pow(p1x-p3x,2) + Math.pow(p1y-p3y, 2));
		}
		
		double d2;
		s = ((p3x-p0x)*(p2x-p0x)+(p3y-p0y)*(p2y-p0y))/((p3x-p0x)*(p3x-p0x)+(p3y-p0y)*(p3y-p0y));
		if(s>0&&s<1){
			// Distance is |DP(s)|
			d2 = Math.sqrt(Math.pow(((1-s)*p0x + s*p3x-p1x), 2)+Math.pow(((1-s)*p0y + s*p3y-p1y),2));
		} else if(s<=0){
			// Distance is |AD|
			d2 = Math.sqrt(Math.pow(p2x-p0x,2) + Math.pow(p2y-p0y, 2));
		} else {
			// Distance is |BD|
			d2 = Math.sqrt(Math.pow(p2x-p3x,2) + Math.pow(p2y-p3y, 2));
		}
		
		// Do p1 and p2 lie within <tolerance> of the line?
		if(d1<=tolerance&&d2<=tolerance){
			// Draw the line between p0 and p3
			midpointLine(p0x, p0y, p3x, p3y, 0, 0, 0);
		} else {
			// Subdivide and recompute
			bezierCubic(p0x, p0y, (0.5*p0x + 0.5*p1x), (0.5*p0y + 0.5*p1y), (0.25*p0x + 0.5*p1x + 0.25*p2x), (0.25*p0y + 0.5*p1y + 0.25*p2y),
					(0.125*p0x + 3*0.125*p1x + 3*0.125*p2x + 0.125*p3x), (0.125*p0y + 3*0.125*p1y + 3*0.125*p2y + 0.125*p3y), tolerance);
			bezierCubic((0.125*p0x + 3*0.125*p1x + 3*0.125*p2x + 0.125*p3x), (0.125*p0y + 3*0.125*p1y + 3*0.125*p2y + 0.125*p3y), 
					(0.25*p1x + 0.5*p2x + 0.25*p3x), (0.25*p1y + 0.5*p2y + 0.25*p3y), (0.5*p2x + 0.5*p3x), (0.5*p2y + 0.5*p3y), p3x, p3y, tolerance);
		}
		
	}
	
	@Override
	public void runAlgorithm(int width, int height) {
		bezierCubic(0,0,2*height,0,-height,width-1,height-1,width-1,1);
	}
	
	public static void main(String[] args){
		Renderer.init2D(40, 40, new AdaptiveBezierCubic());
	}

}
