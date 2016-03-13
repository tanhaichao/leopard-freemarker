package io.leopard.web.freemarker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import io.leopard.web.freemarker.htdocs.ClassPathHtdocs;
import io.leopard.web.freemarker.htdocs.IHtdocs;

/**
 * 资源文件访问.
 * 
 * @author 阿海
 */
public abstract class AbstractFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public abstract String getHtdocsPath();

	private IHtdocs htdocs = new ClassPathHtdocs() {
		@Override
		public String getHtdocsPath() {
			return AbstractFileServlet.this.getHtdocsPath();
		}
	};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getParameter("f");
		if (StringUtils.isEmpty(filename)) {
			throw new IllegalArgumentException("文件名不能为空.");
		}
		htdocs.doFile(request, response, filename);
	}

	// @Override
	// public void doFile(HttpServletRequest request, HttpServletResponse response, String filename) throws ServletException, IOException {
	// if (!isValidFilename(filename)) {
	// throw new IllegalArgumentException("非法文件名[" + filename + "].");
	// }
	//
	// String contentType = parseContentType(filename);
	// InputStream input = readFile(request, filename);
	// byte[] bytes = toBytes(input);
	// input.close();
	// response.setContentType(contentType);
	// response.setContentLength(bytes.length);
	//
	// long expires = this.getExpires(filename);
	// response.setDateHeader("Expires", expires);
	//
	// OutputStream out = response.getOutputStream();
	// out.write(bytes);
	// out.flush();
	// }
	//
	// @Override
	// public long getExpires(String filename) {
	// if (SystemUtils.IS_OS_WINDOWS) {
	// return -1;
	// }
	// else {
	// return System.currentTimeMillis() + 1000 * 3600 * 1;
	// }
	// }
	//
	// protected InputStream readFile(HttpServletRequest request, String filename) {
	// InputStream input = getRealAsInputStream(request, filename);
	// if (input == null) {
	// String filename2 = getHtdocsPath() + filename;
	// input = AbstractFileServlet.class.getResourceAsStream(filename2);
	// if (input == null) {
	// throw new NullPointerException("文件[" + filename2 + "]不存在.");
	// }
	// }
	// return input;
	//
	// }
	//
	// protected InputStream getRealAsInputStream(HttpServletRequest request, String filename) {
	// String path = request.getServletContext().getRealPath(filename);
	// File file = new File(path);
	// if (!file.exists()) {
	// return null;
	// }
	// if (!file.isFile()) {
	// return null;
	// }
	// try {
	// return new FileInputStream(path);
	// }
	// catch (FileNotFoundException e) {
	// return null;
	// }
	// }
	//
	// protected byte[] toBytes(InputStream input) throws IOException {
	// byte[] buffer = new byte[1024];
	// ByteArrayOutputStream output = new ByteArrayOutputStream();
	// int n = 0;
	// while ((n = input.read(buffer)) != -1) {
	// output.write(buffer, 0, n);
	// }
	// return output.toByteArray();
	// }
	//
	// private static Pattern FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-_/\\.]+\\.(css|jpg|png|js|eot|html|json|woff|woff2|ttf|svg|md)$");
	//
	// /**
	// * 文件名称合法性判断.
	// *
	// * @param filename
	// * @return
	// */
	// protected boolean isValidFilename(String filename) {
	// if (filename == null || filename.length() == 0) {
	// throw new NullPointerException("文件名不能为空.");
	// }
	// if (filename.indexOf("..") != -1) {
	// throw new IllegalArgumentException("文件名称不能包含'..'");
	// }
	// Matcher m = FILENAME_PATTERN.matcher(filename);
	// return m.matches();
	// }
	//
	// protected String parseContentType(String filename) {
	// if (filename.endsWith(".css")) {
	// return "text/css";
	// }
	// else if (filename.endsWith(".png")) {
	// return "image/png";
	// }
	// else if (filename.endsWith(".jpg")) {
	// return "image/jpg";
	// }
	// else if (filename.endsWith(".js")) {
	// return "application/javascript";
	// }
	// else if (filename.endsWith(".eot")) {
	// return "application/javascript";
	// }
	// else if (filename.endsWith(".woff")) {
	// return "application/javascript";
	// }
	// else if (filename.endsWith(".woff2")) {
	// return "application/javascript";
	// }
	// else if (filename.endsWith(".ttf")) {
	// return "application/javascript";
	// }
	// else if (filename.endsWith(".svg")) {
	// return "application/javascript";
	// }
	// else if (filename.endsWith(".json")) {
	// return "application/json";
	// }
	// else if (filename.endsWith(".md")) {
	// return "text/plain";
	// }
	// else if (filename.endsWith(".html")) {
	// return "text/html";
	// }
	// throw new IllegalArgumentException("未知文件类型[" + filename + "].");
	// }

}
