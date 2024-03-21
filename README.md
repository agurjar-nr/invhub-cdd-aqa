# Investigation Hub TAF

<h3>Our TAF uses smile:) library as a core. How it is managed?</h3>
<ol>
<li><b>By dependency in pom.xml:</b></li>
    
    <dependency>
        <groupId>com.symphonysensa</groupId>
        <artifactId>smile</artifactId>
        <version>ACTUAL_VERSION</version>
    </dependency>

Note that a lot of dependencies (TestNG, Selenide, Allure etc.) are delivered with smile:), no need to add them separately in internal pom.xml<br>
To make it possible to download smile dependency from GitHub actions, please create in .m2 folder settings.xml with the following:

    <?xml version="1.0" encoding="UTF-8"?>
    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
        <servers>
            <server>
              <id>github_smile</id>
              <configuration>
                <httpHeaders>
                  <property>
                    <name>Authorization</name>
                    <value>Bearer *TOKEN HERE*</value>
                  </property>
                </httpHeaders>
              </configuration>
            </server>
        </servers>
    </settings>

<li><b>If you'd like to define to what report system the data will be sent, you are able to create Maven profiles:</b>

    <profiles>
        <profile>
            <id>report-portal-target</id>
            <activation>
            <activeByDefault>false</activeByDefault>
            </activation>
            <properties>
            <report-portal.enable>true</report-portal.enable>
            </properties>
        </profile>
        <profile>
            <id>allure-report-target</id>
            <activation>
            <activeByDefault>false</activeByDefault>
            </activation>
            <properties>
            <allure-report.enable>true</allure-report.enable>
            </properties>
        </profile>
        <profile>
            <id>report-portal-debug-target</id>
            <activation>
            <activeByDefault>false</activeByDefault>
            </activation>
            <properties>
            <report-portal.mode>debug</report-portal.mode>
            <report-portal.enable>true</report-portal.enable>
            </properties>
        </profile>
    </profiles>
<ol>
    <li><b>In this case you’ll need to pass Maven variables to Java system property by <code>maven-surefire-plugin</code>:</b></li>

    <systemPropertyVariables>
        <allure.results.directory>target/allure-results</allure.results.directory>
        <allure.report.enable>${allure-report.enable}</allure.report.enable>
        <rp.mode>${report-portal.mode}</rp.mode>
        <rp.enable>${report-portal.enable}</rp.enable>
    </systemPropertyVariables>
</ol>
<li><b>To make it possible to work loggers in proper way it is necessary to configure log4j. Don’t forget to add ReportPortalAppender <br> 
to each logger in your project and for com.smile:</b>

    <?xml version="1.0" encoding="UTF-8"?>
    <Configuration status="INFO" packages="com.epam.ta.reportportal.log4j.appender">
        <Appenders>
            <Console name="Console" target="SYSTEM_OUT">
                <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %highlight{%-5level}{DEBUG=blue} %logger{36} - %highlight{%msg%n}{DEBUG=blue}" disableAnsi="false"/>
                <RegexFilter regex="RP_MESSAGE.*" onMatch="DENY" onMismatch="NEUTRAL"/>
            </Console>
            <ReportPortalLog4j2Appender name="ReportPortalAppender">
                <PatternLayout pattern="[%t] %-5level %logger{36} - %msg%n%throwable"/>
                <LevelRangeFilter minLevel="WARN" maxLevel="WARN" onMatch="DENY" onMismatch="NEUTRAL"/>
            </ReportPortalLog4j2Appender>
        </Appenders>
        <Loggers>
            <Logger name="com.smile" additivity="false" level="DEBUG">
                <Appender-ref ref="Console"/>
                <Appender-ref ref="ReportPortalAppender"/>
            </Logger>
            <Logger name="your project test package here" additivity="false" level="DEBUG">
                <Appender-ref ref="Console"/>
                <Appender-ref ref="ReportPortalAppender"/>
            </Logger>
            <Root level="ERROR">
                <AppenderRef ref="Console"/>
            </Root>
        </Loggers>
    </Configuration>
</li>
<li>
For all tests use base class InvHubBaseTest with TestNG <code>@Listeners</code>:<br>

    @Listeners({ExtendedReportPortalUiTestsListener.class, UiTestListener.class})
    public class InvHubBaseTest extends BaseTest {
    }

</li>
</ol>

