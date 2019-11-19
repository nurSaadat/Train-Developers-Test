package backend;

import java.util.List;

public class PagedHelper {
    private List<List<String>> list;
    private String next;
    private String prev;
    
    public List<List<String>> getList() {
        return list;
    }
    public void setList(List<List<String>> list) {
        this.list = list;
    }
    public String getNext() {
        return next;
    }
    public void setNext(String next) {
        this.next = next;
    }
    public String getPrev() {
        return prev;
    }
    public void setPrev(String prev) {
        this.prev = prev;
    }
    
}
