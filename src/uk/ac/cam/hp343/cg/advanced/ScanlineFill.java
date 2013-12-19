package uk.ac.cam.hp343.cg.advanced;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import easel.Algorithm2D;
import easel.Renderer;

public class ScanlineFill implements Algorithm2D{

	public class Edge {
		private double x0;
		private double y0;
		private double x1;
		private double y1;
		private double is;
		public Edge(double x0, double y0, double x1, double y1){
			// y0 is always the higher y value
			if(y0>=y1){ this.y0 = y0; this.x0 = x0; this.y1 = y1; this.x1 = x1; }
			else { this.y0 = y1; this.x0 = x1; this.y1 = y0; this.x1 = x0; }
			this.is = -1;
		}
		
	}
	
	public void drawLine(double x0, double y0, double x1, double y1, int red, int green, int blue){
		
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
	
	public double getSlope(Edge e){
		double slope = (e.y0 - e.y1)/(e.x1 - e.x0);
		return slope;
	}
	
	public double getIntersection(double scanline, Edge e){
		double m = getSlope(e);
		return ((e.y0-scanline)/m) + e.x0;
	}
	
	public void naiveScanline(List<Edge> edgeList){
		
		// Sort edges by lowest y value (or biggest in this case)
		Collections.sort(edgeList, new Comparator<Edge>(){

			@Override
			public int compare(Edge e1, Edge e2) {
				if(e1.y0>e2.y0){ return -1; }
				if(e2.y0>e1.y0){ return 1; }
				return 0;
			}
			
		});
		
		// Define a list of active edges
		List<Edge> activeEdges = new ArrayList<Edge>();
		
		// Start with first scanline that intersects (lowest y)
		double scanline = Math.round(edgeList.get(0).y0);
		while(scanline>=0){
			
			//System.out.println("Scanline: "+scanline);
			
			// Remove old edges from active edge list
			Iterator<Edge> aelit = activeEdges.iterator();
			while(aelit.hasNext()){
				Edge e = aelit.next();
				if((e.y0<scanline||e.y1>=scanline)){
					edgeList.add(e);
					aelit.remove();					
				}
			}
			
			// Add intersecting edges to AEL
			Iterator<Edge> elit = edgeList.iterator();
			while(elit.hasNext()){
				Edge e = elit.next();
				if(e.y0>=scanline&&e.y1<scanline){
					activeEdges.add(e);
					elit.remove();
				}
			}
			
			// Find intersection points
			for(Edge e : activeEdges){
				e.is = getIntersection(scanline, e);
			}
			
			// Sort on x intersection value
			Collections.sort(activeEdges, new Comparator<Edge>(){

				@Override
				public int compare(Edge e1, Edge e2) {
					if(e1.is<e2.is){ return -1; }
					if(e2.is<e1.is){ return 1; }
					return 0;
				}
				
			});
			
			// Active edges and intersection points
/*			System.out.println("Active edges: ");
			for(Edge e : activeEdges){
				System.out.println("Edge: "+ e.x0 + ", " + e.y0 + ", " + e.x1 + ", " + e.y1 + "(intersection: " + e.is+")");
			}*/
			
			// Fill in pixels whilst between edges, assuming only even edges for now
			int numedges = activeEdges.size();
			int i = 0;
			int j = (numedges % 2 == 0) ? numedges : numedges-1;
			//System.out.println("i="+i+"j="+j);
			int y = (int)Math.round(scanline);
			while(i<j){
				int p = (int)Math.round(activeEdges.get(i).is);
				int q = (int)Math.round(activeEdges.get(i+1).is);
				while(p<=q){
					Renderer.setPixel(p, y, 255, 0, 0);
					p++;
				}
				i+=2;
			}
			
			scanline--;
			
		}
		
	}
	
	@Override
	public void runAlgorithm(int width, int height) {
		
		// Create the edges of a shape
		List<Edge> edgeList = new ArrayList<Edge>();
		edgeList.add(new Edge((double)width/3,(double)height/10,width-1,0));
		edgeList.add(new Edge((double)width/3,(double)height/10,0,((double)height/3)*2));
		edgeList.add(new Edge(0,((double)height/3)*2,(double)width/2,(double)height/2));
		edgeList.add(new Edge((double)width/2,(double)height/2,(double)width/3,height-1));
		edgeList.add(new Edge((double)width/3,height-1,(double)width-1,(double)height/3));
		edgeList.add(new Edge(width-1,(double)height/3,((double)width/3)*2,(double)height/3));
		edgeList.add(new Edge(((double)width/3)*2,(double)height/3,width-1,0));
		
		// Draw edges
		for(Edge e : edgeList){
			drawLine(e.x0, e.y0, e.x1, e.y1, 255, 0, 0);
		}
		
		naiveScanline(edgeList);
		
	}
	
	public static void main(String[] args){
		Renderer.init2D(40, 40, new ScanlineFill());
	}

}
