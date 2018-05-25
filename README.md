# DVLA-TEST
To run the test follow the below steps</p>
open the command prompt and navigate the root folder containing the pom.xml file</br>
Type in this command:  mvn clean install test site -Dmaven.test.failure.ignore=true -Dcucumber.options="--plugin json:target/DVLA.json src/test/resources/features/ui/ --glue pageDefinition --tags @CheckCarReg" -DprojectPath="your project path\Michael" -Dmodule="DVLA" --fail-at-end</p>
Reports are stored in Michael/target/site/DVLA-reports/
