class CsvParser {

    static List<CsvEntity> parseImportFile() {
        def entities = []
        def importFileLines = CsvParser.class.getResource('importFile.csv').readLines()
        importFileLines.remove(0)
        importFileLines.each { line ->
            def splittedLine = line.split ','
            def steps = splittedLine[42]
            if(steps != '' || steps == 0) {
                int activeTime = ((int)(splittedLine[36] as double)) * 1000
                if (activeTime != 0) {
                    entities << new CsvEntity(dailyCalories: splittedLine[44],
                            activeTime: activeTime,
                            date: splittedLine[0].replaceAll(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3'),
                            distanceTraveled: splittedLine[38],
                            sleepDuration: splittedLine[59],
                            steps: steps)
                }
            }
        }
        entities
    }
}
