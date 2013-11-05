package uk.ac.cam.hp343.cg.sup1;
import easel.Algorithm2D;
import easel.Renderer;

public class LineDraw implements Algorithm2D {
	public void midpointLine(double x0, double y0, double x1, double y1, int red, int green, int blue){
		
		double a = y1-y0;
		double b = -(x1-x0);
		double c = x1*y0 - x0*y1;
		
		int x = (int)Math.round(x0);
		int y = (int)Math.round((-a*x-c)/b);
		
		
		if(x0<x1){ 
			// Octant 1, 2, 7 or 8
			if(y0<y1){
				// Octant 1 or 2
				if( (y1-y0) <= (x1-x0) ) { 
					// Octant 1: need to go E or NE
					System.out.println("Octant 1");
					double d = a*(x+1) + b*(y+1/2) + c;
					while(x<Math.round(x1)){
						Renderer.setPixel(x, y, red, green, blue);
						if(d<0){
							d = d+a;
						} else {
							d = d+a+b;
							y = y+1;
						}
						x = x+1;
					}
				} else {
					// Octant 2: need to go N or NE
					System.out.println("Octant 2");
					double d = a*(x+1/2) + b*(y+1) + c;
					while(x<Math.round(x1)){
						Renderer.setPixel(x, y, red, green, blue);
						if(d<0){
							d = d+b; 
						} else {
							d = d+a+b;
							x = x+1;
						}
						y = y+1;
					}					
					
				}
			} else {
				// Octant 7 or 8
			}


			


			
		} else {
			// Octant 3, 4, 5 or 6
		}
		
	}

	@Override
	public void runAlgorithm(int width, int height) {
			midpointLine(50.0, 50.0, 70.0, 100.0, 0, 0, 0);
			//midpointLine(0.0, 100.0, 100.0, 90.0, 0, 0, 0);

	}

	public static void main(String[] args){
		Renderer.init2D( 100, 100, new LineDraw() ) ;
	}
	
}
