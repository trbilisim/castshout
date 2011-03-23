package com.thebitstream.shoutcast.services;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.red5.io.utils.ObjectMap;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.plugin.shoutcast.stream.NSVConsumer;
import org.springframework.core.io.Resource;

import com.thebitstream.shoutcast.Application;

public class UserService {
	private Application app;

	public UserService(Application app) {
		this.app = app;
	}

	public List<Object> getStreams() {
		
		ArrayList<Object> list = new ArrayList<Object>();

		List<NSVConsumer> streams = app.getStreams();

		

		for (int i = 0; i < streams.size(); i++) {
			ObjectMap<Object, Object> obj = new ObjectMap<Object, Object>();
			obj.put("id", i);
			obj.put("name", streams.get(i).getMarshal().getStream().getName());
			obj.put("scope", streams.get(i).getMarshal().getStream().getScope().getContextPath());
			obj.put("connected", streams.get(i).isConnected());
			
			
			if (streams.get(i).isConnected()) {
				obj.put("connected", true);
				obj.put("content", streams.get(i).getMarshal().getContentType());

				obj.put("metaData", streams.get(i).getMarshal().getMetadata());

			} else
				obj.put("connected", false);

			list.add(obj);
		}

		return list;
	}
	public int getViewerCount() {
		return app.getViewerCount();
	}
	private String formatDate(Date date) {
		SimpleDateFormat formatter;
		String pattern = "dd/MM/yy H:mm:ss";
		Locale locale = new Locale("en", "US");
		formatter = new SimpleDateFormat(pattern, locale);
		return formatter.format(date);
	}

	/**
	 * Getter for property 'listOfAvailableFLVs'.
	 *
	 * @return Value for property 'listOfAvailableFLVs'.
	 */
	public Map<String, Map<String, Object>> getListOfAvailableFLVs() {
		IScope scope = Red5.getConnectionLocal().getScope();
		Map<String, Map<String, Object>> filesMap = new HashMap<String, Map<String, Object>>();
		try {

			addToMap(filesMap, scope.getResources("streams/*.flv"));

			addToMap(filesMap, scope.getResources("streams/*.f4v"));

			addToMap(filesMap, scope.getResources("streams/*.mp3"));

			addToMap(filesMap, scope.getResources("streams/*.mp4"));

			addToMap(filesMap, scope.getResources("streams/*.m4a"));

			addToMap(filesMap, scope.getResources("streams/*.3g2"));

			addToMap(filesMap, scope.getResources("streams/*.3gp"));

		} catch (IOException e) {

		}
		return filesMap;
	}

	private void addToMap(Map<String, Map<String, Object>> filesMap, Resource[] files) throws IOException {
		if (files != null) {
			for (Resource flv : files) {
				File file = flv.getFile();
				Date lastModifiedDate = new Date(file.lastModified());
				String lastModified = formatDate(lastModifiedDate);
				String flvName = flv.getFile().getName();
				String flvBytes = Long.toString(file.length());

				Map<String, Object> fileInfo = new HashMap<String, Object>();
				fileInfo.put("name", flvName);
				fileInfo.put("lastModified", lastModified);
				fileInfo.put("size", flvBytes);
				filesMap.put(flvName, fileInfo);
			}
		}
	}
}
