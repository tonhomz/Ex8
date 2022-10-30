/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagerdemo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import model.Address;
import model.Customer;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author sarun
 */
public class EntityManagerDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
//        createData();
        printAllCustomer();
        System.out.print("Find By City : ");
        String city = sc.next();
        System.out.println();
        printCustomerByCity(city);

    }

    public void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    public static void createData() {
        Long []id = {1L, 2L, 3L, 4L};
        String []firstName = {"John", "Marry", "Peter", "Bruce"};
        String []lastName = {"Henry", "Jane", "Parker", "Wayn"};
        String []email = {"jh@mail.com", "mj@mail.com", "pp@mail.com", "bw@mail.com"};
        String []street = {"123/4 Viphavadee Rd.", "123/5 Viphavadee Rd.", "543/21 Fake Rd.", "678/90 Unreal Rd."};
        String []city = {"Bangkok", "Bangkok", "Nonthaburi", "Pathumthani"};
        String []country = {"TH", "TH", "TH", "TH"};
        String []zipCode = {"10900", "10900", "20900", "30500"};
       
        for(int i = 0; i < 4; i++) {
            Customer customer = new Customer(id[i], firstName[i], lastName[i], email[i]); 
            Address address = new Address(id[i], street[i], city[i], zipCode[i], country[i]); 
            address.setCustomerFk(customer);
            customer.setAddressId(address);

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();
            try {
                em.persist(address);
                em.persist(customer);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            } finally {
                em.close();
            }
        }
    }
     
    public static List<Customer> findAllCustomer() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        List<Customer> customerList = (List<Customer>) em.createNamedQuery("Customer.findAll").getResultList();
        em.close();
        return customerList;
    }
    
    public static void printAllCustomer() {
        List<Customer> custList = findAllCustomer();
        for(Customer customer : custList) {
            System.out.println("First Name: " + customer.getFirstname());
            System.out.println("Last Name: " + customer.getLastname()+ " ");
            System.out.println("Email: " + customer.getEmail() + " ");
            System.out.println("Street: " + customer.getAddressId().getStreet()+ " ");
            System.out.println("City: " + customer.getAddressId().getCity()+ " ");
            System.out.println("Country: " + customer.getAddressId().getCountry()+ " ");
            System.out.println("Zip Code: " + customer.getAddressId().getZipcode()+ " ");
            System.out.println("------------------------------------------\n" +
                               "------------------------------------------");
        }
    }
    
    public static List<Customer> findByCity(String cityName) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        //String jpql = "SELECT c FROM Customer c WHERE c.addressId.city = ?1";
        String jpql = "SELECT c FROM Customer c WHERE c.addressId.city = :cityName";
        Query query = em.createQuery(jpql);
        //query.setParameter(1, cityName);
        query.setParameter("cityName", cityName);
        List<Customer> custList = (List<Customer>) query.getResultList();
        return custList;
    }
    
    public static void printCustomerByCity(String city) {
        List<Customer> custList = findByCity(city);
        for(Customer customer : custList) {
            System.out.println("First Name: " + customer.getFirstname());
            System.out.println("Last Name: " + customer.getLastname()+ " ");
            System.out.println("Email: " + customer.getEmail() + " ");
            System.out.println("Street: " + customer.getAddressId().getStreet()+ " ");
            System.out.println("City: " + customer.getAddressId().getCity()+ " ");
            System.out.println("Country: " + customer.getAddressId().getCountry()+ " ");
            System.out.println("Zip Code: " + customer.getAddressId().getZipcode()+ " ");
            System.out.println("------------------------------------------\n" +
                               "------------------------------------------");
        }
    }
}
