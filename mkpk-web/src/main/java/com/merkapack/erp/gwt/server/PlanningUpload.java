package com.merkapack.erp.gwt.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.type.MimeType;

@MultipartConfig
@WebServlet(name = "Planning Upload Servlet", urlPatterns = { "/mkpk_gwt/MkpkPlanningUpload" })
public class PlanningUpload extends HttpServlet {

	private static final long serialVersionUID = -5090546196567032401L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(StatelessRemoteServiceServlet.DOMAIN, StatelessRemoteServiceServlet.USER);
			LinkedList<Planning> list = new LinkedList<Planning>();
			
			Part filePart = req.getPart("fileUploadID");
            if (filePart != null) {
                // prints out some information for debugging
                System.out.println(filePart.getName());
                System.out.println(filePart.getSize());
                System.out.println(filePart.getContentType());

                InputStream  in = filePart.getInputStream();
                list = Excel2Planning.importPlanning(ctx, in);
                in.close();
            }
            
            resp.setContentType(MimeType.JSON.getName());
			Gson g = new Gson();
			g.toJson(list, resp.getWriter());
			resp.flushBuffer();
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
		
	}
	

}
