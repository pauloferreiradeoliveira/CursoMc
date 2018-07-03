package com.paulo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.paulo.cursomc.domain.Categoria;
import com.paulo.cursomc.domain.Produto;
import com.paulo.cursomc.repositories.CategoriaRepository;
import com.paulo.cursomc.repositories.ProdutoRepository;
import com.paulo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() ->
			new ObjectNotFoundException("Objeto não encontrado ! ID: " + id + ", Tipo: "
					+ Produto.class.getName()
		));
	}
	
	public Page<Produto> search(String nome, List<Integer> ids,Integer page,Integer linesPerPage, String orderBy,String direction ){
		// New 2.*.*
		PageRequest pageRequest = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias,pageRequest);
		
	}
	
}
