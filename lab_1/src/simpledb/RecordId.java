package simpledb;

import java.security.PrivateKey;

/**
 * A RecordId is a reference to a specific tuple on a specific page of a
 * specific table.
 * RecordId是对特定表的特定页面上的特定元组的引用
 */
public class RecordId {

    /**
     * Creates a new RecordId refering to the specified PageId and tuple number.
     * 参照pageid和tuple号创建新的recordid
     *
     * @param pid the pageid of the page on which the tuple resides
     * @param tupleno the tuple number within the page.
     */

    private PageId pageId;  // 接口，有equal方法
    private int tupleNum;

    public RecordId(PageId pid, int tupleno) {
        // some code goes here
        this.pageId = pid;
        this.tupleNum = tupleno;
    }

    /**
     * @return the tuple number this RecordId references.
     */
    public int tupleno() {
        // some code goes here
        return tupleNum;
    }

    /**
     * @return the page id this RecordId references.
     */
    public PageId getPageId() {
        // some code goes here

        return pageId;
    }

    /**
     * Two RecordId objects are considered equal if they represent the same tuple.
     *
     * @return True if this and o represent the same tuple
     */
    @Override
    public boolean equals(Object o) {
        // some code goes here
        if (o == this) {
            return true;
        } else if (o instanceof RecordId) {
            RecordId another = (RecordId) (o);

            return this.pageId.equals(another.getPageId()) && this.tupleNum == another.tupleno();

        } else return false;
    }

    /**
     * You should implement the hashCode() so that two equal RecordId instances
     * (with respect to equals()) have the same hashCode().
     *
     * @return An int that is the same for equal RecordId objects.
     */
    @Override
    public int hashCode() {
        // some code goes here
        //throw new UnsupportedOperationException("implement this");
        return this.tupleNum + this.pageId.hashCode();
        //.hashCode()方法return a hash code for this page

    }

}
