package com.christ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 自定义用户加载器
 *
 * @author 史偕成
 * @date 2023/07/18 16:51
 **/
public class CustomClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try {
            byte[] classFromCustomPath = getClassFromCustomPath(name);
            return defineClass(name, classFromCustomPath, 0, classFromCustomPath.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new ClassNotFoundException(name);
    }


    private byte[] getClassFromCustomPath(String name) throws IOException {
        // 自定义路径中加载制定类
        // 如果防止泄漏 这里还需要进行加密处理
        return Files.readAllBytes(Paths.get("/Users/sxc/Documents/java/mg/support/report-forms/report-forms-domain/target/" + name + ".jar"));
    }


    public static void main(String[] args) {
        System.gc();
        CustomClassLoader customClassLoader = new CustomClassLoader();

        try {
            Class<?> clazz = Class.forName("report-forms-domain", true, customClassLoader);
            Object o = clazz.getDeclaredConstructor().newInstance();
            System.out.println(o.getClass().getClassLoader());
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
