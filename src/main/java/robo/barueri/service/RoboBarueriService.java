package robo.barueri.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import robo.barueri.exception.GenericException;
import robo.barueri.model.NotaFiscal;

public class RoboBarueriService {

	public void lerExcel(String arquivoExcel) throws GenericException {
		FileInputStream arquivo = null;
		XSSFWorkbook workbook = null;
		try {
			arquivo = new FileInputStream(new File(arquivoExcel));
			workbook = new XSSFWorkbook(arquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		XSSFSheet worksheet = workbook.getSheetAt(0);

		int colStatusNf = -1;
		int colNumeroNf = -1;
		int colDataEmissao = -1;
		int colCnpjPrestador = -1;
		int colRazaoTomador = -1;
		int colUfPrestador = -1;
		int colCidadePrestador = -1;
		int colCnpjTomador = -1;
		int colLc116 = -1;
		int colValorServico = -1;
		int colStatusProcessamento = -1;
		int colSerieRps = -1;
		int colRazaoPrestador = -1;

		XSSFRow row = null;

		for (int col = 0; col < worksheet.getRow(0).getLastCellNum(); col++) {
			row = worksheet.getRow(0);
			switch (row.getCell(col).toString().toLowerCase()) {
			case "status_nf":
				if (colStatusNf == -1) {
					colStatusNf = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "numero_nf":
				if (colNumeroNf == -1) {
					colNumeroNf = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "data_emissao":
				if (colDataEmissao == -1) {
					colDataEmissao = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "cnpj_prestador":
				if (colCnpjPrestador == -1) {
					colCnpjPrestador = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "razao_tomador":
				if (colRazaoTomador == -1) {
					colRazaoTomador = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "cidade":
				if (colCidadePrestador == -1) {
					colCidadePrestador = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "uf":
				if (colUfPrestador == -1) {
					colUfPrestador = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "cnpj_tomador":
				if (colCnpjTomador == -1) {
					colCnpjTomador = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "lc116_03":
				if (colLc116 == -1) {
					colLc116 = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "valor_servico":
				if (colValorServico == -1) {
					colValorServico = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "status_processamento":
				if (colStatusProcessamento == -1) {
					colStatusProcessamento = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "serie_rps":
				if (colSerieRps == -1) {
					colSerieRps = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			case "razao_social":
				if (colRazaoPrestador == -1) {
					colRazaoPrestador = col;
				} else {
					throw new GenericException("Coluna '" + row.getCell(col).toString() + "' duplicada.");
				}
				break;
			}
		}

		if (colNumeroNf == -1 || colDataEmissao == -1 || colCnpjPrestador == -1 || colRazaoTomador == -1
				|| colCnpjTomador == -1 || colLc116 == -1 || colValorServico == -1 || colStatusProcessamento == -1
				|| colUfPrestador == -1 || colSerieRps == -1 /* || colCidadePrestador == -1 */) {
			throw new GenericException("Reveja a estrutura do arquivo!");
		}

		StatusService.inicializar(arquivoExcel, workbook, worksheet);

		RegistroNF registroNf = validarArquivo(worksheet, colStatusNf, colNumeroNf, colDataEmissao, colCnpjPrestador,
				colRazaoTomador, colCnpjTomador, colLc116, colValorServico, colStatusProcessamento);

//		WebDriver driver = logarPrefeituraBarueri(registroNf);
//		selecionarEmpresaPeriodo(driver, registroNf);
//		registrarNF(driver, worksheet, colStatusNf, colNumeroNf, colDataEmissao, colCnpjPrestador, colRazaoSocial, colCidadePrestador, colUfPrestador, colCnpjTomador, colLc116, colValorServico, colStatusProcessamento, workbook, arquivoExcel);

		List<NotaFiscal> listaNotaFiscal = buscarNotaFiscalNaPlanilha(worksheet, colStatusNf, colNumeroNf,
				colDataEmissao, colCnpjPrestador, colRazaoTomador, colCidadePrestador, colUfPrestador, colCnpjTomador,
				colLc116, colValorServico, colStatusProcessamento, colSerieRps, colRazaoPrestador, workbook,
				arquivoExcel);

		WebDriver driver = LoginService.logarPrefeituraBarueri(registroNf);
		RegistroNFService.registrarNF(driver, registroNf, listaNotaFiscal, colStatusProcessamento);

		driver.close();
	}

	public void criarColunaStatusProcessamento(String arquivoExcel) throws IOException {
		FileInputStream arquivo = null;
		XSSFWorkbook workbook = null;
		try {
			arquivo = new FileInputStream(new File(arquivoExcel));
			workbook = new XSSFWorkbook(arquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		XSSFSheet worksheet = workbook.getSheetAt(0);

		int linha = 0;
		gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Status_Processamento", linha, 32);
		gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Data_Hora_Processamento", linha, 33);
	}

	private List<NotaFiscal> buscarNotaFiscalNaPlanilha(XSSFSheet worksheet, int colStatusNf, int colNumeroNf,
			int colDataEmissao, int colCnpjPrestador, int colRazaoTomador, int colCidadePrestador, int colUfPrestador,
			int colCnpjTomador, int colLc116, int colValorServico, int colStatusProcessamento, int colSerieRps,
			int colRazaoPrestador, XSSFWorkbook workbook, String arquivoExcel) {

		XSSFRow row = worksheet.getRow(0);
		List<NotaFiscal> listaNotaFiscal = new ArrayList<>();
		for (int linha = 1; linha < worksheet.getPhysicalNumberOfRows(); linha++) {
			row = worksheet.getRow(linha);
			/*
			 * if (notasCadastradas > 5) { continue; }
			 */

			if (row == null) {
				break;
			}

			String statusNF = null;
			if (row.getCell(colStatusProcessamento) != null) {
				statusNF = row.getCell(colStatusProcessamento).toString();
				if (statusNF == "") {
					statusNF = null;
				}
			} else {
				statusNF = null;
			}

			// Se o status do processamento estiver preenchido n�o faz nada
			if (statusNF != null) {
				continue;
			}

			// Se for diferente de status "V�lida" n�o faz nada
			if (!row.getCell(colStatusNf).toString().equals("Válida")) {
				continue;
			}

			String numeroNf = null;
			if (row.getCell(colNumeroNf).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				BigDecimal bd = new BigDecimal(row.getCell(colNumeroNf).toString());
				numeroNf = bd.toPlainString().toString();
			} else {
				numeroNf = row.getCell(colNumeroNf).toString();
			}

			if (numeroNf.length() > 9 && numeroNf.substring(0, 3).equals("202")) {
				numeroNf = numeroNf.substring(0, 4) + numeroNf.substring((numeroNf.length() - 5), numeroNf.length());
			}

			LocalDate dataEmissao = null;
			if (row.getCell(colDataEmissao).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
				dataEmissao = LocalDate.parse(row.getCell(colDataEmissao).toString(), format);
			} else {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				dataEmissao = LocalDate.parse(row.getCell(colDataEmissao).toString(), format);
			}

			String razaoTomador = row.getCell(colRazaoTomador).toString();
			String cnpjTomador = row.getCell(colCnpjTomador).toString();
			String lc116 = row.getCell(colLc116).toString();
			String valorServico = row.getCell(colValorServico).toString();
			String serieRps = row.getCell(colSerieRps) != null ? row.getCell(colSerieRps).toString() : "";
			String cnpjPrestador = row.getCell(colCnpjPrestador).toString();
			String razaoPrestador = row.getCell(colRazaoPrestador).toString();

			listaNotaFiscal.add(new NotaFiscal(statusNF, numeroNf, dataEmissao, razaoTomador, cnpjTomador, lc116,
					valorServico, serieRps, cnpjPrestador, razaoPrestador));
		}

		return listaNotaFiscal;
	}

	public void registrarNF(WebDriver d, XSSFSheet worksheet, int colStatusNf, int ColNumeroNf, int ColDataEmissao,
			int ColCnpjPrestador, int ColRazaoSocial, int colCidadePrestador, int colUfPrestador, int ColCnpjTomador,
			int ColLc116, int ColValorServico, int colStatusProcessamento, XSSFWorkbook workbook, String arquivoExcel) {
		WebDriver driver = d;
		XSSFRow row = worksheet.getRow(0);
		WebDriverWait wait = new WebDriverWait(driver, 10000);

		// int notasCadastradas = 0;
		for (int linha = 1; linha < worksheet.getPhysicalNumberOfRows(); linha++) {
			row = worksheet.getRow(linha);
			/*
			 * if (notasCadastradas > 5) { continue; }
			 */
			String statusNF = null;
			if (row.getCell(colStatusProcessamento) != null) {
				statusNF = row.getCell(colStatusProcessamento).toString();
				if (statusNF == "") {
					statusNF = null;
				}
			} else {
				statusNF = null;
			}

			// Se o status do processamento estiver preenchido n�o faz nada
			if (statusNF != null) {
				continue;
			}

			// Se for diferente de status "V�lida" n�o faz nada
			if (!row.getCell(colStatusNf).toString().equals("Válida")) {
				continue;
			}

			// Escrevendo a nota fiscal
			String numeroNf;
			if (row.getCell(ColNumeroNf).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				BigDecimal bd = new BigDecimal(row.getCell(ColNumeroNf).toString());
				numeroNf = bd.toPlainString().toString();
			} else {
				numeroNf = row.getCell(ColNumeroNf).toString();
			}

			if (numeroNf.length() > 9 && numeroNf.substring(0, 3).equals("202")) {
				numeroNf = numeroNf.substring(0, 4) + numeroNf.substring((numeroNf.length() - 5), numeroNf.length());
			}
			wait.until(ExpectedConditions.elementToBeClickable(By.id("txtNumDoc")));
			driver.findElement(By.id("txtNumDoc")).sendKeys(numeroNf);

			// Escrevendo a data
			wait.until(ExpectedConditions.elementToBeClickable(By.id("txtDiaEmissao")));
			LocalDate data = null;
			if (row.getCell(ColDataEmissao).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
				data = LocalDate.parse(row.getCell(ColDataEmissao).toString(), format);
			} else {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				data = LocalDate.parse(row.getCell(ColDataEmissao).toString(), format);
			}
			driver.findElement(By.id("txtDiaEmissao")).sendKeys(String.valueOf(data.getDayOfMonth()));

			// Escrevendo o CNPJ
			wait.until(ExpectedConditions.elementToBeClickable(By.id("txtDocPrestador")));
			String cnpjPrestador = row.getCell(ColCnpjPrestador).toString().replace(".", "").replace("/", "")
					.replace("-", "");
			driver.findElement(By.id("txtDocPrestador")).sendKeys(cnpjPrestador);

			// Click no bot�o Buscar CNPJ
			wait.until(ExpectedConditions.elementToBeClickable(By.id("btnBuscaPresServ")));
			driver.findElement(By.id("btnBuscaPresServ")).click();

			// Verificando se existe um alert
			wait = new WebDriverWait(driver, 1);
			// Escrevendo o nome da Empresa
			if (isAlertPresent(driver)) {
				Alert alert = driver.switchTo().alert();
				String textoAlert = alert.getText();
				alert.accept();
				if (textoAlert.equals("Nenhum prestador foi localizado!")) {
					try {
						wait.until(ExpectedConditions.elementToBeClickable(By.id("txtNomePrestador")));
						driver.findElement(By.id("txtNomePrestador")).sendKeys(row.getCell(ColRazaoSocial).toString());

						// Escrevendo a UF
						wait = new WebDriverWait(driver, 10000);
						wait.until(ExpectedConditions.elementToBeClickable(By.id("drpUF")));
						Select selectUF = new Select(driver.findElement(By.id("drpUF")));
						selectUF.selectByValue(row.getCell(colUfPrestador).toString());

						// Escrevendo a Cidade
						wait = new WebDriverWait(driver, 10000);
						wait.until(ExpectedConditions.elementToBeClickable(By.id("drpCidade")));
						Select selectCidade = new Select(driver.findElement(By.id("drpCidade")));
						System.out.println("Cidade: '" + row.getCell(colCidadePrestador).toString() + "'");
						selectCidade.selectByValue(row.getCell(colCidadePrestador).toString());
//						JavascriptExecutor js = (JavascriptExecutor) driver; 
//						js.executeScript("var text = "+row.getCell(colCidadePrestador).toString()+";\r\n"
//								+ "var select = document.querySelector('#drpCidade');\r\n"
//								+ "for (var i = 0; i < select.options.length; i++) {\r\n"
//								+ "    if (select.options[i].text === text) {\r\n"
//								+ "        select.selectedIndex = i;\r\n"
//								+ "        break;\r\n"
//								+ "    }\r\n"
//								+ "}");

						// Conferindo a Cidade
						Select cidadeSelecionada = new Select(driver.findElement(By.id("drpCidade")));
						WebElement optionCidadeSelecionada = cidadeSelecionada.getFirstSelectedOption();
						String cidadeSelecionadaString = optionCidadeSelecionada.getText();

						if (!cidadeSelecionadaString.equals(row.getCell(colCidadePrestador).toString())) {
							gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Erro ao Cadastrar a Empresa",
									linha, colStatusProcessamento);
							limparDadosNf(d);
							continue;
						}
					} catch (Exception e) {
						System.out.println(e);
						try {
							gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Erro ao Cadastrar a Empresa",
									linha, colStatusProcessamento);
							limparDadosNf(d);
							continue;
						} catch (IOException e1) {
							e1.printStackTrace();
							return;
						}
					}
				}
			}

			// Escrevendo a LC
			try {
				String lc = recuperarLC(row.getCell(ColLc116).toString().replace(".", ""));
				if (lc != null) {
					wait = new WebDriverWait(driver, 10000);
					wait.until(ExpectedConditions.elementToBeClickable(By.id("drpAtividades")));
					driver.findElement(By.id("drpAtividades")).sendKeys(lc);

					// Conferindo a LC
					Select lcSelecionada = new Select(driver.findElement(By.id("drpAtividades")));
					WebElement optionLcSelecionada = lcSelecionada.getFirstSelectedOption();
					String lcSelecionadaString = optionLcSelecionada.getText().toString();
					lcSelecionadaString = lcSelecionadaString.substring(0, lcSelecionadaString.indexOf(' '));
					if (!lcSelecionadaString.equals(lc)) {
						gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Erro de LC", linha,
								colStatusProcessamento);
						limparDadosNf(d);
						continue;
					}
				} else {
					try {
						gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Erro de LC", linha,
								colStatusProcessamento);
						limparDadosNf(d);
						continue;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				System.out.println(e.toString());
			}

			// Escrevendo o Valor do documento
			wait.until(ExpectedConditions.elementToBeClickable(By.id("txtValor")));
			driver.findElement(By.id("txtValor")).sendKeys(row.getCell(ColValorServico).toString());

			// Confirmando o envio do relat�rio
			wait.until(ExpectedConditions.elementToBeClickable(By.id("ibtnConfirmarDoc")));
			driver.findElement(By.id("ibtnConfirmarDoc")).click();

			// Verificar o Cadastro da nota fiscal
			if (isAlertPresent(driver)) {
				Alert alert = driver.switchTo().alert();
				String textoAlert = alert.getText();
				alert.accept();

				// Duplicidade
				if (textoAlert.contains("mesmo Tipo") && textoAlert.contains("declarado")) {
					try {
						gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Duplicidade", linha,
								colStatusProcessamento);
						limparDadosNf(d);
						continue;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				// gravarMensagemStatus(arquivoExcel, workbook, worksheet, "NF Registrada",
				// linha, colStatusProcessamento);
				try {
					if (elementoExiste(driver, "imgNovaNota")) {
						driver.findElement(By.id("imgNovaNota")).click();
						gravarMensagemStatus(arquivoExcel, workbook, worksheet, "NF Registrada", linha,
								colStatusProcessamento);
					}
				} catch (ElementNotInteractableException e) {
					System.out.println(e);
					try {
						if (elementoExiste(driver, "ibtnConfirmaAlerta")) {
							driver.findElement(By.id("ibtnConfirmaAlerta")).click();
							gravarMensagemStatus(arquivoExcel, workbook, worksheet, "NF Registrada", linha,
									colStatusProcessamento);
						}
					} catch (Exception ex) {
						System.out.println(ex);
						gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Erro", linha, colStatusProcessamento);
						limparDadosNf(d);
						continue;
					}

				}
				/*
				 * if (elementoExiste(driver, "imgNovaNota")) {
				 * gravarMensagemStatus(arquivoExcel, workbook, worksheet, "NF Registrada",
				 * linha, colStatusProcessamento);
				 * driver.findElement(By.id("imgNovaNota")).click(); //notasCadastradas++; }
				 * else { gravarMensagemStatus(arquivoExcel, workbook, worksheet, "Erro", linha,
				 * colStatusProcessamento); }
				 */
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public boolean elementoExiste(WebDriver driver, String elemento) {
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		Boolean existe = driver.findElements(By.id(elemento)).size() > 0;
		return existe;
	}

	private void limparDadosNf(WebDriver d) {
		WebDriver driver = d;
		WebDriverWait wait = new WebDriverWait(driver, 10000);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("txtNumDoc")));
		driver.findElement(By.id("txtNumDoc")).clear();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("txtDiaEmissao")));
		driver.findElement(By.id("txtDiaEmissao")).clear();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("txtDocPrestador")));
		driver.findElement(By.id("txtDocPrestador")).clear();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("txtNomePrestador")));
		driver.findElement(By.id("txtNomePrestador")).clear();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("txtValor")));
		driver.findElement(By.id("txtValor")).clear();
	}

	public void gravarMensagemStatus(String arquivoExcel, XSSFWorkbook workbook, XSSFSheet worksheet, String mensagem,
			int linha, int colStatus) throws IOException {

		XSSFRow row = worksheet.getRow(linha);
		Cell resultCell = (Cell) worksheet.getRow(linha).getCell(colStatus);
		if (resultCell == null) {
			resultCell = row.createCell(colStatus);
		}
		resultCell.setCellValue(mensagem);

		if (linha > 0) {
			LocalDateTime agora = LocalDateTime.now();
			DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String dataFormatada = formatterData.format(agora);

			colStatus++;
			resultCell = (Cell) worksheet.getRow(linha).getCell(colStatus);
			if (resultCell == null) {
				resultCell = row.createCell(colStatus);
			}
			resultCell.setCellValue(dataFormatada);
		}

		FileOutputStream fileOut = new FileOutputStream(arquivoExcel);
		workbook.write(fileOut);
	}

	public String recuperarLC(String lc) {
		FileInputStream arquivo = null;
		XSSFWorkbook workbook = null;
		String arquivoLc = "/home/anderson-caldeira/Workspaces/ABMC/abmc_robo_barueri/lc/LC_116.xlsx";
		try {
			arquivo = new FileInputStream(new File(arquivoLc));
			workbook = new XSSFWorkbook(arquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		XSSFSheet worksheet = workbook.getSheetAt(0);

		XSSFRow row = null;
		for (int linha = 1; linha < worksheet.getPhysicalNumberOfRows(); linha++) {
			row = worksheet.getRow(linha);
			if (row.getCell(0).toString().replace(".", "").equals(lc)) {
				return row.getCell(1).toString();
			}
		}

		return null;
	}

	public RegistroNF validarArquivo(XSSFSheet worksheet, int colStatusNf, int ColNumeroNf, int ColDataEmissao,
			int ColCnpjPrestador, int ColRazaoSocial, int ColCnpjTomador, int ColLc116, int ColValorServico,
			int ColStatusProcessamento) throws GenericException {

		String cnpjTomador = null;
		String mesAnoEmissao = null;
		LocalDate dataEmissao = null;

		XSSFRow row = worksheet.getRow(0);
		for (int linha = 1; linha < worksheet.getPhysicalNumberOfRows(); linha++) {
			row = worksheet.getRow(linha);

			if (row == null) {
				break;
			}

			// Verificar se o Status_NF � v�lido
			if (row.getCell(colStatusNf) == null) {
				throw new GenericException("Encontrado valor nulo na coluna 'Status_NF' e linha " + (linha + 1));
			}
			if (!row.getCell(colStatusNf).toString().equals("Válida")) {
				continue;
			}

			// Verificar se as notas tem ao menos 1 caractere
			if (row.getCell(ColNumeroNf) == null) {
				throw new GenericException("Encontrado valor nulo na coluna 'Numero_NF' e linha " + (linha + 1));
			}

			String numeroNf;
			if (row.getCell(ColNumeroNf).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				BigDecimal d = new BigDecimal(row.getCell(ColNumeroNf).toString());
				numeroNf = d.toPlainString().toString();
			} else {
				numeroNf = row.getCell(ColNumeroNf).toString();
			}

			if (numeroNf.length() < 1) {
				throw new GenericException("Encontrado valor inv�lido na coluna 'Numero_NF' e linha " + (linha + 1));
			}

			// Verificar se as LCs possuem 4 caracteres
			if (row.getCell(ColLc116) == null) {
				throw new GenericException("Encontrado valor nulo na coluna 'LC116_03' e linha " + (linha + 1));
			}

			if (row.getCell(ColLc116).toString().replace(".", "").length() != 4) {
				throw new GenericException("Encontrado valor inv�lido na coluna 'LC116_03' e linha " + (linha + 1));
			}

			// Verificar se todos s�o do mesmo tomador
			if (row.getCell(ColCnpjTomador) == null) {
				throw new GenericException("Encontrado valor nulo na coluna 'Cnpj_Tomador' e linha " + (linha + 1));
			}

			 String compare = row.getCell(ColCnpjTomador).toString();
			if (!compare.equals(cnpjTomador) && cnpjTomador != null) {
				throw new GenericException("Encontrado CNPJ diferentes na coluna 'Cnpj_Tomador'.");
			}
			cnpjTomador = row.getCell(ColCnpjTomador).toString();

			// Verificar se todos os registros s�o do mesmo m�s
			if (ColDataEmissao >= 0) {
				if (row.getCell(ColDataEmissao) != null) {
					LocalDate data = null;
					if (row.getCell(ColDataEmissao).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
						data = LocalDate.parse(row.getCell(ColDataEmissao).toString(), format);
						if (!(data.getMonth() + "_" + data.getYear()).equals(mesAnoEmissao) && mesAnoEmissao != null) {
							throw new GenericException("Encontrado per�odo diferente na coluna 'Data_Emissao'.");
						}
					} else {
						DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						data = LocalDate.parse(row.getCell(ColDataEmissao).toString(), format);
						if (!(data.getMonth() + "_" + data.getYear()).equals(mesAnoEmissao) && mesAnoEmissao != null) {
							throw new GenericException("Encontrado per�odo diferente na coluna 'Data_Emissao'.");
						}
					}
					mesAnoEmissao = data.getMonth() + "_" + data.getYear();
					dataEmissao = data;
				} else {
					throw new GenericException("Encontrado valor nulo na coluna 'Data_Emissao' e linha " + (linha + 1));
				}
			} else {
				throw new GenericException("Encontrado valor nulo na coluna 'Data_Emissao' e linha " + (linha + 1));
			}

		}

		RegistroNF registroNf = new RegistroNF();
		registroNf.setCnpjTomador(cnpjTomador);
		registroNf.setDataEmissao(dataEmissao);

		return registroNf;
	}

	public WebDriver logarPrefeituraBarueri(RegistroNF registroNf) {

		// System.setProperty("webdriver.chrome.driver", "chromedriver");

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		String usuario = null;
		if (registroNf.getCnpjTomador().equals("03.848.527/0001-69")) {
			usuario = "05542999840";
		} else {
			usuario = "27514495819";
		}
		usuario = "38907501840";

		// Produ��o
//		driver.get(
//				"https://www.barueri.sp.gov.br/PMB/PortalServicos/WF/wfautenticacao.aspx?TpLogin=3&ReturnURL=/nfe/wfPrincipalNF.aspx");

		// ALTERAÇÃO
		driver.get(
				"https://www.barueri.sp.gov.br/PMB/PortalServicos/WF/wfautenticacao.aspx?TpLogin=3&ReturnURL=/nfe/app/index.aspx");

		List<WebElement> avisos = driver.findElements(By.id("divAviso"));
		if (!avisos.isEmpty()) {
			System.out.println("Aviso detectado. Clicando em fechar...");
			WebElement bntFechar = driver.findElement(By.xpath(
					"/html/body/form/table/tbody/tr[1]/td/table[2]/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr/td/div/div/p/a"));
			bntFechar.click();
		}
		// FIM ALTERAÇÃO

		// Homologa��o
		// driver.get("http://testeeiss.barueri.sp.gov.br/PMB/PortalServicos/WF/wfautenticacao.aspx?TpLogin=3&ReturnURL=/nfe/wfPrincipalNF.aspx");

		String ambiente;
		if (driver.getCurrentUrl().toString().equals(
				"https://www.barueri.sp.gov.br/PMB/PortalServicos/WF/wfautenticacao.aspx?TpLogin=3&ReturnURL=/nfe/wfPrincipalNF.aspx")) {
			ambiente = "producao";
		} else {
			ambiente = "teste";
		}

		// Posicionando a p�gina
//		driver.manage().window().setSize(new Dimension(1200, 765));
//		driver.manage().window().setPosition(new Point(-1200, 100));
		driver.manage().window().maximize();

		// Preenchendo formul�rio login
		new Actions(driver)
				.moveToElement(new WebDriverWait(driver, 10)
						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#txtLoginUsuario"))))
				.perform();
		driver.findElement(By.id("txtLoginUsuario")).sendKeys(usuario);
//		driver.findElement(By.id("txtSenhaUsuario")).sendKeys("123654");
		driver.findElement(By.id("txtSenhaUsuario")).sendKeys("ASSURANT123456");
		// driver.findElement(By.id("txtimgSeguranca")).click();

		// Abrindo o link de servi�os tomados
		WebDriverWait wait = new WebDriverWait(driver, 2000);

		// COMEÇO DA ALTERAÇÃO
		driver.get("https://www.barueri.sp.gov.br/nfe/app/index.aspx");
		// FIM DA ALTERAÇÃO

//		wait.until(ExpectedConditions.elementToBeClickable(By.id("NF_DECLARA_TOMADOS")));
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//a[.//text()[contains(., 'Declarar Serviços Tomados')]]")));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"javascript:window.open('wfDeclaraServTomado.aspx?grupodeclaracao=EMISSOR_NFE&origem=declaracao','ifrmPrincipal')",
				"");

//		driver.switchTo().frame("ifrmPrincipal");

		int tomadorServicos = 0;
		if (ambiente.equals("producao")) {
			switch (registroNf.getCnpjTomador()) {
			case "21.552.818/0001-97":
				tomadorServicos = 1;
				break;
			case "03.823.704/0001-52":
				tomadorServicos = 2;
				break;
			case "03.848.527/0001-69":
				tomadorServicos = 3;
				break;
			case "01.788.160/0001-00":
				tomadorServicos = 4;
				break;
			case "22.725.405/0001-20":
				tomadorServicos = 5;
				break;
			case "04.613.348/0001-05":
				tomadorServicos = 6;
				break;
			case "03.823.704/0002-33":
				tomadorServicos = 7;
				break;
			case "03.505.295/0001-46":
				tomadorServicos = 8;
				break;
			}
		} else {

			switch (registroNf.getCnpjTomador()) {
			case "22.725.405/0001-20":
				tomadorServicos = 4;
				break;
			case "03.823.704/0001-52":
				tomadorServicos = 2;
				break;
			case "04.613.348/0001-05":
				tomadorServicos = 5;
				break;
			}
		}

		String contribuinte = null;
		if (ambiente.equals("producao")) {
			if (tomadorServicos != 0) {
				wait.until(ExpectedConditions.elementToBeClickable(By.id("wucContribAcesso_btnSelecinarContribuinte")));
				driver.findElement(By.id("wucContribAcesso_btnSelecinarContribuinte")).click();
				contribuinte = "wucContribuinte_drpContribuinte";
			}
		} else {
			contribuinte = "wucContribAcesso_drpContribuinte";
		}

		// driver.switchTo().frame("ifrmPrincipal");
		if (tomadorServicos != 0) {
			Select sele = new Select(driver.findElement(By.id(contribuinte)));
			sele.selectByIndex(tomadorServicos);
		}

		if (ambiente.equals("producao")) {
			// if (driver.findElement(By.id("chkCiente")).isDisplayed()) {
			if (elementoExiste(driver, "chkCiente")) {
				if (driver.findElement(By.id("chkCiente")).isDisplayed()) {
					wait = new WebDriverWait(driver, 1);
					wait.until(ExpectedConditions.elementToBeClickable(By.id("chkCiente")));
					driver.findElement(By.id("chkCiente")).click();
					wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCiente")));
					driver.findElement(By.id("btnCiente")).click();
				}
			}
			js.executeScript(
					"javascript:window.open('wfDeclaraServTomado.aspx?grupodeclaracao=EMISSOR_NFE&origem=declaracao','ifrmPrincipal')",
					"");
		}

		wait = new WebDriverWait(driver, 10000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("drpAnoCompetencia")));
		Select selectAno = new Select(driver.findElement(By.id("drpAnoCompetencia")));
		selectAno.selectByValue(String.valueOf(registroNf.getDataEmissao().getYear()));

		wait.until(ExpectedConditions.elementToBeClickable(By.id("drpMesCompetencia")));
		Select selectMes = new Select(driver.findElement(By.id("drpMesCompetencia")));
		selectMes.selectByValue(String.valueOf(registroNf.getDataEmissao().getMonthValue()));

		return driver;
	}

//	public WebDriver selecionarEmpresaPeriodo(WebDriver d, RegistroNF registroNf) {
//		WebDriver driver = d;
//		int tomadorServicos = 0;
//		switch (registroNf.getCnpjTomador()) {
//		case "22.725.405/0001-20":
//			tomadorServicos = 4;
//			break;
//		case "03.823.704/0001-52":
//			tomadorServicos = 2;
//			break;
//		case "04.613.348/0001-05":
//			tomadorServicos = 5;
//			break;
//		}
//		
//		if (tomadorServicos != 0) {
//			Select sele = new Select(driver.findElement(By.id("wucContribAcesso_drpContribuinte")));
//			sele.selectByIndex(tomadorServicos);
//		}
//		WebDriverWait wait = new WebDriverWait(driver, 10000);
//		wait = new WebDriverWait(driver, 10000);
//		wait.until(ExpectedConditions.elementToBeClickable(By.id("drpAnoCompetencia")));
//		Select selectAno = new Select(driver.findElement(By.id("drpAnoCompetencia")));
//		selectAno.selectByValue(String.valueOf(registroNf.getDataEmissao().getYear()));
//
//		wait.until(ExpectedConditions.elementToBeClickable(By.id("drpMesCompetencia")));
//		Select selectMes = new Select(driver.findElement(By.id("drpMesCompetencia")));
//		selectMes.selectByValue(String.valueOf(registroNf.getDataEmissao().getMonthValue()));
//
//		return driver;
//	}

//	public String processar() {
//		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
//
//		// Produ��o
//		// driver.get("https://www.barueri.sp.gov.br/PMB/PortalServicos/WF/wfautenticacao.aspx?TpLogin=3&ReturnURL=/nfe/wfPrincipalNF.aspx");
//
//		// Homologa��o
//		driver.get(
//				"http://testeeiss.barueri.sp.gov.br/PMB/PortalServicos/WF/wfautenticacao.aspx?TpLogin=3&ReturnURL=/nfe/wfPrincipalNF.aspx");
//
//		// Posicionando a p�gina
//		driver.manage().window().setSize(new Dimension(1200, 765));
//		driver.manage().window().setPosition(new Point(-1200, 100));
//		driver.manage().window().maximize();
//
//		// Preenchendo formul�rio login
//		new Actions(driver)
//				.moveToElement(new WebDriverWait(driver, 10)
//						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#txtLoginUsuario"))))
//				.perform();
//		driver.findElement(By.id("txtLoginUsuario")).sendKeys("27514495819");
//		driver.findElement(By.id("txtSenhaUsuario")).sendKeys("123654");
//		driver.findElement(By.id("txtimgSeguranca")).click();
//
//		// Abrindo o link de servi�os tomados
//		WebDriverWait wait = new WebDriverWait(driver, 60000);
//		wait.until(ExpectedConditions.elementToBeClickable(By.id("NF_DECLARA_TOMADOS")));
//
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript(
//				"javascript:window.open('wfDeclaraServTomado.aspx?grupodeclaracao=EMISSOR_NFE&origem=declaracao','ifrmPrincipal')",
//				"");
//
//		// Selecionando a compet�ncia
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("drpAnoCompetencia")));
//
//		driver.switchTo().frame("ifrmPrincipal");
//
//		wait.until(ExpectedConditions.elementToBeClickable(By.id("drpTipoDoc")));
//		driver.findElement(By.id("drpTipoDoc")).click();
//
//		return "Ok";
//	}

	class RegistroNF {

		String cnpjTomador;
		LocalDate dataEmissao;

		public String getCnpjTomador() {
			return cnpjTomador;
		}

		public void setCnpjTomador(String cnpjTomador) {
			this.cnpjTomador = cnpjTomador;
		}

		public LocalDate getDataEmissao() {
			return dataEmissao;
		}

		public void setDataEmissao(LocalDate dataEmissao) {
			this.dataEmissao = dataEmissao;
		}

	}

	public boolean isAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}
}
