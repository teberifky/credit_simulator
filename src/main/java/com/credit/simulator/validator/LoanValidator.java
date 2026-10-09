package com.credit.simulator.validator;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Year;
import java.util.regex.Pattern;

import com.credit.simulator.exception.ValidationException;
import com.credit.simulator.factory.LoanRuleFactory;
import com.credit.simulator.model.LoanRequest;
import com.credit.simulator.model.VehicleCondition;
import com.credit.simulator.model.VehicleType;
import com.credit.simulator.rule.LoanRule;

public class LoanValidator {
    public static final int MIN_TENOR = 1;
    public static final int MAX_TENOR = 6;

    public static final BigDecimal MAX_LOAN_AMOUNT = new BigDecimal("1000000000");

    private static final Pattern YEAR = Pattern.compile("\\d{4}");
    private static final Pattern DIGITS = Pattern.compile("\\d+");

    private final Clock clock;

    public LoanValidator(Clock clock) {
        this.clock = clock;
    }

    public int currentYear() {
        return Year.now(clock).getValue();
    }

    public VehicleType parseType(String raw) {
        return VehicleType.fromInput(raw);
    }

    public VehicleCondition parseCondition(String raw) {
        return VehicleCondition.fromInput(raw);
    }

    public int parseYear(String raw, VehicleCondition condition) {
        String value = trim(raw);

        if (!YEAR.matcher(value).matches()) {
            throw new ValidationException("Tahun kendaraan harus angka 4 digit (contoh: 2026).");
        }

        int year = Integer.parseInt(value);
        LoanRuleFactory.create(condition).validateYear(year, currentYear());

        return year;
    }

    public BigDecimal parseLoanAmount(String raw) {
        BigDecimal amount = parseAmount(raw, "Jumlah pinjaman");

        if (amount.signum() <= 0) {
            throw new ValidationException("Jumlah pinjaman harus lebih dari 0.");
        }

        if (amount.compareTo(MAX_LOAN_AMOUNT) > 0) {
            throw new ValidationException("Jumlah pinjaman maksimal Rp. 1,000,000,000 (1 miliar).");
        }

        return amount;
    }

    public int parseTenor(String raw) {
        String value = trim(raw);

        if (!DIGITS.matcher(value).matches() || value.length() > 3) {
            throw new ValidationException("Tenor harus angka bulat 1 sampai 6 tahun.");
        }

        int tenor = Integer.parseInt(value);
        checkTenor(tenor);

        return tenor;
    }

    public BigDecimal parseDownPayment(
            String raw,
            BigDecimal loanAmount,
            VehicleCondition condition
    ) {
        BigDecimal downPayment = parseAmount(raw, "Jumlah DP");
        checkDownPayment(downPayment, loanAmount, condition);

        return downPayment;
    }

    public void validate(LoanRequest request) {
        if (request == null) {
            throw new ValidationException("Data pengajuan kredit tidak boleh kosong.");
        }

        validateVehicleFields(request.vehicleYear(), request.vehicleType(), request.vehicleCondition());

        LoanRuleFactory.create(request.vehicleCondition()).validateYear(request.vehicleYear(), currentYear());
        BigDecimal loanAmount = request.totalLoanAmount();

        if (loanAmount.signum() <= 0 || loanAmount.compareTo(MAX_LOAN_AMOUNT) > 0) {
            throw new ValidationException("Jumlah pinjaman harus > Rp0 dan maksimal Rp1 Milyar.");
        }

        checkTenor(request.loanTenure());
        checkDownPayment(
                request.downPayment(),
                loanAmount,
                request.vehicleCondition()
        );
    }

    private void validateVehicleFields(int vehicleYear, VehicleType type, VehicleCondition condition) {
        if (type == null) {
            throw new ValidationException("Jenis kendaraan tidak boleh kosong.");
        }

        if (condition == null) {
            throw new ValidationException("Kondisi kendaraan tidak boleh kosong.");
        }

        if (vehicleYear < 1000 || vehicleYear > 9999) {
            throw new ValidationException("Tahun kendaraan harus angka 4 digit.");
        }
    }

    private void checkTenor(int tenor) {
        if (tenor < MIN_TENOR || tenor > MAX_TENOR) {
            throw new ValidationException("Tenor harus 1 sampai 6 tahun (maksimal 6 tahun).");
        }
    }

    private void checkDownPayment(BigDecimal downPayment, BigDecimal loanAmount, VehicleCondition condition) {
        LoanRule rule = LoanRuleFactory.create(condition);
        BigDecimal minimum = loanAmount.multiply(BigDecimal.valueOf(rule.minDownPaymentPercent()))
                .divide(BigDecimal.valueOf(100));

        if (downPayment.compareTo(minimum) < 0) {
            throw new ValidationException(String.format(
                    "DP kendaraan %s minimal %d%% dari jumlah pinjaman (Rp. %,.2f).",
                    condition.label().toLowerCase(), 
                    rule.minDownPaymentPercent(), 
                    minimum)
                );
        }

        if (downPayment.compareTo(loanAmount) >= 0) {
            throw new ValidationException("DP harus lebih kecil dari jumlah pinjaman.");
        }
    }

    private BigDecimal parseAmount(String raw, String field) {
        String value = trim(raw);

        if (!DIGITS.matcher(value).matches()) {
            throw new ValidationException(field + " harus berupa angka tanpa titik atau koma " + "(contoh: 1000000).");
        }

        return new BigDecimal(value);
    }

    private static String trim(String raw) {
        return raw == null ? "" : raw.trim();
    }
}