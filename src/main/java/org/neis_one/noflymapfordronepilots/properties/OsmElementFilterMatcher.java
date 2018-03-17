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

import java.util.Collection;

import org.neis_one.noflymapfordronepilots.properties.filter.OsmElementFilter;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

/**
 * Matcher functions for {@link OsmElementFilter}'s.
 * 
 * @author pa5cal
 */
public class OsmElementFilterMatcher {

	public static OsmElementFilter match(Collection<Tag> tagList, Collection<OsmElementFilter> mapElements) {
		return mapElements.stream().filter(me -> match(tagList, me) != null).findFirst().orElse(null);
	}

	public static OsmElementFilter match(Collection<Tag> tagList, OsmElementFilter osmElementFilter) {
		OsmElementFilter result = null;
		boolean matchKeyValue = false;
		boolean matchCombination = false;
		for (Tag tag : tagList) {
			if (osmElementFilter.key.equals(tag.getKey().toLowerCase())
					&& osmElementFilter.values.contains(tag.getValue().toLowerCase())) {
				matchKeyValue = true;
			}
			if (osmElementFilter.combinations.isEmpty() || osmElementFilter.combinations.containsKey(tag.getKey())
					&& osmElementFilter.combinations.get(tag.getKey().toLowerCase()).equals(tag.getValue().toLowerCase())) {
				matchCombination = true;
			}

			if (matchKeyValue && matchCombination) {
				result = osmElementFilter;
				break;
			}
		}
		return result;
	}
}