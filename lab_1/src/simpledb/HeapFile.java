package simpledb;

import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection
 * of tuples in no particular order.  Tuples are stored on pages, each of
 * which is a fixed size, and the file is simply a collection of those
 * pages. HeapFile works closely with HeapPage.  The format of HeapPages
 * is described in the HeapPage constructor.
 * HeapFile无序存储元组集合，元组存储在固定大小的page中，file是page的集合
 * heapfile包含无序的固定大小的page，page中存储元组
 * HeapPage构造函数中描述了HeapPage的格式。
 *
 * @author Sam Madden
 * @see simpledb.HeapPage#HeapPage
 */
public class HeapFile implements DbFile {  // heapfile是对dbfile接口的实现

    private File file;
    private TupleDesc tupleDesc;
    private int numPages;

    /**
     * Constructs a heap file backed by the specified file.
     * heap file 对文件 backed by 支持
     *
     * @param f the file that stores the on-disk backing store for this heap file.
     *          backing store备份存储
     */
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
        this.file = f;
        this.tupleDesc = td;
        // 页大小：一个file块中可以放下多少PAGE_SIZE大小的page
        this.numPages = (int) (file.length() / BufferPool.PAGE_SIZE);

    }

    /**
     * Returns the File backing this HeapFile on disk.
     *
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        return this.file;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere ensure that each
     * HeapFile has a "unique id," and that you always return the same value
     * for a particular HeapFile. We suggest hashing the absolute file name of
     * the file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     *
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // some code goes here
        //throw new UnsupportedOperationException("implement this");
        return this.file.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     *
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        // throw new UnsupportedOperationException("implement this");
        return this.tupleDesc;
    }

    // see DbFile.java for javadocs
    // Read the specified page from disk.
    public Page readPage(PageId pid) throws IllegalArgumentException {  // 实现DbFile接口的方法
        // some code goes here
        // 返回一个HeapPage(HeapPageId id, byte[] data)
        Page page = null;
        byte[] data = new byte[BufferPool.PAGE_SIZE];

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");  // 用于任意读写文件
            // page数量乘以缓冲池中page的数量
            int pos = pid.pageno() * Database.getBufferPool().PAGES_NUM;

            raf.seek(pos);  // raf.seek(long pos)读取时，将指针重置到文件的开始位置。
            /**
             * read(byte b[], int off, int len)
             * @param b     读取数据的缓冲区
             * @param off   数组中写入数据的起始偏移量。
             * @param len   读取的最大字节数。
             * @return the total number of bytes read into the buffer
             * */
            raf.read(data, 0, data.length);
            page = new HeapPage((HeapPageId) pid, data);

        } catch (IOException e) {  // try里边要有内容
            e.printStackTrace();
        }

        return page;
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // some code goes here
        return this.numPages;
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> addTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public Page deleteTuple(TransactionId tid, Tuple t)
            throws DbException, TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        // some code goes here
        // HeapFileIterator 继承DbFileIterator
        return new HeapFileIterator(tid);
    }

    /**
     * Returns an iterator over all the tuples stored in this DbFile
     */
    private class HeapFileIterator implements DbFileIterator {

        private TransactionId tid;  // 事务处理id

        private int pagePos;

        private Iterator<Tuple> tuplesInPage;  // 定义迭代器


        public HeapFileIterator(TransactionId tid) {  // 类构造函数
            this.tid = tid;
        }

        public Iterator<Tuple> getTuplesInPage(HeapPageId pageId) throws TransactionAbortedException, DbException {  // 向方法签名添加异常
            // 通过BufferPool来获得page
            HeapPage page = (HeapPage) Database.getBufferPool().getPage(tid, pageId, Permissions.READ_ONLY);
            // 迭代器返回的是页
            return (Iterator<Tuple>) page.iterator();

        }

        /**
         * Opens the iterator
         *
         * @throws DbException when there are problems opening/accessing the database.
         */
        // 重写方法
        public void open() throws DbException, TransactionAbortedException {
            // 初始化迭代器
            pagePos = 0;
            HeapPageId pageId = new HeapPageId(getId(), pagePos);
            tuplesInPage = getTuplesInPage(pageId);

        }

        /**
         * @return true if there are more tuples available.
         */
        public boolean hasNext() throws DbException, TransactionAbortedException {
            // 迭代器未启动或则关闭
            if (tuplesInPage == null) {
                return false;
            }

            // if there are more tuples available.
            if (tuplesInPage.hasNext()) {
                return true;  // iterator迭代器的hasNext()方法
            } else {
                return false;
            }

        }

        /**
         * Gets the next tuple from the operator (typically implementing by reading
         * from a child operator or an access method).
         *
         * @return The next tuple in the iterator.
         * @throws NoSuchElementException if there are no more tuples
         */
        public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
            // there are no more tuples
            if (!hasNext()) throw new NoSuchElementException("no more tuples");
            else return tuplesInPage.next();  // // iterator迭代器的next()方法
        }

        /**
         * Resets the iterator to the start.
         *
         * @throws DbException When rewind is unsupported.
         */
        public void rewind() throws DbException, TransactionAbortedException {
            // 调用open()初始化迭代器
            open();


        }

        /**
         * Closes the iterator.
         */
        public void close() {
            // 参数设为0，则关闭
            this.pagePos = 0;
            this.tuplesInPage = null;

        }
    }


}

