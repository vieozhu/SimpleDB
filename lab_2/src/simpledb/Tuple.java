package simpledb;

/**
 * Tuple maintains information about the contents of a tuple.
 * Tuples have a specified schema specified by a TupleDesc object and contain
 * Field objects with the data for each field.
 */
public class Tuple {
	TupleDesc td;
	RecordId rid;
	Field[] fs;
	
	public static Tuple combine(Tuple t1, Tuple t2) {
        TupleDesc td1=t1.getTupleDesc(), td2= t2.getTupleDesc();
        TupleDesc combinedTd = TupleDesc.combine(td1,td2);
        Tuple mergedTp = new Tuple(combinedTd);
        int i;
        for(i = 0; i < td1.numFields(); i++){
            mergedTp.setField(i, t1.getField(i));
        }
        
        for(int j = 0; j < td2.numFields(); j++){
            mergedTp.setField(i, t2.getField(j));
            i++;
        }
        return mergedTp;

    }
	
    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td the schema of this tuple. It must be a valid TupleDesc
     * instance with at least one field.
     */
    public Tuple(TupleDesc td) {
        // some code goes here
    	this.td=td;
    	fs=new Field[td.numFields()];
    	for(int i=0;i<td.numFields();i++) {
    		fs[i]=null;
    	}
    	this.rid=null;
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        //return null;
    	return this.td;
    }

    /**
     * @return The RecordId representing the location of this tuple on
     *   disk. May be null.
     */
    public RecordId getRecordId() {
        // some code goes here
        //return null;
    	return this.rid;
    }

    /**
     * Set the RecordId information for this tuple.
     * @param rid the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        // some code goes here
    	this.rid=rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i index of the field to change. It must be a valid index.
     * @param f new value for the field.
     */
    public void setField(int i, Field f) {
        // some code goes here
    	this.fs[i]=f;
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     *
     * @param i field index to return. Must be a valid index.
     */
    public Field getField(int i) {
        // some code goes here
        //return null;
    	return fs[i];
    }

    /**
     * Returns the contents of this Tuple as a string.
     * Note that to pass the system tests, the format needs to be as
     * follows:
     *
     * column1\tcolumn2\tcolumn3\t...\tcolumnN\n
     *
     * where \t is any whitespace, except newline, and \n is a newline
     */
    public String toString() {
        // some code goes here
    	//throw new UnsupportedOperationException("Implement this");
    	String theTupleString="";
    	for(int i=0;i<this.fs.length;i++) {
    		theTupleString+=(this.fs[i]==null?this.fs[i]:this.fs[i].toString());
    		if(i<this.fs.length-1) {
    			theTupleString+="\t";
    		}
    		else {
    			theTupleString+="\n";
    		}
    	}
    	return theTupleString;
    }
}
