package br.com.gestao.financeira.service.transacao.service;

import br.com.gestao.financeira.service.transacao.dto.DadosResumoCategoria;
import br.com.gestao.financeira.service.transacao.dto.DadosResumoDiario;
import br.com.gestao.financeira.service.transacao.dto.DadosResumoMensal;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class RelatorioService {

    private final ResumoTransacaoService resumoService;

    public RelatorioService(ResumoTransacaoService resumoService) {
        this.resumoService = resumoService;
    }

    public ByteArrayInputStream gerarExcel(Long usuarioId) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            criarAbaCategoria(workbook, usuarioId);
            criarAbaDiario(workbook, usuarioId);
            criarAbaMensal(workbook, usuarioId);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void criarAbaCategoria(Workbook workbook, Long usuarioId) {
        Sheet sheet = workbook.createSheet("Por Categoria");
        List<DadosResumoCategoria> dados = resumoService.resumoPorCategoria(usuarioId);

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Categoria");
        header.createCell(1).setCellValue("Total");

        int rowIdx = 1;
        for (DadosResumoCategoria d : dados) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(d.tipoTransacao().name());
            row.createCell(1).setCellValue(d.total().doubleValue());
        }
    }

    private void criarAbaDiario(Workbook workbook, Long usuarioId) {
        Sheet sheet = workbook.createSheet("Por Dia");
        List<DadosResumoDiario> dados = resumoService.resumoPorDia(usuarioId);

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Data");
        header.createCell(1).setCellValue("Total");

        int rowIdx = 1;
        for (DadosResumoDiario d : dados) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(d.data().toString());
            row.createCell(1).setCellValue(d.total().doubleValue());
        }
    }

    private void criarAbaMensal(Workbook workbook, Long usuarioId) {
        Sheet sheet = workbook.createSheet("Por Mês");
        List<DadosResumoMensal> dados = resumoService.resumoPorMes(usuarioId);

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Ano");
        header.createCell(1).setCellValue("Mês");
        header.createCell(2).setCellValue("Total");

        int rowIdx = 1;
        for (DadosResumoMensal d : dados) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(d.ano());
            row.createCell(1).setCellValue(d.mes());
            row.createCell(2).setCellValue(d.total().doubleValue());
        }
    }

    public ByteArrayInputStream gerarPdf(Long usuarioId) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            com.lowagie.text.Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Resumo de Transações Financeiras", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Seção Categoria
            document.add(new Paragraph("Resumo por Categoria", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            PdfPTable tableCat = new PdfPTable(2);
            tableCat.setWidthPercentage(100);
            addTableHeader(tableCat, "Categoria", "Total");
            for (DadosResumoCategoria d : resumoService.resumoPorCategoria(usuarioId)) {
                tableCat.addCell(d.tipoTransacao().name());
                tableCat.addCell(d.total().toString());
            }
            document.add(tableCat);
            document.add(new Paragraph(" "));

            // Seção Diária
            document.add(new Paragraph("Resumo Diário", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            PdfPTable tableDia = new PdfPTable(2);
            tableDia.setWidthPercentage(100);
            addTableHeader(tableDia, "Data", "Total");
            for (DadosResumoDiario d : resumoService.resumoPorDia(usuarioId)) {
                tableDia.addCell(d.data().toString());
                tableDia.addCell(d.total().toString());
            }
            document.add(tableDia);
            document.add(new Paragraph(" "));

            // Seção Mensal
            document.add(new Paragraph("Resumo Mensal", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            PdfPTable tableMes = new PdfPTable(3);
            tableMes.setWidthPercentage(100);
            addTableHeader(tableMes, "Ano", "Mês", "Total");
            for (DadosResumoMensal d : resumoService.resumoPorMes(usuarioId)) {
                tableMes.addCell(String.valueOf(d.ano()));
                tableMes.addCell(String.valueOf(d.mes()));
                tableMes.addCell(d.total().toString());
            }
            document.add(tableMes);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        com.lowagie.text.Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
}
