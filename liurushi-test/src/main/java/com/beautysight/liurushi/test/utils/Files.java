/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.utils;

import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        return ImageIO.read(new File(pathOfFileInClassPath(file)));
    }

    public static File fileInSameDirWith(Class<?> clazz, String file) {
        return new File(clazz.getResource(file).getPath());
    }

    public static List<File> filesInSameDirWith(Class<?> clazz, String[] files) {
        List<File> fileList = new ArrayList<>(4);

        if (ObjectUtils.isEmpty(files)) {
            return fileList;
        }

        for (String aFile : files) {
            fileList.add(fileInSameDirWith(clazz, aFile));
        }

        return fileList;
    }

    public static File fileInClassPath(String file) {
        return new File(pathOfFileInClassPath(file));
    }

    private static String pathOfFileInClassPath(String file) {
        return Files.class.getClassLoader().getResource(file).getPath();
    }

}
