/*

Copyright 2018 Pascal Neis <neis-one.org>.

This file is part of NoFlyMapForDronePilots (https://github.com/pa5cal/NoFlyMapForDronePilots)

NoFlyMapForDronePilots is free software: you can redistribute it and/or modify it under 
the terms of the GNU General Public License as published by the Free Software Foundation, 
either version 3 of the License, or (at your option) any later version.

NoFlyMapForDronePilots is distributed in the hope that it will be useful, but WITHOUT 
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.

 */

package org.neis_one.noflymapfordronepilots;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.neis_one.noflymapfordronepilots.properties.OsmElementFilterProperties;
import org.neis_one.noflymapfordronepilots.properties.filter.OsmElementFilter;
import org.neis_one.noflymapfordronepilots.tasks.ReadOsmFilterElements;
import org.neis_one.noflymapfordronepilots.tasks.WriteDroneMapElements;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlReader;

import crosby.binary.osmosis.OsmosisReader;

/**
 * Main Class for creating a NoFlyMapForDronePilots.
 * 
 * @author pa5cal
 */
public class Main {
	private final ReadOsmFilterElements readOsmFilterElements;

	public Main(String propertiesFilePath) {
		Collection<OsmElementFilter> osmElementFilters = new OsmElementFilterProperties(propertiesFilePath).elements;
		readOsmFilterElements = new ReadOsmFilterElements(osmElementFilters);
	}

	public void create(String osmFilePath, String resultFilePath) {
		processOsm(osmFilePath, readOsmFilterElements);
		processOsm(osmFilePath, readOsmFilterElements);
		processOsm(osmFilePath, readOsmFilterElements);
		WriteDroneMapElements droneMapWriter = new WriteDroneMapElements(resultFilePath);
		droneMapWriter.writeResults(readOsmFilterElements.getDroneMapElementsResult());
	}

	private void processOsm(final String osmFilePath, final ReadOsmFilterElements osmMapElementProcessor) {
		try {
			if(osmFilePath.endsWith(".pbf")){
				OsmosisReader reader = new OsmosisReader(new FileInputStream(osmFilePath));
				reader.setSink(osmMapElementProcessor);
				reader.run();
			}else{
				XmlReader reader = new XmlReader(new File(osmFilePath), false, CompressionMethod.None);
				reader.setSink(osmMapElementProcessor);
				reader.run();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Start...");
		Main droneMap = new Main(args[0]);
		droneMap.create(args[1], args[2]);
		System.out.println("done.");
	}
}