/*
 * Copyright (2009) Schibsted ASA
 *   This file is part of Sesat Commons.
 *
 *   Sesat Commons is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Sesat Commons is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Sesat Commons.  If not, see <http://www.gnu.org/licenses/>.
 */
package no.sesat.commons.jaxb;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter<K, V> extends XmlAdapter<MapElements[], Map<K, V>> {

    public MapElements[] marshal(final Map<K, V> map) throws Exception {

        final MapElements[] mapElements = new MapElements[map.size()];
        int i = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());
        }
        return mapElements;
    }

    public Map<K, V> unmarshal(final MapElements[] elements) throws Exception {

        final Map<K, V> r = new HashMap<K, V>();
        for (MapElements<K, V> mapelement : elements) {
            r.put(mapelement.key, mapelement.value);
        }
        return r;
    }
}
