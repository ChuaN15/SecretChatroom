package chuan.messengertry;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by chuan on 2/12/2019.
 */

@IgnoreExtraProperties
public class Room {

    public String name;

    public Room(String name) {
        this.name = name;
    }
}
