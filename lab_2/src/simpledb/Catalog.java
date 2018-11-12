package simpledb;

import sun.security.krb5.internal.rcache.DflCache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The Catalog keeps track of all available tables in the database and their
 * associated schemas.
 * For now, this is a stub catalog that must be populated with tables by a
 * user program before it can be used -- eventually, this should be converted
 * to a catalog that reads a catalog table from disk.
 */

public class Catalog {

    /**
     * Constructor.
     * Creates a new, empty catalog.
     */
    // HashMap是key与value的映射，put和get方法,
    private HashMap<String, Table> nameToTable;  // 根据名字找表
    private HashMap<Integer, Table> idToTable;  // 根据id找表
    private HashMap<Integer, DbFile> idToFile;  // 根据id找file
    private HashMap<Integer, String> idToName;  // 根据id找name
    private HashMap<String, Integer> nameToId;  // 根据name找id

    public Catalog() {
        // some code goes here
        // 构造函数，实现两个table标记变量
        nameToTable = new HashMap<String, Table>();
        idToTable = new HashMap<Integer, Table>();
        idToFile = new HashMap<Integer, DbFile>();
        idToName = new HashMap<Integer, String>();
        nameToId = new HashMap<String, Integer>();

    }

    public class Table {
        // 创建table结构
        DbFile file;
        String name;
        String pkeyField;

        public Table(DbFile f, String s, String pkey) {
            this.file = f;
            this.name = s;
            this.pkeyField = pkey;

        }
    }

    /**
     * Add a new table to the catalog.
     * This table's contents are stored in the specified DbFile.
     *
     * @param file the contents of the table to add;  file.getId() is the identfier of
     *             this file/tupledesc param for the calls getTupleDesc and getFile
     * @param name the name of the table -- may be an empty string.  May not be null.  If a name
     *             conflict exists, use the last table to be added as the table for a given name.
     */
    public void addTable(DbFile file, String name, String pkey) {
        // some code goes here
        // pkey the name of the primary key field
        /**
         * 将file添加进name(table名字)中
         * table name 不为空，冲突时用最新的表作为操作对象
         */
        if (name == null || pkey == null) {
            throw new IllegalArgumentException();
        }

        /*if (nameToId.containsKey(name)) {  // 添加这个限制，当重名时，会运行不下去
            throw new UnsupportedOperationException("不支持重名");
        }*/

        Table _table = new Table(file, name, pkey);
        int tableId = file.getId();
        // 映射赋值
        nameToTable.put(name, _table);
        idToTable.put(file.getId(), _table);
        idToName.put(tableId, name);
        idToFile.put(tableId, file);
        nameToId.put(name, tableId);
    }

    public void addTable(DbFile file, String name) {
        // 多态
        if (name == null) {
            throw new IllegalArgumentException();
        }

        addTable(file, name, "");
    }

    /**
     * Add a new table to the catalog.
     * This table has tuples formatted using the specified TupleDesc and its
     * contents are stored in the specified DbFile.
     * @param file the contents of the table to add;  file.getId() is the identfier of
     *    this file/tupledesc param for the calls getTupleDesc and getFile
     * @param t the format of tuples that are being added
     */
    /*public void addTable(DbFile file) {
        addTable(file, "");
    }*/

    /**
     * Return the id of the table with a specified name,
     *
     * @throws NoSuchElementException if the table doesn't exist
     */
    public int getTableId(String name) throws NoSuchFieldException {
        // some code goes here
        if (name == null || !nameToId.containsKey(name)) {
            throw new NoSuchFieldException();
        }
        return nameToId.get(name);  // 根据name查找id
    }

    public String getTableName(int id){
        // 根据id返回name
        return idToName.get(id);
    }

    /**
     * Returns the tuple descriptor (schema) of the specified table
     *
     * @param tableid The id of the table, as specified by the DbFile.getId()
     *                function passed to addTable
     */
    public TupleDesc getTupleDesc(int tableid) throws NoSuchElementException {
        // some code goes here
        if (!isIdValid(tableid, idToFile)) {
            throw new NoSuchElementException();
        }
        return idToFile.get(tableid).getTupleDesc();
    }

    /**
     * Returns the DbFile that can be used to read the contents of the
     * specified table.
     *
     * @param tableid The id of the table, as specified by the DbFile.getId()
     *                function passed to addTable
     */
    public DbFile getDbFile(int tableid) throws NoSuchElementException {
        // some code goes here
        if (!isIdValid(tableid, idToTable)) {
            throw new NoSuchElementException();
        }

        // read the contents of the specified table.读取特定表的内容
        // 返回文件DbFile，包含对页的操作
        return idToFile.get(tableid);

    }

    private boolean isIdValid(int tableId, HashMap<?, ?> map) {
        // 检查该key是否存在
        return map.containsKey(tableId);
    }


    /**
     * Delete all tables from the catalog
     */
    public void clear() {
        // some code goes here
        idToTable.clear();
        nameToTable.clear();
        idToFile.clear();
        idToName.clear();
        nameToId.clear();
    }

    /**
     * Reads the schema from a file and creates the appropriate tables in the database.
     *
     * @param catalogFile
     */
    public void loadSchema(String catalogFile) {
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(catalogFile)));

            while ((line = br.readLine()) != null) {
                //assume line is of the format name (field type, field type, ...)
                String name = line.substring(0, line.indexOf("(")).trim();
                //System.out.println("TABLE NAME: " + name);
                String fields = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim();
                String[] els = fields.split(",");
                ArrayList<String> names = new ArrayList<String>();
                ArrayList<Type> types = new ArrayList<Type>();
                for (String e : els) {
                    String[] els2 = e.trim().split(" ");
                    names.add(els2[0].trim());
                    if (els2[1].trim().toLowerCase().equals("int"))
                        types.add(Type.INT_TYPE);
                    else if (els2[1].trim().toLowerCase().equals("string"))
                        types.add(Type.STRING_TYPE);
                    else {
                        System.out.println("Unknown type " + els2[1]);
                        System.exit(0);
                    }
                }
                Type[] typeAr = types.toArray(new Type[0]);
                String[] namesAr = names.toArray(new String[0]);
                TupleDesc t = new TupleDesc(typeAr, namesAr);
                HeapFile tabHf = new HeapFile(new File(name + ".dat"), t);
                addTable(tabHf, name);
                System.out.println("Added table : " + name + " with schema " + t);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid catalog entry : " + line);
            System.exit(0);
        }
    }
}
