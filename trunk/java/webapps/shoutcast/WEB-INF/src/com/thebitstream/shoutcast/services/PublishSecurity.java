package com.thebitstream.shoutcast.services;

import org.red5.server.api.IScope;
import org.red5.server.api.stream.IStreamPublishSecurity;


/**
 * Nothing is allowed but shoutcast streams.
 * @author Andy Shaules
 *
 */
public class PublishSecurity implements IStreamPublishSecurity {

	@Override
	public boolean isPublishAllowed(IScope scope, String name, String mode) {
		
		return false;
	}

}
