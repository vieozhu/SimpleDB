package simpledb;

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
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {

	private TupleDesc tupleDesc;

	private int numOfPage;

	private File file;
    /**
     * Constructs a heap file backed by the specified file.
     *
     * @param f the file that stores the on-disk backing store for this heap file.
     */
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
    	file = f;
        // 页大小：一个file块中可以放下多少PAGE_SIZE大小的page
    	numOfPage = (int) (file.length() / BufferPool.PAGE_SIZE);
    	tupleDesc = td;
    }

    /**
     * Returns the File backing this HeapFile on disk.
     *
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        return file;
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
    	return file.getAbsoluteFile().hashCode();

        //throw new UnsupportedOperationException("implement this");
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
    	// some code goes here
    	return tupleDesc;
    	//throw new UnsupportedOperationException("implement this");
    }

    // see DbFile.java for javadocs
    /**
     * read(byte b[], int off, int len)
     * @param //b     读取数据的缓冲区
     * @param //off   数组中写入数据的起始偏移量。
     * @param //len   读取的最大字节数。
     * @return the total number of bytes read into the buffer
     * */
    public Page readPage(PageId pid) {
        // some code goes here
        // 返回一个HeapPage(HeapPageId id, byte[] data)
    	Page page = null;
        byte[] data = new byte[BufferPool.PAGE_SIZE];

        try
        {
            RandomAccessFile raf = new RandomAccessFile(getFile(), "r");
            // page数量乘以缓冲池中page的数量
            int pos = pid.pageno() * BufferPool.PAGE_SIZE;

            // raf.seek(long pos)读取时，将指针重置到文件的开始位置。
            raf.seek(pos);
            raf.read(data, 0, data.length);
            page = new HeapPage((HeapPageId) pid, data);
        } catch (IOException e) {
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
        return numOfPage;
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

    private class HeapFileIterator implements DbFileIterator {

        private int pagePos;

        private Iterator<Tuple> tuplesInPage;

        private TransactionId tid;  // 事务处理id

        public HeapFileIterator(TransactionId tid) {
            // 构造函数
            this.tid = tid;
        }

        public Iterator<Tuple> getTuplesInPage(HeapPageId pid) throws TransactionAbortedException, DbException {
            // 通过BufferPool来获得page
            HeapPage page = (HeapPage) Database.getBufferPool().getPage(tid, pid, Permissions.READ_ONLY);
            return page.iterator();  // 迭代器返回的是页
        }

        public void open() throws DbException, TransactionAbortedException {
            // 初始化迭代器
            pagePos = 0;
            HeapPageId pid = new HeapPageId(getId(), pagePos);

            tuplesInPage = getTuplesInPage(pid);
        }

        /**
         * @return true if there are more tuples available.
         */
        public boolean hasNext() throws DbException, TransactionAbortedException {
            if (tuplesInPage == null) {
                // 迭代器未启动或则关闭
                return false;
            }

            if (tuplesInPage.hasNext()) {
                // if there are more tuples available.
                return true;
            }
            if (pagePos < numPages() - 1) {
                pagePos++;
                HeapPageId pid = new HeapPageId(getId(), pagePos);
                tuplesInPage = getTuplesInPage(pid);

                return tuplesInPage.hasNext();
            } else return false;
        }

        public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("not opened or no tuple remained");
            }
            return tuplesInPage.next();
        }

        public void rewind() throws DbException, TransactionAbortedException {

            open();
        }

        public void close() {
            pagePos = 0;
            tuplesInPage = null;
        }
    }
}

