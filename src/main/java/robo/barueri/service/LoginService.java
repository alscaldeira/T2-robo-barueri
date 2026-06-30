package robo.barueri.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import robo.barueri.service.RoboBarueriService.RegistroNF;

class LoginService {

	static WebDriver logarPrefeituraBarueri(RegistroNF registroNf) {
		// System.setProperty("webdriver.chrome.driver", "/Users/alscaldeira/Workspaces/T2/robo_barueri/chromedriver");

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();

		String usuario = "38907501840";

		driver.get(
				"https://www.barueri.sp.gov.br/PMB/PortalServicos/WF/wfautenticacao.aspx?TpLogin=3&ReturnURL=/nfe/app/index.aspx");

		new Actions(driver)
				.moveToElement(new WebDriverWait(driver, 10)
						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#txtLoginUsuario"))))
				.perform();
		driver.findElement(By.id("txtLoginUsuario")).sendKeys(usuario);
		driver.findElement(By.id("txtSenhaUsuario")).sendKeys("ASSURANT123456");
		return driver;
	}

}
