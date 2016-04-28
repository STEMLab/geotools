/**
 * 
 */
package org.geotools.gml3.v_3_2.complex.binding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.data.complex.config.ComplexFeatureTypeRegistry;
import org.geotools.feature.NameImpl;
import org.geotools.feature.TypeBuilder;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml3.bindings.ComplexSupportXSAnyTypeBinding;
import org.geotools.util.logging.Logging;
import org.geotools.xml.Binding;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.Schemas;
import org.geotools.xml.impl.BindingWalker;
import org.geotools.xs.bindings.XSAnyTypeBinding;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

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
        return null;
    }

    public static FeatureType featureType(XSDElementDeclaration element, BindingWalkerFactory bwFactory, CoordinateReferenceSystem crs, FeatureTypeFactory typeFactory) throws Exception {
        
        TypeBuilder builder = new TypeBuilder(typeFactory);
        
        // build the feature type by walking through the elements of the
        // actual xml schema type
        List children = Schemas.getChildElementParticles(element.getType(), true);

        for (Iterator itr = children.iterator(); itr.hasNext();) {
            
            TypeBuilder propBuilder = new TypeBuilder(typeFactory);
            
            XSDParticle particle = (XSDParticle) itr.next();
            XSDElementDeclaration property = (XSDElementDeclaration) particle.getContent();

            if (property.isElementDeclarationReference()) {
                property = property.getResolvedElementDeclaration();
            }
            
            final ArrayList bindings = new ArrayList();
            BindingWalker.Visitor visitor = new BindingWalker.Visitor() {
                public void visit(Binding binding) {
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
            
            System.out.println(property.getName() + "\n" + bindings);
            System.out.println();
            
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
            
            final String propName = property.getName();
            final String propNamespace = property.getTargetNamespace();
            
            propBuilder.cardinality(min, max);
            propBuilder.setName(propName);
            propBuilder.setNamespaceURI(propNamespace);
            propBuilder.setBinding(theClass);
            
            if(last instanceof ComplexSupportXSAnyTypeBinding) {
                ComplexSupportXSAnyTypeBinding complexBinding = (ComplexSupportXSAnyTypeBinding) last;
                
                System.out.println(complexBinding);
            } else {
                
                AttributeType type = null;
                
                if (Geometry.class.isAssignableFrom(theClass)) {
                    
                    // if the next property is of type geometry, let's set its CRS
                    if(crs != null) {
                        builder.crs(crs);
                    }
                    type = propBuilder.geometry();
                } else {
                    type = propBuilder.attribute();
                }
                builder.addAttribute(propName, type);
                
                /*
                if (Geometry.class.isAssignableFrom(theClass)
                        && (propNamespace == null || !propNamespace.startsWith(GML.NAMESPACE))) {
                    //only set if non-gml, we do this because of "gml:location", 
                    // we dont want that to be the default if the user has another
                    // geometry attribute
                    if (builder.getDefaultGeometry() == null) {
                        builder.setDefaultGeometry(propName);
                    }
                }
                */
            }
        }
        
        builder.setName(element.getName());
        builder.setNamespaceURI(element.getTargetNamespace());
        return builder.feature();
    }
    
}
