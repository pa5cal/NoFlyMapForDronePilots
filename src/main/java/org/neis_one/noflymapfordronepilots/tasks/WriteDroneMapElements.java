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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.neis_one.noflymapfordronepilots.entity.DroneMapElement;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;

/**
 * Writes {@link DroneMapElement}'s to a file.
 * 
 * @author pa5cal
 */
public class WriteDroneMapElements {

	private static final double deegreeToleranceSimplifier = CoordinateUtils.toDeegrees(10);
	private String filePath;

	public WriteDroneMapElements(String filePath) {
		this.filePath = filePath;
	}

	public void writeResults(Collection<DroneMapElement> droneMapElements) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));) {
			writer.write("var droneMapElements = [");
			int numPolgyons = 0;
			Collection<Geometry> polygons = new ArrayList<>();
			for (DroneMapElement droneMapElement : droneMapElements) {
				if (droneMapElement.osmNodes.size() < 2) {
					System.out.println("Invalid number of points in LineString (found 1 - must be 0 or >= 2) - skip");
					continue;
				}

				// Create geometry
				Coordinate[] coordinates = droneMapElement.osmNodes.stream()
						.map(n -> new Coordinate(n.getLongitude(), n.getLatitude())).toArray(Coordinate[]::new);
				Geometry elementGeometry;
				if (coordinates[0].equals(coordinates[coordinates.length - 1])) {
					elementGeometry = new GeometryFactory().createPolygon(coordinates);
				} else {
					elementGeometry = new GeometryFactory().createLineString(coordinates);
				}

				// Simplify geometry
				elementGeometry = DouglasPeuckerSimplifier.simplify(elementGeometry,
								deegreeToleranceSimplifier);

				// Buffer geometry
				final int buffer = droneMapElement.osmElementFilter.bufferInMeter;
				if (buffer > 0) {
					elementGeometry = elementGeometry.buffer(CoordinateUtils.toDeegrees(buffer));
					polygons.add(elementGeometry);
				} else {
					// Write result
					write(writer, numPolgyons, elementGeometry, new ArrayList<Geometry>());
					numPolgyons++;
				}
			}
			// Union
			Geometry polygon = CascadedPolygonUnion.union(polygons);
			if (polygon != null && polygon instanceof MultiPolygon) {
				MultiPolygon multipolygon = (MultiPolygon) polygon;
				for (int idx = 0; idx < multipolygon.getNumGeometries(); idx++) {
					Polygon polygonN = (Polygon) multipolygon.getGeometryN(idx);
					List<Geometry> inner = new ArrayList<>();
					for (int idxInner = 0; idxInner < polygonN.getNumInteriorRing(); idxInner++) {
						inner.add(polygonN.getInteriorRingN(idxInner));
					}
					write(writer, numPolgyons, polygonN.getExteriorRing(), inner);
					numPolgyons++;
				}
			}
			else {
				System.out.println("Something went wrong :(");
			}

			writer.write("];");
			System.out.println(" Number of polygons: " + numPolgyons);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void write(BufferedWriter writer, int idx, Geometry outer, List<Geometry> inner) throws IOException {
		if (idx > 0) {
			writer.append(",");
		}

		StringBuffer result = new StringBuffer();
		result.append("[");
		result.append(write(outer.getCoordinates()));
		for (Geometry geometry : inner) {
			result.append(",");
			result.append(write(geometry.getCoordinates()));
		}
		result.append("]");

		writer.append(result.toString());
	}

	private String write(Coordinate[] coordinates) {
		StringBuffer result = new StringBuffer();
		result.append("[");
		for (int idx = 0; idx < coordinates.length; idx++) {
			Coordinate coord = coordinates[idx];
			if (idx > 0) {
				result.append(",");
			}
			result.append("[").append(coord.y).append(",").append(coord.x).append("] ");
		}
		result.append("]");
		return result.toString();
	}

	private static class CoordinateUtils {
		private static final int radiusEarth = 6378000;
		private static final double circumferenceEarth = Math.PI * (radiusEarth * 2);
		private static final int equatorDeegreesEarth = 360;
		private static final double metersPerDeegreeEarth = circumferenceEarth / equatorDeegreesEarth;

		static double toDeegrees(int meter) {
			return meter / metersPerDeegreeEarth;
		}
	}
}