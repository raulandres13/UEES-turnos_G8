#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")"

# Limpiar
rm -rf out build 2>/dev/null || true
mkdir -p out

# Compilar
javac -d out $(find src -name "*.java")

# Empaquetar JAR
mkdir -p build/libs
cat > out/MANIFEST.MF <<EOF
Manifest-Version: 1.0
Main-Class: labturnos.MainFrame

EOF

jar cfm build/libs/turnos-final.jar out/MANIFEST.MF -C out .

echo "Listo: build/libs/turnos-final.jar"