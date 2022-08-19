package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.DatabaseException;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CityRepository cityRepository;

    public void delete(Long id) {
        try {
            eventRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id Not Found: " + id);

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    @Transactional
    public EventDTO update(Long id, EventDTO dto) {
        try {
            Event entity =eventRepository.getOne(id);
            dtoToEntity(entity,dto);
            entity = eventRepository.save(entity);
            return new EventDTO(entity);


        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id Not Found"+ id);
        }




    }

    public void dtoToEntity(Event entity,EventDTO dto) {
        entity.setName(dto.getName());
        entity.setCity(cityRepository.getOne(dto.getCityId()));
        entity.setDate(dto.getDate());
        entity.setUrl(dto.getUrl());

    }

}


