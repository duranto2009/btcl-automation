package util;

import java.util.Collection;

public class RecordNavigator
{
	private int m_currentPageNo;
    private int m_totalRecords;
    private int m_pageSize;
    private int m_totalPages;
    private Collection m_ids;
    private String m_searchFieldInfo[][];
    public RecordNavigator()
    {
        m_currentPageNo = 0;
        m_totalRecords = 0;
        m_pageSize = 20;
        m_totalPages = 0;
        m_ids = null;
        m_searchFieldInfo = (String[][])null;
    }

    public String[][] getSearchFieldInfo()
    {
        return m_searchFieldInfo;
    }

    public void setSearchFieldInfo(String p_searchFieldInfo[][])
    {
        m_searchFieldInfo = p_searchFieldInfo;
    }

    public Collection getIDs()
    {
        return m_ids;
    }

    public void setIDs(Collection p_ids)
    {
        m_ids = p_ids;
    }

    public int getCurrentPageNo()
    {
        return m_currentPageNo;
    }

    public void setCurrentPageNo(int p_currentPageNo)
    {
        m_currentPageNo = p_currentPageNo;
    }

    public int getTotalRecords()
    {
        return m_totalRecords;
    }

    public void setTotalRecords(int p_totalRecords)
    {
        m_totalRecords = p_totalRecords;
    }

    public int getPageSize()
    {
        return m_pageSize;
    }

    public void setPageSize(int p_pageSize)
    {
        m_pageSize = p_pageSize;
    }

    public int getTotalPages()
    {
        return m_totalPages;
    }

    public void setTotalPages(int p_totalPages)
    {
        m_totalPages = p_totalPages;
    }

    public String toString()
    {
        String s = ":";
        s = s + "m_pageSize = " + m_pageSize;
        s = s + ", m_totalRecords = " + m_totalRecords;
        s = s + ", m_currentPageNo = " + m_currentPageNo;
        s = s + ", m_totalPages = " + m_totalPages;
        return s;
    }
}