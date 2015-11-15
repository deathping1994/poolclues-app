package in.poolclues.www.poolclues;

import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shyam Yadav on 21-Oct-15.
 */
public class ClsCreate_Event {
    private String email_id;
    public String getEmail_id() {
        return email_id;
    }
    public void setEmail_id(String value) {
        this.email_id = value;
    }

    private String event_name;
    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String value) {
        this.event_name = value;
    }

    private String target_date;
    public String getTarget_date() {
        return target_date;
    }

    public void setTarget_date(String value) {
        this.target_date = value;
    }

    private Integer target_amount;
    public Integer getTarget_amount() {
        return target_amount;
    }

    public void setTarget_amount(Integer value) {
        this.target_amount = value;
    }

    private String description;
    public String getDescription() {
        return email_id;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    private String invites;
    public String getInvites() {
        return invites;
    }

    public void setInvites(String value) {
        this.invites = value;
    }

    private String[] products;
    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] value) {
        this.products = value;
    }

    private String msg;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String value) {
        this.msg = value;
    }

    private String authtoken;
    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String value) {
        this.authtoken = value;
    }

    private String searchable;
    public String getSearchable()
    {
        return searchable;
    }
    public void setSearchable(String value)
    {
        this.searchable=value;
    }

    private JSONArray contributor;
    public JSONArray getContributor()
    {
        return contributor;
    }
    public void setContributor(JSONArray value)
    {
        this.contributor=value;

    }

    private String contributorsEmailid;
    public String getContributorsEmailid()
    {
        return  contributorsEmailid;
    }

    public void setContributorsEmailid(String value)
    {
        this.contributorsEmailid=value;
    }

    private int mycontribution;

    public int getMycontribution()
    {
        return  mycontribution;
    }

    public void setMycontribution(int val)
    {
        this.mycontribution=val;
    }

}
