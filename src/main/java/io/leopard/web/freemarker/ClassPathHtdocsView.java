package io.leopard.web.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import io.leopard.web.freemarker.htdocs.ClassPathHtdocs;

public class ClassPathHtdocsView extends ModelAndView {

	public ClassPathHtdocsView(String folder) {
		super(createView(folder));
	}

	protected static View createView(String folder) {
		HtdocsView view = new HtdocsView(folder);
		return view;
	}

	public static class HtdocsView extends ClassPathHtdocs implements View {

		private String folder;

		private ResourceLoader resourceLoader = new DefaultResourceLoader();

		@Override
		public InputStream readFile(HttpServletRequest request, String filename) throws IOException {
			String path = "/" + this.getHtdocsPath() + filename;
			Resource resource = resourceLoader.getResource(path);
			if (resource == null || !resource.exists()) {
				throw new FileNotFoundException(path);
			}
			return resource.getInputStream();
		}

		public HtdocsView(String folder) {
			this.folder = folder;
		}

		@Override
		public String getContentType() {
			return "text/html; charset=UTF-8";
		}

		@Override
		public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
			String filename = request.getRequestURI();
			this.doFile(request, response, filename);
		}

		@Override
		public String getHtdocsPath() {
			return folder;
		}

	}
}
