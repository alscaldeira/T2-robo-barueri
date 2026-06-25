package robo.barueri.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StatusService {

	private StatusService(String arquivoExcel, XSSFWorkbook workbook, XSSFSheet worksheet) {
		this.arquivoExcel = arquivoExcel;
		this.workbook = workbook;
		this.worksheet = worksheet;
	}

	private String arquivoExcel;
	private XSSFWorkbook workbook;
	private XSSFSheet worksheet;

	// Para fazer um Singleton
	private static volatile StatusService instancia;

	public static synchronized void inicializar(String arquivoExcel, XSSFWorkbook workbook, XSSFSheet worksheet) {
		if (instancia == null) {
			instancia = new StatusService(arquivoExcel, workbook, worksheet);
		}
	}

	public static StatusService getInstance() {
		if (instancia == null) {
			throw new IllegalStateException("Singleton ainda não foi inicializado. Invoque inicializar() primeiro.");
		}
		return instancia;
	}

	public void gravarMensagemStatus(String mensagem, int linha, int colStatus) {
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

		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(arquivoExcel);
			workbook.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
