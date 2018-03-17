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

package org.neis_one.noflymapfordronepilots.properties.filter;

import java.util.Map;
import java.util.Set;

/**
 * Object to filter OSM elements based on Tag/combinations (key/value).
 * 
 * @author pa5cal
 */
public class OsmElementFilter {
	public final String key;
	public final Set<String> values;
	public final Map<String, String> combinations;
	public final int bufferInMeter;

	public OsmElementFilter(String key, Set<String> values, Map<String, String> combinations, int bufferInMeter) {
		this.key = key;
		this.values = values;
		this.combinations = combinations;
		this.bufferInMeter = bufferInMeter;
	}

	@Override
	public String toString() {
		return getClass().getName() + " [key=" + key + ", values=" + values + ", combinations= " + combinations
				+ ", bufferInMeter=" + bufferInMeter + "]";
	}
}