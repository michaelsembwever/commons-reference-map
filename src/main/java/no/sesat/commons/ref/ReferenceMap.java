/* Copyright (2007-2008) Schibsted Søk AS
 * This file is part of Sesat Commons.
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
 *
 */
package no.sesat.commons.ref;

import java.lang.ref.*;
import java.util.Map;

import org.apache.log4j.Logger;

/** Provides a Map where values are referenced either weakly or softly.
 * This can be used to cache immutable objects without preventing them from bing garbaged collected.    <br/><br/>
 *
 * Concurrency and synchronisation of this class fallbacks to the Map implementation passed in as the cache parameter
 *  in the constructor. For example: use a Hashtable for synchronised access, a ConcurrentHashMap for concurrent access,
 *  and a HashMap for single threaded access (make sure to specify the argument indicating single thread use).
 *
 * <br/><br/>
 *
 * This implementation improves over org.apache.commons.collections.map.ReferenceMap in that the synchronisation and
 *  concurrency is determined through delegation to the map supplied in the constructor as described above.   <br/><br/>
 *
 * @param <K> key type
 * @param <V> value type
 * @version $Id$
 */
public final class ReferenceMap<K,V extends Object> {

    public enum Type {
        WEAK,
        SOFT;

        <K,V> Reference<V> createReference(
                final ReferenceMap<K,V> cache,
                final K key,
                final V value){

            switch(this){
                case WEAK:
                    return cache.new WeakReference(key, value);
                case SOFT:
                    return cache.new SoftReference(key, value);
            }
            throw new IllegalStateException("Please implement createReference(..) for " + toString());
        }
    }

    // Constants -----------------------------------------------------

    private static final Logger LOG = Logger.getLogger(ReferenceMap.class);

    // Attributes ----------------------------------------------------

    private final Type type;
    private final Map<K,Reference<V>> cache;
    private final boolean singleThreaded;

    // Static --------------------------------------------------------

    private static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
    private static final ReferenceCleaner cleaner = new ReferenceCleaner(queue);

    static {
        cleaner.start();
    }

    // Constructors --------------------------------------------------

    /**
     *
     * @param type
     * @param cache
     */
    public ReferenceMap(final Type type, final Map<K,Reference<V>> cache) {
        this(type, cache, false);
    }

    /**
     *
     * @param type
     * @param cache
     * @param singleThreaded
     */
    public ReferenceMap(final Type type, final Map<K,Reference<V>> cache, final boolean singleThreaded) {
        this.type = type;
        this.cache = cache;
        this.singleThreaded = singleThreaded;
    }

    // Public --------------------------------------------------------

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public V put(final K key, final V value) {
        // log cache size every 100 increments
        if(LOG.isDebugEnabled() && cache.size() % 100 == 0){
            LOG.debug(value.getClass().getSimpleName() + " cache size is "  + cache.size());
        }

        // clean if in single threaded mode
        if(singleThreaded){
            Reference<?> reference = (Reference<?>)queue.poll();
            while(null != reference){
                reference.clear();
                reference = (Reference<?>)queue.poll();
            }
        }

        final Reference<V> change = cache.put(key, type.createReference(this, key, value));
        return null != change ? change.get() : null;
    }

    /**
     *
     * @param key
     * @return
     */
    public V get(final K key){

        final Reference<V> reference = cache.get(key);
        return null != reference ? reference.get() : null;
    }

    @Override
    public String toString() {
        return super.toString() + " Type:" + type + " Size:" + cache.size();
    }

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------

    private final class WeakReference extends java.lang.ref.WeakReference<V>{

        private K key;

        WeakReference(final K key, final V value){

            super(value, queue);
            this.key = key;
        }

        @Override
        public void clear() {

            // clear the hashmap entry too.
            cache.remove(key);
            key = null;

            // clear the referent
            super.clear();
        }
    }

    private final class SoftReference extends java.lang.ref.SoftReference<V>{

        private K key;

        SoftReference(final K key, final V value){

            super(value, queue);
            this.key = key;
        }

        @Override
        public void clear() {

            // clear the hashmap entry too.
            cache.remove(key);
            key = null;

            // clear the referent
            super.clear();
        }
    }

    private final static class ReferenceCleaner extends Thread{

        private final ReferenceQueue<?> queue;

        ReferenceCleaner(ReferenceQueue<?> queue){
            super("ReferenceMap.ReferenceCleaner");
            this.queue = queue;
            setPriority(Thread.MAX_PRIORITY);
            setDaemon(true);
        }

        @Override
        public void run() {
            while(true){
                try {
                    queue.remove().clear();
                }catch (InterruptedException ex) {
                    LOG.error(ex.getMessage(), ex);
                }
            }
        }
    }
}
