package simpledb;
import java.util.*;


/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc {
	public Type[] typeAr;
	public String[] fieldAr;
    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields
     * fields, with the first td1.numFields coming from td1 and the remaining
     * from td2.
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc combine(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        //return null;
    	Type[] dtTypeAr=new Type[td1.typeAr.length+td2.typeAr.length];
    	System.arraycopy(td1.typeAr, 0, dtTypeAr, 0, td1.typeAr.length);
    	System.arraycopy(td2.typeAr, 0, dtTypeAr, td1.typeAr.length, td2.typeAr.length);
    	String[] dtFieldAr=new String[td1.fieldAr.length+td2.fieldAr.length];
    	System.arraycopy(td1.fieldAr, 0, dtFieldAr, 0, td1.fieldAr.length);
    	System.arraycopy(td2.fieldAr, 0, dtFieldAr, td1.fieldAr.length, td2.fieldAr.length);
    	TupleDesc td=new TupleDesc(dtTypeAr,dtFieldAr);
    	return td;
    }

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr array specifying the number of and types of fields in
     *        this TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
    	this.typeAr=typeAr;
    	if(fieldAr==null) {
    		this.fieldAr=new String[typeAr.length];
    		for(int i=0;i<typeAr.length;i++) {
    			this.fieldAr[i]=null;
    		}
    	}
    	else {
    		this.fieldAr=fieldAr;
    	}
    }

    /**
     * Constructor.
     * Create a new tuple desc with typeAr.length fields with fields of the
     * specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr array specifying the number of and types of fields in
     *        this TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
    	this.typeAr=typeAr;
    	this.fieldAr=new String[typeAr.length];
		for(int i=0;i<typeAr.length;i++) {
			this.fieldAr[i]=null;
		}
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        //return 0;
    	return typeAr.length;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        //return null;
    	if(i>=typeAr.length) {
    		throw new NoSuchElementException("Tuple:getFieldName:i>=typeAr.length");
    	}
        return fieldAr[i];
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     */
    public int nameToId(String name) throws NoSuchElementException {
        // some code goes here
        //return 0;
    	for(int i=0;i<fieldAr.length;i++) {
    		if(name==null) {
    			if(fieldAr[i]==null) {
    				return i;
    			}
    		}else {
    			if(name.equals(fieldAr[i])) {
        			return i;
        		}
    		}
    	}
    	throw new NoSuchElementException("Tuple:nameToId:name not in fieldAr");
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i The index of the field to get the type of. It must be a valid index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getType(int i) throws NoSuchElementException {
        // some code goes here
        //return null;
    	if(i>=typeAr.length) {
    		throw new NoSuchElementException("Tuple:getType:i>=typeAr.length");
    	}
    	return typeAr[i];
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     * Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        //return 0;
    	int size=0;
    	for(Type t:typeAr) {
    		size+=t.getLen();
    	}
    	return size;
    }

    /**
     * Compares the specified object with this TupleDesc for equality.
     * Two TupleDescs are considered equal if they are the same size and if the
     * n-th type in this TupleDesc is equal to the n-th type in td.
     *
     * @param o the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
        //return false;
    	boolean isEquals=true;
    	if(!(o instanceof TupleDesc)) {
    		isEquals=false;
    	}
    	else if(typeAr.length!=((TupleDesc)o).typeAr.length) {
			isEquals=false;
    	}
    	else{
    		for(int i=0;i<typeAr.length;i++) {
	    		if(typeAr[i]!=((TupleDesc)o).typeAr[i]) {
	    			isEquals=false;
	        	}
	    	}
    	}
    	return isEquals;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
    	String hashCodeStr="";
    	for(int i=0;i<typeAr.length;i++) {
    		hashCodeStr+=typeAr[i];
    	}
    	return hashCodeStr.hashCode();
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        //return "";
    	String describing="";
    	for(int i =0;i<typeAr.length;i++) {
    		
    		describing+=typeAr[i]+"("+fieldAr[i]+")";
    		if(i!=typeAr.length-1) {
    			describing+=",";
    		}
    	}
    	return describing;
    }
}
