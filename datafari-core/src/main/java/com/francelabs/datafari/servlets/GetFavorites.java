/*******************************************************************************
 *  * Copyright 2015 France Labs
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *******************************************************************************/
package com.francelabs.datafari.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

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
import com.francelabs.datafari.servlets.constants.OutputConstants;
import com.francelabs.datafari.user.Favorite;

/**
 * Servlet implementation class GetFavorites
 */
@WebServlet("/GetFavorites")
public class GetFavorites extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(GetFavorites.class.getName());
	private static final String FAVORITESLIST = "favoritesList";
	private static final String documentID = "documentID";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetFavorites() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final JSONObject jsonResponse = new JSONObject();
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		try {
			final String[] documentIDs = request.getParameterValues(documentID);
			final Principal userPrincipal = request.getUserPrincipal();
			// checking if the user is connected
			if (userPrincipal == null) {
				jsonResponse.put(OutputConstants.CODE, CodesReturned.NOTCONNECTED.getValue()).put(OutputConstants.STATUS,
						"Please reload the page, you're not connected");
			} else {
				final String username = userPrincipal.getName();
				try {
					jsonResponse.put(FAVORITESLIST, Favorite.getFavorites(username, documentIDs));
					jsonResponse.put(OutputConstants.CODE, CodesReturned.ALLOK.getValue());
				} catch (final DatafariServerException e) {
					jsonResponse.put(OutputConstants.CODE, e.getErrorCode().getValue());
				}
			}
		} catch (final JSONException e) {
			logger.error(e);
		}
		final PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}

}
