package org.csource.client;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.csource.config.FastdfsClientConfig;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinxindong.woyao.system.Env;

public class FastdfsClientFactory {

	private static Logger logger = LoggerFactory.getLogger(FastdfsClientFactory.class);
	private static String configFile = "daily_fdfs_client.conf";
	private static volatile FastdfsClient fastdfsClient;
	private static FastdfsClientConfig config = null;

	private FastdfsClientFactory() {
	}

	static {
		init();
	}

	private static void init() {
		if (Env.isOnline) {
			configFile = "fdfs_client.conf";
		}
	}

	/**
	 * 鑾峰�? fastdfs client 瀹㈡埛绔�?
	 * 
	 * @return FastdfsClient瀹炰�?
	 */
	public synchronized static FastdfsClient getFastdfsClient() {

		if (fastdfsClient == null) {
			try {

				config = new FastdfsClientConfig(configFile);
				ClientGlobal.init1(configFile);
			} catch (Exception e) {
				logger.warn("Load fastdfs config failed.", e);
				System.out.println(e);
			}

			int connectTimeout = config.getConnectTimeout();
			int networkTimeout = config.getNetworkTimeout();

			TrackerClientFactory trackerClientFactory = new TrackerClientFactory();

			GenericKeyedObjectPoolConfig trackerClientPoolConfig = config.getTrackerClientPoolConfig();
			GenericKeyedObjectPoolConfig storageClientPoolConfig = config.getStorageClientPoolConfig();

			trackerClientPoolConfig.setMaxTotalPerKey(4);
			// storageClientPoolConfig.setMaxTotal(10);
			// storageClientPoolConfig.setMaxIdlePerKey(40);
			 storageClientPoolConfig.setMaxTotalPerKey(4);
			GenericKeyedObjectPool<String, TrackerClient> trackerClientPool = new GenericKeyedObjectPool<String, TrackerClient>(
					trackerClientFactory, trackerClientPoolConfig);

			StorageClientFactory storageClientFactory = new StorageClientFactory(connectTimeout, networkTimeout,
					trackerClientPool);
			GenericKeyedObjectPool<String, StorageClient> storageClientPool = new GenericKeyedObjectPool<String, StorageClient>(
					storageClientFactory, storageClientPoolConfig);

			StorageClient1Factory storageClient1Factory = new StorageClient1Factory(connectTimeout, networkTimeout,
					trackerClientPool);

			GenericKeyedObjectPool<String, StorageClient1> storageClient1Pool = new GenericKeyedObjectPool<>(
					storageClient1Factory, storageClientPoolConfig);
			List<String> trackerAddrs = config.getTrackerAddrs();
			fastdfsClient = new FastdfsClient(trackerAddrs, trackerClientPool, storageClient1Pool, storageClientPool);
		}

		return fastdfsClient;
	}

}
