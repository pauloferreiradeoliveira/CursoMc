package com.paulo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.paulo.cursomc.domain.Categoria;
import com.paulo.cursomc.dto.CategoriaDTO;
import com.paulo.cursomc.repositories.CategoriaRepository;
import com.paulo.cursomc.services.exceptions.DataIntegrityException;
import com.paulo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	// Repositorio - Onde Salva/Delete
	@Autowired
	private CategoriaRepository repo;
	
	// Mostrar (uma) Categoria
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() ->
			new ObjectNotFoundException("Objeto não encontrado ! ID: " + id + ", Tipo: "
					+ Categoria.class.getName()
		));
	}
	
	// Mostrar Categorias
	public List<Categoria> findAll(){
		return repo.findAll();
	}
	
	//Para Inserir Categoria
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	// Para Atulizar Categoria
	public Categoria update(Categoria obj) {
		Categoria newObj =  find(obj.getId());
		updateCategoria(newObj, obj);
		return repo.save(obj);
	} 
	
	//Para poder deletar Cadegorias
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não e possível excluir uma categoria que possui produtos");
		}
	}
	
	public Page<Categoria> findPage(Integer page, Integer LinesPerPage,String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, LinesPerPage,Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
	
	private void updateCategoria (Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
		newObj.setId(obj.getId());
	}
}