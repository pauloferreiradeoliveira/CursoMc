package com.paulo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.paulo.cursomc.domain.Cliente;
import com.paulo.cursomc.dto.ClienteDTO;
import com.paulo.cursomc.repositories.ClienteRepository;
import com.paulo.cursomc.services.exceptions.DataIntegrityException;
import com.paulo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() ->
			new ObjectNotFoundException("Objeto não encontrado ! ID: " + id + ", Tipo: "
					+ Cliente.class.getName()
		));
	}
	
		// Mostrar Clientes
		public List<Cliente> findAll(){
			return repo.findAll();
		}
		
		// Para Atulizar Cliente
		public Cliente update(Cliente obj) {
			Cliente newObj = find(obj.getId());
			updateData(newObj, obj);
			return repo.save(newObj);
		} 
		
		//Para poder deletar Cadegorias
		public void delete(Integer id) {
			find(id);
			try {
				repo.deleteById(id);
			}
			catch(DataIntegrityViolationException e) {
				throw new DataIntegrityException("Este cliente tem pedidos");
			}
		}
		
		public Page<Cliente> findPage(Integer page, Integer LinesPerPage,String orderBy, String direction){
			PageRequest pageRequest = PageRequest.of(page, LinesPerPage,Direction.valueOf(direction), orderBy);
			return repo.findAll(pageRequest);
		}
		
		public Cliente fromDTO(ClienteDTO objDTO) {
			return new Cliente(objDTO.getId(),objDTO.getNome(),objDTO.getEmail(),null,null);
		}
		
		private void updateData(Cliente newObj, Cliente obj) {
			newObj.setNome(obj.getNome());
			newObj.setEmail(obj.getEmail());
		}
}
