
package com.thebitstream.shoutcast.servlets;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.red5.server.plugin.shoutcast.stream.NSVConsumer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.thebitstream.shoutcast.Application;
/**
 * @author Andy Shaules
 *
 */
public class Playlist extends HttpServlet {

	/**
	 */
	private static final long serialVersionUID = 3868279185851041808L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp){

		try {
			
		ApplicationContext appCtx = (ApplicationContext) WebApplicationContextUtils.getWebApplicationContext(req
									.getSession().getServletContext());
		
		Application app = (Application) appCtx.getBean("web.handler");


		Iterator<NSVConsumer>streams= app.getStreams().iterator();	
		String hostIP="localhost";
		InetAddress addr;
		
		addr = InetAddress.getLocalHost();		
		
		
		hostIP=addr.getHostAddress();
		int port=1935;		
		String xml = "<rss version=\"2.0\" xmlns:jwplayer=\"http://developer.longtailvideo.com/\">\n"; 
		xml = xml + "<channel>\n";
		xml = xml + "<title>Red5 SHOUTcast playlist</title>\n";
		while(streams.hasNext())
		{
			NSVConsumer stream=streams.next();
			String id=stream.getMarshal().getStream().getPublishedName();
			xml = xml + "\t<item>\n";
			xml = xml +"\t\t<title>"+id+"</title>\n";
			xml = xml +"\t\t<jwplayer:file>"+id+"</jwplayer:file>\n";
			xml = xml +"\t\t<jwplayer:streamer>rtmp://"+hostIP+":"+String.valueOf(port)+"/shoutcast/</jwplayer:streamer>\n";
			xml = xml + "\t</item>\n";
		}
		xml = xml + "</channel>\n</rss>\r\n";
		
		
		
			resp.getOutputStream().write(xml.getBytes());
		} catch (IOException e) {
			
			
		}
		
	}
	
}
