package com.nukedemo;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.PropertyAuthorityFactory;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.util.factory.Hints;

import java.io.IOException;
import java.net.URL;

public class TiffPopulationSetup {
    static {
        URL epsg = TiffPopulationMollweideDataContainer.class.getClassLoader().getResource("epsg.properties");
        Hints hints = new Hints(Hints.CRS_AUTHORITY_FACTORY, PropertyAuthorityFactory.class);
        ReferencingFactoryContainer referencingFactoryContainer = ReferencingFactoryContainer.instance(hints);
        PropertyAuthorityFactory factory;
        try {
            factory = new PropertyAuthorityFactory(referencingFactoryContainer, Citations.fromName("EPSG"), epsg);
        } catch (IOException e) {
            throw new RuntimeException("Reading epsg.properties failed", e);
        }
        ReferencingFactoryFinder.addAuthorityFactory(factory);
        ReferencingFactoryFinder.scanForPlugins();
    }
}
