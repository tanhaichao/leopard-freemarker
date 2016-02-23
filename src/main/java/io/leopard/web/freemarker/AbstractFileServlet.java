package io.leopard.web.freemarker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 资源文件访问.
 * 
 * @author 阿海
 */
public abstract class AbstractFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public abstract String getHtdocsPath();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getParameter("f");

		this.doFile(request, response, filename);
	}

	protected void doFile(HttpServletRequest request, HttpServletResponse response, String filename) throws ServletException, IOException {
		if (!isValidFilename(filename)) {
			throw new IllegalArgumentException("非法文件名[" + filename + "].");
		}

		String contentType = parseContentType(filename);
		InputStream input = getRealAsInputStream(request, filename);
		if (input == null) {
			String filename2 = getHtdocsPath() + filename;
			input = AbstractFileServlet.class.getResourceAsStream(filename2);
			if (input == null) {
				throw new NullPointerException("文件[" + filename2 + "]不存在.");
			}
		}

		byte[] bytes = toBytes(input);
		response.setContentType(contentType);
		response.setContentLength(bytes.length);

		response.setDateHeader("Expires", System.currentTimeMillis() + 1000 * 3600 * 24);
		OutputStream out = response.getOutputStream();
		out.write(bytes);
		out.flush();
	}

	protected InputStream getRealAsInputStream(HttpServletRequest request, String filename) {
		String path = request.getServletContext().getRealPath(filename);
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		if (!file.isFile()) {
			return null;
		}
		try {
			return new FileInputStream(path);
		}
		catch (FileNotFoundException e) {
			return null;
		}
	}

	protected byte[] toBytes(InputStream input) throws IOException {
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int n = 0;
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	private static Pattern FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-_/\\.]+\\.(css|jpg|png|js|eot|html|json)$");

	/**
	 * 文件名称合法性判断.
	 * 
	 * @param filename
	 * @return
	 */
	protected static boolean isValidFilename(String filename) {
		if (filename == null || filename.length() == 0) {
			throw new NullPointerException("文件名不能为空.");
		}
		if (filename.indexOf("..") != -1) {
			throw new IllegalArgumentException("文件名称不能包含'..'");
		}
		Matcher m = FILENAME_PATTERN.matcher(filename);
		return m.matches();
	}

	protected static String parseContentType(String filename) {
		if (filename.endsWith(".css")) {
			return "text/css";
		}
		else if (filename.endsWith(".png")) {
			return "image/png";
		}
		else if (filename.endsWith(".jpg")) {
			return "image/jpg";
		}
		else if (filename.endsWith(".js")) {
			return "application/javascript";
		}
		else if (filename.endsWith(".eot")) {
			return "application/javascript";
		}
		else if (filename.endsWith(".json")) {
			return "application/json";
		}
		else if (filename.endsWith(".html")) {
			return "text/html";
		}
		throw new IllegalArgumentException("未知文件类型[" + filename + "].");
	}

}
