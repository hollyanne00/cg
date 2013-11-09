import easel.Algorithm2D;
import easel.Renderer;

import java.util.Random ;

public class LineDrawer implements Algorithm2D{

	public static int Round( double a )
	{
		return (int) Math.round(a) ; // Math.round returns (long)
	}

	public static void BresenhamsLine( double x0, double y0, double x1, double y1, int red, int green, int blue){

		// make sure that x0 < x1 so in octant 1, 2, 7, 8
		if( x0 > x1 ) {
			double xt = x0 ; x0 = x1 ; x1 = xt ;
			double yt = y0 ; y0 = y1 ; y1 = yt ;
		}
	
		if( y1 > y0 ) { // octant 1 or 2
			if( (y1-y0) <= (x1-x0) ) { // octant 1
				// setup line variables
				double m = (y1 - y0) / (x1 - x0) ;
				int x = Round(x0) ;
				double yi = y0  + m * (x-x0) ;
				int	y = Round(yi) ;
				double yf = yi - y ;

				// draw pixels
				while ( x <= Round(x1) ) {
					Renderer.setPixel(x,y,red, green, blue);
					x = x + 1 ;
					yf = yf + m ;
					if ( yf > 0.5 ) {
						y = y + 1 ;
						yf = yf - 1 ;
					}
				}
			}
			else { // octant 2
				
				System.out.printf( "Octant 2 not yet implemented\n") ;				
			}
		}
		else { // octant 7 or 8
			if( (y0-y1) <= (x1-x0) ) { // octant 8
				System.out.printf( "Octant 8 not yet implemented\n") ;
			}
			else { // octant 7
				System.out.printf( "Octant 7 not yet implemented\n") ;
			}
		}
	}

	public void runAlgorithm( int width, int height ) {

		/* test lines in octant 1 only 
		for( int i = 0 ; i < height ; i += 2 ){
			BresenhamsLine(0,0,width-1,i,i*255/height,0,0);
		}
		*/
		
		for( int i = 0 ; i < 64 ; i++ ) {
			double x0 = new Random().nextDouble() * (width-1);
			double y0 = new Random().nextDouble() * (height-1);
			double x1 = new Random().nextDouble() * (width-1);
			double y1 = new Random().nextDouble() * (height-1);
			int red = i > 32  ? (i-32) * 7 : 0 ;
			int gre = i < 32  ? (32-i) * 7 : 0 ;
			int blu = i < 32 ? i * 7 : ( 64 - i ) * 7 ;
			BresenhamsLine( x0, y0, x1, y1, red, gre, blu);
		}
		
	}
	
	public static void main( String[] args ) {
		System.out.println( "Hello" );
		Renderer.init2D( 40, 30, new LineDrawer() ) ;
	}
}