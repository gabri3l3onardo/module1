# Messenger 

Using the scratch project, we will create a messenger that will pretend to send emails using a custom template generator.  

The implementation of the template generator should be your own. You are not allowed to use any third-parties libraries. Use the TDD approach for template generator. **Each step should be a separate commit.**  

Set of requirements for the mail template generator, you should create a list of TDD tests for these requirements: 

* The system replaces variable placeholders like #{subject} from a template with values provided at runtime. 
* If at least one placeholder value is not provided at runtime – template generator should throw an exception.
* Template generator ignores values for variables provided at runtime that aren’t found from the template. 
* System should support values passed in runtime with #{…}. E.g. template is  “Some text: #{value}” and  at runtime #{value} passed as  #{tag}. Output should be “Some text: #{tag}”. 
* The system supports the full Latin-1 character set in templates and in variables. 

Messenger should work in two modes: console and file mode.  

* In console mode the application takes expression from console and prints result to console. No application parameters should be specified to use this mode.  
* In file mode application takes expression from file and output results to file. To use this mode user should specify input and output file names as application parameters.  