<hr>
<h3>Build profiles</h3>
<p align="justify" id="build_profiles">Managing options of which reporting system will be used occurs with profiles. Profile defines which aspect will be enabled/disabled 
by editing aop-ajc.xml (replaces it by using <code>copy-rename-maven-plugin</code>). Define a specific profile before building
the smile project, there will be no other way to reconfigure this option when the core is already built</p>
<ul>
<li><code>report-portal-only</code> - enable reporting only to ReportPortal</li>
<li><code>allure-report-only</code> - enable reporting only to Allure</li>
<li><code>rp-and-allure</code> - enable reporting to both reporting systems: ReportPortal and Allure</li>
</ul>
<hr>
<h3>Run profiles</h3>
<p align="justify">"Target-reporting" properties define which reporting services will be used. There are 2 services: one for ReportPortal and another
one for Allure. So <code>allure-report.enable</code> and <code>report-portal.enable</code> corresponding defines which service will process report events. 
Usually, those properties define with maven profiles in project with tests where <code>smile:)</code> attached as a dependency. It is possible 
to enable both services at the same time or disable all of them.</p>
<p><em><span style="color:#c0392b">ATTENTION!</span> Enable/disable services will not provide effects if <code>smile:)</code> built
without supporting a <a id="build_profiles">specific reporting system</a></em></p>
<p>Please find full run profiles example in <code>src/main/resources/pom_example</code> in <code>profile</code> section</p>

<hr>

<h3>How to use</h3>
<p>For the creation of nested or main steps, you need to:</p>
<ol>
<li>have a logger in your class - log. Usually, it creates by @Log4j2 Lombok annotation or by the specific logger factory.</li>
<li>Call log invents with level “info” and pass the message only or add some attachment.
<li>To include your class in processing the log events as step reports please add any one of the annotations: <code>@Page, @PageComponent, 
@GeneralComponent, @ModalComponent</code> (for entities that represent UI), or just <code>@Component</code> (for services)</li>
</ol>
<p>Don’t forget to configure <code>log4j.xml</code>, enable specific reporting service, and build <code>smile:)</code> with 
specific report’ attachments</p>
<p>Examples of creating report steps:</p>
<p>Step with only text message and screenshot attachment: <code>log.info("Enter email: " + email);</code></p>

<h4>Modifiers of report steps</h4> 

<p align="justify">There is a possibility to influence the behavior of steps that will be added to the report system. It occurs by adding a 
modifier to the message part of the log action. The modifier will not be attached to the step name in the report but will be 
visible in the console. By default <code>log.info(“Step name”)</code> will create the specific step with the name that passed as the first 
string parameter of the info method, and if a web driver is available - a screenshot will be added to this step</p> 

<p>All available modifiers are placed to <code>LogReportingProperties</code> enum:</p>
<ul>
    <li>
        DSS - disable screenshot. Add this modifier if you wouldn’t like to make and attach a screenshot for your step. 
        Example: <code>log.info("Enter email: " + email + DSS);</code> - the result of this log invent will be a step without an 
        attached screenshot.
    </li>
    <li>
        TXT - this modifier allows you to add a file in text representation. 
        Example: <code>log.info("The file with configurations " + TXT, new File("src/main/resources/config.xml") );</code> - 
        the result of this log invent will be a step without an attached file that will be represented as text.
    </li>
</ul>

<hr>

<h3> Page Object Pattern convention </h3>

