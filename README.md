NoFlyMapForDronePilots
==================

NoFlyMapForDronePilots is a command line Java application to create a web based browser map with no fly areas for drone pilots.
The data of the OpenStreetMap project is applied for the background map and also for displaying the restricted no fly areas.

![NoFlyMapForDronePilots](img/NoFlyMapForDronePilots.png?raw=true "NoFlyMapForDronePilots")

You will find some more information in the FOSSGIS 2018 talk @ [Slideshare (German)](https://de.slideshare.net/pascalneis/eine-konfigurierbare-karte-mit-verbotszonen-fr-drohnenflieger-auf-basis-von-openstreetmap-daten-91729057)

PREREQUISITES
-------------

NoFlyMapForDronePilots needs different dependencies:
```xml
    <dependency>
        <groupId>org.openstreetmap.osmosis</groupId>
        <artifactId>osmosis-pbf</artifactId>
        <version>0.46</version>
    </dependency>
    <dependency>
        <groupId>org.openstreetmap.osmosis</groupId>
        <artifactId>osmosis-xml</artifactId>
        <version>0.46</version>
    </dependency>
    <dependency>
        <groupId>com.vividsolutions</groupId>
        <artifactId>jts</artifactId>
        <version>1.13</version>
    </dependency>
```

INSTALLATION NOTES
-------

To run NoFlyMapForDronePilots, you need:
* The NoFlyMapForDronePilots .jar file, e.g., NoFlyMapForDronePilots-0.0.1-SNAPSHOT.jar (should be located in the target directoy after maven build/install)
* Java Runtime Environment (JRE) 8, or later.

HOW TO RUN
-------
```xml
java -Xmx1024m -cp NoFlyMapForDronePilots-0.0.1-SNAPSHOT.jar org.neis_one.noflymapfordronepilots.Main ./map.properties ./your-osm-file.osm ./map.js
```
(map.js is the output file which contains the no fly areas as JS coordinate arrays)

LICENSE
-------

NoFlyMapForDronePilots is available under the GNU GPL version 3 or later.

AUTHORS
-------

NoFlyMapForDronePilots was written and maintained by Pascal Neis <neis-one.org>.
