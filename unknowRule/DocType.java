package unknowRule;

/**
 * 
     * 类型
     * 
     * @2010-3-18
 */
public enum DocType {
	/**
	 * 目的地名称
	 */
	Destination,
	/**
	 * 地名
	 */
	Address, 
	/**
	 * 后缀
	 */
	Suffix,
	/**
	 * 前缀
	 */
	Prefix,
	/**
	 * 虚拟类型，开始状态
	 */
	Start,
	/**
	 * 虚拟类型，结束状态
	 */
	End,
	/**
	 * 未知词
	 */
	Unknow,
	/**
	 * 链接符号
	 */
	Link,
	/**
	 * 时间
	 */
	Time,
	/**
	 * 日期
	 */
	Date,
	/**
	 * 开始标签
	 */
	StartBraket,
	/**
	 * 结束标签
	 */
	EndBraket,
	/**
	 * 其他
	 */
	Other,
	/**
	 * 开始符号
	 */
	GuillemetStart,
	/**
	 * 结束符号
	 */
	GuillemetEnd, 
}

