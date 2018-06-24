package com.paulo.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{
	
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
	

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null,"Informática"); 
		Categoria cat2 = new Categoria(null,"Escritrio");
		
		Produto p1 = new Produto(null,"Computador",2000.00);
		Produto p2 = new Produto(null,"Impressora",800.00);
		Produto p3 = new Produto(null,"Mouse",80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaReposity.saveAll(Arrays.asList(cat1,cat2));
		produtoReposity.saveAll(Arrays.asList(p1,p2,p3));
		
		// ----------------------------------------------------------------// 
		
		Estado est1 = new Estado(null,"Minas Gerais");
		Estado est2 = new Estado(null,"São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);     
		
		estadoReposity.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		//------------------------------------------------------------------//
		
		Cliente cliente1 = new Cliente(null,"Maria Silva", "maria@gmail.com","343342541149",TipoCliente.PESSOAFISICA);
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
