package com.credit.simulator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

import com.credit.simulator.model.LoanCalculation;
import com.credit.simulator.model.LoanRequest;
import com.credit.simulator.model.VehicleCondition;
import com.credit.simulator.model.VehicleType;
import com.credit.simulator.model.YearlyInstallment;
import com.credit.simulator.service.InstallmentCalculator;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("======================================");
            System.out.println(" KALKULATOR SIMULASI KREDIT KENDARAAN");
            System.out.println("======================================");

            System.out.print("1. Jenis Kendaraan (Mobil/Motor): ");
            VehicleType vehicleType = VehicleType.fromInput(scanner.nextLine());

            System.out.print("2. Kondisi Kendaraan (Baru/Bekas): ");
            VehicleCondition vehicleCondition = VehicleCondition.fromInput(scanner.nextLine());

            System.out.print("3. Tahun Kendaraan: ");
            int vehicleYear = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("4. Jumlah Pinjaman Total: Rp");
            BigDecimal totalLoanAmount = new BigDecimal(scanner.nextLine().trim());

            System.out.print("5. Tenor Pinjaman (1-6 tahun): ");
            int loanTenure = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("6. Jumlah DP: Rp");
            BigDecimal downPayment = new BigDecimal(scanner.nextLine().trim());

            LoanRequest request = new LoanRequest(
                    vehicleType,
                    vehicleCondition,
                    vehicleYear,
                    totalLoanAmount,
                    loanTenure,
                    downPayment
            );

            InstallmentCalculator calculator = new InstallmentCalculator();

            LoanCalculation result = calculator.calculate(request);

            NumberFormat rupiah = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));

            System.out.println("\n======================================");
            System.out.println("    HASIL SIMULASI KREDIT KENDARAAN");
            System.out.println("======================================");
            System.out.printf("Kendaraan       : %s %s, tahun %d%n", request.vehicleType().label(), request.vehicleCondition().label(), request.vehicleYear());
            System.out.println("Total Pinjaman  : Rp" + rupiah.format(totalLoanAmount));
            System.out.println("DP              : Rp" + rupiah.format(downPayment));
            System.out.println("Pokok Hutang    : Rp" + rupiah.format(request.principal()));
            System.out.println("Tenor           : " + loanTenure + " tahun");
            System.out.println("--------------------------------------");
            System.out.println("RINCIAN CICILAN");
            System.out.println("--------------------------------------");

            for (YearlyInstallment y : result.installments()) {
                System.out.printf(
                    "Tahun %d : Rp. %,.2f/bln , Suku Bunga : %s%%%n",
                    y.year(),
                    y.monthlyInstallment(),
                    y.interestRate().stripTrailingZeros().toPlainString()
                        .replace(".", ",")
                );
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Input tidak valid: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
