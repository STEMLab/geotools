/**
 * 
 */
package org.geotools.gml3.v_3_2.complex.binding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.geotools.feature.NameImpl;
import org.geotools.feature.TypeBuilder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.util.logging.Logging;
import org.geotools.xml.Binding;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.Schemas;
import org.geotools.xml.impl.BindingWalker;
import org.geotools.xs.bindings.XSAnyTypeBinding;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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
            FeatureTypeCache ftCache, BindingWalkerFactory bwFactory) throws Exception {

        //get the definition of the element
        XSDElementDeclaration decl = instance.getElementDeclaration();
        
        
        FeatureType fType = null;
        if (!decl.isAbstract()) {
            
            fType = ftCache.get(new NameImpl(decl.getTargetNamespace(), decl.getName()));
            if(fType == null) {
                
                
                fType = featureType(decl, bwFactory, null, new FeatureTypeFactoryImpl());
                
            }
            
        } else {
            
        }
        
        
        
        
        return null;
    }
    
    public static FeatureType featureType(XSDElementDeclaration element, BindingWalkerFactory bwFactory, CoordinateReferenceSystem crs, FeatureTypeFactory typeFactory) throws Exception {
        
        TypeBuilder builder = new TypeBuilder(typeFactory);
        
        // build the feature type by walking through the elements of the
        // actual xml schema type
        List children = Schemas.getChildElementParticles(element.getType(), true);

        for (Iterator itr = children.iterator(); itr.hasNext();) {
            
            TypeBuilder propertyBuilder = new TypeBuilder(typeFactory);
            
            XSDParticle particle = (XSDParticle) itr.next();
            XSDElementDeclaration property = (XSDElementDeclaration) particle.getContent();

            if (property.isElementDeclarationReference()) {
                property = property.getResolvedElementDeclaration();
            }

            final ArrayList bindings = new ArrayList();
            BindingWalker.Visitor visitor = new BindingWalker.Visitor() {
                public void visit(Binding binding) {
                    System.out.println(binding);
                    bindings.add(binding);
                }
            };
            bwFactory.walk(property, visitor);

            if (bindings.isEmpty()) {
                // could not find a binding, use the defaults
                LOGGER.fine( "Could not find binding for " + property.getQName() + ", using XSAnyTypeBinding." );
                bindings.add( new XSAnyTypeBinding() );
            }

            // get the last binding in the chain to execute
            Binding last = ((Binding) bindings.get(bindings.size() - 1));
            Class theClass = last.getType();

            if (theClass == null) {
                throw new RuntimeException("binding declares null type: " + last.getTarget());
            }

            // get the attribute properties
            int min = particle.getMinOccurs();
            int max = particle.getMaxOccurs();

            //check for uninitialized values
            if (min == -1) {
                min = 0;
            }

            if (max == -1) {
                max = 1;
            }
            propertyBuilder.cardinality(min, max);
            
            final String propName = property.getName();
            final String propNamespace = property.getTargetNamespace();
            
            
            
        }
        
        builder.setName(element.getName());
        builder.setNamespaceURI(element.getTargetNamespace());
        return builder.feature();
    }
    
}
