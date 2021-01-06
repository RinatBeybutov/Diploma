package main.Response;

import main.dto.TagDto;

import java.util.ArrayList;

public class TagsResponse {
    private ArrayList<TagDto> tags;

    public TagsResponse()
    {
        tags = new ArrayList<>();
    }

    public ArrayList<TagDto> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagDto> tags) {
        this.tags = tags;
    }
}
