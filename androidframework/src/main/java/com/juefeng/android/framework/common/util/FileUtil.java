package com.juefeng.android.framework.common.util;

import android.os.Environment;
import android.os.StatFs;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.application.BaseApplication;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/22
 * Time: 18:01
 * Description:
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * get available space
     *
     * @return byte unit kb
     */
    public static long getDiskAvailableSize() {
        if (!existsSdcard()) return 0;
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * judge whether exists sdcard
     *
     * @return
     */
    public static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    public static long getFileOrDirSize(File file) {
        if (!file.exists()) return 0;
        if (!file.isDirectory()) return file.length();

        long length = 0;
        File[] list = file.listFiles();
        if (list != null) {
            for (File item : list) {
                length += getFileOrDirSize(item);
            }
        }
        return length;
    }


    public static boolean copy(String fromPath, String toPath) {
        boolean result = false;
        File from = new File(fromPath);
        if (!from.exists()) {
            return result;
        }

        File toFile = new File(toPath);
        IOUtil.deleteFileOrDir(toFile);
        File toDir = toFile.getParentFile();
        if (toDir.exists() || toDir.mkdirs()) {
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(from);
                out = new FileOutputStream(toFile);
                IOUtil.copy(in, out);
                result = true;
            } catch (Throwable ex) {
                LogUtil.d(ex.getMessage(), ex);
                result = false;
            } finally {
                IOUtil.closeQuietly(in);
                IOUtil.closeQuietly(out);
            }
        }
        return result;
    }

    /**
     * read file content
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                stringBuilder.append(tempString);
            }
            reader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    /**
     * read inputstream content
     *
     * @param inputStream
     * @return
     */
    public static String readContentFromStream(InputStream inputStream) {
        if (inputStream == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String tempString = null;
            int count = -1;
            byte[] bytes = new byte[1024];
            while ((count = inputStream.read(bytes)) >= 0) {
                stringBuilder.append(new String(bytes, 0, count));
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

    /**
     * write content at file,don't append
     *
     * @param content
     * @param file
     */
    public static void writeFile(String content, File file) {
        FileWriter writer = null;
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            writer = new FileWriter(file, false);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
