package cl.triskeledu.compras.service;


import cl.triskeledu.compras.client.BoletoClient;
import cl.triskeledu.compras.model.Compra;
import cl.triskeledu.compras.model.DetalleCompra;
import cl.triskeledu.compras.repository.CompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CompraService {

private final CompraRepository compraRepository;
    private final BoletoClient boletoClient;

    @Transactional
    public Compra guardar(Compra compra) {
        int totalCompra = 0;

        if (compra.getDetalles() != null) {
            for (DetalleCompra detalle : compra.getDetalles()) {
                Integer precio = boletoClient.obtenerPrecioBoleto(detalle.getIdBoleto());

                int subtotal = precio * detalle.getCantidad();

                // Convertimos el subtotal a BigDecimal para que la entidad lo acepte
                detalle.setSubtotal(BigDecimal.valueOf(subtotal));
                detalle.setCompra(compra);

                totalCompra += subtotal;
            }
        }

        // Convertimos el totalCompra a BigDecimal para que la entidad lo acepte
        compra.setTotal(BigDecimal.valueOf(totalCompra));
        compra.setEstado(cl.triskeledu.compras.model.EstadoCompra.PENDIENTE);

        return compraRepository.save(compra);
    }

    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }
}
