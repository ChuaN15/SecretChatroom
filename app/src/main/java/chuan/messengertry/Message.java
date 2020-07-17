package chuan.messengertry;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chuan on 2/11/2019.
 */

public class Message implements Comparable<Message> {
    public String Subject;
    public String DateTime;
    public String Room;
    public String sender;
    public int leftright;
    public int showDate;

    public Message(String Subject,String DateTime,String Room,String sender,int leftright,int showDate)
    {
        this.Subject = Subject;
        this.DateTime = DateTime;
        this.Room = Room;
        this.sender = sender;
        this.leftright = leftright;
        this.showDate = showDate;
    }

    public Message ()
    {

    }

    @Override
    public int compareTo(Message next)
    {
        try
        {
            Date date1=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(DateTime);
            Date date2=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(next.DateTime);
            return date1.compareTo(date2);
        }
        catch (Exception e)
        {
            return DateTime.compareTo(next.DateTime);
        }
    }
}
