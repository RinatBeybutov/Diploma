package main.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_votes")
public class Vote {

    public Vote()
    {}

    public Vote( UserModel user, Post post, byte value) {
        this.user = user;
        this.post = post;
        Value = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private UserModel user;

    @OneToOne
    private Post post;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private byte Value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public byte getValue() {
        return Value;
    }

    public void setValue(byte value) {
        Value = value;
    }
}
