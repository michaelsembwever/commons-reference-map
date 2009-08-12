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
package no.sesat.commons.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility to make it easier to handle lists.
 */
public final class ListUtility {

    public static interface Predicate<T> {
        boolean test(T e);
    }

    public static interface Picker<E,T> {
        E value(T e);
    }

    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------

    // Static --------------------------------------------------------

    /**
     * Take a list and copy the values from that list into a new list if the
     * predicate matches
     *
     * @param <T>
     * @param list
     * @param pred
     * @return
     */
    public static <T> List<T> copy(final Collection<T> list, final Predicate<T> pred) {
        final List<T> res = new ArrayList<T>(list.size());

        for (T t : list) {
            if (pred.test(t)) {
                res.add(t);
            }
        }

        return res;
    }

    /**
     * Take a list and returns a new list with values returned by the picker.
     *
     * @param <T>
     * @param list
     * @param seperator
     * @param picker
     * @return
     */
    public static <E,T> List<E> copy(final Collection<T> list, final Picker<E,T> picker) {
        final List<E> res = new ArrayList<E>(list.size());

        for (T t : list) {
            res.add(picker.value(t));
        }

        return res;
    }

    /**
     * Join a list with a separator picking the elements out using a picker.
     *
     * @param <T>
     * @param list
     * @param seperator
     * @param picker
     * @return
     */
    public static <E, T> String join(final Collection<T> list, final String seperator, final Picker<E, T> picker) {
        if (list == null) {
            return "";
        }

        final StringBuilder res = new StringBuilder();
        for (T t : list) {
            if (res.length() > 0) {
                res.append(seperator);
            }
            res.append(picker.value(t));
        }

        return res.toString();
    }

    /**
     * Join a array with a separator picking the elements out using a picker.
     *
     * @param <T>
     * @param list
     * @param seperator
     * @param picker
     * @return
     */
    public static <E, T> String join(final T[] list, final String seperator, final Picker<E, T> picker) {
        if (list == null) {
            return "";
        }

        final StringBuilder res = new StringBuilder();
        for (T t : list) {
            if (res.length() > 0) {
                res.append(seperator);
            }
            res.append(picker.value(t));
        }

        return res.toString();
    }

    /**
     * Check if a list contains some data that will trigger the predicate.
     *
     * @param <T>
     * @param list
     * @param predicate
     * @return
     */
    public static <T> boolean contains(final Collection<T> list, final Predicate<T> predicate) {
        for (T t : list) {
            if(predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a copy of the list with no duplicates. Using equals as the test.
     *
     * @param <T>
     * @param list
     * @return
     */
    public static <T> List<T> removeduplicates(final List<T> list) {
        final List<T> res = new ArrayList<T>(list.size());
        for (T t : list) {
            out: {
                for (T inResult : res) {
                    if(inResult.equals(t)) {
                        break out;
                    }
                }
                res.add(t);
            }

        }
        return res;
    }

    // Constructors --------------------------------------------------

    private ListUtility() {
        super();
    }

    // Public --------------------------------------------------------

    // Getters / Setters ---------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------

}
