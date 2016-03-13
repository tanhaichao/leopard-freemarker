package io.leopard.web.freemarker;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class ClassPathModelAndView extends ModelAndView {

	public ClassPathModelAndView(String viewName) {
		super(createView(viewName));
	}

	protected static View createView(String viewName) {
		FtlView view = new FtlView("ftl", viewName);
		return view;
	}
}
