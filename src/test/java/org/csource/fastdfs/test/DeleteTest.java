package org.csource.fastdfs.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.csource.client.FastdfsClientFactory;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.junit.Test;

public class DeleteTest {

	@Test
	public void test() throws IOException, MyException {

		StorageClient1 client = FastdfsClientFactory.getFastdfsClient().getStorageClient1();

		NameValuePair[] metaList = new NameValuePair[1];
		metaList[0] = new NameValuePair("fileName", "name");
		 
		String fileId = client.upload_appender_file1("d:/school.txt".getBytes(), "txt", metaList);
		System.out.println("upload success. file id is: " + fileId);

		int i = 0;
		while (i++ < 10) {
			byte[] result = client.download_file1(fileId);
			System.out.println(i + ", download result is: " + result.length);
		}

		i = client.delete_file1(fileId);
		System.out.println(i);
	}
}
