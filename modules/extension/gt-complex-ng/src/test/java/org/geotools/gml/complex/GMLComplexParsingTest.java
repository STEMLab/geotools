/**
 * 
 */
package org.geotools.gml.complex;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.geotools.data.DataUtilities;
import org.geotools.data.complex.config.EmfComplexFeatureReader;
import org.geotools.data.complex.config.ComplexFeatureTypeRegistry;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.gml3.complex.GmlFeatureTypeRegistryConfiguration;
import org.geotools.gml3.v_3_2.complex.ComplexGMLConfiguration;
import org.geotools.xml.PullParser;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureTypeFactory;

/**
 * @author hgryoo
 *
 */
public class GMLComplexParsingTest {

    @Test
    public void test() throws Exception {
        
        //Schema download and resolve
        File cacheDirectory = new File(DataUtilities.urlToFile(GMLComplexParsingTest.class
                .getResource("/")), "../");
        SchemaResolver resolver = new SchemaResolver( new SchemaCache(cacheDirectory, true));
        
        EmfComplexFeatureReader reader = EmfComplexFeatureReader.newInstance();
        reader.setResolver(resolver);
        SchemaIndex schemaIndex = reader.parse(new URL("http://schemas.opengis.net/indoorgml/1.0/indoorgmlcore.xsd"));
        
        FeatureTypeFactory factory = new FeatureTypeFactoryImpl();
        
        ComplexFeatureTypeRegistry registry = new ComplexFeatureTypeRegistry(factory,
                new GmlFeatureTypeRegistryConfiguration(null));
        registry.addSchemas(schemaIndex);

        InputStream in = getClass().getResourceAsStream("SMALL.gml");
        ComplexGMLConfiguration configuration = new ComplexGMLConfiguration();
        configuration.setFeatureTypeRegistry(registry);
        PullParser parser = new PullParser( configuration, in, SimpleFeature.class );
        
        int nfeatures = 0;
        Feature f = null;
        while( ( f = (Feature) parser.parse() ) != null ) {
            nfeatures++;
            System.out.println(nfeatures++);
        } 
        
        AttributeDescriptor descriptor = registry.getDescriptor(new NameImpl("http://www.opengis.net/indoorgml/1.0/core",":" ,"IndoorFeatures"), null);
        
        System.out.println();
    }

}
