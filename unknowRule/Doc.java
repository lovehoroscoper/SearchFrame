package unknowRule;

public class Doc {

	public Doc(String p1,long c1, String key1, String feature1, String fun1, String p2,
			String key2, String feature2, String fun2, String smallAddress,
			String matter, String key3, String time) {
		this.place1 = p1;
		this.placeCode1 = c1;
		this.keyWord1 = key1;
		this.feature1 = feature1;
		this.function1 = fun1;
		this.place2 = p2;
		this.keyWord2 = key2;
		this.feature2 = feature2;
		this.function2 = fun2;
		this.smallAddress = smallAddress;
		this.matter = matter;
		this.keyWord3 = key3;
		this.time = time;
	}

	public String place1;
	
	public long placeCode1;

	public String keyWord1;

	public String feature1;

	public String function1;

	public String place2;

	public String keyWord2;

	public String keyWord3;

	public String feature2;

	public String function2;

	public String smallAddress;

	public String matter;

	public String time;

	public String toString() {
		return place1 + "  :" + placeCode1 + "  :" + keyWord1 + "  :" + feature1 + "  :" + function1
				+ "  :" + place2 + "  :" + keyWord2 + "  :" + feature2 + "  :"
				+ function2 + "  :" + smallAddress + "  :" + matter + "  :"
				+ keyWord3 + "  :" + time;
	}
}
