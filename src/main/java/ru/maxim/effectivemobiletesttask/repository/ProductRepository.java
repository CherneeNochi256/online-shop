package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.maxim.effectivemobiletesttask.entity.Product;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product,Long> {

   @Query("select p from Product p where p.id = :id")//почему-то  перестал работать дефолтный метод
   Optional<Product> findById(Long id);


   @Query("select p from Product p join Tag t on p.id = t.product.id where t.tag = :tag")
   Set<Product> findProductsByTag(String tag);

   @Query("select p from Product p join Tag t on p.id = t.product.id where t.tag = :tag")
   Product findProductByTag(String tag);

   @Query("select  p from Product p join Organization o on p.organization = o where o.status ='ACTIVE' ")
   List<Product> findAll();


}
