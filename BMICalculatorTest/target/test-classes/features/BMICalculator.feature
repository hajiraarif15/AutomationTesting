Feature: BMI Calculator Automation
  To verify BMI calculations and input reflections on calculator.net using Metric Units

  Scenario Outline: Validate BMI result for given inputs
    Given I open the BMI calculator
    And I select the "Metric Units" tab
    When I enter age "<age>", gender "<gender>", height "<height>", and weight "<weight>"
    And I calculate BMI
    Then I should see the entered height "<height>" and weight "<weight>" reflected correctly
    And I should see a valid BMI result

    Examples:
      | age | gender | height | weight |
      | 18  | male   | 185    | 50     |
      | 25  | female | 160    | 60     |
      | 30  | male   | 170    | 85     |
      | 35  | female | 155    | 90     |
