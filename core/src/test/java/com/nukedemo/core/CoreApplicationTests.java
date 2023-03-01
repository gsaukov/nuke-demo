package com.nukedemo.core;

import com.nukedemo.core.services.client.OverpassApiClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
//@EnableFeignClients
class CoreApplicationTests {

    @Autowired
    OverpassApiClient client;

    @Test
    public void testClient() {
        client.interpret("data=%2F%2F+Limit+the+search+to+%E2%80%9CMexico%E2%80%9D%0Aarea(id%3A3601428125)-%3E.searchArea%3B%0A%2F%2F+Pull+together+the+results+that+we+want%0A(%0A+%2F%2F+Ask+for+the+objects+we+want%2C+and+the+tags+we+want%0A+way%5B%22landuse%22%3D%22military%22%5D(area.searchArea)%3B%0A+relation%5B%22landuse%22%3D%22military%22%5D(area.searchArea)%3B%0A+node%5B%22landuse%22%3D%22military%22%5D(area.searchArea)%3B%0A)%3B%0A%2F%2F+Print+out+the+results%0Aout+body%3B%0A%3E%3B%0Aout+skel+qt%3B");
    }
}
