package io.leopard.web.freemarker;

import java.io.IOException;
import java.io.InputStream;

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
public abstract class AbstractFileServlet extends HttpServlet implements IHtdocs {

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

	@Override
	public void doFile(HttpServletRequest request, HttpServletResponse response, String filename) throws ServletException, IOException {
		htdocs.doFile(request, response, filename);
	}

	@Override
	public long getExpires(String filename) {
		return htdocs.getExpires(filename);
	}

	@Override
	public InputStream readFile(HttpServletRequest request, String filename) throws IOException {
		return htdocs.readFile(request, filename);
	}

	@Override
	public byte[] toBytes(InputStream input) throws IOException {
		return htdocs.toBytes(input);
	}

}
