package com.smartlogix.servicioenvio.service;

import com.smartlogix.servicioenvio.model.Envio;

import java.util.List;

public interface EnvioService {

    Envio crear(Envio envio);

    List<Envio> listar();

    List<Envio> buscarPorPedido(Long pedidoId);

    Envio actualizarEstado(Long id, String estado);

    void eliminarPorPedido(Long pedidoId);
}