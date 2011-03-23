
package com.thebitstream.shoutcast;
/**
 * 
 * @author Andy Shaules
 *
 */
public class StreamConfiguration {
	
	private int port;
	private String password;
	private String outputName;	
	private boolean clientMode=false;
	private String metaCharset = "UTF-8";
	
	public String getMetaCharset() {
		return metaCharset;
	}
	public void setMetaCharset(String metaCharset) {
		this.metaCharset = metaCharset;
	}
	private String clientConnectionURL;
	
	public void setOutputName(String name){
		this.outputName=name;
	}
	public void setPassword(String pass){
		this.password=pass;
	}
	public void setPort(int number){
		port=number;
	}
	public String getOutputName(){
		return outputName;
	}
	public String getPassword(){
		return password;
	}
	public int getPort(){
		return port;
	}
	public void setClientMode(boolean clientMode) {
		this.clientMode = clientMode;
	}
	public boolean isClientMode() {
		return clientMode;
	}
	public void setClientConnectionURL(String clientConnectionURL) {
		this.clientConnectionURL = clientConnectionURL;
	}
	public String getClientConnectionURL() {
		return clientConnectionURL;
	}	
}
