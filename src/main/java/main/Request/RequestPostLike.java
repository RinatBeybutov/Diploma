package main.Request;

public class RequestPostLike {
  private int post_id;

  public RequestPostLike(){}

  public RequestPostLike(int post_id)
  {
    this.post_id = post_id;
  }

  public int getPost_id() {
    return post_id;
  }

  public void setPost_id(int post_id) {
    this.post_id = post_id;
  }
}
