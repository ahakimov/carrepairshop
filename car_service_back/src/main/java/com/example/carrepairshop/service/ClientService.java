package com.example.carrepairshop.service;

import com.example.carrepairshop.model.Client;
import com.example.carrepairshop.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(String id) {
        return clientRepository.findById(id);
    }

    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(String id) {
        clientRepository.deleteById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }
}
