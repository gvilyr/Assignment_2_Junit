package system;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileSystemTest {

    private static FileSystem testedFileSystem;

    @BeforeClass
    //Create the fileSystem for use it for all tests
    public static void createFileSystem(){
        testedFileSystem = new FileSystem(7);
    }

    // ############## testing dir function ############## //

    @Test
    //Test to check if the first name in path is not root
    public void rootNotFirst() throws BadFileNameException{
        String [] pathDir = {"rootN","someDirectory"};
        try{
            testedFileSystem.dir(pathDir);
        }catch (Exception e){
            assertEquals(e.getClass(),BadFileNameException.class);
        }
    }

    @Before
    //Create someDirectory to check that the function dirExits return the correct exception when we try to create
    //it again in the same path
    public void dir() throws BadFileNameException{
        String [] pathDir = {"root","someDirectory"};
        try{
            testedFileSystem.dir(pathDir);
        }catch (Exception e){

        }
    }

    @Test
    //This code as a bag in the original code when we try to create two directories with the same
    //name, the bag return casting error and not what we excepted BadFileNameException
    //like we send in mail after you correct the bag the test will pass
    public void dirExits()throws BadFileNameException{
        String [] pathDir = {"root","someDirectory"};
        try{
            testedFileSystem.dir(pathDir);
        }catch (Exception e){
            assertEquals(e.getClass(),BadFileNameException.class);
        }
    }

    @Before
    //Create someDirectory to check that the function createDir return the correct path from lsdir function after
    //we create the directory
    public void dirAgain() throws BadFileNameException{
        String [] pathDir = {"root","someDirectory"};
        try{
            testedFileSystem.dir(pathDir);
        }catch (Exception e){

        }
    }

    @Test
    //Test if the function dir insert the directory if the first name path is root and the directory is not
    // exits already - we do it using ls function
    public void createDir() throws BadFileNameException{
        String [] root = {"root"};
        String [] excepted ={"someDirectory"};
        String [] pathReturned = testedFileSystem.lsdir(root);
        assertEquals(pathReturned,excepted);
    }

    // ##############  end testing dir function ############## //


    @Test
    //Insert file and directory to check the function diskReturnsNull
    public void insertDirectoriesAndFiles() throws BadFileNameException {
        String [] insert1 = {"root","Directory1","file1"};
        String [] insert2 = {"root","Directory1","file2"};
        String [] insert3 = {"root","Directory2"};
        String [] insert4 = {"root","Directory3","file0"};
        String [] insert5 = {"root","Direc"};
        try {
            testedFileSystem.dir(insert3);
            testedFileSystem.file(insert1,1);
            testedFileSystem.file(insert2,1);
            testedFileSystem.file(insert4,1);
            testedFileSystem.dir(insert5);
        }catch(Exception e){
        }
        String [][] actual =testedFileSystem.disk();
        for (int i = 0; i < actual.length; i++) {
            System.out.print(i);
            if (actual[i] == null) {
                System.out.println(" null");
            } else {
                for (int j = 0; j < actual[i].length; j++){
                    System.out.print(" " + actual[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    @Test
    //In this test we will check if after we insert directories and files, and after that we remove some of them
    //this function return null in the correct place in the disk
    public void diskReturnsNull() {
        String [] insert2 = {"root","Directory1","file2"};
        testedFileSystem.rmfile(insert2);
        String [][] excepted = new String[7][];
        excepted [0] = new String[]{"root", "Directory1", "file1"};
        excepted [1] = null;
        excepted [2] = new String[]{"root","Directory3","file0"};
        excepted [3] = null;
        excepted [4] = null;
        excepted [5] = null;
        excepted [6] = null;
        String [][] actual =testedFileSystem.disk();
        for (int i = 0; i < actual.length; i++) {
            System.out.print(i);
            if (actual[i] == null) {
                System.out.println(" null");
            } else {
                for (int j = 0; j < actual[i].length; j++){
                    System.out.print(" " + actual[i][j] + " ");
                }
                System.out.println();
            }
        }
        for (int i = 0; i < excepted.length; i++) {
            System.out.print(i);
            if (excepted[i] == null) {
                System.out.println(" null");
            } else {
                for (int j = 0; j < excepted[i].length; j++){
                    System.out.print(" " + excepted[i][j] + " ");
                }
                System.out.println();
            }
        }
        assertEquals(testedFileSystem.disk(),excepted);
    }

    @Test
    public void file() {
    }

    @Test
    public void lsdir() {
    }

    @Test
    public void rmfile() {
    }

    @Test
    public void rmdir() {
    }

    @Test
    public void fileExists() {
    }

    @Test
    public void dirExists() {
    }
}