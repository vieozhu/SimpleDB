package simpledb;

import static org.junit.Assert.assertEquals;

import java.util.NoSuchElementException;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import simpledb.TestUtil.SkeletonFile;
import simpledb.systemtest.SimpleDbTestBase;

public class CatalogTest extends SimpleDbTestBase {
    private static String name = "test";

    @Before
    public void addTables() throws Exception {
        Database.getCatalog().clear();
        Database.getCatalog().addTable(new SkeletonFile(-1, Utility.getTupleDesc(2)), "");
        Database.getCatalog().addTable(new SkeletonFile(-2, Utility.getTupleDesc(2)), name);
    }

    /**
     * Unit test for Catalog.getTupleDesc()
     */
    @Test
    public void getTupleDesc() throws Exception {
        TupleDesc expected = Utility.getTupleDesc(2);
        TupleDesc actual = Database.getCatalog().getTupleDesc(-1);

        assertEquals(expected, actual);
    }

    /**
     * Unit test for Catalog.getTableId()
     */
    @Test
    public void getTableId() throws NoSuchFieldException {
        assertEquals(-2, Database.getCatalog().getTableId(name));
        assertEquals(-1, Database.getCatalog().getTableId(""));

        try {
            Database.getCatalog().getTableId(null);
            Assert.fail("Should not find table with null name");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            Database.getCatalog().getTableId("foo");
            Assert.fail("Should not find table with name foo");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unit test for Catalog.getDbFile()
     */
    @Test
    public void getDbFile() throws Exception {
        DbFile f = Database.getCatalog().getDbFile(-1);

        // NOTE(ghuo): we try not to dig too deeply into the DbFile API here; we
        // rely on HeapFileTest for that. perform some basic checks.
        assertEquals(-1, f.getId());
    }

    /**
     * JUnit suite target
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CatalogTest.class);
    }
}

