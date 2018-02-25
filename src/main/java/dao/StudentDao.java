package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import entity.Student;
import redis.clients.jedis.Jedis;
import redisutils.RedisUtils;

public class StudentDao {

	private static final Logger logger = Logger.getLogger(StudentDao.class);
	/**
	 * 获得所有student对象 
	 * @param rows 每页显示行数
	 * @param page 当前页
	 * 
	 * */
	public List find (int page, int rows) {
		Jedis jedis = RedisUtils.getJedis();
		Set<String> set = jedis.keys("student*");
		List list = new ArrayList();
		for(String str : set ){
			Map<String,String> studentMap = jedis.hgetAll(str);
			Set<Entry<String,String>> entry = studentMap.entrySet();
			Student student = new Student();
			for(Entry e : entry) {
				String key = e.getKey().toString();
				
				if("id".equals(key)){
					student.setId(e.getValue().toString());
				} else if("name".equals(key)){
					student.setName(e.getValue().toString());
				} else if("birthday".equals(key)){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						student.setBirthday(sdf.parse(e.getValue().toString()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				} else if("description".equals(key)){
					student.setDescription(e.getValue().toString());
				} else if("avgscore".equals(key)){
					student.setAvgscore(Integer.parseInt(e.getValue().toString()));
				}
			}
			list.add(student);
		}
		//排序并分页
		List<Student> pageList = this.pageTheStudent(list,page,rows);
		return pageList;
	}
	
	//排序并分页
	private List<Student> pageTheStudent(List<Student> list,int page,int rows) {
		List tempList = new ArrayList();
        Integer[] tempArray = new Integer[list.size()];
        int count = 0;
        //分页用到的List
        List pageList = new ArrayList();
        //获得student列中所有的分数数组
		for(Student stu : list){
			tempArray[count++] = stu.getAvgscore();
		}
		//按顺序排列
		Arrays.sort(tempArray);
		//按分数进行list对象的排序
		for(int i = 0 ; i < list.size(); i++) {
			for(Student stu : list){
				if(tempArray[i]==stu.getAvgscore()){
					tempList.add(stu);
				}
			}
		}
		//进行分页
		for(int i = ((page-1)*rows);i < rows && i<list.size();i++){
			pageList.add(tempList.get(i));
		}
		
		return pageList;
	}
	/**
	 * 保存到hash表
	 * */
	public void saveValue(Student std) {
		Jedis jedis = RedisUtils.getJedis();
		Set<String> set = jedis.keys("student*");
		Iterator it = set.iterator();
		int[] numKey = new int[set.size()];
		int studentCount = 0;
		int MaxStudent = 0;
		String id = null;
		if(set == null || set.size() <= 0){
			id = "1";
		} else {
			while(it.hasNext()){
				String key = (String) it.next();
				key = key.substring(7);
				int count= Integer.parseInt(key);
				numKey[studentCount] = count;
				studentCount++;
			}
			//排序并获得最大值
			Arrays.sort(numKey);
			MaxStudent = numKey[numKey.length-1];
			id = new Integer(MaxStudent + 1).toString();
		}
		String newKey = "student"+id;
		
		jedis.hset(newKey,"id", id);
		jedis.hset(newKey,"name", std.getName());
		jedis.hset(newKey,"birthday", std.getBirthday().toLocaleString());
		jedis.hset(newKey,"description", std.getDescription());
		jedis.hset(newKey,"avgscore", std.getAvgscore().toString());
	}
	/**
	 * 计算总记录数
	 * 
	 * */
	public int getCount() {
		Jedis jedis = RedisUtils.getJedis();
		Set<String> set = jedis.keys("student*");
		return set.size();
	}
	/**
	 * 
	 * 删除
	 * 
	 * */
	public void removeStudent(String[] index) {
		Jedis jedis = RedisUtils.getJedis();
		String newKey = "student";
		for (String str : index){
			newKey = "student"+str;
			jedis.del(newKey);
		}
		
	}
	/**
	 * 获得单个student实例
	 * */
	public Student getStudent(String id) {
		Jedis jedis = RedisUtils.getJedis();
		String newKey = "student"+id;
		Map<String,String> stuMap = jedis.hgetAll(newKey);
		Set<Entry<String,String>> entry = stuMap.entrySet();
		Student student = new Student();
		for(Entry e : entry) {
			String key = e.getKey().toString();
			if("id".equals(key)){
				student.setId(e.getValue().toString());
			} else if("name".equals(key)){
				student.setName(e.getValue().toString());
			} else if("birthday".equals(key)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					student.setBirthday(sdf.parse(e.getValue().toString()));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			} else if("description".equals(key)){
				student.setDescription(e.getValue().toString());
			} else if("avgscore".equals(key)){
				student.setAvgscore(Integer.parseInt(e.getValue().toString()));
			}
		}
		return student;
	}
	/**
	 * 更新操作
	 * 
	 * */
	public void updateStudent(Student tempStu) {
		Jedis jedis = RedisUtils.getJedis();
        String newKey = "student"+tempStu.getId();
		
		jedis.hset(newKey,"name", tempStu.getName());
		jedis.hset(newKey,"birthday", tempStu.getBirthday().toLocaleString());
		jedis.hset(newKey,"description", tempStu.getDescription());
		jedis.hset(newKey,"avgscore", tempStu.getAvgscore().toString());
	}
	
}
