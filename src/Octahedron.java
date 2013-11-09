import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import com.jogamp.common.nio.Buffers;

import javax.media.opengl.*;

import easel.Algorithm3D;
import easel.Renderer;

public class Octahedron implements Algorithm3D {


	static double oct_vertices[] = {
		0.0,0.0,1.0,
		0.0,1.0,0.0,
		1.0,0.0,0.0,
		0.0,0.0,-1.0,
		0.0,-1.0,0.0,
		-1.0,0.0,0.0
		} ;
	
	static byte oct_indices[] = {
		0, 2, 4,
		3, 4, 2,
		0, 1, 2,
		3, 2, 1,
		0, 5, 1, 
		3, 1, 5,
		0, 4, 5,
		3, 5, 4
	};
	
	static double oct_colors[] ={
		1.0, 0.0, 0.0, //red
		0.0, 0.2, 1.0, //blue
		0.9, 0.5, 0.0, //orange
		0.0, 0.8, 0.0, //green but not too bright
		0.9, 0.9, 0.0, // yellow
		0.4, 0.0, 0.7  // purple
	} ;
	
	double theta = 0.0 ;
	
	public void renderFrame(GL2 gl) {
		gl.glEnableClientState( gl.GL_VERTEX_ARRAY ) ;
		gl.glEnableClientState( gl.GL_COLOR_ARRAY ) ;
		gl.glEnableClientState( gl.GL_INDEX_ARRAY ) ;
		theta += 1.0 ;
		System.out.println( theta );
		gl.glScaled(3.0,3.0,3.0);
		gl.glRotated( theta, 1.0,1.0,1.0 );
		gl.glRotated( theta*0.1, 0.0,0.0,1.0 );
		gl.glColorPointer(3, gl.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(oct_colors)) ;
		gl.glVertexPointer(3, gl.GL_DOUBLE,0, Buffers.newDirectDoubleBuffer(oct_vertices));
		gl.glDrawElements(gl.GL_TRIANGLES,24, gl.GL_UNSIGNED_BYTE, Buffers.newDirectByteBuffer(oct_indices));
	}

	public static void main( String[] args ) {
		System.out.println( "Hello" );
		Renderer.init3D( new Octahedron() );
	}

}