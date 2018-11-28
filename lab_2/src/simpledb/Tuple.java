package simpledb;

/**
 * Tuple maintains information about the contents of a tuple.
 * Tuples have a specified schema specified by a TupleDesc object and contain
 * Field objects with the data for each field.
 */
public class Tuple {
    private Field[] fields;  //Field objects

    private TupleDesc tupleDesc;  //TupleDesc object

    private RecordId recordId;  //RecordId


    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td the schema of this tuple. It must be a valid TupleDesc
     *           instance with at least one field.
     */
    public Tuple(TupleDesc td) {
        // some code goes here
        // 构造函数，初始化一个元组
        this.tupleDesc = td;
        // 初始化Field数组
        fields = new Field[td.numFields()];
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        // 返回元组的模式tupleDesc
        return tupleDesc;
    }

    /**
     * @return The RecordId representing the location of this tuple on
     * disk. May be null.
     */
    public RecordId getRecordId() {
        // some code goes here
        // 返回元组的存储位置recordId
        return recordId;
    }

    /**
     * Set the RecordId information for this tuple.
     *
     * @param rid the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        // some code goes here
        // 初始化rid
        this.recordId = rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i index of the field to change. It must be a valid index.
     * @param f new value for the field.
     */

    public void setField(int i, Field f) {
        // some code goes here
        // 设置第i个元组的内容
        if (!isValidIndex(i)) {
            throw new IllegalArgumentException("索引越界");
        }
        fields[i] = f;
    }

    /**
     * @param i field index to return. Must be a valid index.
     * @return the value of the ith field, or null if it has not been set.
     */
    public Field getField(int i) {
        // some code goes here
        // 返回第i个fields内容
        if (!isValidIndex(i)) {
            throw new IllegalArgumentException("索引越界");
        }
        return fields[i];
    }

    private boolean isValidIndex(int index) {
        // 判断fields是否越界,返回值为真
        return index >= 0 && index < fields.length;
    }

    /**
     * Returns the contents of this Tuple as a string.返回元组的内容
     * Note that to pass the system tests, the format needs to be as
     * follows:
     * <p>
     * column1\tcolumn2\tcolumn3\t...\tcolumnN\n
     * <p>
     * where \t is any whitespace, except newline, and \n is a newline
     */
    public String toString() {
        // some code goes here
        //throw new UnsupportedOperationException("Implement this");
        StringBuffer rowString = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length - 1) {
                //如果是最后一个Field，就接换行符，否则接空格
                rowString.append(fields[i].toString() + "\n");
            } else {
                rowString.append(fields[i].toString() + "\t");
            }
        }
        return rowString.toString();
    }
}
