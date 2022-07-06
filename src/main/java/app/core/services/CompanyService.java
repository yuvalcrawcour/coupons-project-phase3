package app.core.services;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.exceptions.CouponSystemServiceException;
import app.core.exceptions.CouponSystemServiceExceptionBadRequest;
import app.core.exceptions.CouponSystemServiceExceptionNotFound;
import app.core.exceptions.CouponSystemServiceExceptionUnauthorized;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
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

public class CompanyService extends ClientService{


    @Override
    public boolean login(String email, String password) throws CouponSystemServiceException {
        if(this.companyRepository.existsByEmailAndPassword(email, password)){
//            this.companyId = this.companyRepository.getByEmailAndPassword(email,password).getId();
            return true;
        }
        return false;
    }
    @Autowired
    public CompanyService(CompanyRepository companyRepository, CouponRepository couponRepository) {
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
    }

    public void addCoupon(Coupon coupon,int companyId) throws CouponSystemServiceExceptionBadRequest {
//        if(coupon.getCompany() == null){
//            throw new CouponSystemServiceExceptionBadRequest("coupon has to have company");
//        }
//        if(companyId!=this.companyId){
//            throw new CouponSystemServiceExceptionBadRequest("the company of the coupon has to be similar to  company that owns the service");
//        }

        if(coupon.getStartDate().isBefore(LocalDate.now())||coupon.getEndDate().isBefore(coupon.getStartDate())){
            throw new CouponSystemServiceExceptionBadRequest("something is wrong with the dates of the coupon");
        }
        if(this.couponRepository.existsById(coupon.getId())){
            throw new CouponSystemServiceExceptionBadRequest("coupon with that id already exist");
        }
        for (Coupon coup:
                getCompanyCoupons(companyId)) {
            if(coup.getTitle().equals(coupon.getTitle())){
                throw new CouponSystemServiceExceptionBadRequest("coupon with that title already exists");
            }
        }
        coupon.setCompany(new Company(companyId));
        this.couponRepository.save(coupon);
    }
    public void updateCoupon(Coupon coupon,int companyId) throws CouponSystemServiceExceptionUnauthorized, CouponSystemServiceExceptionNotFound, CouponSystemServiceExceptionBadRequest {
//        if (companyId!=companyId){
//            throw new CouponSystemServiceExceptionUnauthorized("cannot update coupons of other companies");
//        }
        if(coupon.getStartDate().isBefore(LocalDate.now())||coupon.getEndDate().isBefore(coupon.getStartDate())){
            throw new CouponSystemServiceExceptionBadRequest("something is wrong with the dates of the coupon");
        }
        for (Coupon coup:
                getCompanyCoupons(companyId)) {
            if(coup.getTitle().equals(coupon.getTitle())&&coup.getId()!=coupon.getId()){
                throw new CouponSystemServiceExceptionBadRequest("coupon with that title already exists");
            }
        }
        Optional<Coupon> opt = this.couponRepository.findById(coupon.getId());
        if(!opt.isPresent()){
            throw new CouponSystemServiceExceptionNotFound("coupon was not found");
        }
        Coupon couponFromDb = opt.get();
//        if(couponFromDb.getCompany().getId()!=coupon.getCompany().getId()){
//            throw new CouponSystemServiceExceptionBadRequest("cannot change the company of the coupon");
//        }
        couponFromDb.setAmount(coupon.getAmount());
        couponFromDb.setCategory(coupon.getCategory());
        couponFromDb.setDescription(coupon.getDescription());
        couponFromDb.setEndDate(coupon.getEndDate());
        couponFromDb.setStartDate(coupon.getStartDate());
        couponFromDb.setImage(coupon.getImage());
        couponFromDb.setPrice(coupon.getPrice());
        couponFromDb.setTitle(coupon.getTitle());
        System.out.println("coupon "+ couponFromDb.getId()+ " was updated");


    }
    public void deleteCoupon(int id) throws CouponSystemServiceExceptionNotFound, CouponSystemServiceExceptionUnauthorized {
        Optional<Coupon> opt = couponRepository.findById(id);
        if(!opt.isPresent()){
            throw new CouponSystemServiceExceptionNotFound("coupon to be deleted was not found");
        }
        Coupon coupon = opt.get();
//        if(coupon.getCompany().getId()!= this.companyId){
//            throw new CouponSystemServiceExceptionUnauthorized("coupon to be deleted is not a coupon of that company");
//        }
        couponRepository.deleteById(id);
        System.out.println("coupon " + id + " was deleted");

    }
    public List<Coupon> getCompanyCoupons(int companyId) {
//        Company company = this.getCompanyDetails();
//        System.out.println(company.getCoupons());
//        return company.getCoupons();
        return this.couponRepository.findByCompanyId(companyId);
    }
    public ArrayList<Coupon> getCompanyCoupons(Coupon.Category category, int companyId)  {
        return this.couponRepository.findByCompanyAndCategory(new Company(companyId),category);
    }
    public ArrayList<Coupon> getCompanyCoupons(double maxPrice,int companyId){
        return this.couponRepository.findByCompanyAndPriceLessThanEqual(new Company(companyId),maxPrice);
    }
    public Company getCompanyDetails(int companyId) throws CouponSystemServiceExceptionNotFound {
        Optional<Company> opt =  this.companyRepository.findById(companyId);
        if(opt.isPresent()){
            return opt.get();
        }
        else{
            throw new CouponSystemServiceExceptionNotFound("company not found");
        }
    }

}
