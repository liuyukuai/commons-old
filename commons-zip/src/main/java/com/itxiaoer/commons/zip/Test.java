//package com.itxiaoer.commons.zip;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Test {
//    public static List<File> getFilesOfZipAndRar(String zipPath) throws IOException {
//        String destPath = zipPath.substring(0, zipPath.lastIndexOf(".")) + "/";
////先将该压缩文件解压
//        if (zipPath.contains(".zip"))
//            unZipFile(new File(zipPath), destPath);
//        if (zipPath.contains(".rar"))
//            unRarFile(new File(zipPath), destPath);
////进入解压目录，将该目录的所有zip都解压
//        recursiveCompressedFile(new File(destPath));
////得到该目录下的所有文件
//        Iterator iterator = Directory.walk(destPath).iterator();
//        List<File> files = new ArrayList<File>();
//        File file = null;
//        while (iterator.hasNext()) {
//            file = (File) iterator.next();
//            if (file.getName().contains(".rar"))
//                continue;
//            files.add(file);
//        }
//        return files;
//    }
//
//
//    public static void recursiveCompressedFile(File file) throws IOException
//
//
//    {
////广度优先遍历
//        for (File e : file.listFiles()) {
//            String filePath = e.getAbsolutePath().replace("\\\\", "/");
//            //深度优先遍历
//            if (e.getName().contains(".zip")) {
//                String desString = filePath.substring(0, filePath.lastIndexOf(".")) + "/";
//                unZipFile(e, desString);
//                e.delete();
//                recursiveCompressedFile(new File(desString));
//            }
//            if (e.getName().contains(".rar")) {
//                String desString = filePath.substring(0, filePath.lastIndexOf(".")) + "/";
//                unRarFile(e, desString);
//                e.delete();
//                recursiveCompressedFile(new File(desString));
//            }
//            if (e.isDirectory())
//                recursiveCompressedFile(e);
//        }
//    }
//}
