package com.francelabs.datafari.servlets.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.francelabs.datafari.exception.CodesReturned;
import com.francelabs.datafari.exception.DatafariServerException;
import com.francelabs.datafari.service.db.UserDataService;
import com.francelabs.datafari.servlets.constants.OutputConstants;
import com.francelabs.datafari.user.User;

/**
 * Servlet implementation class getAllUsersAndRoles
 */
@WebServlet("/SearchAdministrator/deleteRole")
public class DeleteRole extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DeleteRole.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final JSONObject jsonResponse = new JSONObject();
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		try {
			if (request.getParameter(UserDataService.USERNAMECOLUMN) != null && request.getParameter(UserDataService.ROLECOLUMN) != null) {
				final User user = new User(request.getParameter(UserDataService.USERNAMECOLUMN).toString(), "");
				try {
					user.deleteRole(request.getParameter(UserDataService.ROLECOLUMN).toString());
					jsonResponse.put(OutputConstants.CODE, CodesReturned.ALLOK.getValue()).put(OutputConstants.STATUS, "User deleted with success");
				} catch (final DatafariServerException e) {
					jsonResponse.put(OutputConstants.CODE, CodesReturned.PROBLEMCONNECTIONDATABASE.getValue()).put(OutputConstants.STATUS,
							"Datafari isn't connected to Database");
				}
			} else {
				jsonResponse.put(OutputConstants.CODE, CodesReturned.PROBLEMQUERY.getValue()).put(OutputConstants.STATUS, "Problem with query");
			}
		} catch (final JSONException e) {
			logger.error(e);
		}
		final PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}
}
