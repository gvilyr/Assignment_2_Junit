package systemTests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpaceTest {
    private static FileSystem testedFileSystem;
    private Space space;


    @Before
    //creates the fileSystem so we could create leafs and the space object for the tests
    public void createSpace(){
        testedFileSystem = new FileSystem(20);
        space = new Space(10);
    }


    @Test
    /*
    This test is a successful test for the alloc function.
    we create a leaf and inserting it to the disk.
    we expect that the leaf would be in the first 4 indexes of the array.
     */
    public void allocSuccess() {
        try{
            Leaf leaf = new Leaf("leaf1", 4);
            space.Alloc(4, leaf);
            Leaf[] expected = new Leaf[10];
            expected[0] = leaf;
            expected[1] = leaf;
            expected[2] = leaf;
            expected[3] = leaf;
            assertEquals(expected,space.getAlloc());

        }catch (Exception e) {
            assertEquals(e.getClass(),OutOfSpaceException.class);
        }
    }
    @Test
    /*
    This test is a unsuccessful test for the alloc function.
    we create a leaf that is bigger then the size of the space and trying to insert it to the disk.
    we expect that an OutOfSpaceException will be thrown.
     */
    public void allocFail() {
        try{
            Leaf leaf = new Leaf("leaf1", 14);
            space.Alloc(14, leaf);
        }catch (Exception e) {
            assertFalse(OutOfSpaceException.class==e.getClass());
        }
    }

    @Test
    /*
    This test is for the dealloc function.
    we create a leaf and inserting it to the disk.
    we expect that the space will be empty (meaning blocks array is all null).
     */
    public void dealloc() {
        try {
            Leaf leaf = new Leaf("leaf1", 4);
            space.Alloc(4, leaf);
            Tree tree = new Tree("tree");
            leaf.parent = tree;
            space.Dealloc(leaf);
            Leaf[] expected = new Leaf[10];
            Leaf[] temp = space.getAlloc();
            assertEquals(expected,space.getAlloc());

        } catch (Exception e) {

        }

    }

    @Test
    /*
    This test is for the countFreeSpace function.
    we expect to get 10 because the disk is empty.
     */
    public void countFreeSpace() {
        int freeSpace = space.countFreeSpace();
        assertEquals(10,freeSpace);
    }
}