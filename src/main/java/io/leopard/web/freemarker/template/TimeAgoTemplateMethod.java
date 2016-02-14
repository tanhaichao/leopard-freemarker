package io.leopard.web.freemarker.template;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.TemplateModelException;

/**
 * 前段时间.
 * 
 * @author 阿海
 *
 */

public class TimeAgoTemplateMethod extends AbstractTemplateMethod {

	@Override
	public Object exec(HttpServletRequest request, Object... args) throws TemplateModelException {
		Date time = (Date) args[0];
		return time.getTime();
	}

	@Override
	public String getKey() {
		return "timeAgo";
	}

}
