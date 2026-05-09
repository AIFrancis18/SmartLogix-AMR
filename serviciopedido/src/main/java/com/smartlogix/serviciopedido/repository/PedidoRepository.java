package com.smartlogix.serviciopedido.repository;

import com.smartlogix.serviciopedido.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCliente(String cliente);

}