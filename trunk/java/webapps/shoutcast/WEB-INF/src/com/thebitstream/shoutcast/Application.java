package com.thebitstream.shoutcast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;
import org.red5.io.utils.ObjectMap;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.plugin.Shoutcast;
import org.red5.server.plugin.shoutcast.IStatusListener;
import org.red5.server.plugin.shoutcast.stream.NSVConsumer;

import com.thebitstream.shoutcast.services.AdminService;
import com.thebitstream.shoutcast.services.PlayBackHandler;
import com.thebitstream.shoutcast.services.PublishSecurity;
import com.thebitstream.shoutcast.services.UserService;


import com.thebitstream.shoutcast.Application;



/**
 * 
 * @author Andy Shaules
 *
 */
public class Application extends MultiThreadedApplicationAdapter implements IStatusListener {

	List<StreamConfiguration> serverPorts;

	IScope scope;

	List<NSVConsumer> streams = new ArrayList<NSVConsumer>();

	private boolean allowPublish=false;
	private int playbackPort=1935;
	
	public int getPlaybackPort() {
		return playbackPort;
	}

	public void setPlaybackPort(int playbackPort) {
		this.playbackPort = playbackPort;
	}
	private String adminPass="admin";
	
	public void FCSubscribe (String streamName)
	{
		
		ObjectMap<Object,Object> param=new ObjectMap<Object,Object>();
		param.put("code", "NetStream.Play.Start");//forStream
		param.put("forStream", streamName);
		((IServiceCapableConnection)Red5.getConnectionLocal()).invoke("onFCSubscribe", new Object[] { param });
		
	}
	
	public void FCPublish (String streamName)
	{
		
		ObjectMap<Object,Object> param=new ObjectMap<Object,Object>();
		param.put("code", "NetStream.Publish.Start");
		((IServiceCapableConnection)Red5.getConnectionLocal()).invoke("onFCPublish", new Object[] { param });
		
	}
	public boolean appStart(IScope app) {
		
		
		super.appStart(scope);
		scope = app;
		System.out.println("Hello Shoutcast");
		scope.registerServiceHandler("user", new UserService(this));
		scope.registerServiceHandler("admin", new AdminService(app, streams, adminPass));
		
		for (int i = 0; i < serverPorts.size(); i++) {
			StreamConfiguration config = serverPorts.get(i);

			if (config.isClientMode()) {

				streams.add(Shoutcast.openExternalURI(scope, config.getOutputName(), config.getClientConnectionURL(),config.getMetaCharset()));
			} else {
				streams.add(Shoutcast.openServerPort(scope, config.getOutputName(), config.getPort(), config
						.getPassword()));
			}
		}
		
	if( ! allowPublish)
			registerStreamPublishSecurity(new PublishSecurity());
		
	this.registerStreamPlaybackSecurity(new PlayBackHandler());
	
	
	
		return true;
	}

	public boolean appConnect(IConnection conn, Object[] params){
		
		return true;
	}
	
	public List<NSVConsumer> getStreams() {
		return streams;
	}

	public int getViewerCount() {
		return scope.getConnections().size();
	}
	
	@Override
	public void onStatus(IBroadcastStream arg0, String arg1) {
		if (arg1.equals(IStatusListener.EVENT_CONNECTED)) {

		} else if (arg1.equals(IStatusListener.EVENT_DISCONNECTED)) {
			Iterator<Set<IConnection>> conns= arg0.getScope().getConnections().iterator();{
				
				while(conns.hasNext()){
					
					Iterator<IConnection> cSet = conns.next().iterator();
					
					while(cSet.hasNext()){
						IConnection con = cSet.next();
						String s= (String) con.getAttribute(PlayBackHandler.Playing,"");
						
						if(s.equals(arg0.getPublishedName())){
							con.close();
						}
					}
					
				}
				
			}
		}

	}
	
	@Override
	public void onAuxData(IBroadcastStream arg0, String arg1, IoBuffer arg2) {
		
	}
	public boolean isAllowPublish() {
		return allowPublish;
	}
	public void setAllowPublish(boolean allowPublish) {
		this.allowPublish = allowPublish;
	}
	public void setServerPorts(List<StreamConfiguration> serverList) {
		serverPorts = serverList;
	}
	public String getAdminPass() {
		return adminPass;
	}
	public void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
	}	
}
