package service;

import java.util.List;

import dao.StudentDao;
import entity.Pagination;
import entity.Student;

public class StudentService {

	private StudentDao sd = new StudentDao();
	public Pagination findAllStudent(Object object, int page, int rows) {
		List list = sd.find();
		int count = sd.getCount();
		Pagination pagination = new Pagination((long)count,page,list);
		return pagination;
	}
	public void addOneStudent(Student stu) {
		sd.saveValue(stu);
	}
	public void updateOneStudent(Student stu) {
		//获得redis中原来的数据
		Student tempStu = sd.getStudent(stu.getId());
		//进行修改
		tempStu.setName(stu.getName());
		tempStu.setBirthday(stu.getBirthday());
		tempStu.setDescription(stu.getDescription());
		tempStu.setAvgscore(stu.getAvgscore());
		//进行保存
		sd.updateStudent(tempStu);
	}
	public void removeBatchStudent(String[] index) {
		sd.removeStudent(index);
	}

}
