package robo.barueri.model;

import java.time.LocalDate;

public class NotaFiscal {
	
	public NotaFiscal(String statusNf, String numeroNf, LocalDate dataEmissao, String razaoTomador,
			String cnpjTomador, String lc116, String valorServico, String serieRps, String cnpjPrestador, String razaoPrestador) {
		super();
		this.statusNf = statusNf;
		this.numeroNf = numeroNf;
		this.dataEmissao = dataEmissao;
		this.razaoTomador = razaoTomador;
		this.cnpjTomador = cnpjTomador;
		this.lc116 = lc116;
		this.valorServico = valorServico;
		this.serieRps = serieRps;
		this.cnpjPrestador = cnpjPrestador;
		this.razaoPrestador = razaoPrestador;
	}
	private String statusNf;
	private String numeroNf;
	private LocalDate dataEmissao;
	private String razaoTomador;
	private String razaoPrestador;
	private String cnpjTomador;
	private String cnpjPrestador;
	private String lc116;
	private String valorServico;
	
	public String getCnpjPrestador() {
		return cnpjPrestador;
	}
	public void setCnpjPrestador(String cnpjPrestador) {
		this.cnpjPrestador = cnpjPrestador;
	}
	public String getRazaoTomador() {
		return razaoTomador;
	}
	public void setRazaoTomador(String razaoTomador) {
		this.razaoTomador = razaoTomador;
	}
	public String getSerieRps() {
		return serieRps;
	}
	public void setSerieRps(String serieRps) {
		this.serieRps = serieRps;
	}
	private String serieRps;
	
	public String getStatusNf() {
		return statusNf;
	}
	public void setStatusNf(String statusNf) {
		this.statusNf = statusNf;
	}
	public String getNumeroNf() {
		return numeroNf;
	}
	public void setNumeroNf(String numeroNf) {
		this.numeroNf = numeroNf;
	}
	public LocalDate getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	public String getCnpjTomador() {
		return cnpjTomador;
	}
	public void setCnpjTomador(String cnpjTomador) {
		this.cnpjTomador = cnpjTomador;
	}
	public String getLc116() {
		return lc116;
	}
	public void setLc116(String lc116) {
		this.lc116 = lc116;
	}
	public String getValorServico() {
		return valorServico;
	}
	public void setValorServico(String valorServico) {
		this.valorServico = valorServico;
	}
	public String getRazaoPrestador() {
		return razaoPrestador;
	}
	public void setRazaoPrestador(String razaoPrestador) {
		this.razaoPrestador = razaoPrestador;
	}
	
}
