package redisutils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

	//redis服务器ip
	private static String ADDR = "120.79.176.108";
	//redis端口
	private static int PORT = 6379;
	//redis访问密码
	private static String AUTH = "123456";
	//可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 1024;
	//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    /**
     * 初始化连接池
     * 
     * */
    static {
    	try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获得连接实例
     * 
     * */
    
    public static synchronized Jedis getJedis() {
    	if (jedisPool != null){
    		Jedis resource = jedisPool.getResource();
    		return resource;
    	} else {
    		return null;
    	}
    }
    
    /**
     * 释放资源
     * 
     * 
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
    
    /**
     * 测试
     * */
    public static void main(String[] args) {
    	Jedis jedis = RedisUtils.getJedis();
		 //设置 redis 字符串数据
        jedis.set("runoobkey", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));
    }
}
