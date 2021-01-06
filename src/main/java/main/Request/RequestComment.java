package main.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestComment {

  @JsonProperty("parent_id")
  private int parentId = 0;

  @JsonProperty("post_id")
  private int postId;

  private String text;

  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
