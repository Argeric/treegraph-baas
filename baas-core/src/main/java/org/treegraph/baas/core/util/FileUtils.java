package org.treegraph.baas.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.treegraph.baas.core.constant.SplitterConstant;

public class FileUtils {

	public static void unZip(File srcFile, String destDirPath) {
		if (!srcFile.exists()) {
			throw new RuntimeException(srcFile.getPath() + " not found");
		}
		
		try (ZipFile zipFile = new ZipFile(srcFile)){
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				System.out.println("解压" + entry.getName());
				
				if (entry.isDirectory()) {
					String dirPath = destDirPath + SplitterConstant.FILE_SEP + entry.getName();
					File dir = new File(dirPath);
					dir.mkdirs();
				} 
				
				if (!entry.isDirectory()) {
					File targetFile = new File(destDirPath + SplitterConstant.FILE_SEP + entry.getName());
					if (!targetFile.getParentFile().exists()) {
						targetFile.getParentFile().mkdirs();
					}
					targetFile.createNewFile();
					try (InputStream is = zipFile.getInputStream(entry);
							FileOutputStream fos = new FileOutputStream(targetFile)) {
						int len = -1;
						byte[] buf = new byte[1024];
						while ((len = is.read(buf)) != -1) {
							fos.write(buf, 0, len);
						}
					}
				}
			}
		} catch(Exception e) {
			throw new RuntimeException("unzip file error!", e);
		}
	}
	
	public static String readFile(String path) {
		StringBuffer str = new StringBuffer();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
			String line = null;
			while ((line = in.readLine()) != null) {
				str.append(line).append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			throw new RuntimeException("read file error!", e);
		}
		return str.toString();
    }
}
