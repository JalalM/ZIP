/*
 * Copyright 2019 Jalal.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jalalm.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/** 
* 
* @author Jalal AlRukhaimi
*/

public class ZipExtractor {

    
    private static String RestrictedChars(String name){
        if(System.getProperty("os.name").toLowerCase().startsWith("windows"))
            return name.replaceAll("[:\"<>|?*]", "_");
        return name;
    }
    
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
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        File extractedPath;
        
        for(ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()){
            extractedPath = new File(BASE + File.separator + RestrictedChars(ze.getName()));
            
            if(ze.isDirectory()){
                if (!extractedPath.exists())
                    extractedPath.mkdir();
            }
            else {
                FileOutputStream fos = new FileOutputStream(extractedPath);
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