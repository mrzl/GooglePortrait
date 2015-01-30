import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;
import processing.core.PShape;
import processing.data.XML;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mar on 13.01.15.
 */
public class GooglePortraitReader {

    private XML xmlData;
    UnfoldingMap map;
    private ArrayList< Date > dates;
    private ArrayList< double[] > positions;
    private PApplet p;
    public GooglePortraitReader( PApplet p, String fileName ) {
        this.p = p;
        map = new UnfoldingMap(p, new Microsoft.AerialProvider());

        //MapUtils.createDefaultEventDispatcher( p, map );
        dates = new ArrayList< Date >(  );
        positions = new ArrayList< double[] >(  );
        loadXml( p, fileName );
        //getShape( p, dates );
        map.zoomAndPanTo( new de.fhpotsdam.unfolding.geo.Location( 52.473236, 13.4269905 ), 20  );
    }

    public PShape getShape( PApplet p ) {
        //map.draw();
        //positions.removeAll( Collections.singleton( null ) );
        //dates.removeAll( Collections.singleton( null ) );
        int start = ( int ) PApplet.map( p.mouseX, 0, p.width, 0, dates.size() );
        ArrayList< Marker > loca = new ArrayList< Marker >(  );
        PShape shp = p.createShape();
        shp.beginShape();
        for( int i = start; i < dates.size(); i++ ) {
            double lon = positions.get( i )[ 0 ];
            double lat = positions.get( i )[ 1 ];
            Location location = new de.fhpotsdam.unfolding.geo.Location( lat, lon );

            //location.setLongitude( lon );
            //location.setLatitude( lat );
            SimplePointMarker marker = new SimplePointMarker(location);
            loca.add( marker );
            //marker.setLocation( location );
            ScreenPosition berlinPos = marker.getScreenPosition(map);
            p.strokeWeight( 2 );
            p.stroke( 255, 255 );
            p.noFill();
            //System.out.println( marker.getLocation().x + " " + marker.getLocation().y );
            shp.vertex( berlinPos.x, berlinPos.y );
            //p.point( berlinPos.x, berlinPos.y );
            //System.out.println( berlinPos.toString() );
        }
        shp.endShape();
        return shp;
    }

    private void loadXml( PApplet p, String fileName ) {
        xmlData = p.loadXML( fileName );
        final int DATA_INDEX = 1;
        XML children = xmlData.getChild( DATA_INDEX );
        final int ALMOST_DATA_INDEX = 13;
        XML longLatChildren = children.getChild( ALMOST_DATA_INDEX );
        final int TRACK_DATA_INDEX = 7;
        XML trackChildren = longLatChildren.getChild( TRACK_DATA_INDEX );
        int index = 0;
        int alternatingIndex = 3;
        for( XML x : trackChildren.getChildren() ) {
            Date date = null;
            double[] parseLonLat = null;
            if( index >= 3 && index % 2 != 0) {
                if( alternatingIndex % 2 == 1) {
                    String whenString = x.getChild( 0 ).getContent();
                    date = parseDate( whenString );
                    dates.add( date );
                } else {
                    String whereString = x.getChild( 0 ).getContent();
                    parseLonLat = parseLonLat( whereString );
                    positions.add( parseLonLat );
                }
                alternatingIndex++;
            }
            //System.out.println( "Adding: " + date + " " + parseLonLat );
            index++;
        }


    }

    private double[] parseLonLat( String whereString ) {
        double[] returnLonLat = new double[ 2 ];

        whereString = whereString.substring( 0, whereString.length() - 2 );
        String[] lonLatString = whereString.split( " " );

        returnLonLat[ 0 ] = Double.parseDouble( lonLatString[ 0 ] );
        returnLonLat[ 1 ] = Double.parseDouble( lonLatString[ 1 ] );

        return returnLonLat;
    }

    Date parseDate( String _date ) {
        _date = _date.replace( "T", "--" );
        _date = _date.substring( 0, 20 );
        DateFormat df2 = new SimpleDateFormat( "yyyy-MM-dd--kk:mm:ss" );
        Date result = null;
        try {
            result = df2.parse(_date);
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        return result;
    }
}
