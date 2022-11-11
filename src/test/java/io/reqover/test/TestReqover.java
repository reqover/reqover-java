package io.reqover.test;

import io.reqover.Reqover;
import io.reqover.core.model.build.BuildInfo;
import io.reqover.core.model.build.ReqoverBuild;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class TestReqover {

    @Test
    public void testCanCreateBuild() {
        Reqover reqover = new Reqover("http://localhost:3000", "ukm9x5zdkcfx");
        ReqoverBuild build = ReqoverBuild.of("Master-1",
                "https://petstore.swagger.io",
                "https://petstore.swagger.io/v2/swagger.json");
        BuildInfo buildInfo = reqover.createBuild(build);
    }

    @Test
    public void testCanCreateBuildFromRaw() {
        Reqover reqover = new Reqover("http://localhost:3000", "ukm9x5zdkcfx");
        ReqoverBuild build = ReqoverBuild.of("Master-1",
                "https://petstore.swagger.io",
                "https://petstore.swagger.io/v2/swagger.json");
        BuildInfo buildInfo = reqover.createBuild(build, true);
    }
}
