package com.medical.turno.client;

import com.medical.turno.datalizer.MedicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "medico-service", url = "${services.medico-service.url}")
public interface MedicoFeignClient {

    @GetMapping("/api/medicos/{id}")
    MedicoDTO buscarPorId(@PathVariable("id") Long id);
}
