package com.paulo.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paulo.cursomc.domain.Cidade;
import com.paulo.cursomc.domain.Estado;
import com.paulo.cursomc.dto.CidadeDTO;
import com.paulo.cursomc.dto.EstadoDTO;
import com.paulo.cursomc.services.CidadeService;
import com.paulo.cursomc.services.EstadoService;

@RestController
@RequestMapping(value="/estados")
public class EstadoResource {

	@Autowired
	private EstadoService service;
	
	@Autowired
	private CidadeService cidadeService;
	
	
	/**
	 * Retorna os Estados
	 * @return Lista de Estados
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll(){
		List<Estado> estados = service.findAll();
		List<EstadoDTO> dtos = estados.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(dtos);
	}
	
	@RequestMapping( value="/{estadoId}/cidades" ,method=RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidadeByEstado(@PathVariable Integer estadoId){
		List<Cidade> cidades = cidadeService.findByEstado(estadoId);
		List<CidadeDTO> cidadeDTOs = cidades.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(cidadeDTOs);
		
	}
}
