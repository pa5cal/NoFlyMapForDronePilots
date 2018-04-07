NoFlyMapForDronePilots
==================

NoFlyMapForDronePilots is a command line Java application to create a web based browser map with no fly areas for drone pilots.
The data of the OpenStreetMap project is applied for the background map and also for displaying the restricted no fly areas.

![NoFlyMapForDronePilots](img/NoFlyMapForDronePilots.png?raw=true "NoFlyMapForDronePilots")

You will find some more information in the FOSSGIS 2018 talk @ [Slideshare](https://de.slideshare.net/pascalneis/eine-konfigurierbare-karte-mit-verbotszonen-fr-drohnenflieger-auf-basis-von-openstreetmap-daten-91729057)

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

LICENSE
-------

NoFlyMapForDronePilots is available under the GNU GPL version 3 or later.


AUTHORS
-------

NoFlyMapForDronePilots was written and is maintained by Pascal Neis <neis-one.org>.
