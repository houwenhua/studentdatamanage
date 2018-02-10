package servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import entity.Pagination;
import entity.Student;
import redis.clients.jedis.Jedis;
import service.StudentService;

@WebServlet("/studentServlet")
public class StudentServlet extends HttpServlet{

	private StudentService ss = new StudentService();
	private static final Logger logger = Logger.getLogger(StudentServlet.class);
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Student student = null;
		String action = request.getParameter("action");
		logger.info(action);
		if("queryAll".equals(action)) {
			int page = Integer.parseInt(request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows"));
			Pagination pagination = ss.findAllStudent(null,page,rows);
			String json = JSON.toJSONString(pagination);
			response.getWriter().println(json);
		}else if("addStudent".equals(action)){
			try {
				addStudent(request,response);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			logger.info("执行添加");
		}else if("updateStudent".equals(action)){
			try {
				updateStudent(request,response);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			logger.info("执行修改");
		}else if("removeStudent".equals(action)){
			removeStudent(request,response);
			logger.info("执行删除");
		}
	}
	private void removeStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ids = request.getParameter("ids");
		String[] index = ids.split(",");
		ss.removeBatchStudent(index);
		response.getWriter().write(new Integer(index.length).toString());
		logger.info(ids);
	}
	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String tempbirthday = request.getParameter("birthday");
		String description = request.getParameter("description");
		String tempAvgScore = request.getParameter("avgscore");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date birthday = sdf.parse(tempbirthday);
		int avgscore = Integer.parseInt(tempAvgScore);
		Student stu = new Student(id,name, birthday, description, avgscore);
		ss.updateOneStudent(stu);
		response.getWriter().println("1");
	}
	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
		String name = request.getParameter("name");
		String tempbirthday = request.getParameter("birthday");
		String description = request.getParameter("description");
		String tempAvgScore = request.getParameter("avgscore");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date birthday = sdf.parse(tempbirthday);
		int avgscore = Integer.parseInt(tempAvgScore);
		Student stu = new Student(name, birthday, description, avgscore);
		ss.addOneStudent(stu);
		response.getWriter().println("1");
	}
}
