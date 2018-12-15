package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

	
    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    private ArrayList<TDItem> items = new ArrayList<TDItem>();
    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
    	//done
    	return items.iterator();
        //return null;
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
    	//items = new ArrayList<TDItem>();
    	for (int i = 0; i < typeAr.length; ++i)
    	{
    		if (i < fieldAr.length)
    		{
    			items.add(new TDItem(typeAr[i], fieldAr[i]));
    		}
    		else
    		{
    			items.add(new TDItem(typeAr[i], ""));
    		}
    	}
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
    	//items = new ArrayList<TDItem>();
    	for (int i = 0; i < typeAr.length; ++i)
    	{
    		items.add(new TDItem(typeAr[i], ""));
    	}
    }
    
    public TupleDesc(Collection<TDItem> s)
    {
    	//items = new ArrayList<TDItem>();
    	items.addAll(s);
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
    	return items.size();
        //return 0;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
    	if (i < items.size())
    	{
    		return items.get(i).fieldName;
    	}
    	else
    	{
    		throw new NoSuchElementException();
    	}
        //return null;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getType(int i) throws NoSuchElementException {
        // some code goes here
    	if (i < items.size())
    	{
    		return items.get(i).fieldType;
    	}
    	else
    	{
    		throw new NoSuchElementException();
    	}
        //return null;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int nameToId(String name) throws NoSuchElementException {
        // some code goes here
    	//done
    	for (int i = 0; i < items.size(); ++i)
    	{
    		if (items.get(i).fieldName.equals(name))
    		{
    			return i;
    		}
    	}
    	throw new NoSuchElementException();
        //return 0;
    }
    

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
    	int size = 0;
    	for (TDItem i:items)
    	{
    		size += i.fieldType.getLen();
    	}
    	return size;
        //return 0;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
    	//done
    	ArrayList<TDItem> a1 = td1.items;
    	ArrayList<TDItem> a2 = td2.items;
    	ArrayList<TDItem> tmp = (ArrayList<TDItem>)a1.clone();
    	boolean flag = tmp.addAll(a2);
    	return new TupleDesc(tmp);
        //return null;
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
    	if (o instanceof TupleDesc)
    	{
    		TupleDesc t = (TupleDesc)o;
    		if (t.numFields() == this.numFields())
    		{
    			for (int i = 0; i < this.numFields();i++)
    			{
    				if (!t.getType(i).equals(this.getType(i)))
    				{
    					return false;
    				}
    			}
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	else
    	{
    		return false;
    	}
        //return false;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
    	String ret = "";
    	for  (TDItem i:items)
    	{
    		ret += i.fieldType.toString()+"("+i.fieldName+")"+",";
    	}
    	if (ret != "")
    	{
    		ret = ret.substring(0, ret.length() - 1);
    	}
        return ret;
    }
}
