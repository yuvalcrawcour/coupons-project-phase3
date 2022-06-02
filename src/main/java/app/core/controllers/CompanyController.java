package app.core.controllers;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemServiceException;
import app.core.exceptions.CouponSystemServiceExceptionBadRequest;
import app.core.exceptions.CouponSystemServiceExceptionNotFound;
import app.core.exceptions.CouponSystemServiceExceptionUnauthorized;
import app.core.services.AdminService;
import app.core.services.CompanyService;
import app.core.token.TokensManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/company")
public class CompanyController extends ClientController{

    @Autowired
    private TokensManager tokensManager;
    @Autowired
    private CompanyService companyService;


    @Override
    @PostMapping
    public String login(String email, String password) {

        return null;
    }

    public CompanyController() {
    }

    @PostMapping("/add-coupon")
    public void addCompany (@RequestBody Coupon coupon, @RequestHeader String token){
        try {
            this.companyService.addCoupon(coupon,tokensManager.extractClient(token).getClientId());
        } catch (CouponSystemServiceExceptionBadRequest e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update-coupon")
    public void updateCoupon (@RequestBody Coupon coupon,@RequestHeader String token){
        try {
            this.companyService.updateCoupon(coupon,tokensManager.extractClient(token).getClientId());

        } catch (CouponSystemServiceExceptionNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (CouponSystemServiceExceptionUnauthorized e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (CouponSystemServiceExceptionBadRequest e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete-coupon/{couponId}")
    public void deleteCoupon (@PathVariable int couponId,@RequestHeader String token){
        try {
            this.companyService.deleteCoupon(couponId);
        } catch (CouponSystemServiceExceptionNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (CouponSystemServiceExceptionUnauthorized e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }



    @GetMapping("/get-company-details")
    public Company getOneCompany (@RequestHeader String token){
        try {
            return this.companyService.getCompanyDetails(tokensManager.extractClient(token).getClientId());
        } catch (CouponSystemServiceExceptionNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/get-company-coupons")
    public List<Coupon> getCompanyCoupons(@RequestHeader String token) {
        return companyService.getCompanyCoupons(this.tokensManager.extractClient(token).clientId);
    }

    @GetMapping("/get-company-coupons-category/{category}")
    public List<Coupon> getCompanyCoupons(@PathVariable String category, @RequestHeader String token) {
        return companyService.getCompanyCoupons(Coupon.Category.valueOf(category),this.tokensManager.extractClient(token).clientId);
    }

    @GetMapping("/get-company-coupons-max-price/{maxPrice}")
    public List<Coupon> getCompanyCoupons(@PathVariable double maxPrice, @RequestHeader String token) {
        return companyService.getCompanyCoupons(maxPrice,this.tokensManager.extractClient(token).clientId);
    }



}
