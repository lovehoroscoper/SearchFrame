package org.json;

public class TestJSONArray {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        JSONArray jsonarray = new JSONArray();
        jsonarray.put("lietu");
        jsonarray.put("lucene");
        System.out.println(jsonarray.toString());
	}

}
