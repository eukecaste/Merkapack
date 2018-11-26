package com.merkapack.erp.gwt.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Planning Upload Servlet", urlPatterns = { "/mkpk_gwt/MkpkPlanningUpload" })
@MultipartConfig
public class PlanningUpload extends HttpServlet  {

	private static final long serialVersionUID = -5090546196567032401L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doPost");
	}
}
