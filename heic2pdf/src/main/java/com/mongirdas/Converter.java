/*
 * Created Date: Saturday, September 16th 2023, 4:30:33 pm
 * Author: Ben Mongirdas
 * 
 * Description: Class that handles conversion of files
 * 
 * Copyright (c) 2023 Your Company
 */


package com.mongirdas;

import java.io.File;
import java.io.FileNotFoundException;
//import java.io.file;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;


public class Converter {
    private int numberOfFiles;
    //Runtime for executing imagemagick commands
    Runtime runtime = Runtime.getRuntime();


    private List<File> ConvertToPDF(File[] files, boolean deleteState){
        List<File> newFiles= new ArrayList<File>();

        for (File file: files){
            Process process = null;
            try {
                process = Runtime.getRuntime().exec("magick -compress JPEG -quality 55 " + file.getAbsolutePath() + " " + file.getAbsolutePath().replace(".HEIC", ".PDF"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            newFiles.add(new File(file.getAbsolutePath().replace(".HEIC", ".PDF")));
            if (deleteState){
                file.delete();
            }
        }
        System.out.println("Finished Conversion to PDF");
        return newFiles;
    }

    //Main conversion function
    public void ConvertHEICToPdf(File[] files, boolean deleteState){
        List<File> pdfFiles;
        pdfFiles = ConvertToPDF(files, deleteState);

        PDFMergerUtility util = new PDFMergerUtility();
        for (File file : pdfFiles){
            try {
                util.addSource(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        util.setDestinationFileName(pdfFiles.get(0).getAbsolutePath().replace(pdfFiles.get(0).getName(), "converted.PDF"));
        try {
            util.mergeDocuments(IOUtils.createMemoryOnlyStreamCache());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (File file : pdfFiles){
            file.delete();
        }
    }
}
