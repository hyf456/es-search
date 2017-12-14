package com.hivescm.service.terminal;

import com.google.gson.Gson;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/22
 */
public class TerminalTest {
	private static final Gson GSON = new Gson();

	@Test
	public void testTerminal() throws Exception {

		//		curl -XPOST '10.12.31.110:9200/test_ool/ool/1/_update?pretty' -d'
		//		{
		//			"script" : "if(ctx._source.student.books==null){ctx._source.student.books=listValue}else{ctx._source
		// .student.books.add(objectValue)}",
		//				"params" : {
		//			listValue:[{"size":2017,"name":"rabbitmq in action","id":5}],
		//			objectValue:{"size":2017,"name":"rabbitmq in action","id":5}
		//		}
		//		}
		//		'

		Map<String, String> param = new HashMap<>();
		String script = "if(ctx._source.student.books==null){ctx._source.student.books=listValue}else{ctx._source.student"
				+ ".books.add(objectValue)}";
		param.put("script", script);

		Map<String, String> params = new HashMap<>();

		Map<String, String> book = new HashMap<>();
		book.put("name", "http");
		book.put("id", "15");

		List<Map<String, String>> books = new ArrayList<>();
		books.add(book);

		params.put("listValue", GSON.toJson(books));
		params.put("objectValue", GSON.toJson(book));

		param.put("params", GSON.toJson(params));

		StringBuilder sb = new StringBuilder();
		sb.append("CURL -XPOST '10.12.31.110:9200/test_ool/ool/1/_update' -d '" + GSON.toJson(param) + "'");

		sb.append("CURL -XGET '10.12.31.110:9200/test_ool/ool/1'");
		//String[] commandArray = new String[]{"CURL","-XPOST","","1","_update'"," -d '", GSON.toJson(param),"'"};
		Process process = Runtime.getRuntime().exec(sb.toString());

		InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(inputStreamReader);
		String line;
		while ((line = input.readLine()) != null) {
			System.out.println(line);
		}
	}
}
