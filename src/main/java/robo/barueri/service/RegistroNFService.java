package robo.barueri.service;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import robo.barueri.model.NotaFiscal;
import robo.barueri.service.RoboBarueriService.RegistroNF;

class RegistroNFService {

	static void registrarNF(WebDriver d, RegistroNF registroNf, List<NotaFiscal> listaNotaFiscal,
			int colStatusProcessamento) {

		sleep(2000);

		Map<Integer, String> mapMeses = Map.ofEntries(Map.entry(1, "Janeiro"), Map.entry(2, "Fevereiro"),
				Map.entry(3, "Março"), Map.entry(4, "Abril"), Map.entry(5, "Maio"), Map.entry(6, "Junho"),
				Map.entry(7, "Julho"), Map.entry(8, "Agosto"), Map.entry(9, "Setembro"), Map.entry(10, "Outubro"),
				Map.entry(11, "Novembro"), Map.entry(12, "Dezembro"));

		WebDriver driver = d;
		WebDriverWait wait = new WebDriverWait(driver, 10000);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"list-item\"]/li[1]/a")));

		for (int linha = 1; linha <= listaNotaFiscal.size(); linha++) {
			NotaFiscal notaFiscal = listaNotaFiscal.get(linha - 1);

			try {
				driver.get("https://www.barueri.sp.gov.br/nfe/app/index.aspx");
				WebElement opcaoContribuinte = driver.findElement(By.xpath(
						"//*[@id=\"ContentPlaceHolder1_wucContribuinte_drpContribuinte\"]//option[contains(text(), '"
								+ notaFiscal.getRazaoTomador() + "')]"));
				opcaoContribuinte.click();
			} catch (Exception e) {
				StatusService.getInstance().gravarMensagemStatus("Erro ao Cadastrar a Empresa", linha,
						colStatusProcessamento);
			}

			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//*[@id=\"ContentPlaceHolder1_wucContribuinte_tblDadosContrib\"]/div[5]/div[2]")));
				driver.findElement(By.xpath("//*[@id=\"list-item\"]/li[16]/a")).click();

//				driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder1_ibtnLocalizar\"]")).click();

				String anoEmissao = String.valueOf(notaFiscal.getDataEmissao().getYear());
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//*[@id=\"ContentPlaceHolder1_drpAnoCompetencia\"]")));
				WebElement opcaoAnoEmissao = driver.findElement(
						By.xpath("//*[@id='ContentPlaceHolder1_drpAnoCompetencia']//option[contains(text(), '"
								+ anoEmissao + "')]"));
				opcaoAnoEmissao.click();

				sleep(2000);

				String mesEmissao = mapMeses.get(notaFiscal.getDataEmissao().getMonth().getValue());
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//*[@id='ContentPlaceHolder1_drpMesCompetencia']")));
				WebElement opcaoMesEmissao = driver.findElement(
						By.xpath("//*[@id='ContentPlaceHolder1_drpMesCompetencia']//option[contains(text(), '"
								+ mesEmissao + "')]"));
				opcaoMesEmissao.click();

				wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//*[@id='ContentPlaceHolder1_txtSerieDoc']")));
				driver.findElement(By.xpath("//*[@id='ContentPlaceHolder1_txtSerieDoc']"))
						.sendKeys(notaFiscal.getSerieRps());
				driver.findElement(By.xpath("//*[@id='ContentPlaceHolder1_txtNumDoc']"))
						.sendKeys(notaFiscal.getNumeroNf());

				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//*[@id=\"ContentPlaceHolder1_txtDiaEmissao\"]")));
				String diaEmissao = String.valueOf(notaFiscal.getDataEmissao().getDayOfMonth());
				driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder1_txtDiaEmissao\"]")).sendKeys(diaEmissao);

				driver.findElement(By.xpath("//*[@id='ContentPlaceHolder1_txtValor']"))
						.sendKeys(notaFiscal.getValorServico().replace(".", "").replace(",", ""));

				wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//*[@id=\"ContentPlaceHolder1_drpAtividades\"]")));
				driver.findElement(
						By.xpath("//*[@id=\"ContentPlaceHolder1_drpAtividades\"]/option[starts-with(text(), '"
								+ notaFiscal.getLc116().replace(".", "") + "')]"))
						.click();
				sleep(4000);
				driver.findElement(By.xpath("//*[@id='ContentPlaceHolder1_txtDocPrestador']")).sendKeys(notaFiscal.getCnpjPrestador());
				driver.findElement(By.xpath("//*[@id='ContentPlaceHolder1_btnBuscaPresServ']")).click();

				sleep(1500);

 				String razaoEncontrado = driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder1_txtNomePrestador\"]")).getAttribute("value");
 				if(razaoEncontrado != null && !razaoEncontrado.equals(notaFiscal.getRazaoPrestador())) {
 					StatusService.getInstance().gravarMensagemStatus("Concluído porém razão social não é compatível", linha, colStatusProcessamento);
 				}

				verificarAlerta(driver, linha, colStatusProcessamento);

				if("10.01".equals(notaFiscal.getLc116()) || "10.02".equals(notaFiscal.getLc116())) {
					boolean aliquotaEm2Porcento = !driver.findElements(By.xpath("//*[@id=\"ContentPlaceHolder1_grdResumoEscritura\"]/tbody/tr/td[6][text()='2,00']")).isEmpty(); // //*[@id="ContentPlaceHolder1_grdResumoEscritura"]/tbody/tr[2]/td[6]
					if (aliquotaEm2Porcento) {
						StatusService.getInstance().gravarMensagemStatus("Observação: alterar alíquota de 2%", linha, colStatusProcessamento);
					}
				}

				driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder1_ibtnConfirmarDoc\"]")).click();

				WebElement duplicidade = driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder1_divAlertaDuplicidadeDoc\"]/table/tbody/tr[1]/td/text()"));
				String duplicidadeStyle = duplicidade.getAttribute("style");
				if(duplicidade.isDisplayed() && !duplicidadeStyle.contains("display: none") && !duplicidadeStyle.contains("visibility: hidden")) {
					StatusService.getInstance().gravarMensagemStatus("Duplicidade", linha, colStatusProcessamento);
				} else {
					verificarAlerta(driver, linha, colStatusProcessamento);
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
				StatusService.getInstance().gravarMensagemStatus("Erro ao preencher formulário", linha, colStatusProcessamento);
			}

			System.out.println("Concluído");
		}
	}

	private static void verificarAlerta(WebDriver driver, int linha, int colStatusProcessamento) {
		try {
			sleep(3000);
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if(alertText.contains("Documento Informado tem o mesmo Tipo, Série, Número, CPF/CNPJ do Prestador e Serviço de um documento já declarado na competência selecionada!")) {
				alertText = "Duplicidade";
			}
			StatusService.getInstance().gravarMensagemStatus(alertText, linha, colStatusProcessamento);

			Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).build().perform();

		} catch (NoAlertPresentException e) {
			StatusService.getInstance().gravarMensagemStatus("Concluído", linha, colStatusProcessamento);
		} catch (UnhandledAlertException e) {
			Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).build().perform();
		}
	}

	private static void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
