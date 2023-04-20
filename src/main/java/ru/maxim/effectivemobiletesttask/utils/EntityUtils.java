package ru.maxim.effectivemobiletesttask.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.CanNotPerformActionException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EntityUtils {

    public static void copyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }


    public static void isOrganizationOwner(User user, Organization organization) {
        if (!organization.getUser().equals(user))
            throw new CanNotPerformActionException("User is not the owner of the organization");
    }

    public static void isProductOwner(Product productFromDb, User user) {
        if (!productFromDb.getOrganization().getUser().equals(user))
            throw new CanNotPerformActionException("User is not the owner of the product");
    }

    public static void isOrganizationActive(Organization organization){
        if (!organization.getStatus().equals("ACTIVE"))
            throw new CanNotPerformActionException("Organization status is frozen");
    }

    public static void canBuyAProduct(User user,Product product){
        if (!(product.getPrice() < user.getBalance())) {
            throw new CanNotPerformActionException("User doesn't have enough money");
        }else if (product.getQuantity() <= 0 ){
            throw new CanNotPerformActionException("Product has been sold");
        }
    }

    public static void checkRefundDateNotExpired(PurchaseHistory purchase) {
        if (!(TimeUnit.MILLISECONDS.toHours(new Date().getTime() - purchase.getDate().getTime()) <= 24L))
            throw new CanNotPerformActionException("Product refund date has expired");
    }

    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
