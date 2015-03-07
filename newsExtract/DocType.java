package newsExtract;

public enum DocType {
    Start,    //��ʼ״̬
    End,    //����״̬
    PrefixAuthor,    //����ǰ׺
    SuffixAuthor,    //���ߺ�׺
    PrefixTitle,    //����ǰ׺
    SuffixTitle,    //�����׺
    Title,          //����
    PrefixText,    //����ǰ׺
    SuffixText,    //���Ľ�β
    Text,          //����
    PrefixInfoSource,    //��Ϣ��Դǰ׺
    InfoSource,    //��Ϣ��Դ
    PrefixPublishDate,    //����ʱ��ǰ׺
    PublishDate,    //����ʱ��
    Time,    // ��ʾʱ�����ʱ��
    Date,     // ��ʾ�����յ�ʱ��    
    Author,     //����
    PrefixTextItem,     //����ǰ׺����
    PrefixTitleItem,     //����ǰ׺����
    GuillemetStart, //��ʼ����	
    GuillemetEnd,     // ��������
    Num,     // ����
    Unknow,     //δ֪
    Other, Link,    // ���� 
}
