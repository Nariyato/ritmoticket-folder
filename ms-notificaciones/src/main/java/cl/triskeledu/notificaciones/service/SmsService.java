package cl.triskeledu.notificaciones.service;

import cl.triskeledu.notificaciones.model.Sms;
import cl.triskeledu.notificaciones.repository.SmsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")

public class SmsService {

    private final SmsRepository smsRepository; // [cite: 13]

    public List<Sms> listarTodos() {
        return smsRepository.findAll();
    }

    public Optional<Sms> buscarPorId(Integer id) {
        return smsRepository.findById(id);
    }

    @Transactional
    public Sms guardar(Sms sms) {
        return smsRepository.save(sms);
    }

    @Transactional
    public void eliminar(Integer id) {
        smsRepository.deleteById(id);
    }

}
