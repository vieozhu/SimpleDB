package simpledb;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * Knows how to compute some aggregate over a set of IntFields.
 */
public class IntAggregator implements Aggregator {
	int gbfield;
	Type gbfieldtype;
	int afield;
	Op what;
	HashMap<Field, ArrayList<Field> > gbagg;
	IntField NO_GROUPINGfield;
	TupleDesc resulttupledesc=null;
	Tuple resulttuple=null;
    /**
     * Aggregate constructor
     * @param gbfield the 0-based index of the group-by field in the tuple, or NO_GROUPING if there is no grouping
     * @param gbfieldtype the type of the group by field (e.g., Type.INT_TYPE), or null if there is no grouping
     * @param afield the 0-based index of the aggregate field in the tuple
     * @param what the aggregation operator
     */

    public IntAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
        // some code goes here
    	this.gbfield = gbfield;
    	this.gbfieldtype = gbfieldtype;
    	this.afield = afield;
    	this.what = what;
    	gbagg = new HashMap<Field, ArrayList<Field> >();
    	NO_GROUPINGfield = new IntField(NO_GROUPING);
    	if(gbfield == NO_GROUPING){
			Type[] typelist= {Type.INT_TYPE};
			resulttupledesc=new TupleDesc(typelist);
			resulttuple=new Tuple(resulttupledesc);
		}
		else {
			Type[] typelist= {gbfieldtype,Type.INT_TYPE};
			resulttupledesc=new TupleDesc(typelist);
			resulttuple=new Tuple(resulttupledesc);
		}
    }

    /**
     * Merge a new tuple into the aggregate, grouping as indicated in the constructor
     * @param tup the Tuple containing an aggregate field and a group-by field
     */
    public void merge(Tuple tup) {
        // some code goes here
    	Field thegbfiled = null;
    	if(this.gbfield == NO_GROUPING) {
    		thegbfiled = NO_GROUPINGfield;
    	}else {
    		thegbfiled = tup.getField(this.gbfield);
    	}
    	if(!gbagg.containsKey(thegbfiled)) {
    		ArrayList<Field> newlist = new ArrayList<Field>();
    		newlist.add(tup.getField(afield));
    		gbagg.put(thegbfiled, newlist);
    	}
    	else {
    		gbagg.get(thegbfiled).add(tup.getField(afield));
    	}
    }

    

    
    /**
     * Create a DbIterator over group aggregate results.
     *
     * @return a DbIterator whose tuples are the pair (groupVal,
     *   aggregateVal) if using group, or a single (aggregateVal) if no
     *   grouping. The aggregateVal is determined by the type of
     *   aggregate specified in the constructor.
     */
    public DbIterator iterator() {
        // some code goes here
        //throw new UnsupportedOperationException("implement me");
    	
    	return new DbIterator() {
    		Iterator<Entry<Field, ArrayList<Field> > > i=null ;

			@Override
			public void rewind() throws DbException, TransactionAbortedException {
				// TODO Auto-generated method stub
				i = gbagg.entrySet().iterator();
			}
			
			@Override
			public void open() throws DbException, TransactionAbortedException {
				// TODO Auto-generated method stub
				i = gbagg.entrySet().iterator();
				
			}
			
			@Override
			public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
				// TODO Auto-generated method stub
				//return null;
				Entry<Field, ArrayList<Field> > entry = i.next();
				Field group=entry.getKey();
				ArrayList<Field> groupaggfields=entry.getValue();
				Iterator<Field> groupaggfieldsi = groupaggfields.iterator();
				IntField aggvalue=null;
				IntField tempf2=null;

				if(what==Op.MIN) {
					while(groupaggfieldsi.hasNext()) {
						tempf2=(IntField)groupaggfieldsi.next();
						if(aggvalue==null) {
							aggvalue=tempf2;
						}else {
							if (aggvalue.getValue()>tempf2.getValue()){
								aggvalue=tempf2;
							}
						}
					}
				}
				
				if(what==Op.MAX) {
					while(groupaggfieldsi.hasNext()) {
						tempf2=(IntField)groupaggfieldsi.next();
						if(aggvalue==null) {
							aggvalue=tempf2;
						}else {
							if (aggvalue.getValue()<tempf2.getValue()){
								aggvalue=tempf2;
							}
						}
					}
				}
				
				if(what==Op.SUM) {
					int sum=0;
					while(groupaggfieldsi.hasNext()) {
						sum+=((IntField)groupaggfieldsi.next()).getValue();
					}
					aggvalue=new IntField(sum);
				}
				
				if(what==Op.AVG) {
					int sum=0;
					while(groupaggfieldsi.hasNext()) {
						sum+=((IntField)groupaggfieldsi.next()).getValue();
					}
					aggvalue=new IntField(sum/groupaggfields.size());
				}
				
				if(what==Op.COUNT) {
					aggvalue=new IntField(groupaggfields.size());
				}
				
				if(gbfield == NO_GROUPING){
					resulttuple.setField(0, aggvalue);
				}
				else {
					resulttuple.setField(0, group);
					resulttuple.setField(1, aggvalue);
				}
				return resulttuple;
			}
			
			@Override
			public boolean hasNext() throws DbException, TransactionAbortedException {
				// TODO Auto-generated method stub
				//return false;
				return i.hasNext();
			}
			
			@Override
			public TupleDesc getTupleDesc() {
				// TODO Auto-generated method stub
				//return null;
				return resulttupledesc;
			}
			
			@Override
			public void close() {
				// TODO Auto-generated method stub
				i=null;
			}
		};
    }

}
