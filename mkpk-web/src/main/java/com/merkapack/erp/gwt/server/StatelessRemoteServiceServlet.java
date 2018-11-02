package com.merkapack.erp.gwt.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.server.Base64Utils;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

public class StatelessRemoteServiceServlet extends RemoteServiceServlet {
	
	protected static final int DOMAIN = 1;
	protected static final String USER = "admin";
	private static final long serialVersionUID = -4124311324608377793L;

}
