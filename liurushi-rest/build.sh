echo off

# mvn clean install -Dmaven.test.skip dependency:sources -Dno.private.repo
# mvn clean install -Dmaven.test.skip -DuserProp=antx.properties
# mvn clean package assembly:assembly -DuserProp=antx-dev.properties

mvn clean package -DskipTests  -Dautoconfig.interactive=false -Dautoconfig.userProperties=antx-dev.properties
mvn clean install -DskipTests  -Dautoconfig.interactive=false -Dautoconfig.userProperties=antx-test-qing.properties
mvn clean install -DskipTests  -Dautoconfig.interactive=false -Dautoconfig.userProperties=antx-prd.properties
#nohup sh ~/app_run_env/apache-tomcat-8.0.24/bin/catalina.sh run > deploy.log &
sh apache-tomcat-8.0.24/bin/catalina.sh run &> deploy.log &
sh apache-tomcat-8.0.22/bin/catalina.sh run &> deploy.log &

#nohup sh ~/app_run_env/apache-tomcat-8.0.24/bin/catalina.sh jpda start > deploy.log &

# mvn assembly:assembly
# PAUSE

# -Djava.security.egd=file:/dev/./urandom