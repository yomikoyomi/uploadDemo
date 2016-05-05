package com.example.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadFormController {
	
	@RequestMapping(value="/uploadTest", method=RequestMethod.POST)
	public String upload(HttpServletResponse response, @RequestParam MultipartFile file) throws IOException{
		
		String boundary = generateBoundary();
		URL url = new URL("http://54.238.148.180:8080/demo/upload");
//		URL url = new URL("http://localhost:8080/demo/upload");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod( "POST" );
		con.setDoInput(true);
		con.setDoOutput( true );
		con.setUseCaches(false);
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary );
		
//		PrintWriter pw;
//		pw = new PrintWriter( con.getOutputStream() );
//		pw.print( file );
//		pw.close();

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		
		out.writeBytes("--" + boundary + "\r\n");
		out.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"");
		out.writeBytes(file.getName());
		out.writeBytes("\"\r\n");
		out.writeBytes("Content-Type: application/octet-stream\r\n");
        out.writeBytes("Content-Transfer-Encoding: binary\r\n");
        out.writeBytes("\r\n");
        
		BufferedInputStream in = new BufferedInputStream(file.getInputStream());
		int buff = 0;
		while((buff = in.read()) != -1){
		    out.write(buff);
		}
		out.writeBytes("\r\n");
		
		out.writeBytes("--" + boundary + "--\r\n");
		out.close();
		
		BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
		System.out.println(br.readLine());
		br.close();

		con.disconnect();
		
		return null;
	}
	
	private String generateBoundary(){
	    String chars = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
	    Random rand = new Random();
	    String boundary = "";
	    for(int i = 0; i < 40; i++){
	        int r = rand.nextInt(chars.length());
	        boundary += chars.substring(r, r + 1);
	    }
	    return boundary;
	}

}
