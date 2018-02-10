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
		
		
	}
	public void removeBatchStudent(String ids) {
		
	}

}
