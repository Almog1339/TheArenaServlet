package arena.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.JSONObject;
import arena.bll.*;

/**
 * Servlet implementation class onlineUsersLocation
 */
@WebServlet("/onlineUsersLocation")
public class OnlineUsersLocation extends HttpServlet {

	/*
	 * -------------------------------------------onlineUsersLocation---------------
	 * http://localhost:8080/TheArenaServlet/onlineUsersLocation
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OnlineUsersLocation() {
		super();
	}

	protected synchronized void doPut(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		//=====================================================
		//    added synchronized because of the jsonResponse
		//	  probably can be removed if we create the jsonResponse locally
		//    body to pass:"lat","lng","email"
		//	  The PUT also updates the user current location in the DB
		//=====================================================
		JSONArray jsonResponse = new JSONArray();
		JSONObject params = getBodyParams(request);
		Map<String, String> jsonMap = new HashMap<>();
		JSONObject res;
		try {
			double lat = (double) params.get("lat");
			double lng = (double) params.get("lng");
			String mail = params.get("email").toString().toLowerCase();
			LocationManager.updateUsersStatus(mail, lat, lng);

			jsonResponse.clear();
			LocationManager.getOnlineUsersLocation(mail, lat, lng);

			if (!LocationManager.location.isEmpty()) {
				jsonResponse.add(LocationManager.json);
				response.getWriter().append(jsonResponse.toJSONString());
			} else {
				jsonMap.put("Error", "No one else was found");
				res = new JSONObject(jsonMap);
				jsonResponse.add(res);
				response.getWriter().append(jsonResponse.toJSONString());
			}
		} catch (JSONException e) {
			jsonMap.put("Error", "Missing some fields");
			res = new JSONObject(jsonMap);
			jsonResponse.add(res);
			response.getWriter().append(jsonResponse.toJSONString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
	//============================================================================
	//	when the app goes to onDestroy this POST sets the user as offline
	//  Headers to pass:"email"(the userName from the android)
	//============================================================================

		JSONArray jsonResponse = new JSONArray();
		String mail;
		JSONObject params = getBodyParams(request);
		Map<String, String> jsonMap = new HashMap<>();
		JSONObject res;
		try {
			mail = params.get("email").toString();
			LocationManager.logOutUser(mail);
			jsonMap.put("Success", "Success");
		} catch (JSONException e) {
			jsonMap.put("Error", "Missing some fields");

		}
		res = new JSONObject(jsonMap);
		jsonResponse.add(res);
		response.getWriter().append(jsonResponse.toJSONString());
	}


	protected JSONObject getBodyParams(HttpServletRequest request) {
		//============================================================================
		//	this function extracts to body of the request
		//  and returns it as JSONObject
		//============================================================================
		StringBuilder sb = new StringBuilder();
		String line = null;
		JSONObject json = null;

		BufferedReader reader;
		try {
			reader = request.getReader();
			while ((line = reader.readLine()) != null)
				sb.append(line);
			json = new JSONObject(sb.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return json;

	}
}