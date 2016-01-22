package io.leopard.web.freemarker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

public abstract class AbstractMappingHttpServlet extends AbstractHttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String method = request.getParameter("method");
			if (StringUtils.isEmpty(method)) {
				this.doService(request, response);
			}
			else {
				this.doMethod(method, request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			output(response, e.getMessage());
		}
	}

	protected void doMethod(String methodName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Method[] methods = this.getClass().getDeclaredMethods();
		if (methods == null) {
			return;
		}
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				try {
					method.invoke(this, request, response);
				}
				catch (IllegalAccessException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
				catch (InvocationTargetException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
				break;
			}
		}
	}

}
