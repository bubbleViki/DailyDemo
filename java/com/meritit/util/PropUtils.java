 package com.meritit.util;
 
 import java.io.IOException;
 import java.io.InputStream;
 import java.util.Properties;
 /**
  * 属性文件操作通用类
  * @author Lenovo
  *
  */
 public class PropUtils
 {
   private static Properties prop = new Properties();
 
   public static Properties getProp(String propName) { 
	 InputStream resourceAsStream = PropUtils.class.getClassLoader().getResourceAsStream(propName);
     try {
       prop.load(resourceAsStream);
     } catch (IOException e) {
       e.printStackTrace();
     }
     return prop; 
    }
 
   public static String getClassesPath()
   {
     String path = PropUtils.class.getClassLoader().getResource("").getPath();
     return path;
   }
 }
