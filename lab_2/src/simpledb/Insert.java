package simpledb;
import java.io.IOException;
import java.util.*;

/**
 * Inserts tuples read from the child operator into
 * the tableid specified in the constructor
 */
public class Insert extends AbstractDbIterator {
	TransactionId t;
	DbIterator child;
	int tableid;
	TupleDesc resulttupledesc;
	boolean called = false;
	/**
	 * Constructor.
	 * @param t The transaction running the insert.
	 * @param child The child operator from which to read tuples to be inserted.
	 * @param tableid The table in which to insert tuples.
	 * @throws DbException if TupleDesc of child differs from table into which we are to insert.
	 */
	public Insert(TransactionId t, DbIterator child, int tableid)
			throws DbException {
		// some code goes here
		this.t = t;
		this.child = child;
		this.tableid = tableid;
		TupleDesc desc = Database.getCatalog().getTupleDesc(tableid);
		if(!desc.equals(child.getTupleDesc())) {
			throw new DbException("TupleDesc of child differs from table into which we are to insert.");
		}
		Type[] typelist = {Type.INT_TYPE};
		resulttupledesc = new TupleDesc(typelist);
	}

	public TupleDesc getTupleDesc() {
		// some code goes here
		//return null;
		return this.resulttupledesc;

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
	 * Inserts tuples read from child into the tableid specified by the
	 * constructor. It returns a one field tuple containing the number of
	 * inserted records. Inserts should be passed through BufferPool.
	 * An instances of BufferPool is available via Database.getBufferPool().
	 * Note that insert DOES NOT need check to see if a particular tuple is
	 * a duplicate before inserting it.
	 *
	 * @return A 1-field tuple containing the number of inserted records, or
	 * null if called more than once.
	 * @see Database#getBufferPool
	 * @see BufferPool#insertTuple
	 */
	protected Tuple readNext()
			throws TransactionAbortedException, DbException {
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
					Database.getBufferPool().insertTuple(t, tableid, child.next());
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
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
