package com.globant.library.repos;

import com.globant.library.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client, String> {
}
