package app.core.repositories;


import app.core.entities.Company;
import app.core.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Integer> {
    ArrayList<Coupon> findByCompany(int companyId);
    ArrayList<Coupon> findByCompanyAndCategory(Company company, Coupon.Category category);
    ArrayList<Coupon> findByCompanyAndPriceLessThanEqual(Company company, double maxPrice);

    void deleteByEndDateLessThan(LocalDate now);

    @Query(value = "select coupon_id from customer_coupon\n" +
            "JOIN Coupon ON customer_coupon.coupon_id=coupon.id\n" +
            "where customer_coupon.customer_id=:customerId and category = :category ;", nativeQuery = true)
    List<Integer> getCouponIdsByCustomerIdAndCategory(int customerId, String category);

    @Query(value = "select coupon_id from customer_coupon\n" +
            "JOIN Coupon ON customer_coupon.coupon_id=coupon.id\n" +
            "where customer_coupon.customer_id=:customerId and price <= :maxPrice ;", nativeQuery = true)
    List<Integer> getCouponIdsByCustomerIdAndMaxPrice(int customerId, double maxPrice);

    List<Coupon> findByCompanyId(int companyId);

    List<Coupon> findByCustomerListId(int customerId);

//    List<Coupon> findByIdLessThan(int id);


//    @Query(value = "select  * from students where gender='M'", nativeQuery = true)
//    List<Student> getMales();

//    @Query("from talmid where age > :minAge")
//    List<Student> getOlderThan(int minAge);

//    List<Store> findAllByNameStartsWith(String prefix);
}
