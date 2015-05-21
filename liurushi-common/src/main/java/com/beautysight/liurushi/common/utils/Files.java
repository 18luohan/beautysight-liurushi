/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public class Files {

    public static byte[] toBytes(String file) throws IOException {
        FileInputStream input = null;
        ByteArrayOutputStream byteOut = null;

        try {
            byte[] byteBuf = new byte[1000];
            //String filePath = Files.class.getResource(file).getPath();
            String filePath = Files.class.getClassLoader().getResource(file).getPath();
            input = new FileInputStream(filePath);
            byteOut = new ByteArrayOutputStream();

            int c;
            while ((c = input.read(byteBuf)) != -1) {
                byteOut.write(byteBuf, 0, c);
            }
            return byteOut.toByteArray();
        } finally {
            if (input != null) input.close();
            if (byteOut != null) byteOut.close();
        }
    }

    public static BufferedImage from(String file) throws IOException {
        return ImageIO.read(new File(pathOfFilePlacedInClassPath(file)));
    }

    private static String pathOfFilePlacedInClassPath(String file) {
        return Files.class.getClassLoader().getResource(file).getPath();
    }

    public static File filePlacedInClassPath(String file) {
        return new File(pathOfFilePlacedInClassPath(file));
    }

}
