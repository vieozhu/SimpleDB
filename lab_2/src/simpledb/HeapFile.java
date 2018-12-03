package simpledb;

import java.io.*;
import java.util.*;

import javax.swing.plaf.synth.SynthSpinnerUI;

/**
 * HeapFile is an implementation of a DbFile that stores a collection
 * of tuples in no particular order.  Tuples are stored on pages, each of
 * which is a fixed size, and the file is simply a collection of those
 * pages. HeapFile works closely with HeapPage.  The format of HeapPages
 * is described in the HeapPage constructor.
 *
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {
	File f;
	TupleDesc td;
	int pageCount;

    /**
     * Constructs a heap file backed by the specified file.
     *
     * @param f the file that stores the on-disk backing store for this heap file.
     */
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
    	this.f=f;
    	this.td=td;
    	pageCount=(int)(f.length()/BufferPool.PAGE_SIZE)+(f.length()%BufferPool.PAGE_SIZE==0?0:1);
    }

    /**
     * Returns the File backing this HeapFile on disk.
     *
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        //return null;
    	return f;
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
    	return f.getAbsoluteFile().hashCode();
    }
    
    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
    	// some code goes here
    	//throw new UnsupportedOperationException("implement this");
    	return td;
    }

    // see DbFile.java for javadocs
    public Page readPage(PageId pid) {
        // some code goes here
        //return null;
    	HeapPage hPage=null;
    	if(pid.getTableId()==getId()&&pid.pageno()<pageCount) {
    		//ҳ�Ŵ�0��ʼ���Ǵ�1��ʼ�������0��ʼ,��ô������
    		try {
    			RandomAccessFile raf=new RandomAccessFile(f, "r");
    			raf.seek(BufferPool.PAGE_SIZE*pid.pageno());
    			byte data[] = new byte[BufferPool.PAGE_SIZE];
    			raf.read(data);
				hPage=new HeapPage((HeapPageId)pid, data);
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return hPage;    	
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    	try {
            HeapPageId hpid= (HeapPageId)page.getId();
            RandomAccessFile rAf=new RandomAccessFile(f,"rw");
            byte[] b=new byte[BufferPool.PAGE_SIZE];
            b=page.getPageData();
            rAf.seek(hpid.pgNo*BufferPool.PAGE_SIZE);
            rAf.write(b, 0, BufferPool.PAGE_SIZE);
            rAf.close();          
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // some code goes here
        //return 0;
    	return pageCount;
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> addTuple(TransactionId tid, Tuple t)
        throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        //return null;
        // not necessary for lab1
    	ArrayList<Page> affectedPages = new ArrayList<Page>();
    	for(int i=0;i<this.numPages();i++) {
    		HeapPage thePage=(HeapPage)Database.getBufferPool().getPage(tid, new HeapPageId(getId(), i), simpledb.Permissions.READ_WRITE);
    		if(thePage.getNumEmptySlots()>0) {
    			thePage.addTuple(t);
//    			this.writePage(thePage);
    			affectedPages.add(thePage);
    			return affectedPages;
    		}
    	}
        HeapPageId hpid=new HeapPageId(this.getId(), this.numPages());
        HeapPage hp=new HeapPage(hpid, new byte[BufferPool.PAGE_SIZE]);
        hp.addTuple(t);
        this.writePage(hp);
        this.pageCount++;
        affectedPages.add(hp); 
        return affectedPages;
    }

    // see DbFile.java for javadocs
    public Page deleteTuple(TransactionId tid, Tuple t)
        throws DbException, TransactionAbortedException {
        // some code goes here
        //return null;
        // not necessary for lab1
		HeapPage thePage=(HeapPage)Database.getBufferPool().getPage(tid,t.getRecordId().getPageId(), simpledb.Permissions.READ_WRITE);
		thePage.deleteTuple(t);
//		try {
//			this.writePage(thePage);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	return thePage;
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(final TransactionId tid) {
        // some code goes here
        //return null;
    	return new DbFileIterator() {
			int currentNumpage=0;
			Iterator<Tuple> it=null;
			public void rewind() throws DbException, TransactionAbortedException {
				// TODO Auto-generated method stub
				currentNumpage=0;
				open();
			}
			
			public void open() throws DbException, TransactionAbortedException {
				// TODO Auto-generated method stub
				if(currentNumpage<pageCount) {
					try {
						it=((HeapPage)Database.getBufferPool().getPage(tid, new HeapPageId(getId(), currentNumpage), simpledb.Permissions.READ_ONLY)).iterator();
					} catch (TransactionAbortedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					it=null;
				}
			}
			
			public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
				// TODO Auto-generated method stub
				if(it!=null) {
					if(it.hasNext()) {
						return it.next();
					}
					else {
						currentNumpage++;
						for(;currentNumpage<pageCount;currentNumpage++) {
							open();
							if(it.hasNext()) {
								return it.next();
							}
						}
						throw new NoSuchElementException("HeapFile:iterator:no more tuple!");
					}
				}
				else {
					throw new NoSuchElementException("HeapFile:iterator:iterator is closed!");
				}
			}
			
			public boolean hasNext() throws DbException, TransactionAbortedException {
				// TODO Auto-generated method stub
				if(it!=null) {
					if(it.hasNext()) {
						return true;
					}
					else {
						int originCurrentNumpage=currentNumpage;
						Iterator<Tuple> originIt=it;
						currentNumpage++;
						for(;currentNumpage<pageCount;currentNumpage++) {
							open();
							if(it.hasNext()) {
								currentNumpage=originCurrentNumpage;
								it=originIt;//�������ﷸ���ˣ�û�������һ��,�������һҳ��ѭ����ʹ��
								return true;
							}
						}
						currentNumpage=originCurrentNumpage;
						it=originIt;
						return false;
					}
				}
				else {
					return false;
				}
				
			}
			
			public void close() {
				// TODO Auto-generated method stub
				it=null;
			}
		};
    	
    }
    
}

