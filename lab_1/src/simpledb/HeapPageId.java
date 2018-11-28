package simpledb;

/**
 * Unique identifier for HeapPage objects.
 */
public class HeapPageId implements PageId {

    private int tableId;
    private int pageNum;

    /**
     * Constructor. Create a page id structure for a specific page of a
     * specific table.
     *
     * @param tableId The table that is being referenced
     * @param pgNo    The page number in that table.
     */
    public HeapPageId(int tableId, int pgNo) {
        // some code goes here
        this.tableId = tableId;
        this.pageNum = pgNo;

    }

    /**
     * @return the table associated with this PageId
     */
    public int getTableId() {
        // some code goes here
        return tableId;
    }

    /**
     * @return the page number in the table getTableId() associated with
     * this PageId
     */
    public int pageno() {
        // some code goes here
        return pageNum;
    }

    /**
     * @return a hash code for this page, represented by the concatenation of
     * the table number and the page number (needed if a PageId is used as a
     * key in a hash table in the BufferPool, for example.)
     * @see BufferPool
     */
    public int hashCode() throws UnsupportedOperationException {
        // some code goes here
        // 由表号和页码的串联表示
        int _hashCode = tableId + pageNum;
        return _hashCode;
    }

    /**
     * Compares one PageId to another.
     *
     * @param o The object to compare against (must be a PageId)
     * @return true if the objects are equal (e.g., page numbers and table
     * ids are the same)
     */
    public boolean equals(Object o) {
        // some code goes here
        // 对象是不是HeapPageId类，然后比较这两个类的pageId
        if (o instanceof HeapPageId) {
            // 表示这个类
            HeapPageId another = (HeapPageId) o;

            if (another.getTableId() == this.getTableId()) {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }

    /**
     * Return a representation of this object as an array of
     * integers, for writing to disk.  Size of returned array must contain
     * number of integers that corresponds to number of args to one of the
     * constructors.
     */
    public int[] serialize() {
        // some code goes here
        // Not necessary for lab 1, 2, or 3
        return null;
    }

}
