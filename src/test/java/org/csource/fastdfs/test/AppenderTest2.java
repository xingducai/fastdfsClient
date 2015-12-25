/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ loop more detail.
**/

package org.csource.fastdfs.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.csource.client.FastdfsClient;
import org.csource.client.FastdfsClientFactory;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageClient1;

/**
 
*/

public class AppenderTest2 {
	private AppenderTest2() {
	}

	public static void main(String args[]) {

		ExecutorService s = Executors.newFixedThreadPool(5);
		Runnable a = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 1; i++) {
					a();
				}

			}
		};
		Runnable a1 = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 1; i++) {
					a();
				}

			}
		};
		Runnable a2 = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 1; i++) {
					a();
				}

			}
		};
		Runnable a3 = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 1; i++) {
					a();
				}

			}
		};

		s.submit(a);

		s.submit(a1);

		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a);

		s.submit(a1);

		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a);

		s.submit(a1);

		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.submit(a2);
		s.shutdown();
	}

	public static void a() {
		FastdfsClient f = null;
		StorageClient1 client = null;
		try {

			long startTime;
			String group_name;
			String remote_filename;
			ServerInfo[] servers;

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

			file_buff = "this is a test".getBytes();
			group_name = null;
			startTime = System.currentTimeMillis();
			f = FastdfsClientFactory.getFastdfsClient();
			client = f.getStorageClient1();
			results = client.upload_appender_file(file_buff, "txt", meta_list);

			if (results == null) {
				return;
			} else {
				System.out.println(results[1]);
				// group_name = results[0];
				// remote_filename = results[1];
				// file_buff = "this is a slave buff".getBytes( );
				// appender_filename = remote_filename;
				// file_ext_name = "txt";
				// startTime = System.currentTimeMillis();
				// // errno = client.append_file(group_name, appender_filename,
				// // file_buff);
				// errno = client.append_file(group_name, appender_filename,
				// "d:/school.txt");
				// if (errno == 0) {
				// System.err.println("success");
				// } else {
				// System.err.println("append file fail, error no: " + errno);
				// }
			}

			// Thread.sleep(3000);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("00000");
		} finally {
			if (f != null)
				try {
					f.returnStorageClient1(client);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("oooo");
				}
		}
		System.out.println("99");
	}
}
