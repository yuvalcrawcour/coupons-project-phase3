package app.core.controllers;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemServiceException;
import app.core.exceptions.CouponSystemServiceExceptionBadRequest;
import app.core.exceptions.CouponSystemServiceExceptionNotFound;
import app.core.services.AdminService;
import app.core.token.TokensManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class AdminController extends ClientController {

    @Autowired
    private TokensManager tokensManager;
    @Autowired
    private AdminService adminService;


    @Override
    @PostMapping
    public String login(String email, String password) {
        try {
            if (adminService.login(email, password)) {
                return "token";
            } else {
                return null;
            }
        } catch (CouponSystemServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("add-company")
    public void addCompany(@RequestBody Company company, @RequestHeader String token) {
        try {
            System.out.println(company);
            this.adminService.addCompany(company);
        } catch (CouponSystemServiceExceptionBadRequest e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("update-company")
    public void updateCompany(@RequestBody Company company, @RequestHeader String token) {
        try {
            System.out.println(company);
            this.adminService.updateCompany(company);

        } catch (CouponSystemServiceExceptionNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (CouponSystemServiceExceptionBadRequest e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("delete-company/{companyId}")
    public void deleteCompany(@PathVariable int companyId, @RequestHeader String token) {
        try {
            this.adminService.deleteCompany(companyId);
        } catch (CouponSystemServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("get-all-companies")
    public ArrayList<Company> updateCompany(@RequestHeader String token) {
        return this.adminService.getAllCompanies();
    }


    @GetMapping("get-one-company/{companyId}")
    public Company getOneCompany(@PathVariable int companyId, @RequestHeader String token) {
        try {
            return this.adminService.getOneCompany(companyId);
        } catch (CouponSystemServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/add-customer")
    public void addCustomer(@RequestBody Customer customer, @RequestHeader String token) {
        try {
            this.adminService.addCustomer(customer);
        } catch (CouponSystemServiceExceptionBadRequest e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update-customer")
    public void updateCustomer(@RequestBody Customer customer, @RequestHeader String token) {
        try {
            this.adminService.updateCustomer(customer);
        } catch (CouponSystemServiceExceptionNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete-customer/{customerId}")
    public void deleteCustomer(@PathVariable int customerId) {
        try {
            this.adminService.deleteCustomer(customerId);
        } catch (CouponSystemServiceExceptionNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/get-all-customers")
    public ArrayList<Customer> getAllCustomers() {
        return adminService.getAllCustomers();
    }

    @GetMapping("/get-one-customer/{customerId}")
    public Customer getOneCustomer(@PathVariable int customerId) {
        try {
            return adminService.getOneCustomer(customerId);
        } catch (CouponSystemServiceExceptionNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


}
