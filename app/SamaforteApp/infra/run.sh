#!/usr/bin/env bash
set -euo pipefail

# caminho para o diretório raiz do projeto (script está em infra/)
BASE_DIR="$(cd "$(dirname "$0")/.." >/dev/null 2>&1 && pwd)"

ant clean -Dlibs.CopyLibs.classpath="${BASE_DIR}/ant-libs/org-netbeans-modules-java-j2seproject-copylibstask-RELEASE90.jar" -Dtomcat.home=/home/otaviano/servers/apache-tomcat-9.0.111
ant dist  -Dlibs.CopyLibs.classpath="${BASE_DIR}/ant-libs/org-netbeans-modules-java-j2seproject-copylibstask-RELEASE90.jar" -Dtomcat.home=/home/otaviano/servers/apache-tomcat-9.0.111

docker compose -f infra/compose.yaml up -d

cp "${BASE_DIR}/dist/SamaforteApp.war" /home/otaviano/servers/apache-tomcat-9.0.111/webapps/

/home/otaviano/servers/apache-tomcat-9.0.111/bin/startup.sh
