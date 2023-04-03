package ru.maxim.effectivemobiletesttask.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.repository.*;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    private final CommentRepository commentRepository;
    private final GradeRepository gradeRepository;

    private final OrganizationsRepository organizationsRepository;



    public UserService(UserRepository userRepository, PurchaseHistoryRepository purchaseHistoryRepository, CommentRepository commentRepository, GradeRepository gradeRepository, OrganizationsRepository organizationsRepository) {
        this.userRepository = userRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.commentRepository = commentRepository;
        this.gradeRepository = gradeRepository;
        this.organizationsRepository = organizationsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public void createUser(User user){
        userRepository.save(user);
    }

    public void buyProduct(Product product, User user) {
        if (product.getPrice() < user.getBalance()) {
            user.setBalance(user.getBalance() - product.getPrice());

            product.getOrganization().getUser().setBalance(product.getOrganization().getUser().getBalance() + product.getPrice() - (product.getPrice() * 0.05));
            product.setQuantity(product.getQuantity() - 1);

            PurchaseHistory purchaseHistory = new PurchaseHistory();

            purchaseHistory.setUser(user);
            purchaseHistory.setProduct(product);
            purchaseHistory.setDate(new Date());
            purchaseHistoryRepository.save(purchaseHistory);
        }
    }

    public void refundProduct(Product product, User user, Set<PurchaseHistory> purchaseHistory) {
        for (PurchaseHistory purchase : purchaseHistory) {
            if (purchase.getProduct().equals(product) && TimeUnit.MILLISECONDS.toHours(new Date().getTime() - purchase.getDate().getTime()) <= 24L) {
                user.setBalance(user.getBalance() + product.getPrice());
                product.getOrganization().getUser().setBalance(product.getOrganization().getUser().getBalance() - product.getPrice());
                product.setQuantity(product.getQuantity() + 1);
                purchaseHistoryRepository.removeById(purchase.getId());
            }
        }
    }

    public void commentProduct(Product product, User user, Comment comment, Set<PurchaseHistory> purchases) {
        for (PurchaseHistory purchase : purchases) {
            if (purchase.getProduct().equals(product)) {

                Comment resultComment = new Comment();

                resultComment.setUser(user);
                resultComment.setMessage(comment.getMessage());
                resultComment.setProduct(product);

                commentRepository.save(resultComment);
            }
        }
    }

    public void estimateProduct(Product product, User user, Grade grade, Set<PurchaseHistory> purchases) {
        for (PurchaseHistory purchase : purchases) {
            if (purchase.getProduct().equals(product)) {

                Grade resultGrade = new Grade();

                resultGrade.setUser(user);
                resultGrade.setValue(grade.getValue());
                resultGrade.setProduct(product);

                gradeRepository.save(resultGrade);
            }
        }
    }


    public void createOrganizationByUser(User user, Organization organization) {
        Organization resultOrganization = new Organization();

        BeanUtils.copyProperties(organization, resultOrganization, "id", "status");
        user.getRoles().add(Role.ORG_OWNER);
        resultOrganization.setUser(user);
        resultOrganization.setStatus("ACTIVE");

        userRepository.save(user);

        organizationsRepository.save(resultOrganization);
    }

    public User userById(Long id){
        return userRepository.findById(id).get();
    }

}
