# Waermeleitung

**Ausführung des Programms**
Gestartet werden kann das Programm über 2 Wege:
    - Ausführung der "warmeleitung.jar"
    - Ausführung der "Main.java"

Bei der Exekution einer der beiden Wege wird beim Start automatisch die "config.yaml" ausgelesen, in der alle Parameter/Konfigurationen des Programms hinterlegt sind.

_Note: Die "warmeleitung.jar" muss immer im selben Verzeichnis wie die "config.yaml" sein._

**Im Folgenden werden die wichtigsten Parameter noch kurz erläutert:**
X: Die Größe der X-Achse des Quaders
Y: Die Größe der Y-Achse des Quaders
Z: Die Größe der Z-Achse des Quaders
Temperature: Konstante Temperatur bei der Wärmewand mit konstantem Wert in Zeit und x-z-Ebene
CaseCuboid: Je nach Zahl wird hier der Ansatz zur Berechnung der Wärmewand ausgewählt (1: Konstant, 2: Linearer Anstieg vom Rand zum heißesten Punkt in der Mitte, 3: Sinusförmige Veränderung in der Zeit über die gesamte Fläche)
EdgeLength: Kantenlaenge
LowestValue: Tiefste mögliche Temperatur im Quader
TemperatureConductivity: Temperaturleitfähigkeit
TimeDifferential: Zeitliche Differenzial
Edge: Temperatur des Randes von CaseCuboid bzw. Ansatz 2
Middle: Temperatur der Mitte von CaseCuboid bzw. Ansatz 2
Height: Höhe der grafischen Oberfläche (GUI)
Width: Breite der grafischen Oberfläche (GUI)
Iterations: Anzahl der durchgeführten Iterationen
SleepTime: Wartezeit innerhalb des Programms nach jeder Iteration (Wurde verwendet, um bei hoher Parallelisierung die Threads für einen Moment warten zu lassen, damit die GUi bei der Zeichnung hinterherkommt )
Threads: Anzahl der Threads
ThreadXSelection: Auswahl des Ansatzes der Parallelisierung (0:ExecutorCuboidCalculation oder 1: ExecutorCuboidCalculationThreadpool)
Basis: Basistemperatur bei der Wärmewand mit sinusförmiger Veränderung in der Zeit über die gesamte Fläche
Variance: Maximale Abweichung der Temperatur bei der Wärmewand mit sinusförmiger Veränderung in der Zeit über die gesamte Fläche