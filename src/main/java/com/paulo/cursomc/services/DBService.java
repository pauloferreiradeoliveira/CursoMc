package com.paulo.cursomc.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paulo.cursomc.domain.Categoria;
import com.paulo.cursomc.domain.Cidade;
import com.paulo.cursomc.domain.Cliente;
import com.paulo.cursomc.domain.Endereco;
import com.paulo.cursomc.domain.Estado;
import com.paulo.cursomc.domain.ItemPedido;
import com.paulo.cursomc.domain.Pagamento;
import com.paulo.cursomc.domain.PagamentoComBoleto;
import com.paulo.cursomc.domain.PagamentoComCartao;
import com.paulo.cursomc.domain.Pedido;
import com.paulo.cursomc.domain.Produto;
import com.paulo.cursomc.domain.enums.EstadoPagamento;
import com.paulo.cursomc.domain.enums.TipoCliente;
import com.paulo.cursomc.repositories.CategoriaRepository;
import com.paulo.cursomc.repositories.CidadeRepository;
import com.paulo.cursomc.repositories.ClienteRepository;
import com.paulo.cursomc.repositories.EnderecoRepository;
import com.paulo.cursomc.repositories.EstadoRepository;
import com.paulo.cursomc.repositories.ItemPedidoRepository;
import com.paulo.cursomc.repositories.PagamentoRepository;
import com.paulo.cursomc.repositories.PedidoRepository;
import com.paulo.cursomc.repositories.ProdutoRepository;

@Service
public class DBService {
	
	@Autowired
	private CategoriaRepository categoriaReposity;
	@Autowired
	private ProdutoRepository produtoReposity;
	@Autowired
	private EstadoRepository estadoReposity;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public void instantiateTestDatabase() throws ParseException {
		Categoria cat1 = new Categoria(null,"Informática"); 
		Categoria cat2 = new Categoria(null,"Escritorio");
		Categoria cat3 = new Categoria(null,"Cama,mesa e Banho"); 
		Categoria cat4 = new Categoria(null,"Eletrônicos");
		Categoria cat5 = new Categoria(null,"Jardinagem"); 
		Categoria cat6 = new Categoria(null,"Decoração");
		Categoria cat7 = new Categoria(null,"Perfumaria"); 
		
		
		
		Produto p1 = new Produto(null,"Computador",2000.00);
		Produto p2 = new Produto(null,"Impressora",800.00);
		Produto p3 = new Produto(null,"Mouse",80.00);
		Produto p4 = new Produto(null,"Mesa de Escritório",300.00);
		Produto p5 = new Produto(null,"Toalha",50.00);
		Produto p6 = new Produto(null,"Colcha",200.00);
		Produto p7 = new Produto(null,"TV true Color",1280.00);
		Produto p8 = new Produto(null,"Roçadeira",800.00);
		Produto p9 = new Produto(null,"Abajur",100.00);
		Produto p10 = new Produto(null,"Pendente",180.00);
		Produto p11 = new Produto(null,"Samphoo",90.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2,p4));
		cat3.getProdutos().addAll(Arrays.asList(p5,p6));
		cat4.getProdutos().addAll(Arrays.asList(p1,p2,p3,p7));
		cat5.getProdutos().addAll(Arrays.asList(p8));
		cat6.getProdutos().addAll(Arrays.asList(p9,p10));
		cat7.getProdutos().addAll(Arrays.asList(p11));
		
		p1.getCategorias().addAll(Arrays.asList(cat1,cat4));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2,cat4));
		p3.getCategorias().addAll(Arrays.asList(cat1,cat4));
		p4.getCategorias().addAll(Arrays.asList(cat2));
		p5.getCategorias().addAll(Arrays.asList(cat3));
		p6.getCategorias().addAll(Arrays.asList(cat3));
		p7.getCategorias().addAll(Arrays.asList(cat4));
		p8.getCategorias().addAll(Arrays.asList(cat5));
		p9.getCategorias().addAll(Arrays.asList(cat6));
		p10.getCategorias().addAll(Arrays.asList(cat6));
		p11.getCategorias().addAll(Arrays.asList(cat7));
		
		
		categoriaReposity.saveAll(Arrays.asList(cat1,cat2,cat3,cat4,cat5,cat6,cat7));
		produtoReposity.saveAll(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11));
		
		// ----------------------------------------------------------------// 
		
		Estado est1 = new Estado(null,"Minas Gerais");
		Estado est2 = new Estado(null,"São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);     
		
		estadoReposity.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		//------------------------------------------------------------------//
		
		Cliente cliente1 = new Cliente(null,"Maria Silva", "mrpauloii@gmail.com","343342541149",TipoCliente.PESSOAFISICA,encoder.encode("123"));
		cliente1.getTelefones().addAll(Arrays.asList("213123212","34234324324"));
		
		Endereco e1 = new Endereco(null,"Ruas Flores","300","Apto 313","Jardim","3822034",cliente1,c1);
		Endereco e2 = new Endereco(null,"Ruas Mados","3105","Casa 1","Jardim","3822034",cliente1,c2);
		
		cliente1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		clienteRepository.save(cliente1);
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
		
		//-----------------------------------------------------------------------//
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cliente1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cliente1, e2);
				
		Pagamento pagato1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagato1);
		
		Pagamento pagato2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, 
				sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagato2);
		
		cliente1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagato1,pagato2));
		
		// --------------------------------------------------------------------------------- //
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItemPedidos().addAll(Arrays.asList(ip1,ip2));
		ped2.getItemPedidos().addAll(Arrays.asList(ip3));
		
		p1.getItemPedidos().add(ip1);
		p2.getItemPedidos().add(ip3);
		p3.getItemPedidos().add(ip2);
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1,ip2,ip3));
	}
}
