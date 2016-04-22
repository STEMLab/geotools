/**
 * 
 */
package org.geotools.gml3.complex;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.geotools.gml3.v_3_2.complex.ComplexGMLConfiguration;
import org.geotools.xml.PullParser;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.SAXException;

/**
 * @author Hyung-Gyu Ryoo (Pusan National University)
 *
 */
public class GMLComplexParsingTest {

    @Test
    public void test() throws XMLStreamException, IOException, SAXException {
        InputStream in = getClass().getResourceAsStream("SMALL.gml");
        ComplexGMLConfiguration gml = new ComplexGMLConfiguration();
        PullParser parser = new PullParser( gml, in, SimpleFeature.class );

        int nfeatures = 0;
        Feature f = null;
        while( ( f = (Feature) parser.parse() ) != null ) {
            nfeatures++;
            System.out.println(nfeatures++);
        }
    }

}
