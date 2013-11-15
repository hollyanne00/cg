package uk.ac.cam.hp343.cg.lines;
import easel.Algorithm2D;
import easel.Renderer;

public class MidpointLineDrawer implements Algorithm2D {
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

	@Override
	public void runAlgorithm(int width, int height) {
		
		// Find center assuming canvas size is odd
    	int cx = (width - 1)/2;
    	int cy = (height - 1)/2;
    	
    	// Remember the y here is flipped with respect to notes, and positive y points downwards, 
    	// hence octants are counted clockwise rather that counter-clockwise    	
    		
    	double stepX = cx/5.0;
    	double stepY = cy/5.0;
    	
    	for(double x = 0; x <= width/2; x += stepX)
    	{
    		
    		midpointLine( cx, cy, cx+x, 0, (255-(int)x), 0, 0); // Drawing in octant 7 
    		midpointLine( cx, cy, cx-x, 0, 0, (255-(int)x), (255-(int)x)); // Drawing in octant 6
    		
    		midpointLine( cx, cy, cx+x, height-1, 0, 0, (255-(int)x)); // Drawing in octant 2
    		midpointLine( cx, cy, cx-x, height-1, (255-(int)x), (255-(int)x), 0); // Drawing in octant 3
    	}
    	
    	for(double y = 0; y <= height/2; y += stepY)
    	{
    		midpointLine( cx, cy, 0, cy+y, 0, (255-(int)y), (255-(int)y)); // Drawing in octant 4 
    		midpointLine( cx, cy, 0, cy-y, (255-(int)y), (255-(int)y), 0); // Drawing in octant 5
    		
    		midpointLine( cx, cy, width-1, cy+y, (255-(int)y), 0, 0); // Drawing in octant 1
    		midpointLine( cx, cy, width-1, cy-y, 0, 0, (255-(int)y)); // Drawing in octant 8
    	}
	}

	public static void main(String[] args){
		Renderer.init2D( 41, 41, new MidpointLineDrawer() ) ;
	}
	
}
