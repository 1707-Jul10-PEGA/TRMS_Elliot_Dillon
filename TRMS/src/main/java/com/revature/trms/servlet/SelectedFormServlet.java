package com.revature.trms.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.trms.DAO.DAOFactory;
import com.revature.trms.DAO.FormDAO;
import com.revature.trms.objects.ReimbursementForm;

@WebServlet("/SelectedForm")
public class SelectedFormServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			FormDAO dao = (FormDAO) DAOFactory.getDAO("FormDAO");
			ReimbursementForm rf = dao.getReimbursementForm(Integer.parseInt(req.getParameter("formID")));
			req.getSession().setAttribute("ReimbursementForm", rf);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		req.getRequestDispatcher("reviewForm.html").forward(req, resp);
	}
		
	
}
