package models;

import java.util.Collection;

public class PagedResult<T> {
    public Collection<T> Result;

    public Boolean HasMore;

    public PagedResult(Collection<T> result, Boolean hasMore) {
        Result = result;
        HasMore = hasMore;
    }
}
