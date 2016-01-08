/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.feature.complex;

import java.io.IOException;

import org.geotools.geometry.GeometryBuilder;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
/**
 * Interface to return appropriate feature parser.
 * 
 * @author HyungGyu Ryoo (Pusan National University)
 */
public interface NewGetParser<F extends Feature> {

    /**
     * @return the next feature in the stream or {@code null} if there are no more features to parse.
     */
    F parse() throws IOException;

    /**
     * Close the parser.
     * @throws IOException
     * 		Throws IOException if there was a problem closing the parser.
     */
    void close() throws IOException;

    /**
     * Get the feature type that the parser is targeting.
     * @return
     * 		The feature type that the parser is targeting.
     */
    public FeatureType getFeatureType();

    /**
     * Set the geometry builder.
     * @param geometryBuilder
     * 		The geometry builder to use.
     */
    public void setGeometryBuilder(GeometryBuilder geometryBuilder);
}
