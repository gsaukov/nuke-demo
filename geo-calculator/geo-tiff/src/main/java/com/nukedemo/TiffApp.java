package com.nukedemo;

import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.Rasters;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.List;


import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.TIFFEncodeParam;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;


public class TiffApp {

    public static void main( String[] args ) throws Exception {

//         based on: http://www.smartjava.org/content/access-information-geotiff-using-java/
        File tiffFile = new File("");
        // load tiff file to memory
        TIFFImage tiffImage = TiffReader.readTiff(tiffFile);
        List<FileDirectory> directories = tiffImage.getFileDirectories();
        FileDirectory directory = directories.get(0);
        Rasters rasters = directory.readRasters();


        doitJAI();

    }


    public static void doitJAI() throws IOException {
        FileSeekableStream ss = new FileSeekableStream("");
        ImageDecoder dec = ImageCodec.createImageDecoder("tiff", ss, null);
        int count = dec.getNumPages();
        TIFFEncodeParam param = new TIFFEncodeParam();
        param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
        param.setLittleEndian(false); // Intel
        System.out.println("This TIF has " + count + " image(s)");
        for (int i = 0; i < count; i++) {
            RenderedImage page = dec.decodeAsRenderedImage(i);
            File f = new File("single_" + i + ".tif");
            System.out.println("Saving " + f.getCanonicalPath());
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(page);
            pb.add(f.toString());
            pb.add("tiff");
            pb.add(param);
            RenderedOp r = JAI.create("filestore",pb);
            r.dispose();
        }
    }

}