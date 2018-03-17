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

package org.neis_one.noflymapfordronepilots.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.neis_one.noflymapfordronepilots.properties.filter.OsmElementFilter;

/**
 * Read properties file and created {@link OsmElementFilter}.
 * 
 * @author pa5cal
 */
public class OsmElementFilterProperties {
	private final String path;
	private Properties prop;
	public Collection<OsmElementFilter> elements = new ArrayList<>();

	public OsmElementFilterProperties(String path) {
		this.path = path;
		loadProperties();
		createMapElements();
	}

	private void loadProperties() {
		try {
			prop = new Properties();
			InputStream is = getClass().getResourceAsStream(path);
			prop.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createMapElements() {
		for (Object keyObj : prop.keySet()) {
			String property = prop.getProperty(keyObj.toString());

			String[] splitParts = property.split(";");

			Map<String, String> combinations = new HashMap<>();
			String key = splitParts[0].split(":")[0];
			String v = splitParts[0].replace(key + ":", "");
			if (v.contains("&")) {
				for (String tag : v.split("&")[1].split(",")) {
					String[] split = tag.split(":");
					combinations.put(split[0], split[1]);
				}
				v = v.split("&")[0];
			}
			Set<String> values = new HashSet<String>(Arrays.asList(v.split(",")));

			int bufferInMeter = 0;
			if (splitParts.length > 1 && splitParts[1].startsWith("buffer:")) {
				bufferInMeter = Integer.parseInt(splitParts[1].replaceAll("buffer:", ""));
			}

			OsmElementFilter mapElement = new OsmElementFilter(key, values, combinations, bufferInMeter);
			elements.add(mapElement);
		}
	}

	public static void main(String a[]) {
		new OsmElementFilterProperties("/map.properties");
	}
}