
package com.thebitstream.shoutcast.servlets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
public class Player extends HttpServlet {

	/**
	 */
	private static final long serialVersionUID = 3868279185851041808L;
	
	private int port=1935;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp){
					
		InetAddress addr;
		
		try {
			
			ApplicationContext appCtx = (ApplicationContext) WebApplicationContextUtils.getWebApplicationContext(req
					.getSession().getServletContext());

			Application app = (Application) appCtx.getBean("web.handler");
			
			port=app.getPlaybackPort();
			
			
			addr = InetAddress.getLocalHost();
			String hostIP=addr.getHostAddress();			
			
			String page=	write("Shoutcast",hostIP,String.valueOf(port),"640","500");
			
			
			resp.getOutputStream().write((page).getBytes());
			
			
		} catch (UnknownHostException e) {
			
			
		} catch (IOException e) {
			
			
		}	
		
			
		
	}
	
	public static String write(String file,String hostIP, String port,String width,String height)
	{	
		
		//lazy :|
		String page="	<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""+
"    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"+
"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">"+
"<head>"+
"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
"<meta name=\"viewport\" content=\"width=device-width\" />"+
""+
"<title>Play</title>"+
"<style type=\"text/css\">"+
"<!--"+
"body {"+
"	text-align : center;"+
"	background-color: #680000;"+
"	font-family: sans-serif;"+
"	font-size: 0.9em;"+
"}"+
""+
".code {"+
"	font-family: monospace;"+
"	font-size: 130%;"+
"}"+
""+
"a {"+
"	color: red;"+
"	background: white;"+
"	text-decoration: none;"+
"}"+
""+
"#information {"+
"	padding-top: 1em;"+
"	padding-bottom: 1em;"+
"}"+
""+
".spiffy {"+
"	display: block"+
"}"+
""+
".spiffy * {"+
"	display: block;"+
"	height: 1px;"+
"	overflow: hidden;"+
"	font-size: .01em;"+
"	background: #fff"+
"}"+
""+
".spiffy1 {"+
"	margin-left: 3px;"+
"	margin-right: 3px;"+
"	padding-left: 1px;"+
"	padding-right: 1px;"+
"	border-left: 1px solid #b60600;"+
"	border-right: 1px solid #b60600;"+
"	background: #df0b00"+
"}"+
""+
".spiffy2 {"+
"	margin-left: 1px;"+
"	margin-right: 1px;"+
"	padding-right: 1px;"+
"	padding-left: 1px;"+
"	border-left: 1px solid #8c0100;"+
"	border-right: 1px solid #8c0100;"+
"	background: #e60c00"+
"}"+
""+
".spiffy3 {"+
"	margin-left: 1px;"+
"	margin-right: 1px;"+
"	border-left: 1px solid #e60c00;"+
"	border-right: 1px solid #e60c00;"+
"}"+
""+
".spiffy4 {"+
"	border-left: 1px solid #b60600;"+
"	border-right: 1px solid #b60600"+
"}"+
""+
".spiffy5 {"+
"	border-left: 1px solid #df0b00;"+
"	border-right: 1px solid #df0b00;"+
"}"+
""+
".spiffyfg {"+
"	padding: 1em;"+
"	background-color: #fff;"+
"	overflow:hidden;"+
"}"+
"-->"+
"</style>"+
""+
"</head>"+
"<body>"+
"<div><b class=\"spiffy\"> <b class=\"spiffy1\"><b></b></b> <b"+
"	class=\"spiffy2\"><b></b></b> <b class=\"spiffy3\"></b> <b class=\"spiffy4\"></b>"+
"<b class=\"spiffy5\"></b></b>"+

"<div class=\"spiffyfg\">"+
""+
""+
""+
"           <object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" width=\""+width+"\" height=\""+height+"\" id=\""+file+"\">"+
"               <param name=\"movie\" value=\""+file+".swf\" />"+
"               <param name=\"quality\" value=\"high\" />"+
"               <param name=\"bgcolor\" value=\"#ffffff\" />"+
"               <param name=\"allowScriptAccess\" value=\"sameDomain\" />"+
"               <param name=\"FlashVars\" value=\"ip="+hostIP+"&port="+port+"\" />"+
"               <param name=\"allowFullScreen\" value=\"true\" />"+
"               <!--[if !IE]>-->"+
"               <object type=\"application/x-shockwave-flash\" data=\""+file+".swf\" width=\""+width+"\" height=\""+height+"\">"+
"                   <param name=\"quality\" value=\"high\" />"+
"                   <param name=\"bgcolor\" value=\"#ffffff\" />"+
"                   <param name=\"allowScriptAccess\" value=\"sameDomain\" />"+
"                   <param name=\"FlashVars\" value=\"ip="+hostIP+"&port="+port+"\"/>"+
"                   <param name=\"allowFullScreen\" value=\"true\" />"+
"               <!--<![endif]-->"+
"               <!--[if gte IE 6]>-->"+
"               	<p> "+
"               		Either scripts and active content are not permitted to run or Adobe Flash Player version"+
"               		10.0.0 or greater is not installed."+
"          	</p>"+
"               <!--<![endif]-->"+
"                   <a href=\"http://www.adobe.com/go/getflashplayer\">"+
"                       <img src=\"http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif\" alt=\"Get Adobe Flash Player\" />"+
"                   </a>"+
"               <!--[if !IE]>-->"+
"               </object>"+
"               <!--<![endif]-->"+
"           </object>"+
"</div>"+

"<b class=\"spiffy\"> <b class=\"spiffy5\"></b> <b class=\"spiffy4\"></b> "+
"<b class=\"spiffy3\"></b> <b class=\"spiffy2\"><b></b></b> <b class=\"spiffy1\"><b></b></b></b>"+
"</div>"+
"</body>"+
"</html>";
		return page;
}
	
}
