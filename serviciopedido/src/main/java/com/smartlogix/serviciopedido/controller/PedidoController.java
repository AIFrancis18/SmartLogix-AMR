package com.smartlogix.serviciopedido.controller;

import com.smartlogix.serviciopedido.model.Pedido;
import com.smartlogix.serviciopedido.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // 🔥 CREAR PEDIDO
    @PostMapping
    public Pedido crear(@RequestBody Pedido pedido){
        return service.guardar(pedido);
    }

    // 🔹 LISTAR PEDIDOS
    @GetMapping
    public List<Pedido> listar(@RequestParam(required = false) String cliente){

        if (cliente != null) {
            return service.buscarPorCliente(cliente);
        }

        return service.listar();
    }

    // 🔥 ACTUALIZAR ESTADO
    @PutMapping("/{id}/estado")
    public Pedido actualizarEstado(@PathVariable Long id,
                                   @RequestParam String estado){

        return service.actualizarEstado(id, estado);
    }

    // 🔹 OBTENER PEDIDO + ENVÍOS
    @GetMapping("/{id}/envios")
    public Map<String, Object> obtenerPedidoConEnvios(@PathVariable Long id){
        return service.obtenerPedidoConEnvios(id);
    }

    // 🔥 ELIMINAR PEDIDO + ENVÍOS
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        service.eliminar(id);
    }
}