<h4>Layers and Namings</h4>
<p>We are using 4 layers for representation UI in java code: <br>
<b>Page<br>
PageComponent<br>
PageModalComponent<br>
GeneralComponent</b><br>
All your classes that represent an interaction with UI pages should be grouped by these layers. The name of the class should 
end with *Page/*PageComponent/*GeneralComponent depending on a layer that represents a current class.

<h4>Page</h4>
<p>Annotated by @Page, represents specific UI page (the entity that has its own URL or view). It extends BasePage and can wrap PageComponent and GeneralComponent (by composition) provided to test classes by getter (Lombok annotation). Page API should return only specific data to test layer – String, List, int, boolean etc. Not SelenideElement, ElementsCollection and other page details. </p>

<h4>PageComponent</h4>
<p>Represents a part of UI page (UI elements that have common logic). Annotated by @PageComponent (not always: we do not use this annotation when PageComponent produced by request to a page), can be extended from GeneralComponent and used directly in a test class by getter from Page. </p>

<h4>PageModalComponent</h4>
<p>Represents modal window parts of UI page, can extend BaseModalComponent if it is needed.</p>

<h4>GeneralComponent (PageGeneralComponent, TableGeneralComponent)</h4>
<p>A low-level abstraction that represents interaction with a part of UI which is common for several Pages or PageComponents.</p>

<h3> Checkstyle configurations</h3>

<h4> POM Dependencies</h4>
<p>
    Checkstyle plugin
    
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>${maven-checkstyle-plugin.version}</version>
            <configuration>
              <configLocation>${basedir}\src\test\resources\checkstyle\google_checks.xml</configLocation>
              <includeTestSourceDirectory>true</includeTestSourceDirectory>
              <consoleOutput>true</consoleOutput>
            </configuration>
        <dependencies>
          <dependency>
            <groupId>com.puppycrawl.tools</groupId>
            <artifactId>checkstyle</artifactId>
            <version>${checkstyle.com.puppycrawl.tools.version}</version>
          </dependency>
          <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>4.1.1</version>
          </dependency>
        </dependencies>
        <executions>
          <execution>
            <id>checkstyle</id>
            <goals>
              <goal>checkstyle</goal>
            </goals>
            <phase>prepare-package</phase>
          </execution>
        </executions>
    </plugin>
   
</p>

<h4> 1. Install CheckStyle-IDEA plugin </h4>
<p>
    1.1 Navigate to <code>Settings</code> -> <code>Plugins</code> -> <code>Marketplace</code> tab -> type into search input  
        <code>CheckStyle-IDEA</code><br>
    1.2 Click on <code>Install</code> button
</p>

<h4> 2. Configure checkstyle tool </h4>
<p>
    2.1 Navigate to <code>Settings</code> -> <code>Tools</code> -> <code>Checkstyle</code> <br>
    2.2 <code>Checkstyle version</code> dropdown -> <code>10.12.2</code><br>
    2.3 <code>Scan Scope</code> dropdown -><code> Only Java sources (including tests)</code>, see screenshot:<br>
    <img width="905" alt="Tools_Checkstyle" src="https://github.com/SymphonySensa/qa-automation-framework/assets/135196526/22122ee5-ed8a-4bf5-ab5f-268e8e38ea04"><br>
    2.4 In <code>Configuration File</code> section click on <code>+</code><br>
    2.5 Fill <code>Description</code> input with <code>SensaCheckstyle</code><br>
    2.6 Click on <code>Browse</code> button and choose checkstyle file <code>src/test/resources/checkstyle/google_checks.xml</code>, see screenshot:<br>
    <img width="307" alt="Tools_Checkstyle_Configuration_file" src="https://github.com/SymphonySensa/qa-automation-framework/assets/135196526/86a61320-7f8c-48be-b80f-c49300022862"><br>
</p>

<h4> 3. Configure code style formatter: </h4>
<p>
    3.1 Navigate to <code>Settings</code> -> <code>Editor</code> -> <code>Code Style</code> click on <code>Show Schema Actions</code> -> <code>Copy to IDE</code>, and leave the name as it is (qa-automation-framework)<br>
    <img width="905" alt="Show_Schema" src="https://github.com/SymphonySensa/qa-automation-framework/assets/135196526/2748ccf5-1fa0-452c-a516-f270c82a7c27"><br>
    3.2 Click on <code>Show Schema Actions</code> -> <code>Import Scheme</code> -> <code>Ceckstyle configuration</code> -> choose src\test\resources\checkstyle\google_checks.xml<br>
    3.3 <code>Settings</code> -> <code>Editor</code> -> <code>Code Style</code> -> <code>Scheme dropdown</code> set to <code>qa-automation-framework</code>
</p>

<h4>How to work with checkstyle</h4>
<p>
    4.1 After finishing your coding make a commit<br>
    4.2 Find an icon on the idea tool panel:<br>
    <img width="25" alt="Checkstyle_icon" src="https://github.com/SymphonySensa/qa-automation-framework/assets/135196526/82d83059-5972-48ea-9fbb-dbacbabfe66c">
    <br>
    4.3 In the <code>Rules</code> dropdown choose <code>SensaCheckstyle</code><br>
    4.4 Choose <code>Check project</code>, <code>Check All Modified files</code> or <code>Check files in the current Check List</code> depending on what you wish<br>
    4.5 Navigate to the class that was highlighted by checkstyle and choose: <code>Code</code> -> <code>Reformat File</code><br>
    <img width="960" alt="Class_and_issues" src="https://github.com/SymphonySensa/qa-automation-framework/assets/135196526/4ad8a967-bc7e-41c1-9dec-4136985fbe7a"><br>
    4.6 In <code>Reformat File</code> pop-up choose <code>Whole file</code>, mark all <code>Options</code> checkboxes, and click <code>Run button </code><br>
    <img width="308" alt="Reformat_file_popup" src="https://github.com/SymphonySensa/qa-automation-framework/assets/135196526/91751981-fb97-41f4-bc85-67f6063a1f30"><br>
    4.7 After reformatting files rerun checkstyle to double-check that all issues are resolved.<br>
    4.8 You are good to make a new local commit (f.e. TC-1111_checkstyle_fix) and push all to the remote. Please, validate the code changes that were brought by checkstyle!<br>
</p>
