package org.csource.client;

import java.io.IOException;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.config.FastdfsClientConfig;
import org.csource.fastdfs.TrackerClient;

public class TrackerClientFactory implements KeyedPooledObjectFactory<String, TrackerClient> {

	public TrackerClientFactory() {
	}

	@Override
	public PooledObject<TrackerClient> makeObject(String key) {
		TrackerClient trackerClient = new TrackerClient();
		System.out.println(key + "   make object tracker");

		PooledObject<TrackerClient> pooledTrackerClient = new DefaultPooledObject<TrackerClient>(trackerClient);
		return pooledTrackerClient;
	}

	@Override
	public void destroyObject(String key, PooledObject<TrackerClient> pooledTrackerClient) throws IOException {
		System.out.println("destory  tracker");
		// TrackerClient trackerClient = pooledTrackerClient.getObject();
		// try {
		// trackerClient.close();
		// } catch (FastdfsClientException e) {
		// throw new IOException(e);
		// }
	}

	@Override
	public boolean validateObject(String key, PooledObject<TrackerClient> p) {
		return true;
	}

	@Override
	public void activateObject(String key, PooledObject<TrackerClient> p) throws Exception {
	}

	@Override
	public void passivateObject(String key, PooledObject<TrackerClient> p) throws Exception {
	}

}
