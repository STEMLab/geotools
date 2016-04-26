/**
 * 
 */
package org.geotools.gml.complex;

import java.io.File;
import java.net.URL;

import org.geotools.data.DataUtilities;
import org.geotools.data.complex.config.EmfComplexFeatureReader;
import org.geotools.data.complex.config.FeatureTypeRegistry;
import org.geotools.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.gml3.complex.GmlFeatureTypeRegistryConfiguration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.junit.Test;

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
        
        FeatureTypeRegistry registry = new FeatureTypeRegistry(new ComplexFeatureTypeFactoryImpl(),
                new GmlFeatureTypeRegistryConfiguration(null));
        registry.addSchemas(schemaIndex);
        
        System.out.println();
    }

}
