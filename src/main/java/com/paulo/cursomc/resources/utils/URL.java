package com.paulo.cursomc.resources.utils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	
	public static String decodeParam(String s) {
		try {
			return	URLDecoder.decode(s, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}
	
	public static List<Integer> deccodeIntList(String s){
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		
		for(String ve:  vet) {
			list.add(Integer.parseInt(ve));
			}
		
		return list;
		
		
	}
}
