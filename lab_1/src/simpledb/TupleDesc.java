package simpledb;

import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc {

    /**
     * TupleDesc的构造只需给出ftnAr和numFields
     */
    private FieldTypeName[] ftnAr;  // field结构数组,还没实现

    private int numFields; //fields的数量

    /**
     * field type and name 结构
     */
    public static class FieldTypeName {
        /**
         * field type and name
         * field有Type类型的数据
         */
        Type fieldType;

        String fieldName;

        public FieldTypeName(Type type, String name) {
            this.fieldName = name;
            this.fieldType = type;
        }
    }


    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields
     * fields, with the first td1.numFields coming from td1 and the remaining
     * from td2.
     *
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc combine(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        FieldTypeName[] fieldTypeNames_1 = td1.ftnAr;
        FieldTypeName[] fieldTypeNames_2 = td2.ftnAr;

        int length_1 = fieldTypeNames_1.length;  //fields数量
        int length_2 = fieldTypeNames_2.length;

        // combine数组的实现，包含length_1 + length_2个field
        FieldTypeName[] _combine = new FieldTypeName[length_1 + length_2];

        // 将两个TupleDesc的内容添加到_combine中
        /**
         * public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
         * 代码解释:
         * 　　Object src : 原数组
         *    int srcPos : 从元数据的起始位置开始
         * 　　Object dest : 目标数组
         * 　　int destPos : 目标数组的开始起始位置
         * 　　int length  : 要copy的数组的长度
         */
        System.arraycopy(fieldTypeNames_1, 0, _combine, 0, length_1);
        System.arraycopy(fieldTypeNames_2, 0, _combine, length_1, length_2);

        return new TupleDesc(_combine);
    }

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr  array specifying the number of and types of fields in
     *                this TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {  // 构造函数
        // some code goes here
        if (typeAr.length == 0) {
            throw new IllegalArgumentException("TupleDesc must contain at least one entry");
        }

        numFields = typeAr.length;

        ftnAr = new FieldTypeName[numFields]; // 数组大小为numFields

        for (int i = 0; i < numFields; i++) {  // 初始化ftnAr数组
            ftnAr[i] = new FieldTypeName(typeAr[i], fieldAr[i]);
        }


    }

    /**
     * Constructor.
     * Create a new tuple desc with typeAr.length fields with fields of the
     * specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr array specifying the number of and types of fields in
     *               this TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        // 构造函数

        this(typeAr, new String[typeAr.length]);  // 调用上边的构造函数，String[] fieldAr为一个有长度的空数组

    }

    public TupleDesc(FieldTypeName[] fieldTypeNames) {
        // 判断数组不为空
        if (fieldTypeNames == null || fieldTypeNames.length == 0) {
            throw new IllegalArgumentException("fieldTypeNames不为空");
        }
        this.ftnAr = fieldTypeNames;
        this.numFields = fieldTypeNames.length;
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return this.numFields;
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
        if (i < 0 || i >= numFields()) {
            throw new NoSuchElementException("无效索引");
        }
        return ftnAr[i].fieldName;
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
        // 给定名称，找到下表索引
        if (name == null) {
            throw new NoSuchElementException();
        }
        // 顺序遍历
        String field_name;
        for (int i = 0; i < ftnAr.length; i++) {
            field_name = ftnAr[i].fieldName;  // 要判定field_name != null
            if (field_name != null && field_name.equals(name)) {
                return i;
            }
        }
        throw new NoSuchElementException();  // 当不返回i要报错，这里代替了return 0
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
        // Gets the type of the ith field of this TupleDesc.
        if (i < 0 || i >= numFields()) {
            throw new NoSuchElementException("无效索引");
        }
        return ftnAr[i].fieldType;
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     * Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int size = 0;
        for (FieldTypeName ftn : ftnAr) {  //ftn为ftnAR中的项
            size += ftn.fieldType.getLen();
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
    public boolean equals(Object o) {  // o是要与this比较的对象
        // some code goes here
        /**
         * 要求fiedl数量相同，且包含的名字name和类型type都相同，返回true
         */
        if (this == o) {
            return true;
        }
        if (o instanceof TupleDesc) {  // 对象o是否是TupleDesc类或者它的子类的一个实例
            TupleDesc otherTupleDesc = (TupleDesc) o;  // otherTupleDesc是TupleDesc的实例，指向o
            if (!(otherTupleDesc.numFields() == this.numFields())) {  // 判定Fields数量一致否
                return false;
            }
            /**
             * 判断tuple内容是否相同
             */
            for (int i = 0; i < numFields(); i++) {
                if (ftnAr[i].equals(otherTupleDesc.ftnAr[i])) {
                    /** 不是递归判断，调用了
                     *  public boolean equals(Object obj) {
                     *         return (this == obj);
                     *     }
                     */
                    return false;
                }
            }
            return true; // 上边的判定都不错

        } else return false;

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
        StringBuffer _print = new StringBuffer();

        for (FieldTypeName ftn : ftnAr) {  // 打印格式
            _print.append(ftn.toString() + ", ");
        }

        return _print.toString();
    }
}
