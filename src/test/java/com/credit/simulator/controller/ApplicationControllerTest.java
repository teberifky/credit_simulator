package com.credit.simulator.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.credit.simulator.factory.CommandFactory;
import com.credit.simulator.repository.InMemorySheetRepository;
import com.credit.simulator.service.InstallmentCalculator;
import com.credit.simulator.validator.LoanValidator;
import com.credit.simulator.view.ConsoleView;
import com.credit.simulator.view.LineSource;

class ApplicationControllerTest {

    private String run(String script) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        LineSource input = new LineSource().addSource(new BufferedReader(new StringReader(script)), true);
        AppContext context = new AppContext(
                new ConsoleView(new PrintStream(buffer, true, StandardCharsets.UTF_8), input),
                new LoanValidator(Clock.fixed(Instant.parse("2026-10-08T00:00:00Z"), ZoneOffset.UTC)),
                new InstallmentCalculator(),
                new InMemorySheetRepository());
        new ApplicationController(context, CommandFactory.createDefault()).run();
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void showListsEveryCommand() {
        String out = run("show\nexit\n");
        for (String command : new String[] {"calculate", "save", "switch", "sheets", "result", "show", "exit"}) {
            assertTrue(out.contains(command), "missing " + command);
        }
    }

    @Test
    void calculateFlowPrintsMonthlyInstallmentPerYear() {
        String out = run("calculate\nMOBIL\nbekas\n2020\n100000000\n3\n25000000\nexit\n");
        assertTrue(out.contains("Tahun 1 : Rp. 2,250,000.00/bln , Suku Bunga : 8%"), out);
        assertTrue(out.contains("Tahun 2 : Rp. 2,432,250.00/bln , Suku Bunga : 8,1%"), out);
        assertTrue(out.contains("Tahun 3 : Rp. 2,641,423.50/bln , Suku Bunga : 8,6%"), out);
    }

    @Test
    void invalidAnswersAreRejectedAndAskedAgain() {
        String out = run("calculate\ntruk\nmobil\nbaru\n2020\n2025\n100000000\n7\n6\n10000000\n35000000\nexit\n");
        assertTrue(out.contains("Jenis kendaraan harus"), out);
        assertTrue(out.contains("Kendaraan baru tidak boleh kurang dari tahun 2025"), out);
        assertTrue(out.contains("Tenor harus 1 sampai 6 tahun"), out);
        assertTrue(out.contains("DP kendaraan baru minimal 35%"), out);
        assertTrue(out.contains("Tahun 6 :"), out);
    }

    @Test
    void saveAndSwitchBetweenSheets() {
        String out = run(
            "calculate\nmobil\nbekas\n2020\n100000000\n3\n25000000\nsave a\n"
            + "calculate\nmotor\nbekas\n2019\n50000000\n1\n12500000\nsave b\n"
            + "sheets\nswitch a\nexit\n"
        );

        assertTrue(out.contains("Sheet 'a' tersimpan"), out);
        assertTrue(out.contains("Sheet 'b' tersimpan"), out);
        assertTrue(out.contains("Sheet          : a"), out);
        assertTrue(out.contains("[b]> sheets"), out);
        assertTrue(out.contains("* b"), out);
    }

    @Test
    void switchToUnknownSheetShowsAnError() {
        String out = run("switch motorbaru\nexit\n");
        assertTrue(out.contains("Sheet 'motorbaru' tidak ditemukan"), out);
    }

    @Test
    void saveWithoutCalculationShowsAnError() {
        String out = run("save x\nexit\n");
        assertTrue(out.contains("Belum ada perhitungan"), out);
    }

    @Test
    void unknownCommandIsReported() {
        String out = run("halo\nexit\n");
        assertTrue(out.contains("Command 'halo' tidak dikenal"), out);
    }

    @Test
    void endOfInputStopsTheLoopQuietly() {
        String out = run("show\n");
        assertEquals(1, out.split("Command yang tersedia", -1).length - 1);
    }
}

