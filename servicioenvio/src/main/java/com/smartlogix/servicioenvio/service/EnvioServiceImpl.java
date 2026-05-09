package com.smartlogix.servicioenvio.service;

import com.smartlogix.servicioenvio.model.Envio;
import com.smartlogix.servicioenvio.repository.EnvioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository repository;

    public EnvioServiceImpl(EnvioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Envio crear(Envio envio) {

        envio.setEstado("PENDIENTE");

        return repository.save(envio);
    }

    @Override
    public List<Envio> listar() {
        return repository.findAll();
    }

    @Override
    public List<Envio> buscarPorPedido(Long pedidoId) {
        return repository.findByPedidoId(pedidoId);
    }

    // 🔥 ACTUALIZAR ESTADO
    @Override
    public Envio actualizarEstado(Long id, String estado) {

        Envio envio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        // 🔥 VALIDACIÓN SIMPLE
        if (estado == null || estado.isEmpty()) {
            throw new RuntimeException("Estado inválido");
        }

        // 🔥 ESTADOS VÁLIDOS
        if (!estado.equals("PENDIENTE") &&
            !estado.equals("EN_CAMINO") &&
            !estado.equals("ENTREGADO")) {

            throw new RuntimeException("Estado no permitido");
        }

        envio.setEstado(estado);

        return repository.save(envio);
    }

    // 🔥 ELIMINAR ENVÍOS POR PEDIDO
    @Override
    public void eliminarPorPedido(Long pedidoId) {

        List<Envio> envios = repository.findByPedidoId(pedidoId);

        repository.deleteAll(envios);

        System.out.println("✅ Envíos eliminados del pedido: " + pedidoId);
    }
}