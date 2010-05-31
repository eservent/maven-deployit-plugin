** JMeter
http://www.ronniealleva.org/index.php/maven-jmeter-plugin/
http://code.google.com/p/jmeter-maven-plugin/wiki/HOWTOUsePlugin

#jmeter
mvn deploy:deploy-file -DgroupId=org.apache.jmeter -DartifactId=jmeter -Dversion=2.3 -Dpackaging=jar -Dfile=jmeter-2.3.jar -DpomFile=jmeter-2.3.pom -Durl=file:///home/bmoussaud/.m2/repository
mvn deploy:deploy-file -DgroupId=jcharts -DartifactId=jcharts -Dversion=0.7.5 -Dpackaging=jar -Dfile=jcharts-0.7.5.jar -Durl=file:///home/bmoussaud/.m2/repository
mvn deploy:deploy-file -DgroupId=org.apache.jorphan -DartifactId=jorphan -Dversion=2.3 -Dpackaging=jar -Dfile=jorphan-2.3.jar -Durl=file:///home/bmoussaud/.m2/repository
mvn deploy:deploy-file -DgroupId=org.mozilla.javascript -DartifactId=javascript -Dversion=1.0 -Dpackaging=jar -Dfile=javascript-1.0.jar -Durl=file:///home/bmoussaud/.m2/repository
#jmeter plugin
mvn deploy:deploy-file -DgroupId=org.apache.jmeter -DartifactId=maven-jmeter-plugin -Dversion=1.0 -Dpackaging=jar -Dfile=maven-jmeter-plugin-1.0.jar -DpomFile=maven-jmeter-plugin-1.0.pom  -Durl=file:///home/bmoussaud/.m2/repository
# in pom
<plugin>
    <groupId>org.apache.jmeter</groupId>
    <artifactId>maven-jmeter-plugin</artifactId>
    <version>1.0</version>
    <executions>
        <execution>
            <id>jmeter-tests</id>
            <phase>verify</phase>
            <goals>
                <goal>jmeter</goal>
            </goals>
       </execution>
    </executions>
    <configuration>
        <reportDir>${project.build.directory}/jmeter-reports</reportDir>
        <jmeterUserProperties>
            <!-- for user properites -->
        </jmeterUserProperties>
    </configuration>
</plugin>

