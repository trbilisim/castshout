package com.thebitstream.shoutcast.services;

import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.stream.IStreamPlaybackSecurity;

public class PlayBackHandler implements IStreamPlaybackSecurity {

	public static String Playing="playing";
	
	@Override
	public boolean isPlaybackAllowed(IScope scope, String name, int start, int length, boolean flushPlaylist) {
		
		IConnection conn=Red5.getConnectionLocal();		
		conn.setAttribute(Playing, name);		
		return true;
	}

}
