/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml3.v_3_2.complex;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;

/**
 * @author hgryoo
 *
 */
public class FeatureCache {

    ConcurrentMap<String, Feature> map = new ConcurrentHashMap<String, Feature>();

    public Feature get(String id) {
        return map.get(id);
    }

    public void put(String id, Feature feature) {
        Feature other = map.putIfAbsent(id, feature);
        if(other != null) {
            if(!other.equals(feature)) {
                String msg = "Feature with same id already exists in cache.";
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
