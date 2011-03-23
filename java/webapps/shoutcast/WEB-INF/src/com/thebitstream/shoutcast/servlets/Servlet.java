
package com.thebitstream.shoutcast.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.thebitstream.shoutcast.Application;
/**
 * @author Andy Shaules
 *
 */
public class Servlet extends HttpServlet {

	/**
	 */
	private static final long serialVersionUID = 3868279185851041808L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp){
		ApplicationContext appCtx = (ApplicationContext) WebApplicationContextUtils.getWebApplicationContext(req
									.getSession().getServletContext());
		
		Application app = (Application) appCtx.getBean("web.handler");
		int viewers=app.getViewerCount();		
		
		try {
			resp.getOutputStream().write(("Number of viewers :"+ viewers).getBytes());
		} catch (IOException e) {

		}
		
		
	}
	
}
