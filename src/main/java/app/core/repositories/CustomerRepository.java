package app.core.repositories;


import app.core.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndPassword(String email, String password);

    Customer getByEmailAndPassword(String email, String password);
//    List<Store> findAllByNameStartsWith(String prefix);
}
