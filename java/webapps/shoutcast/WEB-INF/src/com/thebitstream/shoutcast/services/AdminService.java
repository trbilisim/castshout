package com.thebitstream.shoutcast.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.plugin.Shoutcast;
import org.red5.server.plugin.shoutcast.stream.NSVConsumer;



public class AdminService 
{

	private IScope app;
	private List<NSVConsumer> streams;
	private String pass;

	public AdminService(IScope scope,List<NSVConsumer> strms,String pss)
	{
		System.out.println("pass :"+pss);
		pass=pss;
		app=scope;
		streams=strms;
	}
	public Map<String,Object> getData()
	{
		Map<String,Object> ret = new HashMap<String,Object>();
		
		ArrayList<HashMap<String, Object>> servs= new ArrayList<HashMap<String,Object>>();
		
		Iterator<NSVConsumer> itt=streams.iterator();
		
		while(itt.hasNext())
		{
			NSVConsumer c=itt.next();
			HashMap<String,Object> strm = new HashMap<String,Object>();
			long byteR= c.getMarshal().getStream().getBytesReceived();
			int time=c.getMarshal().getStream().getCurrentTimestamp();
			strm.put("bytes", String.valueOf(byteR));
			strm.put("time", String.valueOf(time));
			strm.put("name", c.getMarshal().getStream().getPublishedName());
			servs.add(strm );
		}
		
		ArrayList<HashMap<String, Object>> clnts= new ArrayList<HashMap<String,Object>>();
		
		Iterator<Set<IConnection>> cons = app.getConnections().iterator();
		
		
		while(cons.hasNext())
		{
			HashMap<String,Object> client = new HashMap<String,Object>();
			
			IConnection connection=cons.next().iterator().next();
			
			String id = connection.getClient().getId();
			String ip = connection.getRemoteAddress();
			long din=connection.getReadBytes();
			long dout=connection.getWrittenBytes();	
			String playing="";
			if(connection.hasAttribute("playing") )
			{
				playing=  connection.getAttribute("playing").toString();
			}
			
			long bTotal=din + dout;
			client.put("id", id);
			client.put("ip", ip);
			client.put("bytes", String.valueOf(bTotal));
			client.put("playing", playing);
			clnts.add(client);
		}
		ret.put("streams",servs );
		ret.put("clients",clnts );
		return ret;
	}
	public String login(Map<Object,Object> data)
	{
		if( !data.containsKey("admin"))
		{
			return "";
		}

		if( !data.get("admin").toString().equals(pass))
			return "";

		IConnection conn=Red5.getConnectionLocal();		
		conn.setAttribute(PlayBackHandler.Playing, "Admin");
		
		return "Admin";
	}
	public String kick(Map<Object,Object> data)
	{		
		if( !data.containsKey("admin"))
		{
			return "";
		}

		if( !data.get("admin").toString().equals(pass))
			return "";
		
		if( !data.containsKey("client"))
			return "client";		
		
		
		Iterator<Set<IConnection>> cons = app.getConnections().iterator();
		int ret=0;
		while(cons.hasNext())
		{
			
			IConnection connection=cons.next().iterator().next();
			
			String id = connection.getClient().getId();
			String ip = connection.getRemoteAddress();
			if(id.equals(data.get("client")) || ip.equals(data.get("client")))
			{
				connection.close();
				ret++;
			}
			
		}		
		
		return String.valueOf(ret);
	}
	public String stop(Map<Object,Object> data)
	{
		if( !data.containsKey("admin"))
		{
			return "admin";
		}

		if( !data.get("admin").toString().equals(pass))
			return "";

		
		Iterator<NSVConsumer> itt=streams.iterator();
		while(itt.hasNext())
		{
			NSVConsumer c=itt.next();
			if(c.getMarshal().getStream().getPublishedName().equals(data.get("name")))
			{
				c.stop();
				streams.remove(c);
				break;
			}
		}
		return "OK";
	}
	public String start(Map<String,Object> data)
	{
		if( !data.containsKey("admin"))
		{
			return "";
		}

		if( !data.get("admin").toString().equals(pass))
			return "pass";		

		Iterator<NSVConsumer> itt=streams.iterator();
		while(itt.hasNext())
		{
			NSVConsumer c=itt.next();

			if(c.getMarshal().getStream().getPublishedName().equals(data.get("name").toString()))
			{
				return "match_name";
			}

			if(c.getMode() == 1 && data.get("clientMode").toString().equals("false"))
			{
				int p=Integer.parseInt(data.get("port").toString());

				if(c.getPort() == p  || c.getPort() == p - 1)
				{
					return "match_port";
				}
			}
		}

		if (Boolean.parseBoolean( data.get("clientMode").toString()))
		{			
			if(!data.containsKey("metaCharset"))
			{
				data.put("metaCharset", "UTF-8");
			}
			streams.add(Shoutcast.openExternalURI(app, data.get("name").toString(),data.get("url").toString(),data.get("metaCharset").toString()));
		} 
		else 
		{
			streams.add(Shoutcast.openServerPort(app,data.get("name").toString(), Integer.parseInt(data.get("port").toString()),
					data.get("password").toString()));
		}
		return "OK";
	}



}
