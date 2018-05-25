Feature: Dvla car registration search

  Background: Load the dvla page
    Given I have navigated to the dvla vehicle search page

  @CheckCarReg
  Scenario Outline: Search for car registration from "<mime type>"
    Given I start the vehicle search process
    When I find existing "<mime type>" files
    Then The car registration from the "<mime type>" files should be found

    Examples: 
      | mime type |
      | csv       |
      | xlsx      |
