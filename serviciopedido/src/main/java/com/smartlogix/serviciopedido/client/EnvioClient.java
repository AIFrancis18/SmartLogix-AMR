package com.smartlogix.serviciopedido.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// 🔥 conexión al microservicio de envíos
@FeignClient(name = "servicioenvio", url = "http://localhost:8083")
public interface EnvioClient {

    // 🔹 obtener envíos por pedidos
    @GetMapping("/envios/pedido/{pedidoId}")
    List<Object> obtenerEnvios(@PathVariable Long pedidoId);

    // 🔹 crear envío automático
    @PostMapping("/envios")
    Object crearEnvio(@RequestBody Map<String, Object> envio);

    // 🔥 ELIMINAR ENVÍOS POR PEDIDO
    @DeleteMapping("/envios/pedido/{pedidoId}")
    void eliminarEnviosPorPedido(@PathVariable Long pedidoId);
}