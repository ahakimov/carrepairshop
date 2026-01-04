package com.example.carrepairshop.controller;

import com.example.carrepairshop.model.Client;
import com.example.carrepairshop.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> clientList() {
        return clientService.getAllClients();
    }

    @GetMapping("{id}")
    public Client getClient(@PathVariable String id) {
        return clientService.findById(id).orElse(null);
    }

    @PutMapping
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);

        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("new")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }
}
