echo off

# mvn clean install -Dmaven.test.skip dependency:sources -Dno.private.repo
# mvn clean install -Dmaven.test.skip -DuserProp=antx.properties
# mvn clean package assembly:assembly -DuserProp=antx-dev.properties

# mvn clean install -Dmaven.test.skip -DuserProp=antx-test.properties
mvn clean install -DskipTests -DuserProp=antx-dev.properties
mvn clean install -DskipTests  -Dautoconfig.interactive=false -Dautoconfig.userProperties=antx-test-qing.properties
#nohup sh ~/app_run_env/apache-tomcat-8.0.24/bin/catalina.sh run > deploy.log &

# mvn assembly:assembly
# PAUSE
