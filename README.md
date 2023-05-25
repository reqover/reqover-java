### Reqover java

[![](https://jitpack.io/v/reqover/reqover-java.svg)](https://jitpack.io/#reqover/reqover-java)

Add dependency:

```
<repositories>
    	<repository>
	    	<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    	<groupId>com.github.reqover</groupId>
	<artifactId>reqover-java</artifactId>
	<version>v0.2.8</version>
</dependency>
```

Add RestAssured filter:

```
import io.reqover.rest.assured.SwaggerCoverage;

RestAssured.given()
        .filter(new SwaggerCoverage());
```

Generate html report:

```
docker run -v $PWD:/reqover \
-v $PWD/.reqover:/tmp/data \
-v $PWD/swagger.json:/tmp/swagger.json \
reqover/reqover-cli generate -f /tmp/swagger.json -d /tmp/data/reqover-results -p /v2 --html
```
