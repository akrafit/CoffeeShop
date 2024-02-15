package com.coffe.shop.repository;

import com.coffe.shop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Integer> {
}
