package app.core.repositories;


import app.core.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
    long countByName(String name);

    long countByEmail(String name);

    boolean existsByEmailAndPassword(String email, String password);

    Company getByEmailAndPassword(String email, String password);

//    List<Product> findAllByPriceLessThanEqual(double maxPrice);
}
