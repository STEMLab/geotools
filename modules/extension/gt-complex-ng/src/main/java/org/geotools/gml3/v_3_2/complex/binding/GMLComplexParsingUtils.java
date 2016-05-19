/**
 * 
 */
package org.geotools.gml3.v_3_2.complex.binding;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.data.complex.config.ComplexFeatureTypeRegistry;
import org.geotools.feature.ComplexFeatureBuilder;
import org.geotools.feature.NameImpl;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;

/**
 * @author hgryoo
 *
 */
public class GMLComplexParsingUtils {
    /**
     * logging instance
     */
    static Logger LOGGER = Logging.getLogger( "org.geotools.gml" );
    
    public static Feature parseFeature(ElementInstance instance, Node node, Object value,
            FeatureTypeCache ftCache, ComplexFeatureTypeRegistry registry) throws Exception {

        //get the definition of the element
        XSDElementDeclaration decl = instance.getElementDeclaration();
        
        System.out.println(decl);
        
        FeatureType fType = null;
        if (!decl.isAbstract()) {
            XSDTypeDefinition def = decl.getTypeDefinition();
            Name name = new NameImpl(decl.getTargetNamespace(), decl.getName()); 
            Name typeName = new NameImpl(def.getTargetNamespace(), def.getName());
            
            fType = ftCache.get(typeName);
            if(fType == null) {
                AttributeDescriptor descriptor;
                descriptor = registry.getDescriptor(name, null, decl);
                fType = (FeatureType) descriptor.getType();
                ftCache.put(fType);
                //fType = featureType(decl, bwFactory, null, new FeatureTypeFactoryImpl());
            }
        } else {
            Name name = new NameImpl(node.getComponent().getNamespace(), node
                    .getComponent().getName());
            
            fType = ftCache.get(name);
            if(fType == null) {
                AttributeDescriptor descriptor = registry.getDescriptor(name, null);
                fType = (FeatureType) descriptor.getType();
                ftCache.put(fType);
            }
        }
        
      //fid
        String fid = (String) node.getAttributeValue("fid");

        if (fid == null) {
            //look for id
            fid = (String) node.getAttributeValue("id");
        }
        
        return GMLComplexParsingUtils.feature(fType, fid, node);
    }
    
    public static Feature feature(FeatureType type, String fid, Node node) {
        
        ComplexFeatureBuilder featureBuilder = new ComplexFeatureBuilder(type);
        
        Collection<PropertyDescriptor> descriptors = type.getDescriptors();
        for(Iterator<PropertyDescriptor> it = descriptors.iterator(); it.hasNext();) {
            PropertyDescriptor prop = it.next();
            PropertyType propType = prop.getType();
            
            Object propValue = node.getChildValue(prop.getName().getLocalPart());
            
            Class<?> binding = propType.getBinding();
            
            if ((propValue != null) && !propType.getBinding().isAssignableFrom(propValue.getClass())) {
                //type mismatch, to try convert
                Object converted = Converters.convert(propValue, propType.getBinding());

                if (converted != null) {
                    propValue = converted;
                }
            }
            
            
            
            
            
        }
        
        
        return null;
    }
    
}
