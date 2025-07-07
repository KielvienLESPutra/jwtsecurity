package kielvien.lourensius.ekasetiaputra.jwtsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
