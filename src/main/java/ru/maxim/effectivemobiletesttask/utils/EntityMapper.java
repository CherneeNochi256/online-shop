package ru.maxim.effectivemobiletesttask.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.maxim.effectivemobiletesttask.dto.CommentDto;
import ru.maxim.effectivemobiletesttask.dto.DiscountDto;
import ru.maxim.effectivemobiletesttask.entity.Comment;
import ru.maxim.effectivemobiletesttask.entity.Discount;

@Component
public class EntityMapper {

    private final ModelMapper mapper;

    public EntityMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Comment commentDtoToEntity(CommentDto.Request commentDto){
        return mapper.map(commentDto,Comment.class);
    }

    public CommentDto.Response entityToCommentDto(Comment comment){
        return mapper.map(comment, CommentDto.Response.class);
    }

    public Discount discountDtoToEntity(DiscountDto.Request discountDto) {
        return mapper.map(discountDto, Discount.class);
    }
    public DiscountDto.Response entityToDiscountDto(Discount discount) {
        return mapper.map(discount,DiscountDto.Response.class);
    }
}
