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

package org.neis_one.noflymapfordronepilots.cache;

import java.util.HashMap;
import java.util.Map;

import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;

/**
 * Stores {@link Node}'s in a {@link Map} as some kind of chache.
 * 
 * @author pa5cal
 */
public class Store<E extends Entity> {

	private final Map<Long, E> store;

	public Store() {
		store = new HashMap<>();
	}

	public E get(long id) {
		return store.get(id);
	}

	public void putIdToSave(long id) {
		if (!store.containsKey(id)) {
			store.put(id, null);
		}
	}

	public boolean saveIfContainsId(E e) {
		boolean saved = false;
		if (store.containsKey(e.getId())) {
			store.put(e.getId(), e);
			saved = true;
		}
		return saved;
	}
}