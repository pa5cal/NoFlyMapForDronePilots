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

package org.neis_one.noflymapfordronepilots.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.neis_one.noflymapfordronepilots.cache.Store;
import org.neis_one.noflymapfordronepilots.entity.DroneMapElement;
import org.neis_one.noflymapfordronepilots.properties.OsmElementFilterMatcher;
import org.neis_one.noflymapfordronepilots.properties.filter.OsmElementFilter;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.EntityType;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

/**
 * Receives data from the Osmosis pipeline and collects data based on
 * {@link OsmElementFilter}'s.
 * 
 * @author pa5cal
 */
public class ReadOsmFilterElements implements Sink {

	private final Collection<OsmElementFilter> osmElementFilters;
	private final Store<Node> nodeStore;
	private final Store<Way> wayStore;
	private final Collection<DroneMapElement> droneMapElementsResult;

	public ReadOsmFilterElements(Collection<OsmElementFilter> osmElementFilters) {
		this.osmElementFilters = osmElementFilters;
		nodeStore = new Store<Node>();
		wayStore = new Store<Way>();
		droneMapElementsResult = new ArrayList<>();
	}

	@Override
	public void initialize(Map<String, Object> arg0) {
	}

	@Override
	public void process(EntityContainer entityContainer) {
		if (entityContainer instanceof NodeContainer) {
			processNode(entityContainer);
		} else if (entityContainer instanceof WayContainer) {
			processWay(entityContainer);
		} else if (entityContainer instanceof RelationContainer) {
			processRelation(entityContainer);
		}
	}

	@Override
	public void complete() {
	}

	@Override
	public void close() {
	}

	public Collection<DroneMapElement> getDroneMapElementsResult() {
		return droneMapElementsResult;
	}

	private void processNode(EntityContainer entityContainer) {
		nodeStore.saveIfContainsId(((NodeContainer) entityContainer).getEntity());
	}

	private void processWay(EntityContainer entityContainer) {
		Way way = ((WayContainer) entityContainer).getEntity();
		OsmElementFilter osmElementFilter = OsmElementFilterMatcher.match(way.getTags(), osmElementFilters);
		if (osmElementFilter != null) {
			way.getWayNodes().forEach(wn -> nodeStore.putIdToSave(wn.getNodeId()));
			List<Node> nodes = way.getWayNodes().stream().map(wn -> nodeStore.get(wn.getNodeId()))
					.filter(n -> n != null).collect(Collectors.toList());
			if (!nodes.isEmpty()) {
				droneMapElementsResult.add(new DroneMapElement(osmElementFilter, way, nodes));
			}
		}
		if (wayStore.saveIfContainsId(way)) {
			way.getWayNodes().forEach(wn -> nodeStore.putIdToSave(wn.getNodeId()));
		}
	}

	private void processRelation(EntityContainer entityContainer) {
		Relation relation = ((RelationContainer) entityContainer).getEntity();
		OsmElementFilter osmElementFilter = OsmElementFilterMatcher.match(relation.getTags(), osmElementFilters);
		if (osmElementFilter != null) {
			relation.getMembers().stream()
					.filter(m -> EntityType.Way.compareTo(m.getMemberType()) == 0
							&& "outer".equalsIgnoreCase(m.getMemberRole()))
					.forEach(w -> wayStore.putIdToSave(w.getMemberId()));
			List<Way> ways = relation.getMembers().stream()
					.filter(m -> EntityType.Way.compareTo(m.getMemberType()) == 0
							&& "outer".equalsIgnoreCase(m.getMemberRole()))
					.map(w -> wayStore.get(w.getMemberId())).filter(w -> w != null).collect(Collectors.toList());
			if (!ways.isEmpty()) {
				List<List<Node>> nodeLists = new ArrayList<>();
				for(Way w : ways){
					List<Node> nodeList = w.getWayNodes().stream().map(wn -> nodeStore.get(wn.getNodeId()))
							.filter(n -> n != null).collect(Collectors.toList());
					if (!nodeList.isEmpty()) {
						nodeLists.add(nodeList);
					}
				}
				if (!nodeLists.isEmpty()) {
					List<Node> nodes = new ArrayList<Node>();
					nodes.addAll(nodeLists.get(0));
					nodeLists.remove(0);
					for (int idx = 0; idx < nodeLists.size(); idx++) {
						List<Node> n = nodeLists.get(idx);
						if (merge(nodes, n) || reverseMerge(nodes, n)) {
							nodeLists.remove(idx);
							idx = -1;
						}
					}
					if (!nodes.isEmpty()) {
						droneMapElementsResult.add(new DroneMapElement(osmElementFilter, relation, nodes));
					}
				}
			}
		}
	}

	private boolean merge(List<Node> nodes, List<Node> n) {
		boolean merged = false;
		if (nodes.get(0).equals(n.get(0))) {
			nodes.remove(0);
			Collections.reverse(nodes);
			nodes.addAll(n);
			merged = true;
		} else if (nodes.get(nodes.size() - 1).equals(n.get(0))) {
			n.remove(0);
			nodes.addAll(n);
			merged = true;
		}
		return merged;
	}

	private boolean reverseMerge(List<Node> nodes, List<Node> n) {
		Collections.reverse(n);
		return merge(nodes, n);
	}
}