package com.paulo.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paulo.cursomc.domain.ItemPedido;
import com.paulo.cursomc.domain.PagamentoComBoleto;
import com.paulo.cursomc.domain.Pedido;
import com.paulo.cursomc.domain.enums.EstadoPagamento;
import com.paulo.cursomc.repositories.ItemPedidoRepository;
import com.paulo.cursomc.repositories.PagamentoRepository;
import com.paulo.cursomc.repositories.PedidoRepository;
import com.paulo.cursomc.services.BoletoService;
import com.paulo.cursomc.services.ProdutoService;
import com.paulo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private PagamentoRepository pagamentoRepository; 
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService; 
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private BoletoService boletoService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() ->
			new ObjectNotFoundException("Objeto não encontrado ! ID: " + id + ", Tipo: "
					+ Pedido.class.getName()
		));
	}
	
	@Transactional
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto,pedido.getInstante());
		}
		
		pedido = repo.save(pedido); 
		pagamentoRepository.save(pedido.getPagamento());
		
		for(ItemPedido ip : pedido.getItemPedidos() ) {
			ip.setDesconto(0.00);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getValor());
			ip.setPedido(pedido);
		}
		
		itemPedidoRepository.saveAll(pedido.getItemPedidos());
		System.out.println(pedido);
		return pedido;
	}
	

}
