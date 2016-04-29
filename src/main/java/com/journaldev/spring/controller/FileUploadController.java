package com.journaldev.spring.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		System.out.println("[LOG]FileUploadController->index");
		return "upload";
	}
	@RequestMapping(value=("/uploadFile"),headers=("content-type=application/x-www-form-urlencoded"),method=RequestMethod.POST)
	public String uploadFileHandler2(HttpServletRequest request)
			throws UnsupportedEncodingException {
		System.out.println("[LOG]FileUploadController->test");
		return "upload";

	}
	/**
	 * Upload single file using Spring Controller
	 *
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value=("/uploadFile"),headers=("content-type=multipart/*"),method=RequestMethod.POST)
	public String uploadFileHandler(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file)
			throws UnsupportedEncodingException {
		System.out.println("[LOG]FileUploadController->uploadFile");
		request.setCharacterEncoding("UTF-8");
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				this.checkEncoding(file.getOriginalFilename());

				System.out.println("[LOG]FileUploadController->uploadFile  ->" + bytes.length);
				System.out.println("[LOG]FileUploadController->uploadFile  ->" + bytes.length);
				System.out.println("[LOG]FileUploadController->uploadFile  ->" + bytes.length);
				System.out.println("[LOG]FileUploadController->uploadFile  ->" + bytes.length);
			} catch (Exception e) {
			}
		}

		//application/x-www-form-urlencoded
		return "upload";

	}


	public void checkEncoding(String src) throws UnsupportedEncodingException {
		System.out.println("[LOG]" + src);
		byte[] bytes4FilenameU8 = src.getBytes("UTF-8");
		byte[] bytes4FilenameB5 = src.getBytes("BIG5");
		byte[] bytes4FilenameISO = src.getBytes("ISO-8859-1");
		byte[] bytes4FilenameGBK = src.getBytes("GBK");
		System.out.println("bytes4FilenameU8----------------------------");
		System.out.println("[LOG]" + new String(bytes4FilenameU8, "ISO-8859-1"));
		System.out.println("[LOG]" + new String(bytes4FilenameU8, "BIG5"));
		System.out.println("[LOG]" + new String(bytes4FilenameU8, "GBK"));
		System.out.println("[LOG]" + new String(bytes4FilenameU8, "UTF-8"));
		System.out.println("bytes4FilenameB5----------------------------");
		System.out.println("[LOG]" + new String(bytes4FilenameB5, "ISO-8859-1"));
		System.out.println("[LOG]" + new String(bytes4FilenameB5, "BIG5"));
		System.out.println("[LOG]" + new String(bytes4FilenameB5, "GBK"));
		System.out.println("[LOG]" + new String(bytes4FilenameB5, "UTF-8"));
		System.out.println("bytes4FilenameISO----------------------------");
		System.out.println("[LOG]" + new String(bytes4FilenameISO, "ISO-8859-1"));
		System.out.println("[LOG]" + new String(bytes4FilenameISO, "BIG5"));
		System.out.println("[LOG]" + new String(bytes4FilenameISO, "GBK"));
		System.out.println("[LOG]" + new String(bytes4FilenameISO, "UTF-8"));
		System.out.println("bytes4FilenameGBK----------------------------");
		System.out.println("[LOG]" + new String(bytes4FilenameGBK, "ISO-8859-1"));
		System.out.println("[LOG]" + new String(bytes4FilenameGBK, "BIG5"));
		System.out.println("[LOG]" + new String(bytes4FilenameGBK, "GBK"));
		System.out.println("[LOG]" + new String(bytes4FilenameGBK, "UTF-8"));
	}

	/**
	 * Upload multiple file using Spring Controller
	 */
	@RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
	public @ResponseBody String uploadMultipleFileHandler(@RequestParam("name") String[] names, @RequestParam("file") MultipartFile[] files) {

		if (files.length != names.length)
			return "Mandatory information missing";

		String message = "";
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String name = names[i];
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location=" + serverFile.getAbsolutePath());

				message = message + "You successfully uploaded file=" + name + "<br />";
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		}
		return message;
	}
}
