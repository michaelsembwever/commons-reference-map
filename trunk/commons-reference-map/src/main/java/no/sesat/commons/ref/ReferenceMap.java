/* Copyright (2007) Schibsted Søk AS
 * This file is part of SESAT.
 *
 *   SESAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SESAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with SESAT.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package no.sesat.commons.ref;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
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
 * @param K
 * @param V
 * @author mick semb wever  - mick@semb.wever.org
 * @version $Id$
 */
public final class ReferenceMap<K,V> {

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
    private final ReferenceQueue<V> queue = new ReferenceQueue<V>();
    private final boolean singleThreaded;
    private final ReferenceCleaner cleaner;

    // Static --------------------------------------------------------

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
        
        if(singleThreaded){
            cleaner = null;
        }else{
            cleaner = new ReferenceCleaner();
            cleaner.start();
        }
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
        if(LOG.isInfoEnabled() && cache.size() % 100 == 0){
            LOG.info(value.getClass().getSimpleName() + " cache size is "  + cache.size());
        }
        
        // clean if in single threaded mode
        if(singleThreaded){
            Reference<? extends V> reference = queue.poll();
            while(null != reference){
                reference.clear();
                reference = queue.poll();
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

    private final class ReferenceCleaner extends Thread{

        ReferenceCleaner(){

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
