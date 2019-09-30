package org.jalalm.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {

    /**
     * @param file
     * @param destDir
     * @throws IOException
     */
    public static void ExtractFile(File file, String destDir) throws IOException{
        String BASE = destDir;
        byte[] buffer = new byte[1024];
        String extractedFileName;
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
    	ZipEntry ze ;
        Path extractedPath;
        
        while((ze = zis.getNextEntry()) != null){	 
            extractedFileName = ze.getName();
            if(ze.isDirectory()){
                extractedPath = Paths.get(BASE + File.separator + extractedFileName);
                if (!Files.exists(extractedPath))
                    Files.createDirectory(extractedPath);
            }
            else {
                FileOutputStream fos = new FileOutputStream(BASE + File.separator + extractedFileName);
                int len;
                while ((len = zis.read(buffer)) > 0)
                    fos.write(buffer, 0, len);
                fos.close();  
            }
            zis.closeEntry();
    	}
        zis.closeEntry();
    	zis.close();		
    }
    
    public static void ExtractFile(String path, String destDir) throws IOException{
        ExtractFile(new File(path), destDir);
    }
        
    public static void ExtractFile(File file) throws IOException{
        ExtractFile(file, file.getParent());	
    }
    
    public static void ExtractFile(String path) throws IOException{
        File file = new File(path);
        ExtractFile(file, file.getParent());
    }
    
    public static void ExtractFromDir(String dirPath, String zipName, boolean remove) throws IOException{
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