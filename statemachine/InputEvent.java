package statemachine;

public class InputEvent {
	public static InputEvent codeStart = new InputEvent("(");//���뿪ʼ
	public static InputEvent codeOne=new InputEvent("codeOne");//һλ����
	public static InputEvent code = new InputEvent("code");//����3λ
	public static InputEvent codeEnd = new InputEvent(")");//�������
	public static InputEvent phoneNumber = new InputEvent("number4");//����4λ
	public static InputEvent phoneNum7=new InputEvent("number7");//����7λ
	public static InputEvent phoneNum6=new InputEvent("number6");//����6λ
	public static InputEvent phoneNum5=new InputEvent("number5");//����6λ
	public static InputEvent phoneNum2=new InputEvent("number2");//2λ
    public static InputEvent splitEventD=new InputEvent(".");//��ָ���
    public static InputEvent splitEventJ=new InputEvent("+"); //+����
    public static InputEvent splitEventx=new InputEvent("/"); ///����
	
	public static InputEvent yearNum = new InputEvent("Y");
	public static InputEvent digital2 = new InputEvent("D2");
	public static InputEvent digital1 = new InputEvent("D1");
	public static InputEvent yearUnitEvent = new InputEvent("��");
	public static InputEvent monthUnitEvent = new InputEvent("��");
	public static InputEvent dayUnitEvent = new InputEvent("��");
	public static InputEvent splitEvent = new InputEvent("-");
	
	public static InputEvent timeUnitEvent = new InputEvent("ʱ");
	public static InputEvent minuteUnitEvent = new InputEvent("��");
	public static InputEvent secondUnitEvent = new InputEvent("��");
	public static InputEvent colonEvent = new InputEvent(":");
	public static InputEvent blankEvent = new InputEvent(" ");
	
	public String word;
	
	public InputEvent(String w)
	{
		this.word = w;
	}
	
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof InputEvent)) {
            return false;
        }
        InputEvent other = (InputEvent) obj;
		
		if(this.word!=null)
		{
			if(!this.word.equals(other.word))
			{
				return false;
			}
			return true;
		}
		
		return true;
    }
    
	public String toString()
	{
		return word;
	}
}
