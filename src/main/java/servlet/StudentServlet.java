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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import entity.Pagination;
import entity.Student;
import redis.clients.jedis.Jedis;
import service.StudentService;

@WebServlet("/studentServlet")
public class StudentServlet extends HttpServlet{

	private ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)  //类级别的设置，JsonInclude.Include.NON_EMPTY标识只有非NULL的值才会被纳入json string之中，其余的都将被忽略
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) //禁止使用出现未知属性之时，抛出异常

.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);//转化后的json的key命名格式

	private StudentService ss = new StudentService();
	private static final Logger logger = Logger.getLogger(StudentServlet.class);
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		logger.info(action);
		if("queryAll".equals(action)) {
			int page = Integer.parseInt(request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows"));
			Pagination pagination = ss.findAllStudent(page,rows);
			String json = OBJECT_MAPPER.writeValueAsString(pagination);
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
