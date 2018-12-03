package simpledb;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * The delete operator.  Delete reads tuples from its child operator and
 * removes them from the table they belong to.
 */
public class Delete extends AbstractDbIterator {
	TransactionId t;
	DbIterator child;
	TupleDesc resulttupledesc;
	boolean called = false;
    /**
     * Constructor specifying the transaction that this delete belongs to as
     * well as the child to read from.
     * @param t The transaction this delete runs in
     * @param child The child operator from which to read tuples for deletion
     */
    public Delete(TransactionId t, DbIterator child) {
        // some code goes here
    	this.t = t;
    	this.child = child;
		Type[] typelist = {Type.INT_TYPE};
		resulttupledesc = new TupleDesc(typelist);
    }

    public TupleDesc getTupleDesc() {
        // some code goes here
        //return null;
    	return resulttupledesc;
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
    	child.open();
    }

    public void close() {
        // some code goes here
    	child.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // some code goes here
    	child.rewind();
    }

    /**
     * Deletes tuples as they are read from the child operator. Deletes are
     * processed via the buffer pool (which can be access via the
     * Database.getBufferPool() method.
     * @return A 1-field tuple containing the number of deleted records.
     * @see Database#getBufferPool
     * @see BufferPool#deleteTuple
     */
    protected Tuple readNext() throws TransactionAbortedException, DbException {
        // some code goes here
        //return null;
    	if (called){
			return null;
		} else {
			called=true;
			Tuple result = new Tuple(resulttupledesc);
			int count=0;
			while(child.hasNext()) {
				try {
					Database.getBufferPool().deleteTuple(t, child.next());
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				count++;
			}
			result.setField(0, new IntField(count));
			return result;
		}
    }
}
