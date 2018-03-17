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

package org.neis_one.noflymapfordronepilots.entity;

import java.util.Collection;

import org.neis_one.noflymapfordronepilots.properties.filter.OsmElementFilter;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;

/**
 * DroneMapElement.
 * 
 * @author pa5cal
 */
public class DroneMapElement {
	public final OsmElementFilter osmElementFilter;
	public final Entity osmElement;
	public final Collection<Node> osmNodes;

	public DroneMapElement(OsmElementFilter osmElementFilter, Entity osmElement, Collection<Node> osmNodes) {
		this.osmElementFilter = osmElementFilter;
		this.osmElement = osmElement;
		this.osmNodes = osmNodes;
	}
}