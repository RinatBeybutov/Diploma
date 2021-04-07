package main.Service;

import main.Model.Tag;
import main.Repository.TagRepository;
import main.Response.TagsResponse;
import main.Dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagsResponse getTags()
    {
        List<Tag> tagList = tagRepository.findAll();
        TagsResponse tagsResponse = new TagsResponse();
        AtomicReference<Float> maxWeight = new AtomicReference<>((float) 0);
        tagList.forEach(e ->{
            float temp = tagRepository.getWeightById(e.getId());
            if(temp > 0) {
                tagsResponse.getTags().add(new TagDto(e.getName(),
                        temp));
                if (maxWeight.get() < temp) {
                    maxWeight.set(temp);
                }
            }
        });

        tagsResponse.getTags().stream().forEach(e -> e.setWeight(e.getWeight() / maxWeight.get()));

        return tagsResponse;
    }

}
