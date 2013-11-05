package uk.ac.cam.hp343.cg.basic;

import java.util.Random;

import easel.Algorithm2D;
import easel.Renderer;

public class Picture implements Algorithm2D {

	@Override
	public void runAlgorithm(int width, int height) {
		
		int pixels = width*height;
		Random r = new Random();
		
		boolean rr = false;
		boolean gr = false;
		boolean br = true;
		
		int count = 0;
		int red = 0;
		int blue = 0;
		int green = 0;
		while(count<pixels){
			int p = r.nextInt(pixels);
			int x = p % width;
			int y = p/height;
			if(red<255){ red ++; }
			else if(green<255) { green++; }
			else if(blue<255) { blue++; }
			else { 
				if(rr) { red = 0; rr = false; gr = true; }
				else if(gr) { green = 0; gr = false; br = true; }
				else if(br) { blue = 0; br = false; rr = true; }
			}
			Renderer.setPixel(x, y, red, green, blue);
			count++;
		}
		
	}
	
	public static void main(String[] args){
		Renderer.init2D(100, 100, new Picture());
	}
}
