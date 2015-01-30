import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.awt.*;
import java.io.File;

/**
 * Created by mar on 13.01.15.
 */
public class Main extends PApplet {
    PeasyCam cam;
    GooglePortraitReader gpr;
    public void setup() {
        size( 1920, 1080, JAVA2D );
        smooth();
        //cam = new PeasyCam( this, 1000 );
        gpr = new GooglePortraitReader( this, "data" + File.separator + "history-11-19-2014.kml" );

    }

    public void draw() {
        background( 0 );
        //translate( -mouseX, 0 );
        PShape shape = gpr.getShape( this );
        shape.translate( mouseX, mouseY );
        //println( shape.getWidth() + " " + shape.getHeight() );
        getBoundingBox( shape );
        shape.scale( 0.1f );
        //println( shape.getWidth() + " " + shape.getHeight() );
        getBoundingBox( shape );

        shape( shape );

        //noLoop();
    }

    private Rectangle getBoundingBox( PShape pShape ) {
        Rectangle r = new Rectangle( );

        float minX = 2000;
        float maxX = 0;
        float minY = 2000;
        float maxY = 0;

        for( int i = 0; i < pShape.getVertexCount(); i++ ) {
            PVector v = pShape.getVertex( i );
            maxX = max( maxX, v.x );
            minX = min( minX, v.x );
            maxY = max( maxY, v.y );
            minY = min( minY, v.y );
        }

        //println( minX + " " + minY + " " + maxX + " " + maxY );
        return r;
    }
    public static void main( String[] args ) {
        PApplet.main( new String[] { "Main" } );
    }
}
