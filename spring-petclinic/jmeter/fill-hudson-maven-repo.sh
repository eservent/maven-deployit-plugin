#jmeter
REPO=/var/lib/hudson/.m2/repository
mvn deploy:deploy-file -DgroupId=org.apache.jmeter -DartifactId=jmeter -Dversion=2.3 -Dpackaging=jar -Dfile=jmeter-2.3.jar -DpomFile=jmeter-2.3.pom -Durl=file://${REPO} -X
mvn deploy:deploy-file -DgroupId=jcharts -DartifactId=jcharts -Dversion=0.7.5 -Dpackaging=jar -Dfile=jcharts-0.7.5.jar -Durl=file://${REPO}
mvn deploy:deploy-file -DgroupId=org.apache.jorphan -DartifactId=jorphan -Dversion=2.3 -Dpackaging=jar -Dfile=jorphan-2.3.jar -Durl=file://${REPO}
mvn deploy:deploy-file -DgroupId=org.mozilla.javascript -DartifactId=javascript -Dversion=1.0 -Dpackaging=jar -Dfile=javascript-1.0.jar -Durl=file://${REPO}
#jmeter plugin
mvn deploy:deploy-file -DgroupId=org.apache.jmeter -DartifactId=maven-jmeter-plugin -Dversion=1.0 -Dpackaging=jar -Dfile=maven-jmeter-plugin-1.0.jar -DpomFile=maven-jmeter-plugin-1.0.pom  -Durl=file://${REPO}

