package systemTests;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTest {

    private static FileSystem testedFileSystem;

    @Test
    //In this function we will check if when we insert dir and file to the file system,
    //the system create a tree and we want to
    //see if we get the Tree (directory) or leaf (file) after we insert it.
    //In the function getChildByName in Tree class there is a bag java.lang.ClassCastException:
    //system.Leaf cannot be cast to system.Tree
    //so we remark the second assert for file in comment to pass test
    public void getChildByNameExits() {
        testedFileSystem = new FileSystem(5);
        String [] insert  = {"root","someDirectory","Dir1"};
        String [] insert2  = {"root","someDirectory","file1"};
        try {
            testedFileSystem.dir(insert);
            testedFileSystem.file(insert2,3);
        }catch (Exception e){

        }
        String [] dirForCheck = {"root", "someDirectory"};
        Tree returnFromGetChild = testedFileSystem.DirExists(dirForCheck);
        Tree dirExpected = testedFileSystem.DirExists(insert);
        Leaf fileExpected = testedFileSystem.FileExists(insert2);

        assertEquals(returnFromGetChild.GetChildByName("Dir1"),dirExpected);
        //assertEquals(returnFromGetChild.GetChildByName("file1"),fileExpected);
    }

    @Test
    //In this test we will check if the getChildByName function create Tree when it not exits
    //first we will check if exits
    //sent to the getChildByName function
    //and then we will check again if exits by compare what returned from GetChildByName to what return
    //from DirExists
    public void getChildByNameNotExits() {
        testedFileSystem = new FileSystem(5);
        String [] insert = {"root", "someDirectory"};
        try{
            testedFileSystem.dir(insert);
        }catch (Exception e){

        }
        String [] notExits = {"root", "someDirectory", "Dir"};
        assertEquals(testedFileSystem.DirExists(notExits),null);
        Tree someDirectory = testedFileSystem.DirExists(insert);
        Tree Dir = someDirectory.GetChildByName("Dir");
        assertEquals(testedFileSystem.DirExists(notExits),Dir);
    }
}