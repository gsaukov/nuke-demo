package com.nukedemo.core;

import com.nukedemo.core.services.client.OverpassApiClient;
import hu.supercluster.overpasser.library.output.OutputModificator;
import hu.supercluster.overpasser.library.output.OutputOrder;
import hu.supercluster.overpasser.library.output.OutputVerbosity;
import hu.supercluster.overpasser.library.query.OverpassQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static hu.supercluster.overpasser.library.output.OutputFormat.JSON;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
class CoreApplicationTests {

    @Autowired
    OverpassApiClient client;

    @Test
    public void testClient() {
        String query = new OverpassQuery()
                .format(JSON)
                .timeout(30)
                .filterQuery()
                .node()
                .amenity("parking")
                .tagNot("access", "private")
                .area().rel()
                .boundingBox(
                        47.48047027491862, 19.039797484874725,
                        47.51331674014172, 19.07404761761427
                )
                .end()
                .output(OutputVerbosity.BODY, OutputModificator.CENTER, OutputOrder.QT, 100)
                .build();
         log.info(query);
        String res = client.getOverpassResponse(query);
        log.info(query);
    }


    String validQuery1 = "[out:json][timeout:25];(node[\"amenity\"=\"post_box\"](47.48047027491862,19.039797484874725,47.51331674014172,19.07404761761427););out body;>;out skel qt;";
    String validQuery2 = "[out:json][timeout:30]; (node[\"amenity\"=\"parking\"][\"access\"!=\"private\"](47.48047027491862,19.039797484874725,47.51331674014172,19.07404761761427);<;); out body center qt 100;";
    String validQuery3 = "[out:json][timeout:180]; area(id:3601428125)->.searchArea; (way[\"landuse\"=\"military\"](area.searchArea);<;); out body center qt 100;";

}
