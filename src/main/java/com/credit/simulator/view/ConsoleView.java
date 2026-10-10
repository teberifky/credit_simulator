package com.credit.simulator.view;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Function;

import com.credit.simulator.exception.InputEndedException;
import com.credit.simulator.exception.ValidationException;
import com.credit.simulator.model.LoanCalculation;
import com.credit.simulator.model.YearlyInstallment;

public class ConsoleView {
    private final PrintStream out;
    private final LineSource input;

    public ConsoleView(PrintStream out, LineSource input) {
        this.out = out;
        this.input = input;
    }

    public void println(String message) {
        out.println(message);
    }

    public void showInfo(String message) {
        out.println(message);
    }

    public void showError(String message) {
        out.println("[ERROR] " + message);
    }

    public String prompt(String label) {
        out.print(label);
        out.flush();
        String line = input.readLine();
        if (line == null) {
            out.println();
            throw new InputEndedException();
        }
        if (input.lastLineEchoed()) {
            out.println(line);
        }
        return line.trim();
    }

    public <T> T ask(String label, Function<String, T> parser) {
        while (true) {
            String answer = prompt(label + " : ");
            try {
                return parser.apply(answer);
            } catch (ValidationException e) {
                showError(e.getMessage());
            }
        }
    }

    public void showResult( String sheetName, LoanCalculation result ) { 
        var request = result.request(); 

        NumberFormat rupiah = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));

        out.println(); 
        out.println("======================================"); 
        out.println("   HASIL SIMULASI KREDIT KENDARAAN"); 
        out.println("======================================"); 
        out.println("Sheet          : " + sheetName); 
        out.printf( "Kendaraan      : %s %s, tahun %d%n", request.vehicleType().label(), request.vehicleCondition().label(), request.vehicleYear() ); 
        out.println( "Total Pinjaman : " + rupiah.format(request.totalLoanAmount()) ); 
        out.println( "DP             : " + rupiah.format(request.downPayment()) ); 
        out.println( "Pokok Hutang   : " + rupiah.format(request.principal()) ); 
        out.println( "Tenor          : " + request.loanTenure() + " tahun" ); 
        out.println("--------------------------------------"); 
        out.println("RINCIAN CICILAN"); 
        out.println("--------------------------------------"); 
        
        for (YearlyInstallment y : result.installments()) {
            out.printf(
                "Tahun %d : Rp. %,.2f/bln , Suku Bunga : %s%%%n",
                y.year(),
                y.monthlyInstallment(),
                y.interestRate().stripTrailingZeros().toPlainString()
                    .replace(".", ",")
            );
        }
    }
}
