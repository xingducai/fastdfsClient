package org.csource.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.csource.exception.FastdfsClientException;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;

public class FastdfsClient {

	private GenericKeyedObjectPool<String, TrackerClient> trackerClientPool;
	private GenericKeyedObjectPool<String, StorageClient> storageClientPool;
	private GenericKeyedObjectPool<String, StorageClient1> storageClient1Pool;
	private List<String> trackerAddrs = new ArrayList<String>();
	private int trackerIndex = 0;

	StorageClient storageClient = null;
	TrackerClient trackerClient = null;
	StorageClient1 storageClient1 = null;
	TrackerClient trackerClient1 = null;
	String storageAddr = null;
	String trackerAddr = null;

	/**
	 * 实例�?
	 * 
	 * @param trackerAddrs
	 *            {trackerHost1:port, trackerHost2:port, ...}
	 * @throws Exception
	 */
	public FastdfsClient(List<String> trackerAddrs) throws Exception {
		this.trackerAddrs = trackerAddrs;
		this.trackerClientPool = new GenericKeyedObjectPool<String, TrackerClient>(new TrackerClientFactory());
		this.storageClientPool = new GenericKeyedObjectPool<String, StorageClient>(new StorageClientFactory());

	}

	public synchronized StorageClient getStorageClient() {
		try {
			storageAddr = getTrackerAddr();
			// trackerClient = trackerClientPool.borrowObject(storageAddr);
			storageClient = storageClientPool.borrowObject(storageAddr);
			return storageClient;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public synchronized StorageClient1 getStorageClient1() {
		try {
			storageAddr = getTrackerAddr();
			// trackerClient1 = trackerClientPool.borrowObject(storageAddr);
			storageClient1 = storageClient1Pool.borrowObject(storageAddr);
			return storageClient1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public StorageClient returnObject() throws Exception {
		try {
			if (storageClient != null) {
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if (trackerClient != null) {
				trackerClientPool.returnObject(storageAddr, trackerClient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println(storageClientPool.getCreatedCount() + "  " + storageClientPool.getReturnedCount() + "  "
				+ storageClientPool.getNumActive());
		System.out.println(trackerClientPool.getCreatedCount() + "  " + trackerClientPool.getReturnedCount() + "  "
				+ trackerClientPool.getNumActive());
		return storageClient;
	}

	public StorageClient returnStorageClient(StorageClient storageClient) {
		System.out.println(storageClientPool.getCreatedCount() + "  " + storageClientPool.getReturnedCount() + "  "
				+ storageClientPool.getNumActive());
		System.out.println(trackerClientPool.getCreatedCount() + "  " + trackerClientPool.getReturnedCount() + "  "
				+ trackerClientPool.getNumActive());
		try {
			if (storageClient != null) {
				storageClientPool.returnObject(storageAddr, storageClient);
			}
		} catch (Exception e) {
			System.out.println(e+"...");
		}

		return storageClient;
	}

	public void returnStorageClient1(StorageClient1 storageClient) {
		System.out.println(storageClient1Pool.getCreatedCount() + "  " + storageClient1Pool.getReturnedCount() + "  "
				+ storageClient1Pool.getNumActive());
		System.out.println(trackerClientPool.getCreatedCount() + "  " + trackerClientPool.getReturnedCount() + "  "
				+ trackerClientPool.getNumActive());
		try {
			if (storageClient != null) {
				storageClient1Pool.returnObject(storageAddr, storageClient);
			}
		} catch (Exception e) {
			System.out.println(e+"....,,,");
		}

	}

	public FastdfsClient(List<String> trackerAddrs, GenericKeyedObjectPool<String, TrackerClient> trackerClientPool,
			GenericKeyedObjectPool<String, StorageClient1> storageClient1Pool,
			GenericKeyedObjectPool<String, StorageClient> storageClientPool) {
		this.trackerAddrs = trackerAddrs;
		this.trackerClientPool = trackerClientPool;
		this.storageClientPool = storageClientPool;
		this.storageClient1Pool = storageClient1Pool;
	}

	/**
	 * 关闭 trackerClient 连接�? �? storageClient连接�?
	 */
	public void close() {
		this.trackerClientPool.close();
		this.storageClientPool.close();
	}

	/**
	 * �?验tracker服务是否可以连接
	 * 
	 * @param trackerAddr
	 * @return
	 */
	private boolean validateConnection(String trackerAddr) {
		String[] hostport = trackerAddr.split(":");
		String host = hostport[0];
		Integer port = Integer.valueOf(hostport[1]);

		Socket socket = new Socket();

		try {
			socket.setSoTimeout(10000);
			socket.connect(new InetSocketAddress(host, port), 10000);
			return true;
		} catch (SocketException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			} finally {
				socket = null;
			}
		}
	}

	/**
	 * 获取可用tracker server连接地址
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getTrackerAddr() throws Exception {
		int currIdx;

		synchronized (FastdfsClient.class) {

			if (trackerIndex >= trackerAddrs.size()) {
				trackerIndex = 0;
			}

			currIdx = trackerIndex;
		}

		if (validateConnection(trackerAddrs.get(currIdx))) {
			return trackerAddrs.get(currIdx);
		}

		for (int i = 0; i < trackerAddrs.size(); i++) {

			if (currIdx == i) {
				continue;
			}

			if (validateConnection(trackerAddrs.get(i))) {

				synchronized (FastdfsClient.class) {
					if (currIdx == trackerIndex) {
						trackerIndex = i;
					}
				}

				return trackerAddrs.get(i);
			}
		}

		throw new FastdfsClientException("can not connect all tracker server");
	}

}
