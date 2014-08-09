package org.openmrs.module.IHEInteroperability;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class SendingToOpenHIE {
	public void simpleHttpMessage(String pixXmlMessage) throws Exception{

		String url = "http://iol.demo.ohie.org:5001/ws/rest/v1/patients/";
		

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		System.out.println("HttpPost object created");

		
		try{
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

			urlParameters.add(new BasicNameValuePair("username", "admin"));
			urlParameters.add(new BasicNameValuePair("password", "rhea-password"));
			urlParameters.add(new BasicNameValuePair("body", pixXmlMessage));
			//urlParameters.add(new BasicNameValuePair("port", "5001"));
			//urlParameters.add(new BasicNameValuePair("num", "12345"));

			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			System.out.println("post.setEntity Called");

			HttpResponse response = client.execute(post);
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + post.getEntity());
			System.out.println("Response Code : " + 
					response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result.toString());

		}
		 catch (IOException e) {
			e.printStackTrace();
		}

	}

}
