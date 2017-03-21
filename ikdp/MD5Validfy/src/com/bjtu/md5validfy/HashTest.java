package com.bjtu.md5validfy;

import java.util.Random;

public class HashTest {
	
	public HashTest(){
		
	}
	
	public String hashPath(int id, int key){
		if(id == 0)
			return "";
		return "\\" + id%key + hashPath(id/key, key*2);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashTest h = new HashTest();
		Random random = new Random();
		long time1 = System.currentTimeMillis();
		
		for(int i = 0; i < 10000000; i++){
//			random.nextInt(1000000000);
			h.hashPath(random.nextInt(10000000), 2);
		}
		System.out.println(System.currentTimeMillis() - time1);
	}

}
