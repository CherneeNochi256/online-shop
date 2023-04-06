package ru.maxim.effectivemobiletesttask.utils;

import org.springframework.stereotype.Component;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.exception.ResourceIsNullException;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;

import java.util.Set;


@Component
public class RestPreconditions {
    public static User checkUser(User user) {
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }

    public static Organization checkOrganization(Organization organization){
        if (organization == null){
            throw new ResourceNotFoundException("Organization not found");
        }
        return organization;
    }

    public static Discount checkDiscount(Discount discount){
        if (discount == null){
            throw new ResourceNotFoundException("Discount not found");
        }
        return discount;
    }

        public static <T> T checkNotNull(T resource) {
        if (resource == null) {
            throw new ResourceIsNullException("Request body is null");
        }
        return resource;
    }

    public static Product checkProduct(Product product){
        if (product == null){
            throw new ResourceNotFoundException("Product not found");
        }
        return product;
    }

    public static Set<PurchaseHistory> checkPurchaseHistory(Set<PurchaseHistory> purchaseHistory){
        if (purchaseHistory == null){
            throw new ResourceNotFoundException("User doesn't hava any purchases");
        }
        return purchaseHistory;
    }
    public static Set<Notification> checkNotifications(Set<Notification> notifications){
        if (notifications == null){
            throw new ResourceNotFoundException("User doesn't hava any notifications");
        }
        return notifications;
    }


    public static Comment checkComment(Comment comment) {
        if (comment == null){
            throw new ResourceNotFoundException("Comment not found");
        }
        return comment;
    }
}


