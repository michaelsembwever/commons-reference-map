/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.ui.pagination;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A generic paginator to enable browse resultset set.
 *
 * Typical usage is you search for data and then only want to display
 * 10 rows at a time.
 *
 * @author <a href="mailto:ola@sesam.no">Ola Hoff Sagli</a>
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class PageNavigator {

    /** Logger for this class. */
    private static Logger log = Logger.getLogger(PageNavigator.class);

    /** The complete result list. */
    private List results;

    /** List with the results for current page. */
    private List pageResults;

    /** Max result limit for the search. */
    private int maxResults;

    /** Number of result elements for one page. */
    private int pageSize;

    /** Current page that is displayed. */
    private int currentPage;

    /** Number of pages with results. */
    private int numberOfPages;

    /**
     * Constructor, needed by serialization.
     */
    public PageNavigator() {
        super();
    }

    /**
     * Create a new pager to enable pagination.
     *
     * @param result the result list to wrap
     * @param pageSize number of elements per page
     * @param maxResults search limit used for the search
     */
    public PageNavigator(final List result, final int pageSize, final int maxResults) {
        super();

        if (result == null) {
            throw new IllegalArgumentException("Result set cannot be null.");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be a positive integer.");
        }
        if (maxResults < 1) {
            throw new IllegalArgumentException("Max results must be a positive integer.");
        }

        this.results = result;
        this.pageSize = pageSize;
        this.maxResults = maxResults;

        // Calculate number of batches.
        if ((result.size() % pageSize) > 0) {
            numberOfPages = (result.size() / pageSize) + 1;
        } else {
            numberOfPages = result.size() / pageSize;
        }

        if (result.size() > 0) {
            setCurrentPage(1);
        } else {
            currentPage = 0;
            pageResults = new ArrayList();
        }
    }

    /**
     * Set current page number, and return the results for the new current page.
     *
     * @param newCurrentPage the page to set
     */
    public void setCurrentPage(final int newCurrentPage) {
        if (newCurrentPage < 1 || newCurrentPage > numberOfPages) {
            throw new IllegalArgumentException("Illegal page number.");
        }

        // Update the current page.
        currentPage = newCurrentPage;
        // Converts the sublist to an arraylist to handle problems with serialization.
        final List tmpResult = results.subList(getOffsetFirstResultInPage() - 1, getOffsetLastResultInPage());
        pageResults = convertListToArrayList(tmpResult);
    }

    /**
     * Browse to next page.
     */
    public void nextPage() {
        setCurrentPage(currentPage + 1);
    }

    /**
     * Browse to previous page.
     */
    public void previousPage() {
        setCurrentPage(currentPage - 1);
    }

    /**
     * @return the current page.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @return if we are at the last page.
     */
    public boolean isLastPage() {
        return (currentPage == numberOfPages);
    }

    /**
     * @return if we are at the first page.
     */
    public boolean isFirstPage() {
        return (currentPage == 1);
    }

    /**
     * @return number of pages with results.
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * @return whether the max limit of results is
     */
    public boolean isMaxResults() {
        return (results.size() >= maxResults);
    }

    /**
     * @return list with all page numbers.
     */
    public List<Integer> getPageNumbers() {
        final ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 1; i <= numberOfPages; i++) {
            numbers.add(new Integer(i));
        }
        return numbers;
    }

    /**
     * @return the whole result set.
     */
    public List getResults() {
        return results;
    }

    /**
     * @return the results for the current page.
     */
    public List getResultsForCurrentPage() {
        return pageResults;
    }

    /**
     * @return total number of results.
     */
    public int getResultSize() {
        return results.size();
    }

    /**
     * @return offset for first result in page.
     */
    public int getOffsetFirstResultInPage() {
        return ((currentPage - 1) * pageSize) + 1;
    }

    /**
     * @return offset for last result in page.
     */
    public int getOffsetLastResultInPage() {
        return Math.min(getOffsetFirstResultInPage() + pageSize - 1, results.size());
    }

    /**
     * Convert list to an arraylist to enable serializable.
     *
     * @param anyList list to convert
     * @return the converted list
     */
    private ArrayList convertListToArrayList(final List anyList) {
        final ArrayList alist = new ArrayList();
        for (Object o : anyList) {
            alist.add(o);
        }
        return alist;
    }

}
