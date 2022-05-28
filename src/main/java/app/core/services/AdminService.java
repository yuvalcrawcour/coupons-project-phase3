package app.core.services;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemServiceException;
import app.core.exceptions.CouponSystemServiceExceptionBadRequest;
import app.core.exceptions.CouponSystemServiceExceptionNotFound;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class AdminService extends ClientService{


    @Autowired
    public AdminService(CompanyRepository companyRepository, CouponRepository couponRepository, CustomerRepository customerRepository){
        this.couponRepository = couponRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
    }
    @Override
    public boolean login(String email, String password) throws CouponSystemServiceException {
        if(email.equals("admin@admin.com")&&password.equals("admin")){
            return true;
        }
        return false;
    }

    public void addCompany (Company company) throws CouponSystemServiceExceptionBadRequest {
        //check if a company with the same name already exists
        if (companyRepository.countByName(company.getName())>0){
            throw new CouponSystemServiceExceptionBadRequest("company with that name already exist");
        }
        //check if a company with the same email already exists
        if (companyRepository.countByEmail(company.getEmail())>0){
            throw new CouponSystemServiceExceptionBadRequest("company with that email already exist");
        }
        //if not, save the company
        this.companyRepository.save(company);

    }
    public void updateCompany(Company company) throws CouponSystemServiceExceptionBadRequest,CouponSystemServiceExceptionNotFound {
        Company companyFromDb;
        try {
            //check if there is an attempt to change the company id
            companyFromDb = getOneCompany(company.getId());
        } catch (CouponSystemServiceException e) {
            throw new CouponSystemServiceExceptionNotFound("company with that id not found");
        }
        //check if there is an attempt to change the company name
        if(!company.getName().equals(companyFromDb.getName())){
            throw new CouponSystemServiceExceptionBadRequest("cannot change company name");
        }

        //if not, update the company
        companyFromDb.setEmail(company.getEmail());
        companyFromDb.setPassword(company.getPassword());

    }
    public void deleteCompany(int companyID) throws CouponSystemServiceException {
        companyRepository.delete(getOneCompany(companyID));
    }

    public ArrayList<Company> getAllCompanies (){
        return (ArrayList<Company>) companyRepository.findAll();
    }


    public Company getOneCompany(int companyId) throws CouponSystemServiceException {
        Optional<Company> opt = this.companyRepository.findById(companyId);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new CouponSystemServiceException("company not found");
        }
    }
    public void addCustomer (Customer customer) throws CouponSystemServiceExceptionBadRequest {
        //check if customer email exist
        if(customerRepository.existsByEmail(customer.getEmail())){
            throw new CouponSystemServiceExceptionBadRequest("customer with that email already exists");
        }
        else{
            customerRepository.save(customer);
        }

    }
    public void updateCustomer(Customer customer) throws CouponSystemServiceExceptionNotFound {
        //check if such customer exists
        Customer customerFromDb = getOneCustomer(customer.getId());
        customerFromDb.setEmail(customer.getEmail());
        customerFromDb.setFirstName(customer.getFirstName());
        customerFromDb.setLastName(customer.getLastName());
        customerFromDb.setPassword(customer.getPassword());

    }
    public void deleteCustomer (int customerID) throws CouponSystemServiceExceptionNotFound {
        this.customerRepository.delete(getOneCustomer(customerID));
    }

    public ArrayList<Customer> getAllCustomers() {
        return (ArrayList<Customer>) this.customerRepository.findAll();
    }


    public Customer getOneCustomer (int customerID) throws CouponSystemServiceExceptionNotFound {
        Optional<Customer> opt = customerRepository.findById(customerID);
        if (opt.isPresent()){
            return opt.get();
        }
        else{
            throw new CouponSystemServiceExceptionNotFound("customer was not found");
        }
    }


    public void checkExpired() {
//        List<Coupon> listOfExpired = this.couponRepository.findAllById(Arrays.asList(3));
//        System.out.println(listOfExpired);
        this.couponRepository.deleteByEndDateLessThan(LocalDate.now());
    }
}
