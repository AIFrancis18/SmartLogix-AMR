package com.smartlogix.serviciopedido.service;

import com.smartlogix.serviciopedido.model.Pedido;

import java.util.List;
import java.util.Map;

public interface PedidoService {

    Pedido guardar(Pedido pedido);

    List<Pedido> listar();

    Pedido actualizarEstado(Long id, String estado);

    List<Pedido> buscarPorCliente(String cliente);

    Map<String, Object> obtenerPedidoConEnvios(Long id);

    void eliminar(Long id);
}