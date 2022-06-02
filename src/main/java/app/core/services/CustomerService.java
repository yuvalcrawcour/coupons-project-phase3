package app.core.services;

import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemServiceException;
import app.core.exceptions.CouponSystemServiceExceptionBadRequest;
import app.core.exceptions.CouponSystemServiceExceptionNotFound;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class CustomerService extends ClientService{



    @Autowired
    public CustomerService(CustomerRepository customerRepository, CouponRepository couponRepository) {
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public boolean login(String email, String password) throws CouponSystemServiceException {
        if(this.customerRepository.existsByEmailAndPassword(email, password)){
            return true;
        }
        return false;

    }
    public List<Coupon> getAllCoupons(){
        return this.couponRepository.findAll();
    }

    public void purchaseCoupon(Coupon coupon,int customerId) throws CouponSystemServiceExceptionNotFound, CouponSystemServiceExceptionBadRequest {
        for (Coupon cur:
             getCustomerCoupons(customerId)) {
            if(cur.getId() == coupon.getId()){
                throw new CouponSystemServiceExceptionBadRequest("cannot buy the same coupon more than once");
            }
        }
        if(!this.couponRepository.existsById(coupon.getId())){
            throw new CouponSystemServiceExceptionNotFound("coupon with that id does not exist, you need to add it first");
        }
        Coupon couponFromDb = this.couponRepository.getById(coupon.getId());
        if(couponFromDb.getAmount()<=0){
            throw new CouponSystemServiceExceptionBadRequest("no coupons of that type left");
        }
        if(couponFromDb.getEndDate().isBefore(LocalDate.now())){
            throw new CouponSystemServiceExceptionBadRequest("coupon is expired");
        }
        Customer thisCustomer = this.getCustomerDetails(customerId);
        thisCustomer.purchaseCoupon(couponFromDb);
        couponFromDb.setAmount(couponFromDb.getAmount()-1);
    }

    public List<Coupon> getCustomerCoupons(int customerId){
//        return getCustomerDetails().getCoupons();
        return this.couponRepository.findByCustomerListId(customerId);
    }

    //TODO
    public ArrayList<Coupon> getCustomerCoupons(Coupon.Category category,int customerId)  {
        List<Integer> couponIdList = this.couponRepository.getCouponIdsByCustomerIdAndCategory(customerId,category.toString());
        ArrayList<Coupon> couponArrayList = new ArrayList<>();
        for (Integer cur:
             couponIdList) {
            couponArrayList.add(this.couponRepository.findById(cur).get());
        }
        return couponArrayList;
    }
    //TODO
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice,int customerId) {
        List<Integer> couponIdList = this.couponRepository.getCouponIdsByCustomerIdAndMaxPrice(customerId,maxPrice);
        ArrayList<Coupon> couponArrayList = new ArrayList<>();
        for (Integer cur:
                couponIdList) {
            couponArrayList.add(this.couponRepository.findById(cur).get());
        }
        return couponArrayList;
    }

    public Customer getCustomerDetails(int customerId) throws CouponSystemServiceExceptionNotFound {
        Optional<Customer> opt =  this.customerRepository.findById(customerId);
        if(!opt.isPresent()){
            throw new CouponSystemServiceExceptionNotFound("customer was not found");
        }
        return opt.get();
    }
}
