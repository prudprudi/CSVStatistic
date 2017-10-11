package com.prudprudi4.csvstatistic;

import com.prudprudi4.csvstatistic.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class CSVStatistic {
    private Path sourceDir;
    private Path resultDir;

    private Map<String, Map<String, Pair<Integer, Integer>>> riskyEntities = new HashMap<>();

    public CSVStatistic(Path sourceDir, Path resultDir) {
        this.sourceDir = sourceDir;
        this.resultDir = resultDir;
    }

    private Stream<String> getLinesStream(Path path) {
        try {
            String prefix = path.getFileName().toString().substring(0, 5);
            return Files.lines(path)
                    .map(l -> String.format("%s;%s", prefix, l));

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void setRiskyEntities(List<String> lines) {
        lines.forEach(l -> {
            String[] splitted = l.split(";");
            String computerId = splitted[0];
            String riskyId = splitted[1];
            String isVirus = splitted[4];

            riskyEntities.putIfAbsent(riskyId, new HashMap<>());
            Map<String, Pair<Integer, Integer>> entity = riskyEntities.get(riskyId);
            entity.putIfAbsent(computerId, new Pair<>(0, 0));
            Pair<Integer, Integer> pair = entity.get(computerId);

            int countRisky = pair.getFirst() + 1;
            int countVirus = pair.getSecond();

            if (isVirus.equals("1")) {
                ++countVirus;
            }

            pair.set(countRisky, countVirus);
        });
    }

    public void createScanReport() {
        try (Stream<Path> paths = Files.list(sourceDir)) {
            List<String> lines = new ArrayList<>();
            paths.map(this::getLinesStream)
                    .forEach(s -> s.forEach(lines::add));

            Path path = Paths.get(String.format("%s%sScanReport.csv", resultDir, File.separator));
            Files.write(path, lines);
            setRiskyEntities(lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTotalReport() {
        List<String> lines = new ArrayList<>();

        riskyEntities.forEach((k, v) -> {
            int countComps = v.size();
            int riskyCount = 0;
            int computerVirusCount = 0;
            int virusCount = 0;

            for (Map.Entry<String, Pair<Integer, Integer>> entry: v.entrySet()) {
                int first = entry.getValue().getFirst();
                int second = entry.getValue().getSecond();

                riskyCount += first;
                virusCount += second;

                if (second > 0) {
                    ++computerVirusCount;
                }
            }

            String line = String.format("%s;%s;%s;%s;%s", k, countComps, riskyCount, computerVirusCount, virusCount);
            lines.add(line);
        });

        try {
            Files.write(Paths.get(String.format("%s%sTotalReport.csv", resultDir, File.separator)), lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
