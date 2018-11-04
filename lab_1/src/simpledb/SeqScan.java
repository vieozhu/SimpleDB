package simpledb;

import java.util.*;

/**
 * SeqScan is an implementation of a sequential scan access method that reads
 * each tuple of a table in no particular order (e.g., as they are laid out on
 * disk).
 */
public class SeqScan implements DbIterator {

    /**
     * Creates a sequential scan over the specified table as a part of the
     * specified transaction.
     * SeqScan是顺序扫描访问方法的一种实现，它以无特定顺序读取表的每个元组
     *
     * @param tid        The transaction this scan is running as a part of.
     *                   此扫描作为其中一部分运行的事务
     * @param tableid    the table to scan.
     * //@param tab leAlias the alias of this table (needed by the parser);
     * the returned tupleDesc should have fields with name tableAlias.fieldName
     * (note: this class is not responsible for handling a case where tableAlias
     * or fieldName are null.  It shouldn't crash if they are, but the resulting
     * name can be null.fieldName, tableAlias.null, or null.null).
     */
    private TransactionId tid;
    private int tableid;
    private String tableAlias;

    // reads each tuple of a table in no particular order
    // sequential scan access method

    public SeqScan(TransactionId tid, int tableid, String tableAlias) {
        // some code goes here
        this.tid = tid;
        this.tableAlias = tableAlias;
        this.tableid = tableid;
    }

    public void open()
            throws DbException, TransactionAbortedException {
        // some code goes here
    }

    /**
     * Returns the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor.
     *
     * @return the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return null;
    }

    public boolean hasNext() throws TransactionAbortedException, DbException {
        // some code goes here
        return false;
    }

    public Tuple next()
            throws NoSuchElementException, TransactionAbortedException, DbException {
        // some code goes here
        return null;
    }

    public void close() {
        // some code goes here
    }

    public void rewind()
            throws DbException, NoSuchElementException, TransactionAbortedException {
        // some code goes here
    }
}
