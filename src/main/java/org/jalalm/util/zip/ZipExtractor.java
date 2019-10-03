package org.jalalm.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/** 
* 
* @author Jalal AlRukhaimi
*/

public class ZipExtractor {

    /**
     * Extract the zip file to the destination directory.
     * 
     * @param file zip file as File Object.
     * @param destDir The Destination directory to place the extracted file.
     * @throws java.io.FileNotFoundException thrown if the file doesn't exist.
     * @throws IOException thrown with any interruption or failure. 
     */
    public static void ExtractFile(File file, String destDir) throws FileNotFoundException, IOException{
        String BASE = destDir;
        byte[] buffer = new byte[1024];
        String extractedFileName;
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        Path extractedPath;
        
        for(ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()){	 
            extractedFileName = ze.getName();
            if(ze.isDirectory()){
                extractedPath = Paths.get(BASE + File.separator + extractedFileName);
                if (!Files.exists(extractedPath))
                    Files.createDirectory(extractedPath);
            }
            else {
                FileOutputStream fos = new FileOutputStream(BASE + File.separator + extractedFileName);
                for (int len = zis.read(buffer); len>0; len = zis.read(buffer))
                    fos.write(buffer, 0, len);
                fos.close();  
            }
            zis.closeEntry();
    	}
        zis.closeEntry();
    	zis.close();		
    }
    
    /**
     * @param path Zip file path as String.
     * @param destDir The Destination directory to place the extracted file.
     * @throws java.io.FileNotFoundException thrown if the file doesn't exist.
     * @throws IOException thrown with any interruption or failure.
     */
    public static void ExtractFile(String path, String destDir) throws FileNotFoundException, IOException{
        ExtractFile(new File(path), destDir);
    }
        
    /**
     * 
     * @param file zip file as File Object.
     * @throws java.io.FileNotFoundException thrown if the file doesn't exist.
     * @throws IOException thrown with any interruption or failure.
     */
    public static void ExtractFile(File file) throws FileNotFoundException, IOException{
        ExtractFile(file, file.getParent());	
    }
    
    /**
     * 
     * @param path Zip file path as String.
     * @throws java.io.FileNotFoundException thrown if the file doesn't exist.
     * @throws IOException thrown with any interruption or failure.
     */
    public static void ExtractFile(String path) throws FileNotFoundException, IOException{
        File file = new File(path);
        ExtractFile(file, file.getParent());
    }
    
    /**
     * Unzip all zip files in a directory and sub-directory.
     * 
     * @param dirPath Directory path as String
     * @param zipName Unzip .zip files that contain the string {zipName}
     * @param remove When set to boolean true, Removes the zip file after extraction. 
     * @throws java.io.FileNotFoundException thrown if the file doesn't exist.
     * @throws IOException thrown with any interruption or failure.
     */
    public static void ExtractFromDir(String dirPath, String zipName, boolean remove) throws FileNotFoundException, IOException{
        Stack<File> stack = new Stack<>();
        stack.push(new File(dirPath));
        
        while (!stack.empty()){
            File folder = stack.pop();
            for (File file : folder.listFiles()){
                if(file.isDirectory()){
                    stack.push(file);
                }
                else if(file.getName().toLowerCase().endsWith(".zip")){
                    if(file.getName().contains(zipName)){
                        ExtractFile(file);
                        if(remove)
                            file.delete();
                    }
                }
            }
        }
    }
    
}