package app.core.services;

import app.core.exceptions.CouponSystemServiceException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;


public abstract class ClientService {

    protected CouponRepository couponRepository;
    protected CompanyRepository companyRepository;
    protected CustomerRepository customerRepository;


    public abstract boolean login(String email, String password) throws CouponSystemServiceException;

}
