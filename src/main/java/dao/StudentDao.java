package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	public List find () {
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
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		return list;
	}
	/**
	 * 设置value
	 * */
	public void saveValue(Student std) {
		Jedis jedis = RedisUtils.getJedis();
		Set<String> set = jedis.keys("student*");
		String id = null;
		if(set == null || set.size() <= 0){
			id = "1";
		} else {
			String key = set.iterator().next();
			id = jedis.hget(key, "id");
			int tempId = Integer.parseInt(id);
			Integer tempId2 = (tempId + 1);
			id = tempId2.toString();
		}
		String newKey = "student"+id;
		
		jedis.hset(newKey,"id", id);
		jedis.hset(newKey,"name", std.getName());
		jedis.hset(newKey,"birthday", std.getBirthday().toLocaleString());
		jedis.hset(newKey,"description", std.getDescription());
		jedis.hset(newKey,"avgscore", std.getAvgscore().toString());
	}
	
	public int getCount() {
		Jedis jedis = RedisUtils.getJedis();
		Set<String> set = jedis.keys("student*");
		return set.size();
	}
	
}
