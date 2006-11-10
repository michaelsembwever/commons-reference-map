/*
 * PagingSupport.java
 *
 * Created on 10 November 2006, 10:56
 *
 */

package no.schibstedsok.commons.seam;

import java.util.List;

/**
 *
 * @author mick
 * @version $Id$
 */
public final class PagingSupport<T> {
    
    private final List<T> pagingList;
    private int pageSize = 0;
    private int currentItem = 0;
    
    /** Creates a new instance of PagingSupport */
    public PagingSupport(final List<T> pagingList, final int pageSize) {
        
        this.pagingList = pagingList;
        this.pageSize = pageSize;
    }
    
    public int getPageSize(){
        return pageSize;
    }
    
    public void setPageSize(final int pageSize){
        this.pageSize = pageSize;
    }
    
    public int getCurrentItem(){
        return currentItem;
    }
    
    public boolean isFirstPage() {
        
        return currentItem == 0;
    }

    public boolean isLastPage() {
        
        return currentItem + pageSize >= pagingList.size();
    }
    
    public List<T> currentPage(){
        
        return pagingList.subList(currentItem, Math.min(currentItem+pageSize, pagingList.size()));
    }
    
    public List<T> nextPage() {
        
        assert !isLastPage();
        currentItem += pageSize;
        return currentPage();
    }
    
    
    public List<T> previousPage() {
        
        assert !isFirstPage();
        currentItem -= pageSize;
        return currentPage();
    }
    
}
