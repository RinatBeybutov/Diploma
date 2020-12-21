package main.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

public class CalendarResponse {

    TreeSet<Integer> years;
    TreeMap <String, Integer> posts;

    public CalendarResponse()
    {
        years = new TreeSet<>();
        posts = new TreeMap<>();
    }

    public TreeSet<Integer> getYears() {
        return years;
    }

    public void setYears(TreeSet<Integer> years) {
        this.years = years;
    }

    public TreeMap<String, Integer> getPosts() {
        return posts;
    }

    public void setPosts(TreeMap<String, Integer> posts) {
        this.posts = posts;
    }
}
