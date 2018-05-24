package com.sinyd.lnram.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.sinyd.platform.utiltools.util.StringUtil;

public class FileUtil {
	/**
	 * 写文件操作
	 * 
	 * @param InputStream
	 *            文件流
	 * @param String
	 *            文件名
	 * @return String 文件路径
	 * @author zd
	 * @throws IOException
	 */
	public static void saveFile(InputStream fileStream, String fileName,
			String filePath) throws IOException {

		File dir = new File(filePath);

		if (!dir.exists()) {
			dir.mkdirs(); // modified by lvq
		}

		FileOutputStream fout = new FileOutputStream(new File(filePath
				+ fileName));

		BufferedInputStream bis = new BufferedInputStream(fileStream);
		byte[] cacheArray = new byte[2048];
		int size;
		while ((size = bis.read(cacheArray)) != -1) {
			fout.write(cacheArray, 0, size);
		}

		fout.close();
		bis.close();
		return;
	}

	/**
	 * 写文件, 并将进度保存进session中, key为 attach_id
	 * 
	 * @param fileStream
	 * @param fileName
	 * @param filePath
	 * @param request
	 * @param fileSize
	 * @param attach_id
	 * @author lvq
	 */
	public static void saveFile(InputStream fileStream, String fileName,
			String filePath, HttpServletRequest request, long fileSize,
			String attach_id) {
		File dir = new File(filePath);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		FileOutputStream fout = null;
		BufferedInputStream bis = null;

		try {
			fout = new FileOutputStream(new File(filePath + fileName));
			bis = new BufferedInputStream(fileStream);

			byte[] cacheArray = new byte[2048];
			int size;
			long total_size = 0l;
			int percent = 0;

			while ((size = bis.read(cacheArray)) != -1) {
				total_size += size;
				percent = (int) (total_size * 100 / fileSize);
				request.getSession().setAttribute(attach_id, percent);

				fout.write(cacheArray, 0, size);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fout) {
					fout.close();
				}
				if (null != bis) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件大小转换, long -> k,m,g...
	 * 
	 * @param filesize
	 * @return
	 * @author lvq
	 */
	public static String convertFileSize(long filesize) {
		String strUnit = "Bytes";
		String strAfterComma = "";
		int intDivisor = 1;

		if (filesize >= 1024 * 1024) {
			strUnit = "MB";
			intDivisor = 1024 * 1024;
		} else if (filesize >= 1024) {
			strUnit = "KB";
			intDivisor = 1024;
		}

		if (intDivisor == 1) {
			return filesize + " " + strUnit;
		}

		strAfterComma = "" + 100 * (filesize % intDivisor) / intDivisor;

		if (StringUtil.isBlank(strAfterComma)) {
			strAfterComma = ".0";
		}

		return filesize / intDivisor + "." + strAfterComma + " " + strUnit;
	}
}
