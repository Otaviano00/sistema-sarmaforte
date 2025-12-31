#!/usr/bin/env bash
set -euo pipefail

# caminho para o diretório raiz do projeto (script está em infra/)
BASE_DIR="$(cd "$(dirname "$0")/.." >/dev/null 2>&1 && pwd)"

cd "$BASE_DIR"

# Cria o arquivo de distribuição .wat do projeto
ant clean -Dlibs.CopyLibs.classpath="${BASE_DIR}/ant-libs/org-netbeans-modules-java-j2seproject-copylibstask-RELEASE90.jar" -Dtomcat.home=/home/otaviano/servers/apache-tomcat-9.0.111
ant dist  -Dlibs.CopyLibs.classpath="${BASE_DIR}/ant-libs/org-netbeans-modules-java-j2seproject-copylibstask-RELEASE90.jar" -Dtomcat.home=/home/otaviano/servers/apache-tomcat-9.0.111

# Inicia o container de banco de dado
docker run --name samaforte-db \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=db_samaforte \
  -e MYSQL_USER=samaforte \
  -e MYSQL_PASSWORD=samaforte123 \
  -p 3306:3306 \
  -v samaforte-mysql-2:/var/lib/mysql \
  -it -d \
  mysql:5.7

# Faz o "deploy" do projeto e inicia o servidor
cp "${BASE_DIR}/dist/SamaforteApp.war" /home/otaviano/servers/apache-tomcat-9.0.111/webapps/

/home/otaviano/servers/apache-tomcat-9.0.111/bin/startup.sh

echo "Aplicação SamaforteApp iniciada com sucesso!"
open http://localhost:8080/SamaforteApp/home.jsp
