/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ loop more detail.
**/

package org.csource.fastdfs.test;

import java.io.*;
import java.net.*;
import java.util.*;
import org.csource.common.*;
import org.csource.fastdfs.*;

/**
 * client test
 * 
 * @author Happy Fish / YuQing
 * @version Version 1.20
 */
public class TestAppender2 {
	private TestAppender2() {
	}

	/**
	 * entry point
	 * 
	 * @param args
	 *            comand arguments
	 *            <ul>
	 *            <li>args[0]: config filename</li>
	 *            </ul>
	 *            <ul>
	 *            <li>args[1]: local filename to upload</li>
	 *            </ul>
	 */
	public static void main(String args[]) {


		try {

			long startTime;
			String group_name;
			String remote_filename;
			ServerInfo[] servers;
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();

			StorageServer storageServer = null;

			StorageClient client = new StorageClient(trackerServer, storageServer);
			byte[] file_buff;
			NameValuePair[] meta_list;
			String[] results;
			String appender_filename;
			String file_ext_name;
			int errno;

			meta_list = new NameValuePair[4];
			meta_list[0] = new NameValuePair("width", "800");
			meta_list[1] = new NameValuePair("heigth", "600");
			meta_list[2] = new NameValuePair("bgcolor", "#FFFFFF");
			meta_list[3] = new NameValuePair("author", "Mike");

			file_buff = "this is a test".getBytes(ClientGlobal.g_charset);
			System.out.println("file length: " + file_buff.length);

			group_name = null;
			StorageServer[] storageServers = tracker.getStoreStorages(trackerServer, group_name);
			if (storageServers == null) {
				System.err.println("get store storage servers fail, error code: " + tracker.getErrorCode());
			} else {
				System.err.println("store storage servers count: " + storageServers.length);
				for (int k = 0; k < storageServers.length; k++) {
					System.err.println(
							(k + 1) + ". " + storageServers[k].getInetSocketAddress().getAddress().getHostAddress()
									+ ":" + storageServers[k].getInetSocketAddress().getPort());
				}
				System.err.println("");
			}

			startTime = System.currentTimeMillis();
			results = client.upload_appender_file(file_buff, "txt", meta_list);
			System.out.println("upload_appender_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

			/*
			 * group_name = ""; results =
			 * client.upload_appender_file(group_name, file_buff, "txt",
			 * meta_list);
			 */
			if (results == null) {
				System.err.println("upload file fail, error code: " + client.getErrorCode());
				return;
			} else {
				group_name = results[0];
				remote_filename = results[1];
				System.err.println("group_name: " + group_name + ", remote_filename: " + remote_filename);
				System.err.println(client.get_file_info(group_name, remote_filename));

				servers = tracker.getFetchStorages(trackerServer, group_name, remote_filename);
				if (servers == null) {
					System.err.println("get storage servers fail, error code: " + tracker.getErrorCode());
				} else {
					System.err.println("storage servers count: " + servers.length);
					for (int k = 0; k < servers.length; k++) {
						System.err.println((k + 1) + ". " + servers[k].getIpAddr() + ":" + servers[k].getPort());
					}
					System.err.println("");
				}

				meta_list = new NameValuePair[4];
				meta_list[0] = new NameValuePair("width", "1024");
				meta_list[1] = new NameValuePair("heigth", "768");
				meta_list[2] = new NameValuePair("bgcolor", "#000000");
				meta_list[3] = new NameValuePair("title", "Untitle");

				startTime = System.currentTimeMillis();
				errno = client.set_metadata(group_name, remote_filename, meta_list,
						ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);
				System.out.println("set_metadata time used: " + (System.currentTimeMillis() - startTime) + " ms");
				if (errno == 0) {
					System.err.println("set_metadata success");
				} else {
					System.err.println("set_metadata fail, error no: " + errno);
				}

				file_buff = "this is a slave buff".getBytes(ClientGlobal.g_charset);
				appender_filename = remote_filename;
				file_ext_name = "txt";
				startTime = System.currentTimeMillis();
				errno = client.append_file(group_name, appender_filename, file_buff);
				System.out.println("append_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
