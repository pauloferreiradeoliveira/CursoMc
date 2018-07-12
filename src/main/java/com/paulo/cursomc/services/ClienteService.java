package com.paulo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paulo.cursomc.domain.Cidade;
import com.paulo.cursomc.domain.Cliente;
import com.paulo.cursomc.domain.Endereco;
import com.paulo.cursomc.domain.enums.Perfil;
import com.paulo.cursomc.domain.enums.TipoCliente;
import com.paulo.cursomc.dto.ClienteDTO;
import com.paulo.cursomc.dto.ClienteNewDTO;
import com.paulo.cursomc.repositories.ClienteRepository;
import com.paulo.cursomc.repositories.EnderecoRepository;
import com.paulo.cursomc.security.UserSS;
import com.paulo.cursomc.services.exceptions.AuthorizationException;
import com.paulo.cursomc.services.exceptions.DataIntegrityException;
import com.paulo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bc;
		
	
		
		
		// Entra um Cliente
		public Cliente find(Integer id) {
			UserSS user =  UserService.authenticated();
			if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
				throw new AuthorizationException("Acesso negado");
			}
			
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
		
		//Para poder deletar Cliente
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
		
		@Transactional
		public Cliente insert(Cliente obj) {
			obj.setId(null);
			obj = repo.save(obj);
			enderecoRepository.saveAll(obj.getEnderecos());
			return obj;
		}
		
		public Cliente fromDTO(ClienteDTO objDTO) {
			return new Cliente(objDTO.getId(),objDTO.getNome(),objDTO.getEmail(),null,null,null);
		}
		
		public Cliente fromDTO(ClienteNewDTO objDTO) {
			Cliente cli =new Cliente(null, objDTO.getNome(), objDTO.getEmail(), 
					objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()),bc.encode(objDTO.getSenha()));
			Cidade cidade = new Cidade(objDTO.getCidadeId(), null,null);
			Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), 
					objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cidade);
			
			cli.getEnderecos().add(end);
			cli.getTelefones().add(objDTO.getTelefone1());
			if (objDTO.getTelefone2() != null) {
				cli.getTelefones().add(objDTO.getTelefone2());
			}
			if (objDTO.getTelefone3() != null) {
				cli.getTelefones().add(objDTO.getTelefone3());
			}
			
			return cli;
			
		}
		
		private void updateData(Cliente newObj, Cliente obj) {
			newObj.setNome(obj.getNome());
			newObj.setEmail(obj.getEmail());
		}
}
