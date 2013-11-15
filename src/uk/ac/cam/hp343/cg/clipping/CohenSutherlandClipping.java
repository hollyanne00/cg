package uk.ac.cam.hp343.cg.clipping;

import easel.Algorithm2D;
import easel.Renderer;

public class CohenSutherlandClipping implements Algorithm2D {
	
	private double xl;
	private double xr;
	private double yt;
	private double yb;
	
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
	
	public void drawClippingBox(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y, double p4x, double p4y){
		
		// Draw clipping box
		midpointLine(p1x, p1y, p2x, p2y, 255, 0, 0);
		midpointLine(p2x, p2y, p3x, p3y, 255, 0, 0);
		midpointLine(p3x, p3y, p4x, p4y, 255, 0, 0);
		midpointLine(p4x, p4y, p1x, p1y, 255, 0, 0);
		
		this.xl = p1x;
		this.xr = p2x;
		this.yb = p3y;
		this.yt = p1y;
		
		System.out.println(xl+" "+xr+" "+yb+" "+yt);
		
	}
	
	public void drawLines(int width, int height){
		
		// Unclipped
		midpointLine(0, 0, width-1, height-1, 0, 0, 255);
		midpointLine(0, height/2, width/2, height/2, 0, 0, 255);
		midpointLine((width/3)*2, height/3, width/3, (height/3)*2, 0, 0, 255);
		
		// Clipped
		drawClippedLine(0, 0, width-1, height-1);
		drawClippedLine(0, height/2, width/2, height/2);
		drawClippedLine((width/3)*2, height/3, width/3, (height/3)*2);
		
	}
	
	public void drawClippedLine(double x0, double y0, double x1, double y1){
		
		int q1 = 0;
		int q2 = 0;
		
		if(x0 < xl) { q1 |= 8; }
		if(x0 > xr) { q1 |= 4; }
		if(y0 > yb) { q1 |= 2; }
		if(y0 < yt) { q1 |= 1; }
		
		if(x1 < xl) { q2 |= 8; }
		if(x1 > xr) { q2 |= 4; }
		if(y1 > yb) { q2 |= 2; }
		if(y1 < yt) { q2 |= 1; }
		
		System.out.println("q1 = " + q1 + " q2 = " + q2);
		
		if((q1 | q2) == 0) {
			// Both ends in center so just draw the line
			midpointLine(x0, y0, x1, y1, 0, 255, 0);
		} else if((q1 & q2)>0) { 
			// Dont draw: lines are in the same plane outside the center so wont be seen
		} else {
			// Intersect with one of the edges and try again 
			int q = q1>0 ? q1 : q2;
			
			System.out.println(x0+" "+y0+" "+x1+" "+y1);
			
			double x, y;
			if((q & 8)>0){ // left
				x = xl;
				y = y0 + (y1 - y0)*((x - x0)/(x1-x0));
			} else if((q & 4)>0){ // right
				x = xr;
				y = y0 + (y1 - y0)*((x - x0)/(x1-x0));
			} else if((q & 2)>0){ // bottom
				y = yb;
				x = x0 + (x1 - x0)*((y - y0)/(y1-y0));
			} else { // top
				y = yt;
				x = x0 + (x1 - x0)*((y - y0)/(y1-y0));
			}
			
			System.out.println(x+" "+y);
			
			if(q==q1){
				// intersected at q1 edge
				System.out.println("q1 edge");
				drawClippedLine(x, y, x1, y1);
			} else {
				// intersected at q2 edge
				System.out.println("q2 edge");
				drawClippedLine(x0, y0, x, y);
			}
			
			
		}
		
	}

	@Override
	public void runAlgorithm(int width, int height) {
		double qW = width/8;
		double qH = height/8;
		drawClippingBox(qW, qH, qW*7, qH, qW*7, qH*7, qW, qH*7);
		drawLines(width, height);
	}
	
	public static void main(String[] args){
		Renderer.init2D(60, 40, new CohenSutherlandClipping());
	}
	
}
