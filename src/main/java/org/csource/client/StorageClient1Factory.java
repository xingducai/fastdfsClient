package org.csource.client;

import java.io.IOException;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.csource.config.FastdfsClientConfig;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class StorageClient1Factory implements KeyedPooledObjectFactory<String, StorageClient1> {

	private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
	private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;
	private GenericKeyedObjectPool<String, TrackerClient> trackerClientPool = null;

	public StorageClient1Factory() {
	}

	/**
	 * 实例�?
	 * 
	 * @param connectTimeout
	 *            连接超时时间(�?)
	 * @param networkTimeout
	 *            传输超时时间(�?)
	 */
	public StorageClient1Factory(Integer connectTimeout, Integer networkTimeout) {
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
	}

	public StorageClient1Factory(int connectTimeout, int networkTimeout,
			GenericKeyedObjectPool<String, TrackerClient> trackerClientPool) {
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
		this.trackerClientPool = trackerClientPool;
	}

	@Override
	public PooledObject<StorageClient1> makeObject(String key) throws Exception {
		TrackerServer trackerServer = trackerClientPool.borrowObject(key).getConnection();
		System.out.println(key + "   make object storage  " + trackerServer);

		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		PooledObject<StorageClient1> pooledStorageClient = new DefaultPooledObject<StorageClient1>(storageClient);
		return pooledStorageClient;
	}

	@Override
	public void destroyObject(String key, PooledObject<StorageClient1> pooledStorageClient) throws IOException {
		StorageClient storageClient = pooledStorageClient.getObject();
		// try {
		//// storageClient.close();
		// } catch (FastdfsClientException e) {
		// throw new IOException(e);
		// }
	}

	@Override
	public boolean validateObject(String key, PooledObject<StorageClient1> p) {
		return true;
	}

	@Override
	public void activateObject(String key, PooledObject<StorageClient1> p) throws Exception {
	}

	@Override
	public void passivateObject(String key, PooledObject<StorageClient1> p) throws Exception {
	}

}
