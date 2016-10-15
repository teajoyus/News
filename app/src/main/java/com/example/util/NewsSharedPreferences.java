package com.example.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.entry.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsSharedPreferences {
	Context context;
	SharedPreferences mySharedPreferences;
	public NewsSharedPreferences(Context context,String key){
		this.context=context;
		 mySharedPreferences= context.getSharedPreferences(key,
				Activity.MODE_PRIVATE); 	
	}
	public void save(String key,String value) {
		//实例化SharedPreferences.Editor对象
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//用putString的方法保存数据 
		editor.putString(key, value);

		//提交当前数据 
		editor.commit(); 

	}
	public String load(String key){
		// 使用getString方法获得value，注意第2个参数是value的默认值
		return mySharedPreferences.getString(key, ""); 
	}
	public void remove(String key){
		// 使用getString方法获得value，注意第2个参数是value的默认值
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		 editor.remove(key);
		editor.commit();

	}
	public void removeAll(){
		// 使用getString方法获得value，注意第2个参数是value的默认值
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.clear();
		editor.commit();

	}

	public List<Label> getAll(){
		List<Label> list = new ArrayList<Label>();
		Label label =null;
		Map<String, ?> allContent = mySharedPreferences.getAll();
		//注意遍历map的方法
		for(Map.Entry<String, ?>  entry : allContent.entrySet()){
			label =new Label();
			label.setKey((String) entry.getKey());
			label.setName((String)entry.getValue());
			list.add(label);
		}
		return list;
	}

}
