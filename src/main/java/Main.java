import peasy.PeasyCam;
import processing.core.PApplet;

import java.io.File;

/**
 * Created by mar on 13.01.15.
 */
public class Main extends PApplet {
    PeasyCam cam;
    GooglePortraitReader gpr;
    public void setup() {
        size( 1920, 1080, P2D );
        //cam = new PeasyCam( this, 1000 );
        gpr = new GooglePortraitReader( this, "data" + File.separator + "history-11-19-2014.kml" );

    }

    public void draw() {
        background( 0 );
        //translate( -mouseX, 0 );
        gpr.draw();
    }
    public static void main( String[] args ) {
        PApplet.main( new String[] { "Main" } );
    }
}
