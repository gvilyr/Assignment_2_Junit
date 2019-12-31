package systemTests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.DirectoryNotEmptyException;

import static org.junit.Assert.*;

public class FileSystemTest {

    private static FileSystem testedFileSystem;

    @Before
    //Create the fileSystem to use it for all tests
    public void createFileSystem(){
        testedFileSystem = new FileSystem(7);
    }

    // ############## testing dir function ############## //

    @Test
    //Test to check if the first name in path is not root
    public void rootNotFirst() {
        String [] pathDir = {"rootN","someDirectory"};
        try{
            testedFileSystem.dir(pathDir);
            Tree t = new Tree("rootN");
            t.GetChildByName("someDirectory");
            assertEquals(t,testedFileSystem.DirExists(pathDir));
        } catch (Exception e){
            assertEquals(e.getClass(),BadFileNameException.class);
        }
        try{
            testedFileSystem.rmdir(pathDir);
        }catch (Exception e){

        }
    }

    @Before
    //Create someDirectory to check that the function dirExits return the correct exception when we try to create
    //it again in the same path
    public void dir(){
        testedFileSystem = new FileSystem(7);
        String [] pathDir = {"root","someDirectory"};
        try{
            testedFileSystem.dir(pathDir);
        }catch (Exception e){
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
    }

    @Test
    //This code as a bag in the original code when we try to create two directories with the same
    //name, the bag return casting error and not what we excepted BadFileNameException
    //like we send in mail after you correct the bag the test will pass
    public void dirExits(){
        String [] pathDir = {"root","someDirectory"};
        try{
            testedFileSystem.dir(pathDir);
        }catch (Exception e){

        }
        String [] pathDir1 = {"root","someDirectory"};
        try{
            testedFileSystem.dir(pathDir1);
            Tree t = new Tree("root");
            t.GetChildByName("someDirectory");
            t = testedFileSystem.DirExists(pathDir);
            Tree temp = testedFileSystem.DirExists(pathDir1);
            assertEquals(t,testedFileSystem.DirExists(pathDir1));
        }catch (Exception e){
            assertEquals(e.getClass(),BadFileNameException.class);
        }
    }

    @After
    public void deleteDir(){
        String [] pathDir = {"root","someDirectory"};
        try{
            testedFileSystem.rmdir(pathDir);
        }catch (Exception e){

        }
    }



    //Create someDirectory to check that the function createDir return the correct path from lsdir function after
    //we create the directory
    @Test
    //Test if the function dir insert the directory if the first name path is root and the directory is not
    // exits already - we do it using ls function
    public void createDir() throws BadFileNameException{
        testedFileSystem = new FileSystem(7);
        String [] pathDir = {"root","someDirectory"};
        try{
            testedFileSystem.dir(pathDir);
        }catch (Exception e){
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        String [] root = {"root"};
        String [] excepted ={"someDirectory"};
        String [] pathReturned = testedFileSystem.lsdir(root);
        assertEquals(pathReturned,excepted);
    }

    // ##############  end testing dir function ############## //

    // ##############  testing disk function ############## //

    @Test
    //Insert file and directory to check the function diskReturnsNull
    //In this test we will check if after we insert directories and files, and after we remove some of them
    //this function return null in the correct place in the disk in this function we check if files write in the
    //insert order - so we check all what this function do.
    public void diskReturnsNull() {
        testedFileSystem = new FileSystem(7);
        String [] insert1 = {"root","Directory1","file1"};
        String [] insert2 = {"root","Directory1","file2"};
        String [] insert3 = {"root","Directory3","file0"};
        try {
            testedFileSystem.file(insert1,1);
            testedFileSystem.file(insert2,1);
            testedFileSystem.file(insert3,1);

        }catch(Exception e){
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        testedFileSystem.rmfile(insert2);
        String [][] excepted = new String[7][];
        excepted [0] = new String[]{"root", "Directory1", "file1"};
        excepted [1] = null;
        excepted [2] = new String[]{"root","Directory3","file0"};
        excepted [3] = null;
        excepted [4] = null;
        excepted [5] = null;
        excepted [6] = null;
        assertEquals(testedFileSystem.disk(),excepted);
    }

    // ##############  end testing disk function ############## //

    // ##############  testing file function ############## //

    //When we check the disk we check there that file created successfully and located correct place in disk
    //there we check also that if we are send path with directory that not exists it will create the directory too

    @Test
    //In this function we will check if we try to create file with same name of the directory we want to create inside
    //we expected to receive BadFileNameException
    public void CreteFileWithSameDirectoryName() {
        testedFileSystem = new FileSystem(7);
        String [] insert = {"root","file0","file0"};
        try {
            testedFileSystem.file(insert,2);
        }
        catch (Exception e){
            assertEquals(e.getClass(),BadFileNameException.class);
        }
    }

    @Test
    //In function CreateFileNoSpaceInDisk1 we will check if we try to create file that is size is bigger then file system,
    //and in function CreateFileNoSpaceInDisk2 we try to create file there are no place for is size in the disk,
    //both, we will receive OutOfSpaceException
    public void CreateFileNoSpaceInDisk1(){
        testedFileSystem = new FileSystem(7);
        String [] insert = {"root", "Directory1", "bigFile"};
        try {
            testedFileSystem.file(insert,10);
        }catch (Exception e){
            assertEquals(e.getClass(),OutOfSpaceException.class);
        }
    }

    @Test
    public void CreateFileNoSpaceInDisk2(){
        testedFileSystem = new FileSystem(7);
        String [] insert = {"root", "Directory1", "bigFile"};
        String [] insert1 = {"root", "file0"};
        String [] insert2 = {"root", "Directory2","file"};

        try {
            testedFileSystem.file(insert,3);
            testedFileSystem.file(insert1,3);
            testedFileSystem.file(insert2,2);
        }catch (Exception e){
            assertEquals(e.getClass(),OutOfSpaceException.class);
        }
    }

    @Test
    //In this function we will check if we receive BadFileNameException when we try to create file where the in the
    //path there are no root in first place
    public void CreateFileNoNameRoot(){
        testedFileSystem = new FileSystem(7);
        String [] insert = {"rootN", "Directory1", "file"};
        try {
            testedFileSystem.file(insert,1);
        }catch (Exception e){
            assertEquals(e.getClass(),BadFileNameException.class);
        }
    }

    @Test
    //In this test we check that if we insert file in same name like file already
    //exists and there are enough place for this file
    public void CreateFileOnOldFile(){
        FileSystem testedFileSystem2 = new FileSystem(7);
        String [] insert1 = {"root", "Directory1", "file"};
        try {
            testedFileSystem2.file(insert1,5);
            testedFileSystem2.file(insert1,3);
        }catch (Exception e){
            assertEquals(BadFileNameException.class, e.getClass());
        }
        String [][] excepted = new String[7][];
        excepted [0] = new String[]{"root", "Directory1", "file"};
        excepted [1] = null;
        excepted [2] = null;
        excepted [3] = null;
        excepted [4] = null;
        excepted [5] = new String[]{"root", "Directory1", "file"};
        excepted [6] = new String[]{"root", "Directory1", "file"};
        assertEquals(testedFileSystem2.disk(),excepted);
    }

    @Test
    //In this function there is a bag when we try to create large file the function receive null pointer
    //exception
    //In this test we check that if we insert file in same name like file already
    //exists and there are no enough place for this file this function will return OutOfSpaceException
    public void createBigFileOnOlderFile(){
        testedFileSystem = new FileSystem(7);
        String [] insert = {"root","file"};
        try {
            testedFileSystem.file(insert,6);
        }catch (Exception e){
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        try {
            testedFileSystem.file(insert,8);
        }catch (Exception e){
            assertEquals(OutOfSpaceException.class,e.getClass());
        }
        String [][] expected = new String[7][];
        expected [0] = new String[]{"root", "file"};
        expected [1] = new String[]{"root", "file"};
        expected [2] = new String[]{"root", "file"};
        expected [3] = new String[]{"root", "file"};
        expected [4] = new String[]{"root", "file"};
        expected [5] = new String[]{"root", "file"};
        expected [6] = null;
        assertNotEquals(testedFileSystem.disk(),expected);
    }

    // ##############  end testing file function ############## //


    // ##############  testing lsdir function ############## //


    @Test
    //In this function we will check if we get null when we try to send to lsdir function name of directory does'nt
    // exits.
    public void checkLsdirNoSuchDirectory() {
        testedFileSystem = new FileSystem(7);
        String [] insert = {"root", "someDirectory","file0"};
        try{
            testedFileSystem.file(insert,6);
        }catch (Exception e){
//            assertEquals(OutOfSpaceException.class,e.getClass());
            assertEquals(BadFileNameException.class, e.getClass());

        }
        String [] directoryToCheck = {"root", "noSuch"};
        String [] expected = testedFileSystem.lsdir(directoryToCheck);
        assertEquals(null,expected);
    }


    @Test
    //In this function we will check if we get the correct sub directories and files located in some directory
    public void correctFileUnderDirectory(){
        testedFileSystem = new FileSystem(7);
        String [] insert1 = {"root", "someDirectory", "someDirectory2","file0"};
        String [] insert2 = {"root", "someDirectory", "someDirectory3"};
        try{
            testedFileSystem.file(insert1,2);
            testedFileSystem.dir(insert2);
        }catch (Exception e){
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        String [] directoryToCheck = {"root", "someDirectory"};
        String [] expected = {"someDirectory2","someDirectory3"};
        String [] lsdirReturn = testedFileSystem.lsdir(directoryToCheck);
        assertEquals(lsdirReturn,expected);
    }

    // ##############  end testing lsdir function ############## //

    // ##############  testing rmfile function ############## //

    @Test
    //In this function we will check if we try to remove file that not exits the function do nothing and
    //not return some error
    public void removeFileNotExits() {
        testedFileSystem = new FileSystem(7);
        String [] insert = {"root", "file0"};
        try{
            testedFileSystem.file(insert,3);
        }catch (Exception e){
            assertEquals(BadFileNameException.class, e.getClass());
        }
        String [] fileToRemove = {"root","file1"};
        testedFileSystem.rmfile(fileToRemove);
        String [][] expected = new String[7][];
        expected [0] = new String[]{"root", "file0"};
        expected [1] = new String[]{"root", "file0"};
        expected [2] = new String[]{"root", "file0"};
        expected [3] = null;
        expected [4] = null;
        expected [5] = null;
        expected [6] = null;
        assertEquals(testedFileSystem.disk(),expected);
    }

    // ##############  end testing rmfile function ############## //


    // ##############  testing rmdir function ############## //


    @Test
    //In this function we will check if we get DirectoryNotEmptyException when we try to remove directory with files
    // inside.
    public void removeDirWithFiles() {
        testedFileSystem=new FileSystem(7);
        String [] insert = {"root", "Directory", "file"};
        try {
            testedFileSystem.file(insert,3);
        }catch (Exception e){
            assertEquals(BadFileNameException.class, e.getClass());
        }
        String [] dirToRemove = {"root", "Directory"};
        try {
            testedFileSystem.rmdir(dirToRemove);
            assertEquals(null, testedFileSystem.DirExists(dirToRemove));
        }catch (Exception e){
            assertEquals(e.getClass(), DirectoryNotEmptyException.class);
            assertFalse(testedFileSystem.lsdir(dirToRemove).length == 0);
        }
    }

    @Test
    //In this function we will check if the rmdir remove the dir without errors.
    //because directories does'nt occupied blocks in disk we can not check the disk after removing
    //so we decide to check if we get an BadFileNameException when we try to insert some file to the directory we removed
    public void removeDir(){
        testedFileSystem = new FileSystem(7);
        String [] insert = {"root", "Directory"};
        try {
            testedFileSystem.dir(insert);
        }catch (Exception e){
            assertEquals(IndexOutOfBoundsException.class, e.getClass());

        }
        try {
            testedFileSystem.rmdir(insert);
        }catch (Exception e){

        }
        String [] insertFile ={"root", "Directory", "file"};
        try {
            testedFileSystem.file(insertFile,4);
        }catch (Exception e){
            assertEquals(e.getClass(),BadFileNameException.class);
        }
    }

    @Test
    //Here we check if there are no exception when we try to remove directory that not exits.
    //function supposed do nothing
    public void removeNotExitsDirectory(){
        testedFileSystem = new FileSystem(7);
        String [] directoryToRemove = {"root", "Dir"};
        try {
            testedFileSystem.rmdir(directoryToRemove);
        }catch (Exception e){
            System.out.println(e.toString());
        }
        //To check if actual this dir not exits
        assertEquals(testedFileSystem.DirExists(directoryToRemove),null);
    }


    // ##############  end testing rmdir function ############## //


    // ##############  testing fileExits function ############## //


    @Test
    //Here we check if we get null when we try to search file that not exits
    public void fileExistsIfNotExits() {
        testedFileSystem = new FileSystem(7);
        String [] fileNotExits = {"root", "file"};
        assertEquals(testedFileSystem.FileExists(fileNotExits),null);
    }

    @Test
    //We check if we get the correct leaf from fileExits function when we try to create same file name in different
    //size and in different path
    public void fileExitsIfExits(){
        testedFileSystem = new FileSystem(7);
        String [] insertNotCorrect = {"root", "Directory1", "file"};
        String [] insertCorrect = {"root", "Directory2", "file"};
        try {
            testedFileSystem.file(insertNotCorrect,2);
            testedFileSystem.file(insertCorrect,4);
        }catch (Exception e){
            assertEquals(BadFileNameException.class, e.getClass());
        }
        try {
            Leaf expected = new Leaf("file", 4);
            assertEquals(testedFileSystem.FileExists(insertCorrect),expected);
        }catch (Exception e){

        }
    }

    // ##############  end testing fileExits function ############## //

    // ##############  testing dirExits function ############## //



    @Test
    //In this test we will check if we get null when we search directory that not exists
    public void dirExistsNotExits() {
        testedFileSystem = new FileSystem(7);
        String [] dirNotExists = {"root", "someDirectory"};
        assertEquals(testedFileSystem.DirExists(dirNotExists),null);
    }

    @Test
    //We check if we get the correct Tree from dirExits function when we try to create same dir name in different
    //path
    public void dirExistsIfExits(){
        testedFileSystem = new FileSystem(7);
        String [] insertNotCorrect = {"root", "someDir"};
        String [] insertCorrect = {"root" , "someDir1", "someDir"};
        try{
            testedFileSystem.dir(insertNotCorrect);
            testedFileSystem.dir(insertCorrect);
        }catch (Exception e){
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        assertEquals(testedFileSystem.DirExists(insertNotCorrect).name,"someDir");
    }
}