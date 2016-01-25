package io.leopard.web.freemarker;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import io.leopard.json.Json;
import io.leopard.web.freemarker.js.NgController;

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
		String json = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				json = this.doMethod(method, request, response);
				break;
			}
		}
		if (json == null) {
			return;
		}

		byte[] bytes = json.getBytes();
		response.setContentType("text/plain; charset=UTF-8");
		response.setContentLength(bytes.length);

		// response.setDateHeader("Expires", System.currentTimeMillis() + 1000 * 3600 * 24);
		// Flush byte array to servlet output stream.
		OutputStream out = response.getOutputStream();
		out.write(bytes);
		out.flush();
	}

	protected String doMethod(Method method, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("doMethod url:" + request.getRequestURL().toString());
		String[] names = CtClassUtil.getParameterNames(method);
		Class<?>[] types = method.getParameterTypes();
		Object[] args = new Object[names.length];
		for (int i = 0; i < names.length; i++) {
			args[i] = this.getParameter(request, names[i], types[i]);
		}

		NgController anno = method.getAnnotation(NgController.class);
		if (anno != null) {
			String javascript = this.doNgController(method, args);
			System.out.println("ng javascript:" + javascript);
			return javascript;
		}

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Object data = method.invoke(this, args);
			map.put("status", "success");
			map.put("data", data);
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("status", e.getClass().getSimpleName());
			map.put("data", e.getMessage());
		}
		String json = Json.toFormatJson(map);
		return json;
	}

	protected String doNgController(Method method, Object[] args) {
		Object data;
		try {
			data = method.invoke(this, args);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		String methodName = method.getName();
		String json = Json.toFormatJson(data);
		StringBuilder sb = new StringBuilder();
		sb.append("function " + methodName + "Ctrl($scope) {\n");
		sb.append("$scope.logs =");
		sb.append(json);
		sb.append(";}");
		return sb.toString();
	}

	protected Object getParameter(HttpServletRequest request, String name, Class<?> type) {
		String value = request.getParameter(name);
		if (String.class.equals(type)) {
			return value;
		}
		else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
			return Boolean.parseBoolean(value);
		}
		else if (int.class.equals(type) || Integer.class.equals(type)) {
			return Integer.parseInt(value);
		}
		else if (long.class.equals(type) || Long.class.equals(type)) {
			return Integer.parseInt(value);
		}
		throw new IllegalArgumentException("未知数据类型[" + type.getName() + "].");
	}

}
