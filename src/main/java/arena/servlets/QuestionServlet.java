package arena.servlets;

import arena.bll.QuestionManager;
import org.json.simple.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {
    /*
     * -------------------------------------------QuestionServlet-------------------------------------------
     * GET: http://localhost:8080/TheArenaServlet/QuestionServlet
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONArray jsonResponse = new JSONArray();
        QuestionManager.getQuestions();
        if (!QuestionManager.questions.isEmpty()) {
            jsonResponse.add(QuestionManager.json);
            response.getWriter().append(jsonResponse.toJSONString());
        } else {
            jsonResponse.add(null);
            response.getWriter().append(jsonResponse.toJSONString());
        }
        jsonResponse.clear();
    }
}

