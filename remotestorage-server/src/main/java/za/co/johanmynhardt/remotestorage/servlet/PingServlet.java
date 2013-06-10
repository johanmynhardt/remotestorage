package za.co.johanmynhardt.remotestorage.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.johanmynhardt.remotestorage.impl.HelloServiceImpl;

/**
 * @author Johan Mynhardt
 */
@WebServlet(urlPatterns = {"/ping"})
public class PingServlet extends HttpServlet {

	@Inject
	private HelloServiceImpl helloService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");

		resp.getOutputStream().println(helloService.getHello(getNameFromRequest(req)));

	}

	private String getNameFromRequest(HttpServletRequest request) {
		return request.getParameter("name") == null ? "pietie" : request.getParameter("name");
	}
}
