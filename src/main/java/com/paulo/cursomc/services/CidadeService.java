package com.paulo.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paulo.cursomc.domain.Cidade;
import com.paulo.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository repository;
	
	
	public List<Cidade> findByEstado(Integer estadoID){
		return repository.findCidades(estadoID);
	}
}
