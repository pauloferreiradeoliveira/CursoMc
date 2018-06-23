package com.paulo.cursomc.domain.enums;

public enum EstadoPagamento {

	PENDENTE(1,"Pendente"),
	QUITADO(2, "Quitado"),
	CANCELADO(3, "Cancelado");
	
	private Integer cod;
	private String descricao;
	
	private EstadoPagamento(Integer cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public Integer getCod() {
		return cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static EstadoPagamento toEmun(Integer id) throws IllegalAccessException {
		if(id == null) {
			return null;
		}
		for (EstadoPagamento p: EstadoPagamento.values()) {
			if(id.equals(p.getCod())) {
				return p;
			}
		}
		
		throw new IllegalAccessError("Id inválido" + id);		
	}
	
	
	
}
