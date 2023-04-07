package ru.maxim.effectivemobiletesttask.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.maxim.effectivemobiletesttask.dto.*;
import ru.maxim.effectivemobiletesttask.entity.*;

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

    public Grade gradeDtoToEntity(GradeDto.Request gradeDto) {
        return mapper.map(gradeDto, Grade.class);
    }

    public GradeDto.Response entityToGradeDto(Grade grade){
        return mapper.map(grade, GradeDto.Response.class);
    }

    public Notification notificationDtoToEntity(NotificationDto.Request notificationDto) {
        return mapper.map(notificationDto,Notification.class);
    }

    public NotificationDto.Response entityToNotificationDto(Notification notification){
        return mapper.map(notification, NotificationDto.Response.class);
    }

    public Organization organizationDtoToEntity(OrganizationDto.Request organizationDto) {
        return mapper.map(organizationDto, Organization.class);
    }

    public Product productDtoToEntity(ProductDto.Request productDto) {
        return mapper.map(productDto, Product.class);
    }
    public ProductDto.Response entityToProductDto(Product product){
        return mapper.map(product, ProductDto.Response.class);
    }

    public User userDtoToEntity(UserDto.Request productDto){
        return mapper.map(productDto, User.class);
    }

    public UserDto.Response entityToUserDto(User user){
        return mapper.map(user, UserDto.Response.class);
    }
}
