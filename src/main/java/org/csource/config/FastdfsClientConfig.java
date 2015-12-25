package org.csource.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

public class FastdfsClientConfig {

	public static final int DEFAULT_CONNECT_TIMEOUT = 5; // second
	public static final int DEFAULT_NETWORK_TIMEOUT = 30; // second

	private int connectTimeout = DEFAULT_CONNECT_TIMEOUT * 1000;
	private int networkTimeout = DEFAULT_NETWORK_TIMEOUT * 1000;
	private List<String> trackerAddrs = new ArrayList<String>();

	/**
	 * 瀹炰緥鍖�
	 */
	public FastdfsClientConfig() {
	}

	/**
	 * 瀹炰緥鍖�
	 * 
	 * @param configFile
	 *            閰嶇疆鏂囦欢鍚�
	 * @throws ConfigurationException
	 */
	public FastdfsClientConfig(String configFile) throws ConfigurationException {
		Configuration config = new PropertiesConfiguration(configFile);

		this.connectTimeout = config.getInt("connect_timeout", DEFAULT_CONNECT_TIMEOUT) * 1000;
		this.networkTimeout = config.getInt("network_timeout", DEFAULT_NETWORK_TIMEOUT) * 1000;

		List<Object> trackerServers = config.getList("tracker_server");

		for (Object trackerServer : trackerServers) {
			trackerAddrs.add((String) trackerServer);
		}
	}

	public GenericKeyedObjectPoolConfig getTrackerClientPoolConfig() {
		GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();

		return poolConfig;
	}

	public GenericKeyedObjectPoolConfig getStorageClientPoolConfig() {
		GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();

		return poolConfig;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getNetworkTimeout() {
		return networkTimeout;
	}

	public void setNetworkTimeout(int networkTimeout) {
		this.networkTimeout = networkTimeout;
	}

	public List<String> getTrackerAddrs() {
		return trackerAddrs;
	}

	public void setTrackerAddrs(List<String> trackerAddrs) {
		this.trackerAddrs = trackerAddrs;
	}

}
