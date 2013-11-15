package uk.ac.cam.hp343.cg.curves;

import easel.Algorithm2D;
import easel.Renderer;

public class MidpointCircle implements Algorithm2D {
	
	public void drawOctant(double x, double y, double r){
		

		
	}
	
	public void midpointCircle(double r, double cx0, double cy0, int red, int green, int blue){
		
		// Compute center
		int cx = (int)Math.round(cx0);
		int cy = (int)Math.round(cy0);
		
		double x = r;
		double y = 0;
		
		double d = x*x + y*y - r*r;
		
		while(x>=y){
			
			Renderer.setPixel((int)Math.round(x)+cx, (int)Math.round(y)+cy, red, green, blue);
			Renderer.setPixel(-(int)Math.round(x)+cx, (int)Math.round(y)+cy, red, green, blue);
			Renderer.setPixel(-(int)Math.round(x)+cx, -(int)Math.round(y)+cy, red, green, blue);
			Renderer.setPixel((int)Math.round(x)+cx, -(int)Math.round(y)+cy, red, green, blue);
			Renderer.setPixel((int)Math.round(y)+cy, (int)Math.round(x)+cx, red, green, blue);
			Renderer.setPixel(-(int)Math.round(y)+cy, (int)Math.round(x)+cx, red, green, blue);
			Renderer.setPixel((int)Math.round(y)+cy, -(int)Math.round(x)+cx, red, green, blue);
			Renderer.setPixel(-(int)Math.round(y)+cy, -(int)Math.round(x)+cx, red, green, blue);

			
			System.out.println(d);
			
			if(d<=0){
				d = d + 2*y + 3;
			} else {
				d = d - 2*x + 2*y + 3;

				x = x-1;
			}
			y = y+1;
		}	
		
	}

	@Override
	public void runAlgorithm(int width, int height) {
		// TODO Auto-generated method stub
		midpointCircle(1.0, 20.0, 20.0, 0, 0, 0);
		midpointCircle(5.0, 20.0, 20.0, 0, 0, 0);
		midpointCircle(10.0, 20.0, 20.0, 255, 0, 0);
		midpointCircle(15.0, 20.0, 20.0, 0, 0, 255);
		midpointCircle(20.0, 20.0, 20.0, 0, 255, 0);
	}

	public static void main(String[] args){
		Renderer.init2D(41, 41, new MidpointCircle()) ;
	}
}
