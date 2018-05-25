package test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


//@CucumberOptions(strict = false)

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/ui/", glue = "pageDefinition",
tags ="@CheckCarReg")

	public class AllTest  {

	}
